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

package org.jboss.com.sun.xml.ws.spi.runtime;

import javax.xml.stream.XMLStreamWriter;
import java.io.OutputStream;

/**
 * SOAPEncoder to encode JAXWS runtime objects. Using this caller could optimize
 * SOAPMessage creation and not use JAXWS default encoding of SOAPMessage
 */
public interface InternalSoapEncoder {
    /**
     *  Writes an object to output stream
     * @param obj payload to be written
     * @param messageInfo object containing informations to help JAXWS write the objects. Get
     *        this object from SOAPMessageContext.getMessageInfo()
     * @param out stream to write to
     * @param mtomCallback callback is called if there any attachments while
     *                     encoding the object
     */
    public void write(Object obj, Object messageInfo, OutputStream out, MtomCallback mtomCallback);

    /**
     * Writes an object to output stream
     * @param obj payload to be written
     * @param messageInfo object containing informations to help JAXWS write the objects. Get
     *        this object from SOAPMessageContext.getMessageInfo()
     * @param out stream writer to write to
     * @param mtomCallback callback is called if there any attachments while
     *                     encoding the object
     */
    public void write(Object obj, Object messageInfo, XMLStreamWriter out, MtomCallback mtomCallback);
}
