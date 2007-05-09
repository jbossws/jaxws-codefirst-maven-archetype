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

package org.jboss.com.sun.xml.ws.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.ws.WebServiceException;
import javax.xml.ws.http.HTTPException;
import javax.xml.soap.MimeHeader;
import javax.xml.soap.MimeHeaders;                                           
import java.io.ByteArrayInputStream;
import java.net.HttpURLConnection;
import javax.xml.transform.stream.StreamSource;

import org.jboss.com.sun.xml.ws.encoding.xml.XMLMessage;
import org.jboss.com.sun.xml.ws.pept.ept.MessageInfo;
import org.jboss.com.sun.xml.ws.spi.runtime.WSConnection;

import static org.jboss.com.sun.xml.ws.developer.JAXWSProperties.CONTENT_NEGOTIATION_PROPERTY;

/**
 * @author WS Development Team
 */
public class XMLConnectionUtil {

    public static XMLMessage getXMLMessage(WSConnection con, MessageInfo mi) {
        try {
            Map<String, List<String>> headers = con.getHeaders();
            MimeHeaders mh = new MimeHeaders();
            if (headers != null) {
                for (Map.Entry<String, List<String>> entry : headers.entrySet())
                {
                    String name = entry.getKey();
                    for (String value : entry.getValue()) {
                        try {
                            mh.addHeader(name, value);
                        } catch (IllegalArgumentException ie) {
                            // Not a mime header. Ignore it.
                        }
                    }
                }
            }
            return new XMLMessage(mh, con.getInput());
        } catch (Exception e) {
            throw (HTTPException)new HTTPException(HttpURLConnection.HTTP_INTERNAL_ERROR).initCause(e);
        }
    }

    private static void send(WSConnection con, XMLMessage xmlMessage) {
        try {
            Map<String, List<String>> headers = new HashMap<String, List<String>>();
            MimeHeaders mhs = xmlMessage.getMimeHeaders();
            Iterator i = mhs.getAllHeaders();
            while (i.hasNext()) {
                MimeHeader mh = (MimeHeader) i.next();
                String name = mh.getName();
                List<String> values = headers.get(name);
                if (values == null) {
                    values = new ArrayList<String>();
                    headers.put(name, values);
                }
                values.add(mh.getValue());
            }
            con.setHeaders(headers);
            xmlMessage.writeTo(con.getOutput());

        } catch (Exception e) {
            throw (HTTPException)new HTTPException(HttpURLConnection.HTTP_INTERNAL_ERROR).initCause(e);
        }
        try {
            con.closeOutput();
        } catch (Exception e) {
            throw (HTTPException)new HTTPException(HttpURLConnection.HTTP_INTERNAL_ERROR).initCause(e);
        }
    }

    public static void sendResponse(WSConnection con, XMLMessage xmlMessage) {
        setStatus(con, xmlMessage.getStatus());
        send(con, xmlMessage);
    }

    public static void sendResponseOneway(WSConnection con) {
        setStatus(con, WSConnection.ONEWAY);
        con.getOutput();
        con.closeOutput();
    }

    public static void sendResponseError(WSConnection con, MessageInfo messageInfo) {
        try {
            StreamSource source = new StreamSource(
                new ByteArrayInputStream(DEFAULT_SERVER_ERROR.getBytes()));
            String conneg = (String) messageInfo.getMetaData(CONTENT_NEGOTIATION_PROPERTY);
            XMLMessage message = new XMLMessage(source, conneg == "optimistic");
            setStatus(con, WSConnection.INTERNAL_ERR);
            send(con, message);
        }
        catch (Exception e) {
            throw new WebServiceException(e);
        }
    }

    public static Map<String, List<String>> getHeaders(WSConnection con) {
        return con.getHeaders();
    }

    /**
     * sets response headers.
     */
    public static void setHeaders(WSConnection con,
                                  Map<String, List<String>> headers) {
        con.setHeaders(headers);
    }

    public static void setStatus(WSConnection con, int status) {
        con.setStatus(status);
    }

    private final static String DEFAULT_SERVER_ERROR =
        "<?xml version='1.0' encoding='UTF-8'?>"
            + "<err>Internal Server Error</err>";

}
