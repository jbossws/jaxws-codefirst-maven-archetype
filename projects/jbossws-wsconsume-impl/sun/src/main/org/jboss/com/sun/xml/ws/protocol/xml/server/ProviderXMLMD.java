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
package org.jboss.com.sun.xml.ws.protocol.xml.server;

import javax.xml.ws.Provider;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.transform.Source;

import javax.activation.DataSource;

import org.jboss.com.sun.xml.ws.encoding.xml.XMLMessage;
import org.jboss.com.sun.xml.ws.handler.XMLHandlerContext;
import org.jboss.com.sun.xml.ws.pept.ept.MessageInfo;
import org.jboss.com.sun.xml.ws.pept.presentation.MessageStruct;
import org.jboss.com.sun.xml.ws.server.RuntimeContext;
import org.jboss.com.sun.xml.ws.server.RuntimeEndpointInfo;
import org.jboss.com.sun.xml.ws.server.provider.ProviderModel;
import org.jboss.com.sun.xml.ws.server.provider.ProviderPeptTie;
import org.jboss.com.sun.xml.ws.util.MessageInfoUtil;

import static org.jboss.com.sun.xml.ws.client.BindingProviderProperties.*;

/**
 * @author WS Development Team
 *
 */

public class ProviderXMLMD extends XMLMessageDispatcher {

    /*
     * Fill the parameters, method in MessageInfo for Provider interface.
     * invoke(Source, XMLHandlerContext) to Object[]
     * invoke(SOAPMessage, XMLHandlerContext) to Object[]
     */
    @Override
    protected void toMessageInfo(MessageInfo messageInfo, XMLHandlerContext context) {
        Object[] data = new Object[1];
        RuntimeContext rtCtxt = MessageInfoUtil.getRuntimeContext(messageInfo);
        RuntimeEndpointInfo endpointInfo = rtCtxt.getRuntimeEndpointInfo();
        Provider provider = (Provider)endpointInfo.getImplementor();
        Class providerClass = provider.getClass();
        ProviderModel model = endpointInfo.getProviderModel();
        boolean isSource = model.isSource();
        Service.Mode mode = model.getServiceMode();
        XMLMessage xmlMessage = context.getXMLMessage();
        try {
            if (isSource) {
                data[0] = xmlMessage.getSource();
            } else {
                data[0] = xmlMessage.getDataSource();
            }
        } catch(Exception e) {
            messageInfo.setResponseType(MessageStruct.UNCHECKED_EXCEPTION_RESPONSE);
            messageInfo.setResponse(e);
        }
        messageInfo.setData(data);
        messageInfo.setMethod(ProviderPeptTie.invoke_Method);
    }

    /*
     * MessageInfo contains the endpoint invocation results. If the endpoint
     * returns a SOAPMessage, just set the object in HandlerContext. If the
     * endpoint returns a Source in Mode.MESSAGE, it is converted to SOAPMessage
     * and set in HandlerContext. If the endpoint returns a Source in
     * Mode.PAYLOAD, it is set in InternalMessage, and InternalMessage is set
     * in HandlerContext
     */
    @Override
    protected void setResponseInContext(MessageInfo messageInfo,
            XMLHandlerContext context) {
        Object obj = messageInfo.getResponse();
        RuntimeContext rtCtxt = MessageInfoUtil.getRuntimeContext(messageInfo);
        RuntimeEndpointInfo endpointInfo = rtCtxt.getRuntimeEndpointInfo();
        Provider provider = (Provider)endpointInfo.getImplementor();
        Class providerClass = provider.getClass();
        
        boolean useFastInfoset = 
            messageInfo.getMetaData(CONTENT_NEGOTIATION_PROPERTY) == "optimistic";
        
        XMLMessage xmlMessage = null;
        if (messageInfo.getResponseType() == MessageInfo.NORMAL_RESPONSE) {
            xmlMessage = (obj instanceof DataSource) 
                ? new XMLMessage((DataSource)obj, useFastInfoset) 
                : new XMLMessage((Source)obj, useFastInfoset);
        } else {
            xmlMessage = new XMLMessage((Exception)obj, useFastInfoset);
        }
        context.setXMLMessage(xmlMessage);
        context.setInternalMessage(null);
    }

}
