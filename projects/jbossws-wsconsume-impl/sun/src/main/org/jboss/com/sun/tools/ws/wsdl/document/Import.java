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

package org.jboss.com.sun.tools.ws.wsdl.document;

import javax.xml.namespace.QName;

import org.jboss.com.sun.tools.ws.wsdl.framework.Entity;


/**
 * Entity corresponding to the "import" WSDL element.
 *
 * @author WS Development Team
 */
public class Import extends Entity{

    public Import() {
    }

    public String getNamespace() {
        return _namespace;
    }

    public void setNamespace(String s) {
        _namespace = s;
    }

    public String getLocation() {
        return _location;
    }

    public void setLocation(String s) {
        _location = s;
    }

    public QName getElementName() {
        return WSDLConstants.QNAME_IMPORT;
    }

    public Documentation getDocumentation() {
        return _documentation;
    }

    public void setDocumentation(Documentation d) {
        _documentation = d;
    }

    public void accept(WSDLDocumentVisitor visitor) throws Exception {
        visitor.visit(this);
    }

    public void validateThis() {
        if (_location == null) {
            failValidation("validation.missingRequiredAttribute", "location");
        }
        if (_namespace == null) {
            failValidation("validation.missingRequiredAttribute", "namespace");
        }
    }

    private Documentation _documentation;
    private String _location;
    private String _namespace;
}
