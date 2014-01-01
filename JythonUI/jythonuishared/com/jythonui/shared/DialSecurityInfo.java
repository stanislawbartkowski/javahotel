/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.gwtmodel.table.common.CUtil;

public class DialSecurityInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    protected Map<String, Set<String>> fieldSec = new HashMap<String, Set<String>>();
    protected Map<String, Set<String>> buttSec = new HashMap<String, Set<String>>();

    public Map<String, Set<String>> getFieldSec() {
        return fieldSec;
    }

    public Map<String, Set<String>> getButtSec() {
        return buttSec;
    }

    static public void setSec(Map<String, Set<String>> sec, String field,
            String attr) {
        Set<String> attrS = sec.get(field);
        if (attrS == null) {
            attrS = new HashSet<String>();
            sec.put(field, attrS);
        }
        attrS.add(attr);
    }

    private boolean isAttr(ElemDescription e, String attr,
            Map<String, Set<String>> sec) {
        if (!e.isAttr(attr))
            return false;
        return sec.get(e.getId()).contains(attr);
    }

    private String getAttr(ElemDescription e, String attr,
            Map<String, Set<String>> sec) {
        String val = e.getAttr(attr);
        if (CUtil.EmptyS(val))
            return val;
        if (sec.get(e.getId()).contains(attr))
            return val;
        return null;
    }

    public boolean isFieldAttr(FieldItem f, String attr) {
        return isAttr(f, attr, fieldSec);
    }

    public boolean isButtAttr(ButtonItem b, String attr) {
        return isAttr(b, attr, buttSec);
    }

    public String getFieldAttr(FieldItem f, String attr) {
        return getAttr(f, attr, fieldSec);
    }

    public String getButtAttr(ButtonItem b, String attr) {
        return getAttr(b, attr, buttSec);
    }

    public boolean isFieldHidden(FieldItem f) {
        return isFieldAttr(f, ICommonConsts.HIDDEN);
    }

    public boolean isButtHidden(ButtonItem b) {
        return isButtAttr(b, ICommonConsts.HIDDEN);
    }

    public boolean isFieldReadOnly(FieldItem f) {
        return isFieldAttr(f, ICommonConsts.READONLY);
    }

    public boolean isButtReadOnly(ButtonItem b) {
        return isButtAttr(b, ICommonConsts.READONLY);
    }

}
