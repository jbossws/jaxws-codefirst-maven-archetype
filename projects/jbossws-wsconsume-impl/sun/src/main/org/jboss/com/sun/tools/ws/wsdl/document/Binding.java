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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.jboss.com.sun.tools.ws.wsdl.framework.AbstractDocument;
import org.jboss.com.sun.tools.ws.wsdl.framework.Defining;
import org.jboss.com.sun.tools.ws.wsdl.framework.Entity;
import org.jboss.com.sun.tools.ws.wsdl.framework.EntityAction;
import org.jboss.com.sun.tools.ws.wsdl.framework.EntityReferenceAction;
import org.jboss.com.sun.tools.ws.wsdl.framework.ExtensibilityHelper;
import org.jboss.com.sun.tools.ws.wsdl.framework.Extensible;
import org.jboss.com.sun.tools.ws.wsdl.framework.Extension;
import org.jboss.com.sun.tools.ws.wsdl.framework.GlobalEntity;
import org.jboss.com.sun.tools.ws.wsdl.framework.Kind;
import org.jboss.com.sun.tools.ws.wsdl.framework.QNameAction;


/**
 * Entity corresponding to the "binding" WSDL element.
 *
 * @author WS Development Team
 */
public class Binding extends GlobalEntity implements Extensible {

    public Binding(Defining defining) {
        super(defining);
        _operations = new ArrayList();
        _helper = new ExtensibilityHelper();
    }

    public void add(BindingOperation operation) {
        _operations.add(operation);
    }

    public Iterator operations() {
        return _operations.iterator();
    }

    public QName getPortType() {
        return _portType;
    }

    public void setPortType(QName n) {
        _portType = n;
    }

    public PortType resolvePortType(AbstractDocument document) {
        return (PortType) document.find(Kinds.PORT_TYPE, _portType);
    }

    public Kind getKind() {
        return Kinds.BINDING;
    }

    public QName getElementName() {
        return WSDLConstants.QNAME_BINDING;
    }

    public Documentation getDocumentation() {
        return _documentation;
    }

    public void setDocumentation(Documentation d) {
        _documentation = d;
    }

    public void withAllSubEntitiesDo(EntityAction action) {
        for (Iterator iter = _operations.iterator(); iter.hasNext();) {
            action.perform((Entity) iter.next());
        }
        _helper.withAllSubEntitiesDo(action);
    }

    public void withAllQNamesDo(QNameAction action) {
        super.withAllQNamesDo(action);

        if (_portType != null) {
            action.perform(_portType);
        }
    }

    public void withAllEntityReferencesDo(EntityReferenceAction action) {
        super.withAllEntityReferencesDo(action);
        if (_portType != null) {
            action.perform(Kinds.PORT_TYPE, _portType);
        }
    }

    public void accept(WSDLDocumentVisitor visitor) throws Exception {
        visitor.preVisit(this);
        //bug fix: 4947340, extensions should be the first element
        _helper.accept(visitor);
        for (Iterator iter = _operations.iterator(); iter.hasNext();) {
            ((BindingOperation) iter.next()).accept(visitor);
        }
        visitor.postVisit(this);
    }

    public void validateThis() {
        if (getName() == null) {
            failValidation("validation.missingRequiredAttribute", "name");
        }
        if (_portType == null) {
            failValidation("validation.missingRequiredAttribute", "type");
        }
    }

    public void addExtension(Extension e) {
        _helper.addExtension(e);
    }

    public Iterator extensions() {
        return _helper.extensions();
    }

    private ExtensibilityHelper _helper;
    private Documentation _documentation;
    private QName _portType;
    private List _operations;
}
