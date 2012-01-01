/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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

    public interface IGetMap {

        Map<String, String> getM();
    }

    private static class MapToList implements IGetList {

        private class MapE implements IMapEntry {

            private final String key, val;

            MapE(String key, String val) {
                this.key = key;
                this.val = val;
            }

            public String getKey() {
                return key;
            }

            public String getValue() {
                return val;
            }
        }
        private final IGetMap getM;

        private MapToList(IGetMap getM) {
            this.getM = getM;
        }

        public List<IMapEntry> getL() {

            List<IMapEntry> li = new ArrayList<IMapEntry>();
            Map<String, String> ma = getM.getM();
            for (Entry<String, String> e : ma.entrySet()) {
                MapE ee = new MapE(e.getKey(), e.getValue());
                li.add(ee);
            }
            return li;

        }
    }

    public interface IGetList {

        List<IMapEntry> getL();
    }
    private Map<String, String> vals = null;
    private Map<String, String> keys = null;
    private List<IMapEntry> eL = null;
    private final IGetList iGet;

    protected AbstractListT(IGetList iGet) {
        this.iGet = iGet;
    }

    protected AbstractListT(IGetMap iGet) {
        this.iGet = new MapToList(iGet);
    }

    public Map<String, String> getMap() {
        setMaps();
        return keys;
    }

    private void setMaps() {
        if (vals != null) {
            return;
        }
        eL = iGet.getL();
        vals = new HashMap<String, String>();
        keys = new HashMap<String, String>();
        for (IMapEntry k : eL) {
            vals.put(k.getValue(), k.getKey());
            keys.put(k.getKey(), k.getValue());
        }
    }
    
    public List<IMapEntry> getEntryList() {
        setMaps();
        return eL;        
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
        for (IMapEntry e : eL) {
            li.add(e.getValue());
        }
        return li;
    }
    
    public List<String> getListKeys() {
        setMaps();
        List<String> li = new ArrayList<String>();
        for (IMapEntry e : eL) {
            li.add(e.getKey());
        }
        return li;
        
    }
}
