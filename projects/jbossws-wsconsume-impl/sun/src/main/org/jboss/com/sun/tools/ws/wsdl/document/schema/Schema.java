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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.jboss.com.sun.tools.ws.wsdl.framework.AbstractDocument;
import org.jboss.com.sun.tools.ws.wsdl.framework.Defining;
import org.jboss.com.sun.tools.ws.wsdl.framework.Extension;
import org.jboss.com.sun.tools.ws.wsdl.framework.Kind;
import org.jboss.com.sun.tools.ws.wsdl.framework.ValidationException;
import org.jboss.com.sun.tools.ws.wsdl.parser.Constants;


/**
 *
 * @author WS Development Team
 */
public class Schema extends Extension implements Defining {

    public Schema(AbstractDocument document) {
        _document = document;
        _nsPrefixes = new HashMap();
        _definedEntities = new ArrayList();
    }

    public QName getElementName() {
        return SchemaConstants.QNAME_SCHEMA;
    }

    public SchemaElement getContent() {
        return _content;
    }

    public void setContent(SchemaElement entity) {
        _content = entity;
        _content.setSchema(this);
    }

    public void setTargetNamespaceURI(String uri) {
        _targetNamespaceURI = uri;
    }

    public String getTargetNamespaceURI() {
        return _targetNamespaceURI;
    }

    public void addPrefix(String prefix, String uri) {
        _nsPrefixes.put(prefix, uri);
    }

    public String getURIForPrefix(String prefix) {
        return (String) _nsPrefixes.get(prefix);
    }

    public Iterator prefixes() {
        return _nsPrefixes.keySet().iterator();
    }

    public void defineAllEntities() {
        if (_content == null) {
            throw new ValidationException(
                "validation.shouldNotHappen",
                "missing schema content");
        }

        for (Iterator iter = _content.children(); iter.hasNext();) {
            SchemaElement child = (SchemaElement) iter.next();
            if (child.getQName().equals(SchemaConstants.QNAME_ATTRIBUTE)) {
                QName name =
                    new QName(
                        _targetNamespaceURI,
                        child.getValueOfMandatoryAttribute(
                            Constants.ATTR_NAME));
                defineEntity(child, SchemaKinds.XSD_ATTRIBUTE, name);
            } else if (
                child.getQName().equals(
                    SchemaConstants.QNAME_ATTRIBUTE_GROUP)) {
                QName name =
                    new QName(
                        _targetNamespaceURI,
                        child.getValueOfMandatoryAttribute(
                            Constants.ATTR_NAME));
                defineEntity(child, SchemaKinds.XSD_ATTRIBUTE_GROUP, name);
            } else if (
                child.getQName().equals(SchemaConstants.QNAME_ELEMENT)) {
                QName name =
                    new QName(
                        _targetNamespaceURI,
                        child.getValueOfMandatoryAttribute(
                            Constants.ATTR_NAME));
                defineEntity(child, SchemaKinds.XSD_ELEMENT, name);
            } else if (child.getQName().equals(SchemaConstants.QNAME_GROUP)) {
                QName name =
                    new QName(
                        _targetNamespaceURI,
                        child.getValueOfMandatoryAttribute(
                            Constants.ATTR_NAME));
                defineEntity(child, SchemaKinds.XSD_GROUP, name);
            } else if (
                child.getQName().equals(SchemaConstants.QNAME_COMPLEX_TYPE)) {
                QName name =
                    new QName(
                        _targetNamespaceURI,
                        child.getValueOfMandatoryAttribute(
                            Constants.ATTR_NAME));
                defineEntity(child, SchemaKinds.XSD_TYPE, name);
            } else if (
                child.getQName().equals(SchemaConstants.QNAME_SIMPLE_TYPE)) {
                QName name =
                    new QName(
                        _targetNamespaceURI,
                        child.getValueOfMandatoryAttribute(
                            Constants.ATTR_NAME));
                defineEntity(child, SchemaKinds.XSD_TYPE, name);
            }
        }
    }

    public void defineEntity(SchemaElement element, Kind kind, QName name) {
        SchemaEntity entity = new SchemaEntity(this, element, kind, name);
        _document.define(entity);
        _definedEntities.add(entity);
    }

    public Iterator definedEntities() {
        return _definedEntities.iterator();
    }

    public void validateThis() {
        if (_content == null) {
            throw new ValidationException(
                "validation.shouldNotHappen",
                "missing schema content");
        }
    }

    public String asString(QName name) {
        if (name.getNamespaceURI().equals("")) {
            return name.getLocalPart();
        } else {
            // look for a prefix
            for (Iterator iter = prefixes(); iter.hasNext();) {
                String prefix = (String) iter.next();
                if (prefix.equals(name.getNamespaceURI())) {
                    return prefix + ":" + name.getLocalPart();
                }
            }

            // not found
            return null;
        }
    }

    private AbstractDocument _document;
    private String _targetNamespaceURI;
    private SchemaElement _content;
    private List _definedEntities;
    private Map _nsPrefixes;
}
