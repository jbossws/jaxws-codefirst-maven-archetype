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

import java.util.Iterator;

import javax.xml.namespace.QName;

import org.jboss.com.sun.tools.ws.wsdl.framework.AbstractDocument;
import org.jboss.com.sun.tools.ws.wsdl.framework.Defining;
import org.jboss.com.sun.tools.ws.wsdl.framework.EntityAction;
import org.jboss.com.sun.tools.ws.wsdl.framework.EntityReferenceAction;
import org.jboss.com.sun.tools.ws.wsdl.framework.ExtensibilityHelper;
import org.jboss.com.sun.tools.ws.wsdl.framework.Extensible;
import org.jboss.com.sun.tools.ws.wsdl.framework.Extension;
import org.jboss.com.sun.tools.ws.wsdl.framework.GlobalEntity;
import org.jboss.com.sun.tools.ws.wsdl.framework.Kind;
import org.jboss.com.sun.tools.ws.wsdl.framework.QNameAction;


/**
 * Entity corresponding to the "port" WSDL element.
 *
 * @author WS Development Team
 */
public class Port extends GlobalEntity implements Extensible {

    public Port(Defining defining) {
        super(defining);
        _helper = new ExtensibilityHelper();
    }

    public Service getService() {
        return _service;
    }

    public void setService(Service s) {
        _service = s;
    }

    public QName getBinding() {
        return _binding;
    }

    public void setBinding(QName n) {
        _binding = n;
    }

    public Binding resolveBinding(AbstractDocument document) {
        return (Binding) document.find(Kinds.BINDING, _binding);
    }

    public Kind getKind() {
        return Kinds.PORT;
    }

    public QName getElementName() {
        return WSDLConstants.QNAME_PORT;
    }

    public Documentation getDocumentation() {
        return _documentation;
    }

    public void setDocumentation(Documentation d) {
        _documentation = d;
    }

    public void withAllQNamesDo(QNameAction action) {
        super.withAllQNamesDo(action);

        if (_binding != null) {
            action.perform(_binding);
        }
    }

    public void withAllEntityReferencesDo(EntityReferenceAction action) {
        super.withAllEntityReferencesDo(action);
        if (_binding != null) {
            action.perform(Kinds.BINDING, _binding);
        }
    }

    public void accept(WSDLDocumentVisitor visitor) throws Exception {
        visitor.preVisit(this);
        _helper.accept(visitor);
        visitor.postVisit(this);
    }

    public void validateThis() {
        if (getName() == null) {
            failValidation("validation.missingRequiredAttribute", "name");
        }
        if (_binding == null) {
            failValidation("validation.missingRequiredAttribute", "binding");
        }
    }

    public void addExtension(Extension e) {
        _helper.addExtension(e);
    }

    public Iterator extensions() {
        return _helper.extensions();
    }

    public void withAllSubEntitiesDo(EntityAction action) {
        _helper.withAllSubEntitiesDo(action);
    }

    private ExtensibilityHelper _helper;
    private Documentation _documentation;
    private Service _service;
    private QName _binding;
}
