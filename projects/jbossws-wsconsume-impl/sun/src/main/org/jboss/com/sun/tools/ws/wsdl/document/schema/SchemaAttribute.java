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

package org.jboss.com.sun.tools.ws.wsdl.document.schema;

import javax.xml.namespace.QName;

import org.jboss.com.sun.tools.ws.wsdl.framework.WriterContext;


/**
 *
 * @author WS Development Team
 */
public class SchemaAttribute {

    public SchemaAttribute() {
    }

    public SchemaAttribute(String localName) {
        _localName = localName;
    }

    public String getNamespaceURI() {
        return _nsURI;
    }

    public void setNamespaceURI(String s) {
        _nsURI = s;
    }

    public String getLocalName() {
        return _localName;
    }

    public void setLocalName(String s) {
        _localName = s;
    }

    public QName getQName() {
        return new QName(_nsURI, _localName);
    }

    public String getValue() {
        if (_qnameValue != null) {
            if (_parent == null) {
                throw new IllegalStateException();
            } else {
                return _parent.asString(_qnameValue);
            }
        } else {
            return _value;
        }
    }

    public String getValue(WriterContext context) {
        if (_qnameValue != null) {
            return context.getQNameString(_qnameValue);
        } else {
            return _value;
        }
    }

    public void setValue(String s) {
        _value = s;
    }

    public void setValue(QName name) {
        _qnameValue = name;
    }

    public SchemaElement getParent() {
        return _parent;
    }

    public void setParent(SchemaElement e) {
        _parent = e;
    }

    private String _nsURI;
    private String _localName;
    private String _value;
    private QName _qnameValue;
    private SchemaElement _parent;
}
