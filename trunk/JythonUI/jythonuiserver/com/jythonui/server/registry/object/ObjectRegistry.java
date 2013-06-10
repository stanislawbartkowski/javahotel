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
package com.jythonui.server.registry.object;

import java.util.logging.Logger;

import com.gwtmodel.commoncache.ICommonCache;
import com.jythonui.server.Util;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.registry.IStorageRegistry;

class ObjectRegistry implements ICommonCache {

    private final IStorageRegistry iRegistry;
    private final IGetLogMess gMess;

    ObjectRegistry(IStorageRegistry iRegistry, IGetLogMess gMess) {
        this.iRegistry = iRegistry;
        this.gMess = gMess;
    }

    @Override
    public Object get(String key) {
        byte[] value = iRegistry.getEntry(key);
        return Util.get(value, key, gMess);
    }

    @Override
    public void put(String key, Object o) {
        byte[] val = Util.put(o, key, gMess);
        if (val == null)
            return;
        iRegistry.putEntry(key, val);
    }

    @Override
    public void remove(String key) {
        iRegistry.removeEntry(key);

    }

}