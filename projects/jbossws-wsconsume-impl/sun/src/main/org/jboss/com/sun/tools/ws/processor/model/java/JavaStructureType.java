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

package org.jboss.com.sun.tools.ws.processor.model.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.com.sun.tools.ws.processor.model.ModelException;


/**
 *
 * @author WS Development Team
 */
public class JavaStructureType extends JavaType {

    public JavaStructureType() {}

    public JavaStructureType(String name, boolean present, Object owner) {
        super(name, present, "null");
        this.owner = owner;
    }

    public void add(JavaStructureMember m) {
        if (membersByName.containsKey(m.getName())) {
            throw new ModelException("model.uniqueness.javastructuretype",
                new Object[] {m.getName(), getRealName()});
        }
        members.add(m);
        membersByName.put(m.getName(), m);
    }


    public JavaStructureMember getMemberByName(String name) {
        if (membersByName.size() != members.size()) {
            initializeMembersByName();
        }
        return membersByName.get(name);
    }

    public Iterator getMembers() {
        return members.iterator();
    }

    public int getMembersCount() {
        return members.size();
    }

    /* serialization */
    public List<JavaStructureMember> getMembersList() {
        return members;
    }

    /* serialization */
    public void setMembersList(List<JavaStructureMember> l) {
        members = l;
    }

    private void initializeMembersByName() {
        membersByName = new HashMap<String, JavaStructureMember>();
        if (members != null) {
            for (JavaStructureMember m : members) {
                if (m.getName() != null &&
                    membersByName.containsKey(m.getName())) {

                    throw new ModelException("model.uniqueness");
                }
                membersByName.put(m.getName(), m);
            }
        }
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    public JavaStructureType getSuperclass() {
        return superclass;
    }

    public void setSuperclass(JavaStructureType superclassType) {
        superclass = superclassType;
    }

    public void addSubclass(JavaStructureType subclassType) {
        subclasses.add(subclassType);
        subclassType.setSuperclass(this);
    }

    public Iterator getSubclasses() {
        if (subclasses == null || subclasses.size() == 0) {
            return null;
        }
        return subclasses.iterator();
    }

    public Set getSubclassesSet() {
        return subclasses;
    }

    /* serialization */
    public void setSubclassesSet(Set s) {
        subclasses = s;
        for (Iterator iter = s.iterator(); iter.hasNext();) {
            ((JavaStructureType) iter.next()).setSuperclass(this);
        }
    }

    public Iterator getAllSubclasses() {
        Set subs = getAllSubclassesSet();
        if (subs.size() == 0) {
            return null;
        }
        return subs.iterator();
    }

    public Set getAllSubclassesSet() {
        Set transitiveSet = new HashSet();
        Iterator subs = subclasses.iterator();
        while (subs.hasNext()) {
            transitiveSet.addAll(
                ((JavaStructureType)subs.next()).getAllSubclassesSet());
        }
        transitiveSet.addAll(subclasses);
        return transitiveSet;
    }

    public Object getOwner() {

        // usually a SOAPStructureType
        return owner;
    }

    public void setOwner(Object owner) {

        // usually a SOAPStructureType
        this.owner = owner;
    }

    private List<JavaStructureMember> members = new ArrayList();
    private Map<String, JavaStructureMember> membersByName = new HashMap();

    // known subclasses of this type
    private Set subclasses = new HashSet();
    private JavaStructureType superclass;

    // usually a SOAPStructureType
    private Object owner;
    private boolean isAbstract = false;
}
