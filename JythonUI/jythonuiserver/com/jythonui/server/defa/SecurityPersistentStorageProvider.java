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
import com.jythonui.server.IConsts;
import com.jythonui.server.registry.IStorageRegistry;
import com.jythonui.server.registry.IStorageRegistryFactory;
import com.jythonui.server.registry.object.ObjectRegistryFactory;
import com.jythonui.server.security.cache.ISecuritySessionPersistent;

public class SecurityPersistentStorageProvider implements
        Provider<ISecuritySessionPersistent> {

    private final ObjectRegistryFactory oProvider;
    private final IStorageRegistryFactory iStorage;

    @Inject
    public SecurityPersistentStorageProvider(ObjectRegistryFactory oProvider,
            IStorageRegistryFactory iStorage) {
        this.oProvider = oProvider;
        this.iStorage = iStorage;
    }

    private class SecuritySessionPersistentDecorator implements
            ISecuritySessionPersistent {

        private final ICommonCache iCache;

        SecuritySessionPersistentDecorator(ICommonCache iCache) {
            this.iCache = iCache;
        }

        @Override
        public Object get(String key) {
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
    public ISecuritySessionPersistent get() {
        IStorageRegistry sRegistry = iStorage
                .construct(IConsts.SECURITYREGISTRY);
        return new SecuritySessionPersistentDecorator(
                oProvider.construct(sRegistry));
    }

}
