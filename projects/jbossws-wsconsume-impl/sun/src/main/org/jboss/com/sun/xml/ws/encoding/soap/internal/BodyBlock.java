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
package org.jboss.com.sun.xml.ws.encoding.soap.internal;

import javax.xml.transform.Source;

import org.jboss.com.sun.xml.ws.encoding.jaxb.JAXBBeanInfo;
import org.jboss.com.sun.xml.ws.encoding.jaxb.JAXBBridgeInfo;
import org.jboss.com.sun.xml.ws.encoding.jaxb.RpcLitPayload;
import org.jboss.com.sun.xml.ws.encoding.soap.SOAPConstants;
import org.jboss.com.sun.xml.ws.encoding.soap.message.SOAP12FaultInfo;
import org.jboss.com.sun.xml.ws.encoding.soap.message.SOAPFaultInfo;


/**
 * @author WS Development Team
 */
public class BodyBlock {
     
    private Object value;
    
    public BodyBlock(Object value) {
        this.value = value;
    }
       
    public BodyBlock(JAXBBeanInfo beanInfo) {
        this.value = beanInfo;
    }
    
    public BodyBlock(JAXBBridgeInfo bridgeInfo) {
        this.value = bridgeInfo;
    }
    
    public BodyBlock(Source source) {
    	setSource(source);
    }
    
    public BodyBlock(SOAPFaultInfo faultInfo) {
    	setFaultInfo(faultInfo);
    }    

    public BodyBlock(RpcLitPayload rpcLoad) {
        this.value = rpcLoad;
    }
    
    public void setSource(Source source) {
        this.value = source;
    }
    
    public void setFaultInfo(SOAPFaultInfo faultInfo) {
        this.value = faultInfo;
    }

    /**
     * There is no need to have so many setter to set to an Object. Just setValue is all that we need?
     * @param value
     */
    public void setValue(Object value){
        this.value = value;
    }
    public Object getValue() {
        return value;
    }
	 
}
