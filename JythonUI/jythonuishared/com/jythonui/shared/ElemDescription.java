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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.gwtmodel.table.common.CUtil;

/**
 * @author hotel
 * 
 */
public abstract class ElemDescription implements Serializable {

    // important: not final !
    private Map<String, String> attr = new HashMap<String, String>();

    public void setAttr(String key, String value) {
        attr.put(key, value);
    }

    public Iterator<String> getKeys() {
        return attr.keySet().iterator();
    }

    private int findSecurity(String s) {
        if (CUtil.EmptyS(s))
            return -1;
        int startp = 0;
        int len = s.length();

        while (true) {
            int pos = s.indexOf(ICommonConsts.PERMSIGN, startp);
            if (pos == -1)
                return -1;
            if (pos == len - 1)
                return -1; // empty security
            if (s.charAt(pos + 1) != ICommonConsts.PERMSIGN)
                return pos;
            // double (escaped) $
            startp = pos + 2;
        }
    }

    public String getAttr(String key) {
        String val = attr.get(key);
        int sec = findSecurity(val);
        if (sec == -1)
            return val; // no security string
        if (sec == 0) return "";
        return val.substring(0, sec - 1); // remove security part
    }

    public String getSecuriyPart(String key) {
        String val = attr.get(key);
        int sec = findSecurity(val);
        if (sec == -1)
            return null;
        return val.substring(sec + 1);
    }

    public String getId() {
        return getAttr(ICommonConsts.ID);
    }

    public void setId(String id) {
        setAttr(ICommonConsts.ID, id);
    }

    public String getDisplayName() {
        return getAttr(ICommonConsts.DISPLAYNAME);
    }

    public boolean eqId(String id) {
        return CUtil.EqNS(getId(), id);
    }

    protected boolean isAttr(String attr) {
        return getAttr(attr) != null;
    }

    public String getWidth() {
        return getAttr(ICommonConsts.WIDTH);
    }

    public String getHtmlId() {
        return getAttr(ICommonConsts.HTMLID);
    }

    public boolean isHtmlId() {
        return isAttr(ICommonConsts.HTMLID);
    }

}
