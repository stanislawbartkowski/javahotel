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
package com.jythonui.server.registry.object;

import java.util.List;

import com.gwtmodel.commoncache.ICommonCache;
import com.jython.ui.shared.UObjects;
import com.jythonui.server.registry.IStorageRegistry;

class ObjectRegistry implements ICommonCache {

    private final IStorageRegistry iRegistry;

    ObjectRegistry(IStorageRegistry iRegistry) {
        this.iRegistry = iRegistry;
    }

    @Override
    public Object get(String key) {
        byte[] value = iRegistry.getEntry(key);
        return UObjects.get(value, key);
    }

    @Override
    public void put(String key, Object o) {
        byte[] val = UObjects.put(o, key);
        if (val == null)
            return;
        iRegistry.putEntry(key, val);
    }

    @Override
    public void remove(String key) {
        iRegistry.removeEntry(key);

    }

    @Override
    public void invalidate() {
        // TODO: add hoc done, improve, make transactional
        // used now only in test environment
        List<String> keys = iRegistry.getKeys();
        for (String k : keys)
            iRegistry.removeEntry(k);
    }

}