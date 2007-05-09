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
import java.io.IOException;

import org.jboss.com.sun.tools.ws.processor.util.GeneratedFileInfo;
import org.jboss.com.sun.tools.ws.processor.util.ProcessorEnvironment;

import com.sun.codemodel.JPackage;
import com.sun.codemodel.writer.FileCodeWriter;

/**
 * {@link FileCodeWriter} implementation that notifies
 * JAX-WS about newly created files.
 *
 * @author
 *     Kohsuke Kawaguchi (kohsuke.kawaguchi@sun.com)
 */
public class WSCodeWriter extends FileCodeWriter {
    private final ProcessorEnvironment env;

    public WSCodeWriter( File outDir, ProcessorEnvironment _env ) throws IOException {
        super(outDir);
        this.env = _env;
    }

    protected File getFile(JPackage pkg, String fileName ) throws IOException {
        File f = super.getFile(pkg, fileName);

        // notify JAX-WS RI
        GeneratedFileInfo fi = new GeneratedFileInfo();
        fi.setType("JAXB"/*GeneratorConstants.FILE_TYPE_VALUETYPE*/);
        fi.setFile(f);
        env.addGeneratedFile(fi);
        // we can't really tell the file type, for we don't know
        // what this file is used for. Fortunately,
        // FILE_TYPE doesn't seem to be used, so it doesn't really
        // matter what we set.

        return f;
    }
}
