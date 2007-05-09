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

package org.jboss.com.sun.tools.ws.wsdl.document.http;

import javax.xml.namespace.QName;

/**
 * Interface defining HTTP-extension-related constants.
 *
 * @author WS Development Team
 */
public interface HTTPConstants {

    // namespace URIs
    public static String NS_WSDL_HTTP = "http://schemas.xmlsoap.org/wsdl/http/";

    // QNames
    public static QName QNAME_ADDRESS = new QName(NS_WSDL_HTTP, "address");
    public static QName QNAME_BINDING = new QName(NS_WSDL_HTTP, "binding");
    public static QName QNAME_OPERATION = new QName(NS_WSDL_HTTP, "operation");
    public static QName QNAME_URL_ENCODED =
        new QName(NS_WSDL_HTTP, "urlEncoded");
    public static QName QNAME_URL_REPLACEMENT =
        new QName(NS_WSDL_HTTP, "urlReplacement");
}
