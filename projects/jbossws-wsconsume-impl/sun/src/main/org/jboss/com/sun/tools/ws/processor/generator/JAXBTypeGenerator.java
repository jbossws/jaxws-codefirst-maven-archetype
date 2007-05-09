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
package org.jboss.com.sun.tools.ws.processor.generator;

import java.util.Properties;

import org.jboss.com.sun.tools.ws.processor.ProcessorOptions;
import org.jboss.com.sun.tools.ws.processor.config.Configuration;
import org.jboss.com.sun.tools.ws.processor.model.Model;
import org.jboss.com.sun.tools.ws.processor.model.jaxb.JAXBType;
import org.jboss.com.sun.tools.ws.processor.model.jaxb.RpcLitStructure;
import org.jboss.com.sun.tools.ws.processor.modeler.wsdl.ConsoleErrorReporter;
import org.jboss.com.sun.tools.ws.wscompile.WSCodeWriter;
import org.jboss.com.sun.xml.ws.encoding.soap.SOAPVersion;
import org.xml.sax.SAXParseException;

import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.writer.ProgressCodeWriter;
//import com.sun.tools.xjc.addon.Augmenter;
import com.sun.tools.xjc.api.ErrorListener;
import com.sun.tools.xjc.api.JAXBModel;
import com.sun.tools.xjc.api.S2JJAXBModel;

/**
 * @author Vivek Pandey
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class JAXBTypeGenerator extends GeneratorBase {

    /**
     * @author Vivek Pandey
     *
     * To change the template for this generated type comment go to
     * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
     */
    public static class JAXBErrorListener implements ErrorListener {

        /**
         *
         */
        public JAXBErrorListener() {
            super();
        }

        /* (non-Javadoc)
         * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
         */
        public void error(SAXParseException arg0) {
            // TODO Auto-generated method stub

        }

        /* (non-Javadoc)
         * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
         */
        public void fatalError(SAXParseException arg0) {
            // TODO Auto-generated method stub

        }

        /* (non-Javadoc)
         * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
         */
        public void warning(SAXParseException arg0) {
            // TODO Auto-generated method stub

        }

        /* (non-Javadoc)
         * @see com.sun.tools.xjc.api.ErrorListener#info(org.xml.sax.SAXParseException)
         */
        public void info(SAXParseException arg0) {
            // TODO Auto-generated method stub

        }

    }
    /**
     *
     */
    public JAXBTypeGenerator() {
        super();
        // TODO Auto-generated constructor stub
    }
    /**
     * @param model
     * @param config
     * @param properties
     */
    public JAXBTypeGenerator(Model model, Configuration config,
            Properties properties) {
        super(model, config, properties);
    }
    /* (non-Javadoc)
     * @see GeneratorBase#getGenerator(org.jboss.com.sun.xml.ws.processor.model.Model, org.jboss.com.sun.xml.ws.processor.config.Configuration, java.util.Properties)
     */
    public GeneratorBase getGenerator(Model model, Configuration config,
            Properties properties) {
        return new JAXBTypeGenerator(model, config, properties);
    }
    /* (non-Javadoc)
     * @see cGeneratorBase#getGenerator(org.jboss.com.sun.xml.ws.processor.model.Model, org.jboss.com.sun.xml.ws.processor.config.Configuration, java.util.Properties, org.jboss.com.sun.xml.ws.soap.SOAPVersion)
     */
    public GeneratorBase getGenerator(Model model, Configuration config,
            Properties properties, SOAPVersion ver) {
        return new JAXBTypeGenerator(model, config, properties);
    }

    /* (non-Javadoc)
     * @see JAXBTypeVisitor#visit(JAXBType)
     */
    public void visit(JAXBType type) throws Exception {
        //this is a raw type, probably from rpclit
        if(type.getJaxbModel() == null)
            return;
        S2JJAXBModel model = type.getJaxbModel().getS2JJAXBModel();
        if (model != null)
            generateJAXBClasses(model);
    }


    /* (non-Javadoc)
     * @see JAXBTypeVisitor#visit(org.jboss.com.sun.xml.ws.processor.model.jaxb.RpcLitStructure)
     */
    public void visit(RpcLitStructure type) throws Exception {
        S2JJAXBModel model = type.getJaxbModel().getS2JJAXBModel();
        generateJAXBClasses(model);
    }

    private static boolean doneGeneration = true;
    private void generateJAXBClasses(S2JJAXBModel model) throws Exception{
        if(doneGeneration)
            return;
        JCodeModel cm = null;

        // get the list of jaxb source files
        CodeWriter cw = new WSCodeWriter(sourceDir,env);

        if(env.verbose())
            cw = new ProgressCodeWriter(cw, System.out); // TODO this should not be System.out, should be
                                                         // something from ProcessorEnvironment
        //TODO:set package level javadoc in JPackage        
        cm = model.generateCode(null, new ConsoleErrorReporter(env, printStackTrace));
        cm.build(cw);
        doneGeneration = true;
    }


}
