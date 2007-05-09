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

package org.jboss.com.sun.xml.ws.encoding.soap.streaming;

/**
 * @author WS Development Team
 */
public class SOAPNamespaceConstants {
    public static final String NSPREFIX_SOAP_ENVELOPE = "soapenv";
    public static final String ENVELOPE = "http://schemas.xmlsoap.org/soap/envelope/";
    public static final String ENCODING =
        "http://schemas.xmlsoap.org/soap/encoding/";
    public static final String XSD = "http://www.w3.org/2001/XMLSchema";
    public static final String XSI =
        "http://www.w3.org/2001/XMLSchema-instance";
    public static final String XMLNS = "http://www.w3.org/XML/1998/namespace";
    public static final String TRANSPORT_HTTP =
        "http://schemas.xmlsoap.org/soap/http"; 
    public static final String ACTOR_NEXT =
        "http://schemas.xmlsoap.org/soap/actor/next";

    public static final String TAG_ENVELOPE = "Envelope";
    public static final String TAG_HEADER = "Header";
    public static final String TAG_BODY = "Body";
    public static final String TAG_FAULT = "Fault";

    public static final String ATTR_ACTOR = "actor";
    public static final String ATTR_MUST_UNDERSTAND = "mustUnderstand";
    public static final String ATTR_ENCODING_STYLE = "encodingStyle";
}
