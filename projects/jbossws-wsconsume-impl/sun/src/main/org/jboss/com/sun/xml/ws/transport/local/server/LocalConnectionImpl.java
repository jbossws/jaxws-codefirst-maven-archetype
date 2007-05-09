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

package org.jboss.com.sun.xml.ws.transport.local.server;
import java.util.List;
import java.util.Map;

import java.io.InputStream;
import java.io.OutputStream;

import org.jboss.com.sun.xml.ws.transport.WSConnectionImpl;
import org.jboss.com.sun.xml.ws.transport.local.LocalMessage;
import org.jboss.com.sun.xml.ws.util.ByteArrayBuffer;


/**
 * @author WS Development Team
 *
 * Server-side Local transport implementation
 */
public class LocalConnectionImpl extends WSConnectionImpl {
    private int status;
    private LocalMessage lm;
    
    public LocalConnectionImpl (LocalMessage localMessage) {
        this.lm = localMessage;
    }
    
    public Map<String,List<String>> getHeaders () {
        return lm.getHeaders ();
    }
    
    /**
     * sets response headers.
     */
    public void setHeaders (Map<String,List<String>> headers) {
        lm.setHeaders (headers);
    }
    
    public void setStatus (int status) {
        this.status = status;
    }
    
    public InputStream getInput () {
        return lm.getOutput().newInputStream();
    }
    
    public OutputStream getOutput () {
        ByteArrayBuffer bab = new ByteArrayBuffer();
        lm.setOutput(bab);
        return bab;
    }
}

