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
package org.jboss.com.sun.xml.ws.client;


import javax.xml.ws.soap.SOAPBinding;

import org.jboss.com.sun.xml.ws.encoding.internal.InternalEncoder;
import org.jboss.com.sun.xml.ws.encoding.soap.ClientEncoderDecoder;
import org.jboss.com.sun.xml.ws.encoding.soap.SOAPDecoder;
import org.jboss.com.sun.xml.ws.encoding.soap.SOAPEPTFactory;
import org.jboss.com.sun.xml.ws.encoding.soap.SOAPEncoder;
import org.jboss.com.sun.xml.ws.pept.encoding.Decoder;
import org.jboss.com.sun.xml.ws.pept.encoding.Encoder;
import org.jboss.com.sun.xml.ws.pept.ept.ContactInfo;
import org.jboss.com.sun.xml.ws.pept.ept.MessageInfo;
import org.jboss.com.sun.xml.ws.pept.presentation.TargetFinder;
import org.jboss.com.sun.xml.ws.pept.protocol.Interceptors;
import org.jboss.com.sun.xml.ws.pept.protocol.MessageDispatcher;
import org.jboss.com.sun.xml.ws.spi.runtime.WSConnection;


/**
 * @author WS Development Team
 */
public class ContactInfoBase implements ContactInfo, SOAPEPTFactory {
    protected WSConnection _connection;
    protected MessageDispatcher _messageDispatcher;
    protected Encoder _encoder;
    protected Decoder _decoder;
    private String bindingId;
    private InternalEncoder internalEncoder;

    public ContactInfoBase(WSConnection connection,
                           MessageDispatcher messageDispatcher, Encoder encoder, Decoder decoder,
                           String bindingId) {
        _connection = connection;
        _messageDispatcher = messageDispatcher;
        _encoder = encoder;
        _decoder = decoder;
        internalEncoder = new ClientEncoderDecoder();
        this.bindingId = bindingId;
    }

    public ContactInfoBase() {
        _connection = null;
        _messageDispatcher = null;
        _encoder = null;
        _decoder = null;
    }

    /* (non-Javadoc)
     * @see com.sun.pept.ept.ContactInfo#getConnection(com.sun.pept.ept.MessageInfo)
     */
    public WSConnection getConnection(MessageInfo arg0) {
        return _connection;
    }

    /* (non-Javadoc)
     * @see com.sun.pept.ept.EPTFactory#getMessageDispatcher(com.sun.pept.ept.MessageInfo)
     */
    public MessageDispatcher getMessageDispatcher(MessageInfo arg0) {
        return _messageDispatcher;
    }

    /* (non-Javadoc)
     * @see com.sun.pept.ept.EPTFactory#getEncoder(com.sun.pept.ept.MessageInfo)
     */
    public Encoder getEncoder(MessageInfo arg0) {
        return _encoder;
    }

    /* (non-Javadoc)
     * @see com.sun.pept.ept.EPTFactory#getDecoder(com.sun.pept.ept.MessageInfo)
     */
    public Decoder getDecoder(MessageInfo arg0) {
        return _decoder;
    }

    /* (non-Javadoc)
     * @see com.sun.pept.ept.EPTFactory#getInterceptors(com.sun.pept.ept.MessageInfo)
     */
    public Interceptors getInterceptors(MessageInfo arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.sun.pept.ept.EPTFactory#getTargetFinder(com.sun.pept.ept.MessageInfo)
     */
    public TargetFinder getTargetFinder(MessageInfo arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public SOAPEncoder getSOAPEncoder() {
        return (SOAPEncoder) _encoder;
    }

    public SOAPDecoder getSOAPDecoder() {
        return (SOAPDecoder) _decoder;
    }

    public InternalEncoder getInternalEncoder() {
        return internalEncoder;
    }

    public String getBindingId() {
        if (bindingId == null) {
            return SOAPBinding.SOAP11HTTP_BINDING;
        }

        return bindingId;
    }
}
