/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.commoncache;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

/**
 * @author hotel
 * 
 */
class GaeCache implements ICommonCache {

    private final MemcacheService cache;

    GaeCache(String nameSpace) {
        cache = MemcacheServiceFactory.getMemcacheService(nameSpace);
    }

    @Override
    public Object get(String key) {
        return cache.get(key);
    }

    @Override
    public void put(String key, Object o) {
        cache.put(key, o);
    }

    @Override
    public void remove(String key) {
        cache.delete(key);
    }

    @Override
    public void invalidate() {
        cache.clearAll();

    }

}
