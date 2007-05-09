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

package org.jboss.com.sun.xml.ws.util.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

import org.jboss.com.sun.xml.ws.server.ServerRtException;
import org.jboss.com.sun.xml.ws.util.ByteArrayBuffer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;


import java.net.URL;
import java.util.Enumeration;
import javax.xml.ws.WebServiceException;
import com.sun.org.apache.xml.internal.resolver.CatalogManager;
import com.sun.org.apache.xml.internal.resolver.tools.CatalogResolver;
import org.xml.sax.EntityResolver;

/**
 * @author WS Development Team
 */
public class XmlUtil {

    public static String getPrefix(String s) {
        int i = s.indexOf(':');
        if (i == -1)
            return null;
        return s.substring(0, i);
    }

    public static String getLocalPart(String s) {
        int i = s.indexOf(':');
        if (i == -1)
            return s;
        return s.substring(i + 1);
    }



    public static String getAttributeOrNull(Element e, String name) {
        Attr a = e.getAttributeNode(name);
        if (a == null)
            return null;
        return a.getValue();
    }

    public static String getAttributeNSOrNull(
        Element e,
        String name,
        String nsURI) {
        Attr a = e.getAttributeNodeNS(nsURI, name);
        if (a == null)
            return null;
        return a.getValue();
    }

/*    public static boolean matchesTagNS(Element e, String tag, String nsURI) {
        try {
            return e.getLocalName().equals(tag)
                && e.getNamespaceURI().equals(nsURI);
        } catch (NullPointerException npe) {

            // localname not null since parsing would fail before here
            throw new WSDLParseException(
                "null.namespace.found",
                e.getLocalName());
        }
    }

    public static boolean matchesTagNS(
        Element e,
        javax.xml.namespace.QName name) {
        try {
            return e.getLocalName().equals(name.getLocalPart())
                && e.getNamespaceURI().equals(name.getNamespaceURI());
        } catch (NullPointerException npe) {

            // localname not null since parsing would fail before here
            throw new WSDLParseException(
                "null.namespace.found",
                e.getLocalName());
        }
    }*/

    public static Iterator getAllChildren(Element element) {
        return new NodeListIterator(element.getChildNodes());
    }

    public static Iterator getAllAttributes(Element element) {
        return new NamedNodeMapIterator(element.getAttributes());
    }

    public static List<String> parseTokenList(String tokenList) {
        List<String> result = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(tokenList, " ");
        while (tokenizer.hasMoreTokens()) {
            result.add(tokenizer.nextToken());
        }
        return result;
    }

    public static String getTextForNode(Node node) {
        StringBuffer sb = new StringBuffer();

        NodeList children = node.getChildNodes();
        if (children.getLength() == 0)
            return null;

        for (int i = 0; i < children.getLength(); ++i) {
            Node n = children.item(i);

            if (n instanceof Text)
                sb.append(n.getNodeValue());
            else if (n instanceof EntityReference) {
                String s = getTextForNode(n);
                if (s == null)
                    return null;
                else
                    sb.append(s);
            } else
                return null;
        }

        return sb.toString();
    }

    public static InputStream getUTF8Stream(String s) {
        try {
            ByteArrayBuffer bab = new ByteArrayBuffer();
            Writer w = new OutputStreamWriter(bab, "utf-8");
            w.write(s);
            w.close();
            return bab.newInputStream();
        } catch (IOException e) {
            throw new RuntimeException("should not happen");
        }
    }

    static final TransformerFactory transformerFactory = TransformerFactory.newInstance();

    public static Transformer newTransformer() {
        try {
            return transformerFactory.newTransformer();
        } catch (TransformerConfigurationException tex) {
            throw new IllegalStateException("Unable to create a JAXP transformer");
        }
    }
    
    /*
     * Gets an EntityResolver using XML catalog
     */
    public static EntityResolver createEntityResolver(URL catalogUrl) {
        // set up a manager
        CatalogManager manager = new CatalogManager();
        manager.setIgnoreMissingProperties(true);
        try {
            if (catalogUrl != null) {
                manager.getCatalog().parseCatalog(catalogUrl);
            }
        } catch (IOException e) {
            throw new ServerRtException("server.rt.err",e);
        }
        return new CatalogResolver(manager);
    }

    /**
     * Gets a default EntityResolver for catalog at META-INF/jaxws-catalog.xml
     */
    public static EntityResolver createDefaultCatalogResolver() {
    
        // set up a manager
        CatalogManager manager = new CatalogManager();
        manager.setIgnoreMissingProperties(true);

        // parse the catalog
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> catalogEnum;
        try {
            if (cl == null) {
                catalogEnum = ClassLoader.getSystemResources("META-INF/jax-ws-catalog.xml");
            } else {
                catalogEnum = cl.getResources("META-INF/jax-ws-catalog.xml");
            }

            while(catalogEnum.hasMoreElements()) {
                URL url = catalogEnum.nextElement();
                manager.getCatalog().parseCatalog(url);
            }
        } catch (IOException e) {
            throw new WebServiceException(e);
        }

        return new CatalogResolver(manager);
    }
}
