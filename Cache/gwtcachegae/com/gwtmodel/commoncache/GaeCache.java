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
package com.gwtmodel.commoncache;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

/**
 * @author hotel
 * 
 */
class GaeCache implements ICommonCache {
    // private static Cache cache;

    private final MemcacheService cache;
    static final Logger lo = Logger.getLogger(GaeCache.class.getName());

    GaeCache() {
        cache = MemcacheServiceFactory.getMemcacheService();
    }

    public Object get(String key) {
        return cache.get(key);
    }

    public Object getE(String key) {
        Object o = cache.get(key);
        if (o == null) {
            lo.log(Level.SEVERE, "", new RuntimeException(key
                    + " entry not found"));
        }
        return o;
    }

    public void put(String key, Object o) {
        cache.put(key, o);
    }

    public void remove(String key) {
        cache.delete(key);
    }

    public long inc(String iKey, boolean plus) {
        long de;
        if (plus) {
            de = 1;
        } else {
            de = -1;
        }
        Long l = cache.increment(iKey, de);
        if (l == null) {
            cache.put(iKey, de);
            return de;
        }
        return l;
    }

    @Override
    public void clearAll() {
        cache.clearAll();
    }

}
