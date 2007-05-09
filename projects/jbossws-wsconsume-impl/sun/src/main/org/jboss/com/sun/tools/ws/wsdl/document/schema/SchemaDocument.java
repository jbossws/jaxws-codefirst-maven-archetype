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

import java.util.Set;

import javax.xml.namespace.QName;

import org.jboss.com.sun.tools.ws.wsdl.framework.AbstractDocument;
import org.jboss.com.sun.tools.ws.wsdl.framework.Entity;
import org.jboss.com.sun.tools.ws.wsdl.framework.EntityAction;
import org.jboss.com.sun.tools.ws.wsdl.framework.EntityReferenceAction;
import org.jboss.com.sun.tools.ws.wsdl.framework.EntityReferenceValidator;
import org.jboss.com.sun.tools.ws.wsdl.framework.GloballyKnown;
import org.jboss.com.sun.tools.ws.wsdl.framework.Kind;
import org.jboss.com.sun.tools.ws.wsdl.framework.NoSuchEntityException;
import org.jboss.com.sun.tools.ws.wsdl.framework.ValidationException;


/**
 * A XML Schema document.
 *
 * @author WS Development Team
 */
public class SchemaDocument extends AbstractDocument {

    public SchemaDocument() {
    }

    public Schema getSchema() {
        return _schema;
    }

    public void setSchema(Schema s) {
        _schema = s;
    }

    public Set collectAllNamespaces() {
        Set result = super.collectAllNamespaces();
        if (_schema.getTargetNamespaceURI() != null) {
            result.add(_schema.getTargetNamespaceURI());
        }
        return result;
    }

    public void validate(EntityReferenceValidator validator) {
        GloballyValidatingAction action =
            new GloballyValidatingAction(this, validator);
        withAllSubEntitiesDo(action);
        if (action.getException() != null) {
            throw action.getException();
        }
    }

    protected Entity getRoot() {
        return _schema;
    }

    private Schema _schema;

    private class GloballyValidatingAction
        implements EntityAction, EntityReferenceAction {
        public GloballyValidatingAction(
            AbstractDocument document,
            EntityReferenceValidator validator) {
            _document = document;
            _validator = validator;
        }

        public void perform(Entity entity) {
            try {
                entity.validateThis();
                entity.withAllEntityReferencesDo(this);
                entity.withAllSubEntitiesDo(this);
            } catch (ValidationException e) {
                if (_exception == null) {
                    _exception = e;
                }
            }
        }

        public void perform(Kind kind, QName name) {
            try {
                GloballyKnown entity = _document.find(kind, name);
            } catch (NoSuchEntityException e) {
                // failed to resolve, check with the validator
                if (_exception == null) {
                    if (_validator == null
                        || !_validator.isValid(kind, name)) {
                        _exception = e;
                    }
                }
            }
        }

        public ValidationException getException() {
            return _exception;
        }

        private ValidationException _exception;
        private AbstractDocument _document;
        private EntityReferenceValidator _validator;
    }
}
