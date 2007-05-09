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
package org.jboss.com.sun.tools.ws.processor.config.parser;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.namespace.QName;

import org.jboss.com.sun.tools.ws.processor.config.Configuration;
import org.jboss.com.sun.tools.ws.processor.util.ProcessorEnvironment;
import org.jboss.com.sun.xml.ws.util.localization.LocalizableMessageFactory;
import org.xml.sax.InputSource;


/**
 * @author Vivek Pandey
 *
 *
 */
public abstract class InputParser{
    protected LocalizableMessageFactory _messageFactory =
        new LocalizableMessageFactory(
            "org.jboss.com.sun.tools.ws.resources.configuration");

    public InputParser(ProcessorEnvironment env, Properties options) {
        this._env = env;
        this._options = options;
        _modelInfoParsers = new HashMap<QName, Object>();

//        /*
//         * Load modelinfo parsers from the plugins which want to extend
//         * this functionality
//         */
//        Iterator i = ToolPluginFactory.getInstance().getExtensions(
//            ToolPluginConstants.WSCOMPILE_PLUGIN,
//            ToolPluginConstants.WSCOMPILE_MODEL_INFO_EXT_POINT);
//        while(i != null && i.hasNext()) {
//            ModelInfoPlugin plugin = (ModelInfoPlugin)i.next();
//            _modelInfoParsers.put(plugin.getModelInfoName(),
//                plugin.createModelInfoParser(env));
//        }
    }

    protected Configuration parse(InputStream is) throws Exception{
        //TODO: Not implemented exception
        return null;
    }

    protected Configuration parse(InputSource is) throws Exception{
        //TODO: Not implemented exception
        return null;
    }

    protected Configuration parse(List<String> inputSources) throws Exception{
        //TODO: Not implemented exception
        return null;
    }

    /**
     * @return Returns the _env.
     */
    public  ProcessorEnvironment getEnv(){
        return _env;
    }

    /**
     * @param env The ProcessorEnvironment to set.
     */
    public void setEnv(ProcessorEnvironment env){
        this._env = env;
    }

    protected void warn(String key) {
        _env.warn(_messageFactory.getMessage(key));
    }

    protected void warn(String key, String arg) {
        _env.warn(_messageFactory.getMessage(key, arg));
    }

    protected void warn(String key, Object[] args) {
        _env.warn(_messageFactory.getMessage(key, args));
    }

    protected void info(String key) {
        _env.info(_messageFactory.getMessage(key));
    }

    protected void info(String key, String arg) {
        _env.info(_messageFactory.getMessage(key, arg));
    }

    protected ProcessorEnvironment _env;
    protected Properties _options;
    protected Map<QName, Object> _modelInfoParsers;
}