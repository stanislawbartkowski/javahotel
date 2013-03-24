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
package com.gwtmodel.mapcache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.gwtmodel.commoncache.ICommonCache;

/**
 * @author hotel
 * 
 */
public class SimpleMapCache implements ICommonCache {

    // thread safe
    private final static Map<String, Object> cMap = new ConcurrentHashMap<String, Object>();

    @Override
    public Object get(String key) {
        return cMap.get(key);
    }

    @Override
    public void put(String key, Object o) {
        cMap.put(key, o);
    }

    @Override
    public void remove(String key) {
        cMap.remove(key);

    }

}
