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

package org.jboss.com.sun.tools.ws.processor.util;

import java.io.File;

/**
 * A container to hold info on the files that get
 * generated.
 *
 * @author WS Development Team
 */
public class GeneratedFileInfo {

    /**
     * local variables
     */
    private File file = null;
    private String type = null;

    /* constructor */
    public GeneratedFileInfo() {}

    /**
     * Adds the file object to the container
     *
     * @param file instance of the file to be added
     */
    public void setFile( File file ) {
        this.file = file;
    }

    /**
     * Adds the type of file it is the container
     *
     * @param type string which specifices the type
     */
    public void setType( String type ) {
        this.type = type;
    }

    /**
     * Gets the file that got added
     *
     * @return File that got added
     */
    public File getFile() {
        return( file );
    }

    /**
     * Get the file type that got added
     *
     * @return File type of datatype String
     */
    public String getType() {
        return ( type );
    }
}
