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
package org.jboss.com.sun.xml.ws.encoding.jaxb;

import com.sun.xml.bind.api.BridgeContext;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.Source;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.XMLStreamReader;

import org.jboss.com.sun.xml.ws.encoding.soap.SerializationException;

import java.io.OutputStream;

/**
 * XML infoset represented as a JAXB object.
 *
 * @author WS Development Team
 */
public final class JAXBBeanInfo {
    private final Object jaxbBean;
    private JAXBContext jaxbContext;
    private BridgeContext bc;
    private Marshaller marshaller;
    private Unmarshaller unmarshaller;

    public JAXBBeanInfo(Object payload, JAXBContext jaxbContext) {
        this.jaxbBean = payload;
        this.jaxbContext = jaxbContext;
    }

    public static JAXBBeanInfo fromSource(Source source, JAXBContext context) {
        Object obj = JAXBTypeSerializer.deserialize(source, context);
        return new JAXBBeanInfo(obj, context);
    }

    public static JAXBBeanInfo fromStAX(XMLStreamReader reader, JAXBContext context) {

        Object obj = JAXBTypeSerializer.deserialize(reader, context);
        return new JAXBBeanInfo(obj, context);
    }

    public static JAXBBeanInfo fromStAX(XMLStreamReader reader, JAXBContext context, Unmarshaller um) {

        Object obj = JAXBTypeSerializer.deserialize(reader, context, um);
        return new JAXBBeanInfo(obj, context);
    }



    public Object getBean() {
        return jaxbBean;
    }

    public JAXBContext getJAXBContext() {
        return jaxbContext;
    }

    /**
     * Creates a {@link DOMSource} from this JAXB bean.
     */
    public DOMSource toDOMSource() {
        return JAXBTypeSerializer.serialize(jaxbBean,jaxbContext);
    }

    /**
     * Writes this bean to StAX.
     */
        public void writeTo(XMLStreamWriter w) {
            if (marshaller != null)
                JAXBTypeSerializer.serialize(jaxbBean, w, jaxbContext, marshaller);
            else
            JAXBTypeSerializer.serialize(jaxbBean, w, jaxbContext);
        }

        public void writeTo(OutputStream os) {
            if (marshaller != null)
                JAXBTypeSerializer.serialize(jaxbBean, os, jaxbContext, marshaller);
            else
             JAXBTypeSerializer.serialize(jaxbBean,os,jaxbContext);
        }

    public void setMarshallers(Marshaller m, Unmarshaller u) {
        this.marshaller = m;
        this.unmarshaller = u;
    }
}
