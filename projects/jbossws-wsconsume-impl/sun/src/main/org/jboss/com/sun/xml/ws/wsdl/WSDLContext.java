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
package org.jboss.com.sun.xml.ws.wsdl;

import org.jboss.com.sun.xml.ws.wsdl.parser.*;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPBinding;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * $author: JAXWS Development Team
 */
public class WSDLContext {
    private final URL orgWsdlLocation;
    private String targetNamespace;
    private final WSDLDocument wsdlDoc;

    /**
     * Creates a {@link WSDLContext} by parsing the given wsdl file.
     */
    public WSDLContext(URL wsdlDocumentLocation, EntityResolver entityResolver) throws WebServiceException {
        //must get binding information
        assert entityResolver != null;

        if (wsdlDocumentLocation == null)
            throw new WebServiceException("No WSDL location Information present, error");

        orgWsdlLocation = wsdlDocumentLocation;
        try {
            wsdlDoc = RuntimeWSDLParser.parse(wsdlDocumentLocation, entityResolver);
        } catch (IOException e) {
            throw new WebServiceException(e);
        } catch (XMLStreamException e) {
            throw new WebServiceException(e);
        } catch (SAXException e) {
            throw new WebServiceException(e);
        }        
    }

    public URL getWsdlLocation() {
        return orgWsdlLocation;
    }

    public String getOrigURLPath() {
        return orgWsdlLocation.getPath();
    }

    public QName getServiceQName() {
        return wsdlDoc.getFirstServiceName();
    }

    public boolean contains(QName serviceName) {
        return (wsdlDoc.getServices().containsKey(serviceName));
    }

    //just get the first one for now
    public String getEndpoint(QName serviceName) {
        if (serviceName == null)
            throw new WebServiceException("Service unknown, can not identify ports for an unknown Service.");
        Service service = wsdlDoc.getService(serviceName);
        String endpoint = null;
        if (service != null) {
            Iterator<Map.Entry<QName, Port>> iter = service.entrySet().iterator();
            if (iter.hasNext()) {
                Port port = iter.next().getValue();
                endpoint = port.getAddress();
            }
        }
        if (endpoint == null)
            throw new WebServiceException("Endpoint not found. Check WSDL file to verify endpoint was provided.");
        return endpoint;
    }

    //just get the first one for now
    public QName getPortName() {
        return wsdlDoc.getFirstPortName();
    }

    public String getBindingID(QName serviceName, QName portName) {
        return getWsdlDocument().getBindingId(serviceName, portName);
    }

    public String getTargetNamespace() {
        return targetNamespace;
    }

    public void setTargetNamespace(String tns) {
        targetNamespace = tns;
    }

    public Set<QName> getPortsAsSet(QName serviceName) {
        Service service = wsdlDoc.getService(serviceName);
        if (service != null) {
            return service.keySet();
        }
        return null;
    }


    public boolean contains(QName serviceName, QName portName) {
        Service service = wsdlDoc.getService(serviceName);
        if (service != null) {

            Iterator<Map.Entry<QName, Port>> iter = service.entrySet().iterator();
            while (iter.hasNext()) {
                Port port = iter.next().getValue();
                if (port.getName().equals(portName))
                    return true;
            }
        }
        return false;
    }

    public QName getFirstServiceName() {
        return wsdlDoc.getFirstServiceName();
    }

    public Set<QName> getAllServiceNames() {
        return wsdlDoc.getServices().keySet();
    }

    public WSDLDocument getWsdlDocument() {
        return wsdlDoc;
    }

    public Binding getWsdlBinding(QName service, QName port) {
        if (wsdlDoc == null)
            return null;
        return wsdlDoc.getBinding(service, port);
    }

    public String getEndpoint(QName serviceName, QName portQName) {
        Service service = wsdlDoc.getService(serviceName);
        if (service != null) {
            Port p = service.get(portQName);
            if (p != null)
                return p.getAddress();
            else
                throw new WebServiceException("No ports found for service " + serviceName);
        } else {
            throw new WebServiceException("Service unknown, can not identify ports for an unknown Service.");
        }
    }
        
}
