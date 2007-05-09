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
package org.jboss.com.sun.xml.ws.server.provider;

import java.lang.reflect.Method;
import javax.xml.ws.Provider;
import javax.xml.ws.WebServiceException;

import org.jboss.com.sun.xml.ws.pept.ept.MessageInfo;
import org.jboss.com.sun.xml.ws.pept.presentation.MessageStruct;
import org.jboss.com.sun.xml.ws.server.PeptTie;
import org.jboss.com.sun.xml.ws.server.RuntimeContext;
import org.jboss.com.sun.xml.ws.server.RuntimeEndpointInfo;
import org.jboss.com.sun.xml.ws.util.MessageInfoUtil;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Creates a Stateless Tie object so that it is created only once and reused.
 */
public class ProviderPeptTie extends PeptTie {

    private static final Logger logger = Logger.getLogger(
        org.jboss.com.sun.xml.ws.util.Constants.LoggingDomain + ".server.ProviderPeptTie");

    public static final Method invoke_Method;
    static {
        try {
            Class[] methodParams = { Object.class };
            invoke_Method = (Provider.class).getMethod("invoke", methodParams);
        } catch (NoSuchMethodException e) {
            throw new WebServiceException(e.getMessage(), e);
        }
    };

    /*
     * @see Tie#_invoke(MessageInfo)
     */
    public void _invoke(MessageInfo messageInfo) {
        Object[] oa = messageInfo.getData();
        RuntimeContext rtCtxt = MessageInfoUtil.getRuntimeContext(messageInfo);
        RuntimeEndpointInfo endpointInfo = rtCtxt.getRuntimeEndpointInfo();
        Provider servant = (Provider)endpointInfo.getImplementor();
        try {
            Object response = servant.invoke(oa[0]);
            messageInfo.setResponse(response);
            messageInfo.setResponseType(MessageStruct.NORMAL_RESPONSE);
            if (response == null) {
                messageInfo.setMEP(MessageStruct.ONE_WAY_MEP);
            } else {
                messageInfo.setMEP(MessageStruct.REQUEST_RESPONSE_MEP);
            }
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            messageInfo.setResponseType(MessageStruct.UNCHECKED_EXCEPTION_RESPONSE);
            messageInfo.setResponse(e);
        } catch (Exception e) {
            messageInfo.setResponseType(MessageStruct.CHECKED_EXCEPTION_RESPONSE);
            messageInfo.setResponse(e);
        }
    }

}
