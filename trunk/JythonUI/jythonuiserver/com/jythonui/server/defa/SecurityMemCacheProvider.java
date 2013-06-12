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
package com.jythonui.server.defa;

import javax.inject.Inject;
import javax.inject.Provider;

import com.gwtmodel.commoncache.ICommonCache;
import com.gwtmodel.mapcache.ICommonCacheFactory;
import com.jythonui.server.IConsts;
import com.jythonui.server.security.cache.ISecuritySessionMemCache;

public class SecurityMemCacheProvider implements
        Provider<ISecuritySessionMemCache> {

    private final ICommonCacheFactory cFactory;

    @Inject
    public SecurityMemCacheProvider(ICommonCacheFactory cFactory) {
        this.cFactory = cFactory;
    }

    // private final static boolean testCache = true;
    private final static boolean testCache = false;
    private static int noTest = 0;

    private class SessionMemCacheDecorator implements ISecuritySessionMemCache {
        private final ICommonCache iCache;

        SessionMemCacheDecorator(ICommonCache iCache) {
            this.iCache = iCache;
        }

        @Override
        public Object get(String key) {
            if (testCache) {
                noTest++;
                // emulate empty cache
                if (noTest % 2 == 0)
                    return null;
            }
            return iCache.get(key);
        }

        @Override
        public void put(String key, Object o) {
            iCache.put(key, o);
        }

        @Override
        public void remove(String key) {
            iCache.remove(key);
        }

        @Override
        public void invalidate() {
            iCache.invalidate();
        }
    }

    @Override
    public ISecuritySessionMemCache get() {
        return new SessionMemCacheDecorator(
                cFactory.construct(IConsts.SECURITYMEMNAME));
    }

}
