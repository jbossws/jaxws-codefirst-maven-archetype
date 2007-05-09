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
package org.jboss.com.sun.tools.ws.processor.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.namespace.QName;

import org.jboss.com.sun.tools.ws.processor.generator.GeneratorUtil;
import org.jboss.com.sun.tools.ws.processor.model.java.JavaException;

import com.sun.codemodel.JClass;

/**
 *
 * @author WS Development Team
 */
public class Fault extends ModelObject {

    public Fault() {}

    public Fault(String name) {
        this.name = name;
        parentFault = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String s) {
        name = s;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block b) {
        block = b;
    }

    public JavaException getJavaException() {
        return javaException;
    }

    public void setJavaException(JavaException e) {
        javaException = e;
    }

    public void accept(ModelVisitor visitor) throws Exception {
        visitor.visit(this);
    }

    public Fault getParentFault() {
        return parentFault;
    }

    public void setParentFault(Fault parentFault) {
        if (this.parentFault != null &&
            parentFault != null &&
            !this.parentFault.equals(parentFault)) {

            throw new ModelException("model.parent.fault.already.set",
                new Object[] {
                    getName(),
                    this.parentFault.getName(),
                    parentFault.getName()
            });
        }
        this.parentFault = parentFault;
    }

    public void addSubfault(Fault fault) {
        subfaults.add(fault);
        fault.setParentFault(this);
    }

    public Iterator getSubfaults() {
        if (subfaults.size() == 0) {
            return null;
        }
        return subfaults.iterator();
    }

    public Iterator getSortedSubfaults() {
        Set sortedFaults = new TreeSet(new GeneratorUtil.FaultComparator());
        sortedFaults.addAll(subfaults);
        return sortedFaults.iterator();
    }

    /* serialization */
    public Set getSubfaultsSet() {
        return subfaults;
    }

    /* serialization */
    public void setSubfaultsSet(Set s) {
        subfaults = s;
    }

    public Iterator getAllFaults() {
        Set allFaults = getAllFaultsSet();
        if (allFaults.size() == 0) {
            return null;
        }
        return allFaults.iterator();
    }

    public Set getAllFaultsSet() {
        Set transSet = new HashSet();
        Iterator iter = subfaults.iterator();
        while (iter.hasNext()) {
            transSet.addAll(((Fault)iter.next()).getAllFaultsSet());
        }
        transSet.addAll(subfaults);
        return transSet;
    }

    public QName getElementName() {
        return elementName;
    }

    public void setElementName(QName elementName) {
        this.elementName = elementName;
    }

    public String getJavaMemberName() {
        return javaMemberName;
    }

    public void setJavaMemberName(String javaMemberName) {
        this.javaMemberName = javaMemberName;
    }

    /**
     * @return Returns the wsdlFault.
     */
    public boolean isWsdlException() {
            return wsdlException;
    }
    /**
     * @param wsdlFault The wsdlFault to set.
     */
    public void setWsdlException(boolean wsdlFault) {
            this.wsdlException = wsdlFault;
    }

    public void setExceptionClass(JClass ex){
        exceptionClass = ex;
    }

    public JClass getExceptionClass(){
        return exceptionClass;
    }

    private boolean wsdlException = true;
    private String name;
    private Block block;
    private JavaException javaException;
    private Fault parentFault;
    private Set subfaults = new HashSet();
    private QName elementName = null;
    private String javaMemberName = null;
    private JClass exceptionClass;
}
