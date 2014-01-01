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
package com.jythonui.server.defa;

import java.util.List;

import javax.inject.Inject;

import com.jythonui.server.registry.IStorageRegistry;
import com.jythonui.server.registry.IStorageRegistryFactory;
import com.jythonui.server.storage.registry.IStorageRealmRegistry;

public class StorageRealmRegistryFactory implements IStorageRegistryFactory {

    private final IStorageRealmRegistry iRegistry;

    @Inject
    public StorageRealmRegistryFactory(IStorageRealmRegistry iRegistry) {
        this.iRegistry = iRegistry;
    }

    private class StorageRealmRegistry implements IStorageRegistry {

        private final String realm;

        StorageRealmRegistry(String realm) {
            this.realm = realm;
        }

        @Override
        public void putEntry(String key, byte[] value) {
            iRegistry.putEntry(realm, key, value);
        }

        @Override
        public byte[] getEntry(String key) {
            return iRegistry.getEntry(realm, key);
        }

        @Override
        public void removeEntry(String key) {
            iRegistry.removeEntry(realm, key);
        }

        @Override
        public List<String> getKeys() {
            return iRegistry.getKeys(realm);
        }

    }

    @Override
    public IStorageRegistry construct(String realm) {
        return new StorageRealmRegistry(realm);
    }

}
