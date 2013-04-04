/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package com.jythonui.shared;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class DialSecurityInfo implements Serializable {

    protected Set<String> fieldAccess = new HashSet<String>();
    protected Set<String> fieldReadOnly = new HashSet<String>();
    protected Set<String> buttonAccess = new HashSet<String>();
    protected Set<String> buttonReadOnly = new HashSet<String>();

    public enum AccessType {
        ACCESS, READONLY, NOACCESS;
    }

    private static AccessType accessType(String id, Set<String> saccess,
            Set<String> sreadonly) {
        if (saccess.contains(id))
            return AccessType.ACCESS;
        if (sreadonly.contains(id))
            return AccessType.READONLY;
        return AccessType.NOACCESS;
    }

    public AccessType fieldAccess(String id) {
        return accessType(id, fieldAccess, fieldReadOnly);
    }

    public AccessType buttonAccess(String id) {
        return accessType(id, buttonAccess, buttonReadOnly);
    }

    public Set<String> getFieldAccess() {
        return fieldAccess;
    }

    public Set<String> getFieldReadOnly() {
        return fieldReadOnly;
    }

    public Set<String> getButtonAccess() {
        return buttonAccess;
    }

    public Set<String> getButtonReadOnly() {
        return buttonReadOnly;
    }

}
