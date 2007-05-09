/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License).  You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the license at
 * https://glassfish.dev.java.net/public/CDDLv1.0.html.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at https://glassfish.dev.java.net/public/CDDLv1.0.html.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * you own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
 */
package org.jboss.com.sun.xml.ws.handler;

import java.util.Iterator;
import javax.xml.soap.SOAPElement;

import org.jboss.com.sun.xml.ws.encoding.jaxb.JAXBBeanInfo;
import org.jboss.com.sun.xml.ws.encoding.jaxb.JAXBBridgeInfo;
import org.jboss.com.sun.xml.ws.encoding.jaxb.JAXBTypeSerializer;
import org.jboss.com.sun.xml.ws.encoding.jaxb.RpcLitPayload;
import org.jboss.com.sun.xml.ws.encoding.soap.SOAPEPTFactory;
import org.jboss.com.sun.xml.ws.encoding.soap.SOAPEncoder;
import org.jboss.com.sun.xml.ws.encoding.soap.internal.BodyBlock;
import org.jboss.com.sun.xml.ws.encoding.soap.internal.InternalMessage;
import org.jboss.com.sun.xml.ws.encoding.soap.message.SOAPFaultInfo;
import org.jboss.com.sun.xml.ws.pept.ept.MessageInfo;
import org.jboss.com.sun.xml.ws.util.xml.XmlUtil;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.ws.LogicalMessage;
import javax.xml.ws.WebServiceException;

/**
 * Implementation of LogicalMessage. This class implements the methods
 * used by LogicalHandlers to get/set the request or response either
 * as a JAXB object or as javax.xml.transform.Source.
 *
 * <p>The {@link HandlerContext} that is passed into the constructor
 * is used to retrieve the payload of the request or response.
 *
 * @see HandlerContext
 * @see LogicalMessageContextImpl
 *
 * @author WS Development Team
 */
public class LogicalMessageImpl implements LogicalMessage {

    private SOAPHandlerContext ctxt;

    public LogicalMessageImpl(SOAPHandlerContext ctxt) {
        this.ctxt = ctxt;
    }

    /*
     * If the payload is DOMSource, return it
     * If the payload is Source/SOAPFaultInfo/JAXBBridgeInfo/JAXBBeanInfo,
     * convert to DOMSource and return it. DOMSource is also stored in BodyBlock
     */
    public Source getPayload() {
        try {
            InternalMessage internalMessage = ctxt.getInternalMessage();
            if (internalMessage == null) {
                SOAPMessage soapMessage = ctxt.getSOAPMessage();
                if (soapMessage == null) {
                    return null;
                } else {
                    Iterator it = soapMessage.getSOAPBody().getChildElements();
                    SOAPElement elem = null;
                    while(it.hasNext()) {
                        Node child = (Node)it.next();
                        if (child instanceof SOAPElement) {
                            elem = (SOAPElement)child;
                            break;
                        }
                    }
                    if (elem != null) {
                        setSource(new DOMSource(elem));
                    } else {
                        return null;
                    }
                }
            }
            internalMessage = ctxt.getInternalMessage();
            BodyBlock bodyBlock = internalMessage.getBody();
            if (bodyBlock == null) {
                return null;
            } else {
                Object obj = bodyBlock.getValue();
                if (obj instanceof DOMSource) {
                    return (Source)obj;
                } else if (obj instanceof Source) {
                    Source source = (Source)obj;
                    Transformer transformer = XmlUtil.newTransformer();
                    DOMResult domResult = new DOMResult();
                    transformer.transform(source, domResult);
                    DOMSource domSource = new DOMSource(domResult.getNode());
                    bodyBlock.setSource(domSource);
                    return domSource;
                } else if (obj instanceof JAXBBridgeInfo) {
                    MessageInfo messageInfo = ctxt.getMessageInfo();
                    SOAPEPTFactory eptf = (SOAPEPTFactory)messageInfo.getEPTFactory();
                    SOAPEncoder encoder = eptf.getSOAPEncoder();
                    DOMSource domSource = encoder.toDOMSource((JAXBBridgeInfo)obj, messageInfo);
                    bodyBlock.setSource(domSource);
                    return domSource;
                } else if (obj instanceof JAXBBeanInfo) {
                    DOMSource domSource = ((JAXBBeanInfo)obj).toDOMSource();
                    bodyBlock.setSource(domSource);
                    return domSource;
                } else if (obj instanceof RpcLitPayload) {
                    MessageInfo messageInfo = ctxt.getMessageInfo();
                    SOAPEPTFactory eptf = (SOAPEPTFactory)messageInfo.getEPTFactory();
                    SOAPEncoder encoder = eptf.getSOAPEncoder();
                    DOMSource domSource = encoder.toDOMSource((RpcLitPayload)obj, messageInfo);
                    bodyBlock.setSource(domSource);
                    return domSource;
                } else if (obj instanceof SOAPFaultInfo) {
                    MessageInfo messageInfo = ctxt.getMessageInfo();
                    SOAPEPTFactory eptf = (SOAPEPTFactory)messageInfo.getEPTFactory();
                    SOAPEncoder encoder = eptf.getSOAPEncoder();
                    DOMSource domSource = encoder.toDOMSource((SOAPFaultInfo)obj, messageInfo);
                    bodyBlock.setSource(domSource);
                    return domSource;
                } else {
                    throw new WebServiceException("Unknown type "+obj.getClass()+" in BodyBlock");
                }
            }
        } catch(TransformerException te) {
            throw new WebServiceException(te);
        } catch(SOAPException se) {
            throw new WebServiceException(se);
        }
    }

    /*
     * Sets the Source as payload in the BodyBlock of InternalMessage.
     */
    public void setPayload(Source source) {
        setSource(source);
    }

    /*
     * Converts to DOMSource and keeps it in BodyBlock. Then it unmarshalls this
     * DOMSource to a jaxb object. Any changes done in jaxb object are lost if
     * the object isn't set again.
     */
    public Object getPayload(JAXBContext jaxbContext) {
        return JAXBTypeSerializer.deserialize(getPayload(), jaxbContext);
    }

    /*
     * The object is marshalled into DOMSource and stored in BodyBlock. If an
     * error occurs when using the supplied JAXBContext to marshall the
     * payload, it throws a JAXWSException.
     */
    public void setPayload(Object bean, JAXBContext jaxbContext) {
        Source source;
        try {
            source = JAXBTypeSerializer.serialize(bean,jaxbContext);
        } catch(Exception e) {
            throw new WebServiceException(e);
        }
        setSource(source);              // set Source in BodyBlock
    }

    public HandlerContext getHandlerContext() {
        return ctxt;
    }

    /*
     * It stores Source in the BodyBlock. If necessary, it creates
     * InternalMessage, and BodyBlock
     */
    private void setSource(Source source) {
        InternalMessage internalMessage = ctxt.getInternalMessage();
        if (internalMessage == null) {
            internalMessage = new InternalMessage();
            ctxt.setInternalMessage(internalMessage);
        }
        BodyBlock bodyBlock = internalMessage.getBody();
        if (bodyBlock == null) {
            bodyBlock = new BodyBlock(source);
            internalMessage.setBody(bodyBlock);
        } else {
            bodyBlock.setSource(source);
        }
    }

}
