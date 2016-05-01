/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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
package com.jythonui.server.guavacache;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.gwtmodel.commoncache.ICommonCache;
import com.gwtmodel.mapcache.ICommonCacheFactory;

public class GuavaCacheFactory implements ICommonCacheFactory {

    private class GCache implements ICommonCache {

        private final Cache<String, Object> gCache;

        GCache() {
            gCache = CacheBuilder.newBuilder().maximumSize(1000)
                    .expireAfterAccess(1, TimeUnit.HOURS).build();
        }

        @Override
        public Object get(String key) {
            return gCache.getIfPresent(key);
        }

        @Override
        public void put(String key, Object o) {
            gCache.put(key, o);
        }

        @Override
        public void remove(String key) {
            gCache.invalidate(key);
        }

        @Override
        public void invalidate() {
            gCache.invalidateAll();
        }

    }

    @Override
    public ICommonCache construct(String cName) {
        // cName is not necessary, all instances are different
        return new GCache();
    }

}
