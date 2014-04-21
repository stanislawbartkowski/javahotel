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
package com.gwtmodel.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class FileUtil {

    public static Map<String, String> PropertyToMap(Properties pro) {
        Map<String, String> map = new HashMap<String, String>();
        for (Entry<Object, Object> e : pro.entrySet()) {
            String key = (String) e.getKey();
            String val = (String) e.getValue();
            map.put(key, val);
        }
        return map;
    }

    public static Properties MapToProperty(Map<String, String> ma) {
        Properties prop = new Properties();
        for (Entry<String, String> e : ma.entrySet()) {
            prop.put(e.getKey(), e.getValue());
        }
        return prop;
    }

}
