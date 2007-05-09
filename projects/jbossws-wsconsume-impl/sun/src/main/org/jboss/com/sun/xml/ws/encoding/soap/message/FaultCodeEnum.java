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
package org.jboss.com.sun.xml.ws.encoding.soap.message;


import javax.xml.namespace.QName;

import org.jboss.com.sun.xml.ws.encoding.soap.streaming.SOAP12NamespaceConstants;
import org.jboss.com.sun.xml.ws.encoding.soap.streaming.SOAPNamespaceConstants;

public enum FaultCodeEnum {
    VersionMismatch(new QName(SOAP12NamespaceConstants.ENVELOPE, "VersionMismatch", SOAPNamespaceConstants.NSPREFIX_SOAP_ENVELOPE)),
    MustUnderstand(new QName(SOAP12NamespaceConstants.ENVELOPE, "MustUnderstand", SOAPNamespaceConstants.NSPREFIX_SOAP_ENVELOPE)),
    DataEncodingUnknown(new QName(SOAP12NamespaceConstants.ENVELOPE, "DataEncodingUnknown", SOAPNamespaceConstants.NSPREFIX_SOAP_ENVELOPE)),
    Sender(new QName(SOAP12NamespaceConstants.ENVELOPE, "Sender", SOAPNamespaceConstants.NSPREFIX_SOAP_ENVELOPE)),
    Receiver(new QName(SOAP12NamespaceConstants.ENVELOPE, "Receiver", SOAPNamespaceConstants.NSPREFIX_SOAP_ENVELOPE));

    private FaultCodeEnum(QName code){
        this.code = code;
    }

    public QName value(){
        return code;
    }

    public String getLocalPart(){
        return code.getLocalPart();
    }

    public String getNamespaceURI(){
        return code.getNamespaceURI();
    }

    public String getPrefix(){
        return code.getPrefix();
    }

    public static FaultCodeEnum get(QName soapFaultCode){
        if(VersionMismatch.code.equals(soapFaultCode))
            return VersionMismatch;
        else if(MustUnderstand.code.equals(soapFaultCode))
            return MustUnderstand;
        else if(DataEncodingUnknown.code.equals(soapFaultCode))
            return DataEncodingUnknown;
        else if(Sender.code.equals(soapFaultCode))
            return Sender;
        else if(Receiver.code.equals(soapFaultCode))
            return Receiver;
        return null;
    }

    private final QName code;
}
