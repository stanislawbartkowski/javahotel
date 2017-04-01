/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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
package com.gwtmodel.table.map;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.DecimalUtils;

abstract public class XMap implements Serializable {

    private static final long serialVersionUID = 1L;

    // cannot be final
    protected Map<String, String> attr = new HashMap<String, String>();

    public Map<String, String> getMap() {
        return attr;
    }

    public void setMap(Map<String, String> attr) {
        this.attr = attr;
    }

    public void setAttr(String key, String value) {
        attr.put(key, value);
    }

    public Iterator<String> getKeys() {
        return attr.keySet().iterator();
    }

    public String getAttr(String key) {
        return attr.get(key);
    }

    public boolean isAttr(String attr) {
        return getAttr(attr) != null;
    }

    public String getAttr(String key, String defa) {
        if (!isAttr(key))
            return defa;
        return getAttr(key);
    }

    protected int getInt(String key, int defaultval) {
        String s = getAttr(key);
        if (CUtil.EmptyS(s))
            return defaultval;
        return CUtil.getInteger(s);
    }

    protected BigDecimal getAttrBig(String key) {
        String s = getAttr(key);
        if (s == null)
            return null;
        return DecimalUtils.toBig(s);
    }

    protected void setAttrBig(String key, BigDecimal b) {
        if (b == null) {
            setAttr(key, null);
            return;
        }
        setAttr(key, b.toString());
    }

}
