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

package org.jboss.com.sun.xml.ws.util;

import java.io.OutputStream;
import java.io.StringReader;
import java.util.List;
import java.util.StringTokenizer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPException;

import org.jboss.com.sun.xml.ws.pept.ept.MessageInfo;
import org.jboss.com.sun.xml.ws.util.xml.XmlUtil;

import org.jboss.com.sun.xml.messaging.saaj.soap.MessageImpl;

import static org.jboss.com.sun.xml.ws.developer.JAXWSProperties.CONTENT_NEGOTIATION_PROPERTY;

public class FastInfosetUtil {
    
    public static boolean isFastInfosetAccepted(String[] accepts) {
        if (accepts != null) {
            for (String accept : accepts) {
                if (isFastInfosetAccepted(accept)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isFastInfosetAccepted(String accept) {
        StringTokenizer st = new StringTokenizer(accept, ",");
        while (st.hasMoreTokens()) {
            final String token = st.nextToken().trim();
            if (token.equalsIgnoreCase("application/fastinfoset")) {
                return true;
            }
        }        
        return false;
    }
    
    public static String getFastInfosetFromAccept(List<String> accepts) {
        for (String accept : accepts) {
            StringTokenizer st = new StringTokenizer(accept, ",");
            while (st.hasMoreTokens()) {
                final String token = st.nextToken().trim();
                if (token.equalsIgnoreCase("application/fastinfoset")) {
                    return "application/fastinfoset";
                }
                if (token.equalsIgnoreCase("application/soap+fastinfoset")) {
                    return "application/soap+fastinfoset";
                }
            }       
        }
        return null;        
    }
    
    public static void transcodeXMLStringToFI(String xml, OutputStream out) {
        try {
            XmlUtil.newTransformer().transform(
                new StreamSource(new java.io.StringReader(xml)),
                FastInfosetReflection.FastInfosetResult_new(out));
        }
        catch (Exception e) {
            // Ignore
        }
    }
    
    public static void ensureCorrectEncoding(MessageInfo messageInfo, 
        SOAPMessage message) 
    {
        String conneg = (String) messageInfo.getMetaData(CONTENT_NEGOTIATION_PROPERTY);
        if (conneg == "optimistic") {
            ((MessageImpl) message).setIsFastInfoset(true);
        }
    }

}
