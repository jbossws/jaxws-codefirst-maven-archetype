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
package org.jboss.com.sun.tools.ws.processor.modeler.annotation;

import java.io.File;


import java.net.URL;
import java.util.Properties;

import com.sun.mirror.apt.*;
import com.sun.mirror.declaration.*;
import com.sun.mirror.type.*;
import com.sun.mirror.util.*;

import javax.xml.namespace.QName;

import org.jboss.com.sun.tools.ws.processor.model.Port;
import org.jboss.com.sun.tools.ws.processor.model.Service;
import org.jboss.com.sun.tools.ws.processor.modeler.ModelerException;
import org.jboss.com.sun.tools.ws.processor.modeler.annotation.*;
import org.jboss.com.sun.tools.ws.processor.util.ProcessorEnvironment;
import org.jboss.com.sun.xml.ws.util.localization.Localizable;

/**
 *
 * @author WS Development Team
 */
public interface ModelBuilder {
    public AnnotationProcessorEnvironment getAPEnv();
    public void createModel(TypeDeclaration d, QName modelName, String targetNamespace, String modelerClassName);
    public void setService(Service service);
    public void setPort(Port port);
    public String getOperationName(String methodName);
    public String getResponseName(String operationName);
    public TypeMirror getHolderValueType(TypeMirror type);
    public boolean checkAndSetProcessed(TypeDeclaration typeDecl);
    public boolean isRemoteException(TypeDeclaration typeDecl);
    public boolean isRemote(TypeDeclaration typeDecl);
    public boolean canOverWriteClass(String className);
    public void setWrapperGenerated(boolean wrapperGenerated);
    public TypeDeclaration getTypeDeclaration(String typeName);
    public String getSourceVersion();
    public ProcessorEnvironment getProcessorEnvironment();
    public File getSourceDir();
    public String getXMLName(String javaName);
    public void onError(String key);
    public void onError(String key, Object[] args) throws ModelerException;
    public void onError(SourcePosition srcPos, String key, Object[] args) throws ModelerException;
    public void onError(Localizable msg) throws ModelerException;
    public void onWarning(Localizable msg);
    public void onInfo(Localizable msg);
    public void log(String msg);
}
