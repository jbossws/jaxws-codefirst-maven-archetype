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

package org.jboss.com.sun.tools.ws.wsdl.parser;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.xml.namespace.QName;

import org.jboss.com.sun.tools.ws.wsdl.document.schema.Schema;
import org.jboss.com.sun.tools.ws.wsdl.document.schema.SchemaAttribute;
import org.jboss.com.sun.tools.ws.wsdl.document.schema.SchemaDocument;
import org.jboss.com.sun.tools.ws.wsdl.document.schema.SchemaElement;
import org.jboss.com.sun.tools.ws.wsdl.framework.WriterContext;


/**
 * A writer for XML Schema fragments within a WSDL document.
 *
 * @author WS Development Team
 */
public class SchemaWriter {

    public SchemaWriter() {
    }

    public void write(SchemaDocument document, OutputStream os)
        throws IOException {
        WriterContext context = new WriterContext(os);
        writeSchema(context, document.getSchema());
        context.flush();
    }

    public void writeSchema(WriterContext context, Schema schema)
        throws IOException {
        context.push();
        try {
            writeTopSchemaElement(context, schema);
        } catch (Exception e) {
        } finally {
            context.pop();
        }
    }

    protected void writeTopSchemaElement(WriterContext context, Schema schema)
        throws IOException {
        SchemaElement schemaElement = schema.getContent();
        QName name = schemaElement.getQName();

        // make sure that all namespaces we expect are actually declared
        for (Iterator iter = schema.prefixes(); iter.hasNext();) {
            String prefix = (String) iter.next();
            String expectedURI = schema.getURIForPrefix(prefix);
            if (!expectedURI.equals(context.getNamespaceURI(prefix))) {
                context.declarePrefix(prefix, expectedURI);
            }
        }

        for (Iterator iter = schemaElement.prefixes(); iter.hasNext();) {
            String prefix = (String) iter.next();
            String uri = schemaElement.getURIForPrefix(prefix);
            context.declarePrefix(prefix, uri);
        }

        context.writeStartTag(name);

        for (Iterator iter = schemaElement.attributes(); iter.hasNext();) {
            SchemaAttribute attribute = (SchemaAttribute) iter.next();
            if (attribute.getNamespaceURI() == null) {
                context.writeAttribute(
                    attribute.getLocalName(),
                    attribute.getValue(context));
            } else {
                context.writeAttribute(
                    context.getQNameString(attribute.getQName()),
                    attribute.getValue(context));
            }
        }

        context.writeAllPendingNamespaceDeclarations();

        for (Iterator iter = schemaElement.children(); iter.hasNext();) {
            SchemaElement child = (SchemaElement) iter.next();
            writeSchemaElement(context, child);
        }

        context.writeEndTag(name);
    }

    protected void writeSchemaElement(
        WriterContext context,
        SchemaElement schemaElement)
        throws IOException {
        QName name = schemaElement.getQName();

        if (schemaElement.declaresPrefixes()) {
            context.push();
        }

        context.writeStartTag(name);

        if (schemaElement.declaresPrefixes()) {
            for (Iterator iter = schemaElement.prefixes(); iter.hasNext();) {
                String prefix = (String) iter.next();
                String uri = schemaElement.getURIForPrefix(prefix);
                context.writeNamespaceDeclaration(prefix, uri);
                context.declarePrefix(prefix, uri);
            }
        }

        for (Iterator iter = schemaElement.attributes(); iter.hasNext();) {
            SchemaAttribute attribute = (SchemaAttribute) iter.next();
            if (attribute.getNamespaceURI() == null) {
                context.writeAttribute(
                    attribute.getLocalName(),
                    attribute.getValue(context));
            } else {
                context.writeAttribute(
                    context.getQNameString(attribute.getQName()),
                    attribute.getValue(context));
            }
        }

        for (Iterator iter = schemaElement.children(); iter.hasNext();) {
            SchemaElement child = (SchemaElement) iter.next();
            writeSchemaElement(context, child);
        }

        context.writeEndTag(name);

        if (schemaElement.declaresPrefixes()) {
            context.pop();
        }
    }
}
