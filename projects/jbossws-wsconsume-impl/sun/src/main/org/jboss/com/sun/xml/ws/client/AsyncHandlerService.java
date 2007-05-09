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

import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;
import java.rmi.server.UID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

public class AsyncHandlerService {

    private AsyncHandler _handler;
    private UID _uid;
    private Executor _executor;
    private WSFuture wsfuture;
    private Response response;

    public AsyncHandlerService(AsyncHandler handler, Executor executor) {
        _uid = new UID();
        _handler = handler;
        _executor = executor;
    }

    public synchronized UID getUID() {
        return _uid;
    }

    public void executeWSFuture() {

        _executor.execute((Runnable) wsfuture);
    }

    public WSFuture<Object> setupAsyncCallback(final Response<Object> result) {
        response = result;

        wsfuture = new WSFuture<Object>(new Callable<Object>() {

            public Object call() throws Exception {
                _handler.handleResponse(response);
                return null;
            }
        });
        return wsfuture;
    }
}
