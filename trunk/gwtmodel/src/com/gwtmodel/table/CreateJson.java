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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CreateJson {

    private final String objectName;

    private class JsonElem {

        final String val;
        final boolean number;

        JsonElem(String val, boolean number) {
            this.val = val;
            this.number = number;
        }

        public String getVal() {
            return val;
        }

        public boolean isNumber() {
            return number;
        }
    }
    private final Map<String, JsonElem> vals = new HashMap<String, JsonElem>();

    public CreateJson(String objectName) {
        this.objectName = objectName;
    }

    public void addElem(String key, String val, boolean isNumber) {
        vals.put(key, new JsonElem(val, isNumber));
    }

    public void addElem(String key, String val) {
        addElem(key, val, false);
    }

    public String createJsonString() {
        String res = "{ \"" + objectName + "\": {";
        boolean first = true;
        for (Entry<String, JsonElem> e : vals.entrySet()) {
            if (!first) {
                res += " , ";
            }
            first = false;
            String val;
            if (e.getValue().getVal() == null) {
                val = "null";
            } else {
                if (e.getValue().isNumber()) {
                    val = e.getValue().getVal();
                } else {
                    String s = e.getValue().getVal();
                    // escape all "
                    s = s.replaceAll("\"", "");
                    // escape all \n
                    s = s.replaceAll("\n", "");                                        
                    s = s.replaceAll("\r", "");  
                    val = '\"' + s + '\"';
                }
            }
            res += '\"' + e.getKey() + "\" : " + val;
        }
        res += "}}";
        return res;
    }
}
