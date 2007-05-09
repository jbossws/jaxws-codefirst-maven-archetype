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

import javax.xml.ws.handler.LogicalMessageContext;
import javax.xml.soap.SOAPMessage;

import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.jboss.com.sun.xml.ws.encoding.soap.internal.InternalMessage;
import org.jboss.com.sun.xml.ws.pept.ept.MessageInfo;
import org.jboss.com.sun.xml.ws.spi.runtime.Invoker;

/**
 * The HandlerContext is used in the client and server runtime
 * in {@link org.jboss.com.sun.xml.ws.protocol.soap.client.SOAPMessageDispatcher} and 
 * {@link org.jboss.com.sun.xml.ws.protocol.soap.server.SOAPMessageDispatcher} to hold
 * information about the current message.
 *
 * <p>It stores a {@link org.jboss.com.sun.xml.ws.pept.ept.MessageInfo} and
 * {@link org.jboss.com.sun.xml.ws.encoding.soap.internal.InternalMessage}
 * which are used by the rest of the runtime, and provides a bridge
 * between these and the soap and logical message contexts that
 * are used by the handlers.
 *
 * @see LogicalMessageContextImpl
 * @see MessageContextImpl
 * @see SOAPMessageContextImpl
 *
 * @author WS Development Team
 */
public class SOAPHandlerContext extends HandlerContext {

    private SOAPMessage soapMessage;
    private SOAPMessageContext soapContext;
    private SHDSOAPMessageContext shdsoapContext;
    private LogicalMessageContext logicalContext;

    public SOAPHandlerContext(MessageInfo messageInfo,
            InternalMessage internalMessage,
            SOAPMessage soapMessage) {
        super(messageInfo, internalMessage);
        this.soapMessage = soapMessage;
    }

    public SOAPMessageContext getSOAPMessageContext() {
        if (soapContext == null) {
            soapContext = new SOAPMessageContextImpl(this);
        }
        return soapContext;
    }
    
    public SHDSOAPMessageContext getSHDSOAPMessageContext() {
        if (shdsoapContext == null) {
            shdsoapContext = new SHDSOAPMessageContext(this);
        }
        return shdsoapContext;
    }
    
    public LogicalMessageContext getLogicalMessageContext() {
        if (logicalContext == null) {
            logicalContext = new LogicalMessageContextImpl(this);
        }
        return logicalContext;
    }


    /**
     * @return Returns the soapMessage.
     */
    public SOAPMessage getSOAPMessage() {
        return soapMessage;
    }


    /**
     * @param soapMessage The soapMessage to set.
     */
    public void setSOAPMessage(SOAPMessage soapMessage) {
        this.soapMessage = soapMessage;
    }

    /**
     * If there is a SOAPMessage already, use getSOAPMessage(). Ignore all other
     * methods
     */
    public boolean isAlreadySoap() {
        return getSOAPMessage() != null;
    }
    
}
