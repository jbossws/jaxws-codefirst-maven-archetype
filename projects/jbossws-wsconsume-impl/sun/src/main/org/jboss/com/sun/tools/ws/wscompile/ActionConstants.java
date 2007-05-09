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
package org.jboss.com.sun.tools.ws.wscompile;

/**
 * @author WS Development Team
 */
public interface ActionConstants {
//    public static final String ACTION_SERVICE_INTERFACE_GENERATOR =
//        "service.interface.generator";
    public static final String ACTION_SERVICE_GENERATOR =
        "service.generator";
    public static final String ACTION_REMOTE_INTERFACE_GENERATOR  =
        "remote.interface.impl.generator";
    public static final String ACTION_REMOTE_INTERFACE_IMPL_GENERATOR  =
        "remote.interface.impl.generator";
    public static final String ACTION_JAXB_TYPE_GENERATOR =
        "jaxb.type.generator";
    public static final String ACTION_CUSTOM_EXCEPTION_GENERATOR = 
        "custom.exception.generator";
    public static final String ACTION_WSDL_GENERATOR = 
        "wsdl.generator";
}
