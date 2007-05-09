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

package org.jboss.com.sun.xml.ws.server;

import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.MessageContext.Scope;

import org.jboss.com.sun.xml.ws.developer.JAXWSProperties;
import org.jboss.com.sun.xml.ws.encoding.soap.internal.DelegateBase;
import org.jboss.com.sun.xml.ws.model.RuntimeModel;
import org.jboss.com.sun.xml.ws.pept.Delegate;
import org.jboss.com.sun.xml.ws.pept.ept.EPTFactory;
import org.jboss.com.sun.xml.ws.pept.ept.MessageInfo;
import org.jboss.com.sun.xml.ws.pept.protocol.MessageDispatcher;
import org.jboss.com.sun.xml.ws.spi.runtime.WSConnection;
import org.jboss.com.sun.xml.ws.util.MessageInfoUtil;

/**
 * Entry point for all server requests.
 *
 * @author WS Development Team
 */
public class Tie implements org.jboss.com.sun.xml.ws.spi.runtime.Tie {
    
    /**
     * Common entry point for server runtime. <br>
     * Creates a MessageInfo for every Request/Response.<br>
     * Creates a RuntimeContext for every Request/Response and sets that as a metadata in 
     * MessageInfo. Doesn't create any other metadata on MessageInfo. If anything is needed, 
     * that can be created on RuntimeContext<br>
     * EPTFactoryFactoryBase is used to select a correct EPTFactory<br>
     * Calls MessageDispatcher.receive(MessageInfo). <br>
     * MessageDispatcher orchestrates all the flow: reading from WSConnection, 
     * decodes message to parameters, invoking implementor, encodes parameters to message, 
     * and writing to WSConnection
     *
     * @param connection encapsulates multiple transports
     * @param endpoint has all the information about target endpoint
     * @throws Exception throws Exception if any error occurs
     */
    public void handle(WSConnection connection,
        org.jboss.com.sun.xml.ws.spi.runtime.RuntimeEndpointInfo endpoint)
    throws Exception {
        
        // Create MessageInfo. MessageInfo holds all the info for this request
        Delegate delegate = new DelegateBase();
        MessageInfo messageInfo = (MessageInfo)delegate.getMessageStruct();
        
        // Create runtime context, runtime model for dynamic runtime
        RuntimeEndpointInfo endpointInfo = (RuntimeEndpointInfo)endpoint;
        RuntimeModel runtimeModel = endpointInfo.getRuntimeModel();
        RuntimeContext runtimeContext = new RuntimeContext(runtimeModel);
        runtimeContext.setRuntimeEndpointInfo(endpointInfo);
        
        // Update MessageContext
        MessageContext msgCtxt =
            endpointInfo.getWebServiceContext().getMessageContext();
        updateMessageContext(endpointInfo, msgCtxt);
        
        // Set runtime context on MessageInfo
        MessageInfoUtil.setRuntimeContext(messageInfo, runtimeContext);
        messageInfo.setConnection(connection);

        // Select EPTFactory based on binding, and transport
        EPTFactory eptFactory = EPTFactoryFactoryBase.getEPTFactory(messageInfo);
        messageInfo.setEPTFactory(eptFactory);
        
        // MessageDispatcher archestrates the flow
        MessageDispatcher messageDispatcher =
            messageInfo.getEPTFactory().getMessageDispatcher(messageInfo);
        messageDispatcher.receive(messageInfo);
    }
    
    /**
     * Updates MessageContext object with Service, and Port QNames
     */
    private void updateMessageContext( RuntimeEndpointInfo endpoint,
        MessageContext ctxt) {
   
        ctxt.put(MessageContext.WSDL_SERVICE, endpoint.getServiceName());
        ctxt.setScope(MessageContext.WSDL_SERVICE, Scope.APPLICATION);
        ctxt.put(MessageContext.WSDL_PORT, endpoint.getPortName());          
        ctxt.setScope(MessageContext.WSDL_PORT, Scope.APPLICATION);
        ctxt.put(JAXWSProperties.MTOM_THRESHOLOD_VALUE, endpoint.getMtomThreshold());
        ctxt.setScope(JAXWSProperties.MTOM_THRESHOLOD_VALUE, Scope.APPLICATION);
    }
    
}
