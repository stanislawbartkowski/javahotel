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
package com.jythonui.server.ejb;

import java.util.List;

import com.jythonui.server.storage.registry.IStorageRealmRegistry;

abstract class AbstractStorageJpaRegistry implements IStorageRealmRegistry {

    protected IStorageRealmRegistry iStorage;

    @Override
    public void putEntry(String realM, String key, byte[] value) {
        iStorage.putEntry(realM, key, value);
    }

    @Override
    public byte[] getEntry(String realM, String key) {
        return iStorage.getEntry(realM, key);
    }

    @Override
    public void removeEntry(String realM, String key) {
        iStorage.removeEntry(realM, key);
    }

    @Override
    public List<String> getKeys(String realM) {
        return iStorage.getKeys(realM);
    }

    @Override
    public void addNewEntry(String realM, String key, byte[] value) {
        iStorage.addNewEntry(realM, key, value);

    }
}