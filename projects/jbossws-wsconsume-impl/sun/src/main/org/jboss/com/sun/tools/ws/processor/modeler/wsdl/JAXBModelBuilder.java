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
package org.jboss.com.sun.tools.ws.processor.modeler.wsdl;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.xml.namespace.QName;

import org.jboss.com.sun.tools.ws.processor.ProcessorOptions;
import org.jboss.com.sun.tools.ws.processor.config.ModelInfo;
import org.jboss.com.sun.tools.ws.processor.config.WSDLModelInfo;
import org.jboss.com.sun.tools.ws.processor.model.ModelException;
import org.jboss.com.sun.tools.ws.processor.model.java.JavaSimpleType;
import org.jboss.com.sun.tools.ws.processor.model.java.JavaType;
import org.jboss.com.sun.tools.ws.processor.model.jaxb.JAXBMapping;
import org.jboss.com.sun.tools.ws.processor.model.jaxb.JAXBModel;
import org.jboss.com.sun.tools.ws.processor.model.jaxb.JAXBType;
import org.jboss.com.sun.tools.ws.processor.modeler.JavaSimpleTypeCreator;
import org.jboss.com.sun.tools.ws.processor.util.ClassNameCollector;
import org.jboss.com.sun.tools.ws.processor.util.ProcessorEnvironment;
import org.jboss.com.sun.xml.ws.util.JAXWSUtils;
import org.jboss.com.sun.xml.ws.util.localization.LocalizableMessageFactory;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

import com.sun.tools.xjc.api.ErrorListener;
import com.sun.tools.xjc.api.SchemaCompiler;
import com.sun.tools.xjc.api.XJC;
import com.sun.tools.xjc.api.TypeAndAnnotation;

/**
 * @author Kathy Walsh, Vivek Pandey
 *
 * Uses JAXB XJC apis to build JAXBModel and resolves xml to java type mapping from JAXBModel
 */
public class JAXBModelBuilder {
    public JAXBModelBuilder(ModelInfo modelInfo,
                            Properties options, ClassNameCollector classNameCollector, List elements) {
        _messageFactory =
            new LocalizableMessageFactory("org.jboss.com.sun.tools.ws.resources.model");
        _modelInfo = modelInfo;
        _env = (ProcessorEnvironment) modelInfo.getParent().getEnvironment();
        _classNameAllocator = new ClassNameAllocatorImpl(classNameCollector);

        printstacktrace = Boolean.valueOf(options.getProperty(ProcessorOptions.PRINT_STACK_TRACE_PROPERTY));
        consoleErrorReporter = new ConsoleErrorReporter(_env, false);
        internalBuildJAXBModel(elements);
    }

    /**
     * Builds model from WSDL document. Model contains abstraction which is used by the
     * generators to generate the stub/tie/serializers etc. code.
     *
     * @see org.jboss.com.sun.tools.ws.processor.modeler.Modeler#buildModel()
     */

    private void internalBuildJAXBModel(List elements){
        try {
            schemaCompiler = XJC.createSchemaCompiler();
            schemaCompiler.setClassNameAllocator(_classNameAllocator);
            schemaCompiler.setErrorListener(consoleErrorReporter);
            schemaCompiler.setEntityResolver(_modelInfo.getEntityResolver());
            int schemaElementCount = 1;
            for(Iterator iter = elements.iterator(); iter.hasNext();){
                Element schemaElement = (Element)iter.next();
                String location = schemaElement.getOwnerDocument().getDocumentURI();
                String systemId = new String(location + "#types?schema"+schemaElementCount++);
                schemaCompiler.parseSchema(systemId,schemaElement);
            }

            //feed external jaxb:bindings file
            Set<InputSource> externalBindings = ((WSDLModelInfo)_modelInfo).getJAXBBindings();
            if(externalBindings != null){
                for(InputSource jaxbBinding : externalBindings){
                    schemaCompiler.parseSchema(jaxbBinding);
                }
            }
        } catch (Exception e) {
            throw new ModelException(e);
        }
    }

    public JAXBType  getJAXBType(QName qname){
        JAXBMapping mapping = jaxbModel.get(qname);
        if (mapping == null){
            fail("model.schema.elementNotFound", new Object[]{qname});
        }

        JavaType javaType = new JavaSimpleType(mapping.getType());
        JAXBType type =  new JAXBType(qname, javaType, mapping, jaxbModel);
        return type;
    }

    public TypeAndAnnotation getElementTypeAndAnn(QName qname){
        JAXBMapping mapping = jaxbModel.get(qname);
        if (mapping == null){
            fail("model.schema.elementNotFound", new Object[]{qname});
        }
        return mapping.getType().getTypeAnn();
    }

    protected void bind(){
        com.sun.tools.xjc.api.JAXBModel rawJaxbModel = schemaCompiler.bind();
        if(consoleErrorReporter.hasError()){
            throw new ModelException(consoleErrorReporter.getException());
        }
        jaxbModel = new JAXBModel(rawJaxbModel);
        jaxbModel.setGeneratedClassNames(_classNameAllocator.getJaxbGeneratedClasses());
    }

    protected SchemaCompiler getJAXBSchemaCompiler(){
        return schemaCompiler;
    }

    protected void fail(String key, Object[] arg) {
        throw new ModelException(key, arg);
    }

    protected void error(String key, Object[] args){
        _env.error(_messageFactory.getMessage(key, args));
    }

    protected void warn(String key, Object[] args) {
        _env.warn(_messageFactory.getMessage(key, args));
    }

    protected void inform(String key, Object[] args) {
        _env.info(_messageFactory.getMessage(key, args));
    }

    public JAXBModel getJAXBModel(){
        return jaxbModel;
    }

    private JAXBModel jaxbModel;
    private SchemaCompiler schemaCompiler;
    private final LocalizableMessageFactory _messageFactory;
    private final ModelInfo _modelInfo;
    private final ProcessorEnvironment _env;
    private final boolean printstacktrace;
    private final ClassNameAllocatorImpl _classNameAllocator;
    private final ConsoleErrorReporter consoleErrorReporter;
}
