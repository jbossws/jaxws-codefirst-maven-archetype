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

package org.jboss.com.sun.tools.ws.processor.config;

import java.util.Properties;


import org.jboss.com.sun.tools.ws.processor.model.Model;
import org.jboss.com.sun.tools.ws.processor.modeler.Modeler;
import org.jboss.com.sun.xml.ws.util.xml.XmlUtil;
import org.xml.sax.EntityResolver;

/**
 * This class contiains information used by {@link org.jboss.com.sun.tools.ws.processor.modeler.Modeler
 * Modelers} to build {@link org.jboss.com.sun.tools.ws.processor.model.Model Models}.
 *
 * @author WS Development Team
 */
public abstract class ModelInfo {

    protected ModelInfo() {}

    public Configuration getParent() {
        return _parent;
    }

    public void setParent(Configuration c) {
        _parent = c;
    }

    public String getName() {
        return _name;
    }

    public void setName(String s) {
        _name = s;
    }

    public Configuration getConfiguration() {
        return _parent;
    }

    public HandlerChainInfo getClientHandlerChainInfo() {
        return _clientHandlerChainInfo;
    }

    public void setClientHandlerChainInfo(HandlerChainInfo i) {
        _clientHandlerChainInfo = i;
    }

    public HandlerChainInfo getServerHandlerChainInfo() {
        return _serverHandlerChainInfo;
    }

    public void setServerHandlerChainInfo(HandlerChainInfo i) {
        _serverHandlerChainInfo = i;
    }

    public String getJavaPackageName() {
        return _javaPackageName;
    }

    public void setJavaPackageName(String s) {
        _javaPackageName = s;
    }

    public Model buildModel(Properties options){
        return getModeler(options).buildModel();
    }

    public EntityResolver getEntityResolver() {
        return entityResolver;
    }

    public void setEntityResolver(EntityResolver entityResolver) {
        this.entityResolver = entityResolver;
    }

    public String getDefaultJavaPackage() {
        return _defaultJavaPackage;
    }

    public void setDefaultJavaPackage(String _defaultJavaPackage) {
        this._defaultJavaPackage = _defaultJavaPackage;
    }

    protected abstract Modeler getModeler(Properties options);

    private Configuration _parent;
    private String _name;
    private String _javaPackageName;
    private String _defaultJavaPackage;
    private HandlerChainInfo _clientHandlerChainInfo;
    private HandlerChainInfo _serverHandlerChainInfo;
    private EntityResolver entityResolver;
}
