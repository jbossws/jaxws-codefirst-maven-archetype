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

import java.lang.reflect.Method;

import javax.xml.namespace.QName;

import org.jboss.com.sun.xml.ws.handler.HandlerContext;
import org.jboss.com.sun.xml.ws.model.RuntimeModel;
import org.jboss.com.sun.xml.ws.pept.ept.MessageInfo;

import com.sun.xml.bind.api.BridgeContext;




/**
 * $author: WS Development Team
 */
public class RuntimeContext {

    public RuntimeContext(RuntimeModel model) {
        this.model = model;
    }
    
    /**
     * @return Returns the model.
     */
    public RuntimeModel getModel() {
        return model;
    }
    
    /**
     * @return Returns info about endpoint
     */
    public RuntimeEndpointInfo getRuntimeEndpointInfo() {
        return endpointInfo;
    }
    
    /**
     * sets info about endpoint
     */
    public void setRuntimeEndpointInfo(RuntimeEndpointInfo endpointInfo) {
        this.endpointInfo = endpointInfo;
    }
    
    /**
     * @param name
     * @param mi
     * @return the <code>Method</code> associated with the operation named name
     */
    public Method getDispatchMethod(QName name, MessageInfo mi) {
        return getDispatchMethod(name);
    }
    
    /**
     * @param name
     * @return the <code>Method</code> associated with the operation named name
     */
    public Method getDispatchMethod(QName name){
        return model.getDispatchMethod(name);
    }
        
    /**
     * @param qname
     * @param mi
     */
    public void setMethodAndMEP(QName qname, MessageInfo mi) {
        if (model != null) {
            mi.setMethod(model.getDispatchMethod(qname));
            
            // if null, default MEP is ok
            if (qname != null && model.getJavaMethod(qname) != null) {
                mi.setMEP(model.getJavaMethod(qname).getMEP());
            }
        }
    }
    
    /**
     * @param name
     * @return the decoder Info associated with operation named name
     */
    public Object getDecoderInfo(QName name) {
        return model.getDecoderInfo(name);
    }
    
    public BridgeContext getBridgeContext() {
        return (model != null)?model.getBridgeContext():null;
    }
       
    public HandlerContext getHandlerContext() {
        return handlerContext;
    }

    public void setHandlerContext(HandlerContext handlerContext) {
        this.handlerContext = handlerContext;
    }

    private RuntimeModel model;
    private HandlerContext handlerContext;
    private RuntimeEndpointInfo endpointInfo;
}
