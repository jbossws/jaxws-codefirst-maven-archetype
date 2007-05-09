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

package org.jboss.com.sun.tools.ws.processor.modeler.wsdl;

import com.sun.tools.xjc.api.ErrorListener;

import org.jboss.com.sun.tools.ws.processor.util.ProcessorEnvironment;
import org.jboss.com.sun.xml.ws.util.localization.LocalizableMessageFactory;
import org.xml.sax.SAXParseException;

import java.util.ResourceBundle;
import java.text.MessageFormat;

public class ConsoleErrorReporter implements ErrorListener{

    private LocalizableMessageFactory messageFactory;
    private ProcessorEnvironment env;
    private boolean printStackTrace;
    private boolean hasError;

    public ConsoleErrorReporter(ProcessorEnvironment env, boolean printStackTrace) {
        this.env = env;
        this.printStackTrace = printStackTrace;
        messageFactory =
            new LocalizableMessageFactory("org.jboss.com.sun.tools.ws.resources.model");
    }

    public boolean hasError() {
        return hasError;
    }

    // will be null unless set in #error or #fatalError
    //TODO: remove it after error handling is straightened
    private Exception e;
    Exception getException(){
        return e;
    }

    public void error(SAXParseException e) {
        hasError = true;
        this.e = e;
        if(printStackTrace)
            e.printStackTrace();
        env.error(messageFactory.getMessage("model.saxparser.exception",
                new Object[]{e.getMessage(), getLocationString(e)}));
    }

    public void fatalError(SAXParseException e) {
        hasError = true;
        this.e = e;
        if(printStackTrace)
            e.printStackTrace();

        env.error(messageFactory.getMessage("model.saxparser.exception",
                new Object[]{e.getMessage(), getLocationString(e)}));        
    }

    public void warning(SAXParseException e) {
        env.warn(messageFactory.getMessage("model.saxparser.exception",
                new Object[]{e.getMessage(), getLocationString(e)}));
    }

    /**
     * Used to report possibly verbose information that
     * can be safely ignored.
     */
    public void info(SAXParseException e) {
        env.info(messageFactory.getMessage("model.saxparser.exception",
                new Object[]{e.getMessage(), getLocationString(e)}));
    }

     /**
    * Returns the human readable string representation of the
    * {@link org.xml.sax.Locator} part of the specified
    * {@link SAXParseException}.
    *
    * @return  non-null valid object.
    */
    protected final String getLocationString( SAXParseException e ) {
      if(e.getLineNumber()!=-1 || e.getSystemId()!=null) {
          int line = e.getLineNumber();
          return format("ConsoleErrorReporter.LineXOfY", line==-1?"?":Integer.toString( line ),
              getShortName( e.getSystemId() ) );
      } else {
          return format("ConsoleErrorReporter.UnknownLocation");
      }
    }

    /** Computes a short name of a given URL for display. */
    private String getShortName( String url ) {
        if(url==null)
            return format("ConsoleErrorReporter.UnknownLocation");
        return url;
    }

    private String format( String property, Object... args ) {
        String text = ResourceBundle.getBundle("org.jboss.com.sun.tools.ws.resources.model").getString(property);
        return MessageFormat.format(text,args);
    }

}
