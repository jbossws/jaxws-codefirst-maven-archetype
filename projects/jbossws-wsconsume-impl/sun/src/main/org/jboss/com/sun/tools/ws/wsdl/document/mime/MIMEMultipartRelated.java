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

package org.jboss.com.sun.tools.ws.wsdl.document.mime;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.jboss.com.sun.tools.ws.wsdl.framework.Entity;
import org.jboss.com.sun.tools.ws.wsdl.framework.EntityAction;
import org.jboss.com.sun.tools.ws.wsdl.framework.Extension;
import org.jboss.com.sun.tools.ws.wsdl.framework.ExtensionVisitor;


/**
 * A MIME multipartRelated extension.
 *
 * @author WS Development Team
 */
public class MIMEMultipartRelated extends Extension {

    public MIMEMultipartRelated() {
        _parts = new ArrayList<MIMEPart>();
    }

    public QName getElementName() {
        return MIMEConstants.QNAME_MULTIPART_RELATED;
    }

    public void add(MIMEPart part) {
        _parts.add(part);
    }

    public Iterator<MIMEPart> getParts() {
        return _parts.iterator();
    }

    public void withAllSubEntitiesDo(EntityAction action) {
        super.withAllSubEntitiesDo(action);

        for (Iterator iter = _parts.iterator(); iter.hasNext();) {
            action.perform((Entity) iter.next());
        }
    }

    public void accept(ExtensionVisitor visitor) throws Exception {
        visitor.preVisit(this);
        visitor.postVisit(this);
    }

    public void validateThis() {
    }

    private List<MIMEPart> _parts;
}
