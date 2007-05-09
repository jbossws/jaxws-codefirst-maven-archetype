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


import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;

import org.jboss.com.sun.xml.ws.binding.BindingImpl;
import org.jboss.com.sun.xml.ws.pept.Delegate;
import org.jboss.com.sun.xml.ws.pept.ept.MessageInfo;
import org.jboss.com.sun.xml.ws.spi.runtime.ClientTransportFactory;
import org.jboss.com.sun.xml.ws.transport.http.client.HttpClientTransportFactory;

import java.util.Map;

/**
 * @author WS Development Team
 */
public class EndpointIFBase implements org.jboss.com.sun.xml.ws.pept.presentation.Stub,
    org.jboss.com.sun.xml.ws.spi.runtime.StubBase, BindingProvider, InternalBindingProvider {

    protected Map<String, Object> _requestContext;
    protected Map<String, Object> _responseContext;

    protected String _bindingId = null;
    protected Delegate _delegate = null;
    protected BindingImpl binding;

    private ClientTransportFactory _transportFactory;

    void setResponseContext(ResponseContext context) {
        _responseContext = context;
    }

    public void _setDelegate(Delegate delegate) {
        _delegate = delegate;
    }

    public Delegate _getDelegate() {
        return _delegate;
    }

    public ClientTransportFactory _getTransportFactory() {
        _transportFactory =
            (org.jboss.com.sun.xml.ws.spi.runtime.ClientTransportFactory)getRequestContext().get(BindingProviderProperties.CLIENT_TRANSPORT_FACTORY);

        if (_transportFactory == null) {
            _transportFactory = new HttpClientTransportFactory();
        }
        return _transportFactory;
    }

    public void _setTransportFactory(ClientTransportFactory f) {
        getRequestContext().put(BindingProviderProperties.CLIENT_TRANSPORT_FACTORY, f);
        _transportFactory = f;
    }

    //toDo: have to update generator on PeptStub to getContext
    public void updateResponseContext(MessageInfo messageInfo) {
        ResponseContext responseContext = (ResponseContext)
            messageInfo.getMetaData(BindingProviderProperties.JAXWS_RESPONSE_CONTEXT_PROPERTY);
        if (responseContext != null) { // null in async case
            setResponseContext(responseContext);
        }
    }

    /**
     * Get the JAXWSContext that is used in processing request messages.
     * <p/>
     * Modifications to the request context do not affect asynchronous
     * operations that have already been started.
     *
     * @return The JAXWSContext that is used in processing request messages.
     */
    public Map<String, Object> getRequestContext() {
        if (_requestContext == null)
            _requestContext = new RequestContext(this);

        return _requestContext;
    }

    /**
     * Get the JAXWSContext that resulted from processing a response message.
     * <p/>
     * The returned context is for the most recently completed synchronous
     * operation. Subsequent synchronous operation invocations overwrite the
     * response context. Asynchronous operations return their response context
     * via the Response interface.
     *
     * @return The JAXWSContext that is used in processing request messages.
     */
    public Map<String, Object> getResponseContext() {
        if (_responseContext == null)
            _responseContext = new ResponseContext(this);
        return _responseContext;
    }

    public Binding getBinding() {
        return binding;
    }

    public void _setBinding(BindingImpl binding) {
        this.binding = binding;
    }

    /**
     * returns binding id from BindingImpl
     *
     * @return the String representing the BindingID
     */
    public String _getBindingId() {
        return _bindingId;
    }

}
