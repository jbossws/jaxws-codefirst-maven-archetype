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
package org.jboss.com.sun.xml.ws.encoding.soap.internal;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.jboss.com.sun.xml.ws.pept.encoding.Decoder;
import org.jboss.com.sun.xml.ws.pept.encoding.Encoder;
import org.jboss.com.sun.xml.ws.pept.ept.EPTFactory;
import org.jboss.com.sun.xml.ws.pept.ept.MessageInfo;
import org.jboss.com.sun.xml.ws.pept.protocol.MessageDispatcher;
import org.jboss.com.sun.xml.ws.spi.runtime.WSConnection;


/**
 * @author WS Development Team
 */
public class MessageInfoBase implements MessageInfo {

    protected Object[] _data;
    protected Method _method;
    protected Map _metadata;
    protected int _messagePattern;
    protected Object _response;
    protected int _responseType;
    protected EPTFactory _eptFactory;
    protected MessageDispatcher _messageDispatcher;
    protected Encoder _encoder;
    protected Decoder _decoder;
    protected WSConnection _connection;

    public void setData(Object[] data) {
        _data = data;
    }

    public Object[] getData() {
        return _data;
    }

    public void setMethod(Method method) {
        _method = method;
    }

    public Method getMethod() {
        return _method;
    }

    public void setMetaData(Object name, Object value) {
        if (_metadata == null)
           _metadata = new HashMap();
        _metadata.put(name, value);
    }

    public Object getMetaData(Object name) {
        Object value = null;

        if ((name != null) && (_metadata != null)) {
             value = _metadata.get(name);
        }
        return value;
    }

    public int getMEP() {
        return _messagePattern;
    }

    public void setMEP(int messagePattern) {
        _messagePattern = messagePattern;
    }

    public int getResponseType() {
        return _responseType;
    }

    public void setResponseType(int responseType) {
        _responseType = responseType;
    }

    public Object getResponse() {
        return _response;
    }

    public void setResponse(Object response) {
        _response = response;
    }

    public EPTFactory getEPTFactory() {
        return _eptFactory;
    }

    public void setEPTFactory(EPTFactory eptFactory) {
        _eptFactory = eptFactory;
    }

    /*
     * @see MessageInfo#getMessageDispatcher()
     */
    public MessageDispatcher getMessageDispatcher() {
        return _messageDispatcher;
    }

    /*
     * @see MessageInfo#getEncoder()
     */
    public Encoder getEncoder() {
        return _encoder;
    }

    /*
     * @see MessageInfo#getDecoder()
     */
    public Decoder getDecoder() {
        return _decoder;
    }

    /*
     * @see MessageInfo#getConnection()
     */
    public WSConnection getConnection() {
        return _connection;
    }

    /*
     * @see MessageInfo#setMessageDispatcher(MessageDispatcher)
     */
    public void setMessageDispatcher(MessageDispatcher arg0) {
        this._messageDispatcher = arg0;
    }

    /*
     * @see MessageInfo#setEncoder(Encoder)
     */
    public void setEncoder(Encoder encoder) {
        this._encoder = encoder;
    }

    /*
     * @see MessageInfo#setDecoder(Decoder)
     */
    public void setDecoder(Decoder decoder) {
        this._decoder = decoder;
    }

    /*
     * @see MessageInfo#setConnection(Connection)
     */
    public void setConnection(WSConnection connection) {
        this._connection = connection;
    }

    public static MessageInfo copy(MessageInfo mi){
        MessageInfoBase mib = (MessageInfoBase)mi;
        MessageInfoBase newMi = new MessageInfoBase();
        if(newMi._data != null){
            Object[] data = new Object[mib._data.length];
            int i = 0;
            for(Object o : mib._data){
                data[i++] = o;
            }
            newMi._data = data;
        }
        newMi.setConnection(mi.getConnection());
        newMi.setMethod(mi.getMethod());
        newMi.setDecoder(mi.getDecoder());
        newMi.setEncoder(mi.getEncoder());
        newMi.setEPTFactory(mi.getEPTFactory());
        newMi.setMEP(mi.getMEP());
        newMi._messageDispatcher = mib._messageDispatcher;
        newMi._metadata = new HashMap(mib._metadata);
        return (MessageInfo)newMi;
    }
}
