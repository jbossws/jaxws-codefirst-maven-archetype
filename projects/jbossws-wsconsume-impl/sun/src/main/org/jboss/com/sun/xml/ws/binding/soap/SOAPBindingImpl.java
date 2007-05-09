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
package org.jboss.com.sun.xml.ws.binding.soap;



import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPFactory;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.soap.SOAPBinding;
import javax.xml.namespace.QName;

import org.jboss.com.sun.xml.ws.binding.BindingImpl;
import org.jboss.com.sun.xml.ws.encoding.soap.streaming.SOAP12NamespaceConstants;
import org.jboss.com.sun.xml.ws.encoding.soap.streaming.SOAPNamespaceConstants;
import org.jboss.com.sun.xml.ws.handler.HandlerChainCaller;
import org.jboss.com.sun.xml.ws.spi.runtime.SystemHandlerDelegate;
import org.jboss.com.sun.xml.ws.spi.runtime.SystemHandlerDelegateFactory;
import org.jboss.com.sun.xml.ws.util.SOAPUtil;
import org.jboss.com.sun.xml.ws.util.localization.Localizable;
import org.jboss.com.sun.xml.ws.util.localization.LocalizableMessageFactory;
import org.jboss.com.sun.xml.ws.util.localization.Localizer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;



/**
 * @author WS Development Team
 */
public class SOAPBindingImpl extends BindingImpl implements SOAPBinding {


    public static final String X_SOAP12HTTP_BINDING =
        "http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/";

    protected static String ROLE_NONE;

    protected Set<String> requiredRoles;
    protected Set<String> roles;
    protected boolean enableMtom = false;


     // called by DispatchImpl
    public SOAPBindingImpl(String bindingId, QName serviceName) {
        super(bindingId, serviceName);
        setup(getBindingId(), getActualBindingId());
        setupSystemHandlerDelegate(serviceName);
    }

     public SOAPBindingImpl(String bindingId) {
        super(bindingId, null);
        setup(getBindingId(), getActualBindingId());
        setupSystemHandlerDelegate(null);
    }

    public SOAPBindingImpl(List<Handler> handlerChain, String bindingId, QName serviceName) {
        super(handlerChain, bindingId, serviceName);
        setup(getBindingId(), getActualBindingId());
        setupSystemHandlerDelegate(serviceName);
    }

    // if the binding id is unknown, no roles are added
    protected void setup(String bindingId, String actualBindingId) {
        requiredRoles = new HashSet<String>();
        if (bindingId.equals(SOAPBinding.SOAP11HTTP_BINDING)) {
            requiredRoles.add(SOAPNamespaceConstants.ACTOR_NEXT);
        } else if (bindingId.equals(SOAPBinding.SOAP12HTTP_BINDING)) {
            requiredRoles.add(SOAP12NamespaceConstants.ROLE_NEXT);
            requiredRoles.add(SOAP12NamespaceConstants.ROLE_ULTIMATE_RECEIVER);
        }
        ROLE_NONE = SOAP12NamespaceConstants.ROLE_NONE;
        roles = new HashSet<String>();
        addRequiredRoles();
        setRolesOnHandlerChain();
        if (actualBindingId.equals(SOAP11HTTP_MTOM_BINDING)
            || actualBindingId.equals(SOAP12HTTP_MTOM_BINDING)) {            
            setMTOMEnabled(true);
        }
    }

    /**
     * For a non standard SOAP1.2 binding, return actual SOAP1.2 binding
     * For SOAP 1.1 MTOM binding, return SOAP1.1 binding
     * For SOAP 1.2 MTOM binding, return SOAP 1.2 binding
     */
    @Override
    public String getBindingId() {
        String bindingId = super.getBindingId();
        if (bindingId.equals(SOAPBindingImpl.X_SOAP12HTTP_BINDING)) {
            return SOAP12HTTP_BINDING;
        }
        if (bindingId.equals(SOAPBinding.SOAP11HTTP_MTOM_BINDING)) {
            return SOAP11HTTP_BINDING;
        }
        if (bindingId.equals(SOAPBinding.SOAP12HTTP_MTOM_BINDING)) {
            return SOAP12HTTP_BINDING;
        }
        return bindingId;
    }

    /*
    * Use this to distinguish SOAP12HTTP_BINDING or X_SOAP12HTTP_BINDING
    */
    @Override
    public String getActualBindingId() {
        return super.getBindingId();
    }

    /*
     * When client sets a new handler chain, must also set roles on
     * the new handler chain caller that gets created.
     */
    public void setHandlerChain(List<Handler> chain) {
        super.setHandlerChain(chain);
        setRolesOnHandlerChain();
    }

    protected void addRequiredRoles() {
        roles.addAll(requiredRoles);
    }

    public java.util.Set<String> getRoles() {
        return roles;
    }

    /*
     * Adds the next and other roles in case this has
     * been called by a user without them.
     */
    public void setRoles(Set<String> roles) {
        if (roles == null) {
            roles = new HashSet<String>();
        }
        if (roles.contains(ROLE_NONE)) {
            LocalizableMessageFactory messageFactory =
                new LocalizableMessageFactory("org.jboss.com.sun.xml.ws.resources.client");
            Localizer localizer = new Localizer();
            Localizable locMessage =
                messageFactory.getMessage("invalid.soap.role.none");
            throw new WebServiceException(localizer.localize(locMessage));
        }
        this.roles = roles;
        addRequiredRoles();
        setRolesOnHandlerChain();
    }


    /**
     * Used typically by the runtime to enable/disable Mtom optimization
     *
     * @return true or false
     */
    public boolean isMTOMEnabled() {
        return enableMtom;
    }

    /**
     * Client application can set if the Mtom optimization should be enabled
     *
     * @param b
     */
    public void setMTOMEnabled(boolean b) {
        this.enableMtom = b;
    }

    public SOAPFactory getSOAPFactory() {
        return SOAPUtil.getSOAPFactory(getBindingId());
    }


    public MessageFactory getMessageFactory() {
        return SOAPUtil.getMessageFactory(getBindingId());
    }

    /**
     * This call defers to the super class to get the
     * handler chain caller. It then sets the roles on the
     * caller before returning it.
     *
     * @see org.jboss.com.sun.xml.ws.binding.BindingImpl#getHandlerChainCaller
     */
    public HandlerChainCaller getHandlerChainCaller() {
        HandlerChainCaller caller = super.getHandlerChainCaller();
        caller.setRoles(roles);
        return chainCaller;
    }
    
    protected void setRolesOnHandlerChain() {
        if (chainCaller != null) {
            chainCaller.setRoles(roles);
        }
    }

    protected void setupSystemHandlerDelegate(QName serviceName) {
        SystemHandlerDelegateFactory shdFactory =
            SystemHandlerDelegateFactory.getFactory();
        if (shdFactory != null) {
            setSystemHandlerDelegate((SystemHandlerDelegate)
                shdFactory.getDelegate(serviceName));
        }
    }
}
