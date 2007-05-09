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
package org.jboss.com.sun.xml.ws.transport.http.servlet;

import java.io.InputStream;
import java.net.URL;
import javax.servlet.ServletContext;

import org.jboss.com.sun.xml.ws.server.DocInfo;
import org.jboss.com.sun.xml.ws.wsdl.parser.Service;

public class ServletDocInfo implements DocInfo {
    private ServletContext context;
    private String resource;
    private String queryString;
    private DOC_TYPE docType;
    private Service service;
    private boolean hasPortType;
    private String tns;

    public ServletDocInfo(ServletContext context, String resource) {
        this.context = context;
        this.resource = resource;
    }
    
    public InputStream getDoc() {
        return context.getResourceAsStream(resource);
    }
    
    public String getPath() {
        return resource;
    }
    
    public URL getUrl() {
        try {
            return context.getResource(resource);
        } catch(Exception e) {
            return null;
        }
    }
    
    public String getQueryString() {
        return queryString;
    }
    
    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }
    
    public void setDocType(DOC_TYPE docType) {
        this.docType = docType;
    }
    
    public DOC_TYPE getDocType() {
        return docType;
    }

    public void setTargetNamespace(String ns) {
        this.tns = ns;
    }
    
    public String getTargetNamespace() {
        return tns;
    }
    
    public void setService(Service service) {
        this.service = service;
    }
    
    public Service getService() {
        return service;
    }
    
    public void setHavingPortType(boolean portType) {
        this.hasPortType = portType;
    }
    
    public boolean isHavingPortType() {
        return hasPortType;
    }
    
}
