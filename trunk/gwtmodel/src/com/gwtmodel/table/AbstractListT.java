/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author perseus
 */
public abstract class AbstractListT {

    private Map<String, String> vals = null;
    private final Map<String, String> keys;

    protected AbstractListT(Map<String,String> keys) {
        this.keys = keys;
    }

    public Map<String, String> getMap() {
        setMaps();
        return keys;
    }

    private void setMaps() {
        if (vals != null) {
            return;
        }
        vals = new HashMap<String, String>();
        for (Entry<String, String> k : keys.entrySet()) {
            vals.put(k.getValue(), k.getKey());
        }
    }

    public String getValueS(String key) {
        setMaps();
        return keys.get(key);
    }

    public String getValue(String val) {
        setMaps();
        return vals.get(val);
    }

    public List<String> getListVal() {
        setMaps();
        List<String> li = new ArrayList<String>();
        for (String s : keys.values()) {
            li.add(s);
        }
        return li;
    }
}
