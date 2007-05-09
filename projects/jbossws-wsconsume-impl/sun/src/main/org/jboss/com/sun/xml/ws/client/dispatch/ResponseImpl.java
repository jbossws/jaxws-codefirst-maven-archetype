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
package org.jboss.com.sun.xml.ws.client.dispatch;


import javax.xml.ws.Response;

import org.jboss.com.sun.xml.ws.client.AsyncHandlerService;
import org.jboss.com.sun.xml.ws.client.ResponseContext;

import java.rmi.server.UID;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * The <code>Response</code> interface provides methods used to obtain the
 * payload and context a message sent in response to an operation invocation.
 * For asynchronous operation invocations it provides additional methods to
 * check the status of the request.
 *
 * @author JAXWS Development Team
 * @version 1.0
 */


public class ResponseImpl<T> extends FutureTask<T> implements Response<T> {
    private static final Logger logger =
        Logger.getLogger(new StringBuffer().append(org.jboss.com.sun.xml.ws.util.Constants.LoggingDomain).append(".client.dispatch").toString());
    private UID uid;
    private Lock _lock;
    private AsyncHandlerService _handlerService;
    private ResponseContext _responseContext;
    private boolean handler;

    public ResponseImpl(Callable<T> callable) {
        super(callable);
        _lock = new ReentrantLock();
    }

    public ResponseImpl(Runnable runable, T result) {
        super(runable, result);
        _lock = new ReentrantLock();
    }

    //protected method need to overide
    public void setException(Exception ex) {
        _lock.lock();
        try {
            super.setException(ex);
        } catch (Exception e) {
        } finally {
            _lock.unlock();
        }
    }

    public void set(T result) {
        _lock.lock();
        try {
            super.set(result);
        } catch (Exception e) {
        } finally {
            _lock.unlock();
        }
    }

    /**
     * Gets the contained response context.
     *
     * @return The contained response context. May be <code>null</code> if a
     *         response is not yet available.
     */
    public Map<String, Object> getContext() {
        if (!isDone()) {
            return null;
        } else {
            return (_responseContext);
        }
    }

    public void setResponseContext(Map context) {
        _responseContext = (ResponseContext) context;
    }

    public synchronized void setUID(UID id) {
        uid = id;
    }

    public synchronized UID getUID() {
        return uid;
    }

    public void setHandlerService(AsyncHandlerService handlerService) {
        _handlerService = handlerService;
    }

    //got to lock

    public void done() {
        _lock.lock();
        try {
            if (!isCancelled())
                _handlerService.executeWSFuture();

        } catch (Exception e) {
        } finally {
            _lock.unlock();
        }
    }
}
