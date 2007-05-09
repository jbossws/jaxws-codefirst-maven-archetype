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
package org.jboss.com.sun.tools.ws.wscompile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JPackage;
import java.io.BufferedOutputStream;

import org.jboss.com.sun.tools.ws.processor.util.ProcessorEnvironment;

import com.sun.mirror.apt.Filer;
        
/**
 * Writes all the source files using the specified Filer.
 * 
 * @author WS Development Team
 */
public class FilerCodeWriter extends WSCodeWriter {

    /** The Filer used to create files. */
    private final Filer filer;
    
    private Writer w;
    
    public FilerCodeWriter(File outDir, ProcessorEnvironment env ) throws IOException {
        super(outDir, env);
        this.filer = env.getFiler();
    }
    
    
    public Writer openSource(JPackage pkg, String fileName) throws IOException {
        String tmp = fileName.substring(0, fileName.length()-5);
        w = filer.createSourceFile(pkg.name()+"."+tmp);
        return w;
    }
    

    public void close() throws IOException {
        super.close();
        if (w != null)
            w.close();
        w = null;
    }
}
