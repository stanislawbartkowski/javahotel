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
package com.jythonui.server.memstorage;

import com.gwtmodel.commoncache.ICommonCache;
import com.jython.ui.shared.UtilHelper;
import com.jythonui.server.IStorageMemCache;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.logmess.ILogMess;

class StorageCache extends UtilHelper implements IStorageMemCache {

    private final ICommonCache iMemCache;
    private final ICommonCache iPersistent;
    private final IGetLogMess gMess;

    StorageCache(ICommonCache iMemCache, ICommonCache iPersistent,
            IGetLogMess gMess) {
        this.iMemCache = iMemCache;
        this.iPersistent = iPersistent;
        this.gMess = gMess;
    }

    @Override
    public Object get(String key) {
        Object val = iMemCache.get(key);
        if (val != null)
            return val;
        val = iPersistent.get(key);
        if (val == null)
            return null;
        // add value to cache again
        logDebug(gMess.getMessN(ILogMess.PUTINCACHEAGAIN, key));
        iMemCache.put(key, val);
        return val;
    }

    @Override
    public void put(String key, Object o) {
        iMemCache.put(key, o);
        iPersistent.put(key, o);
    }

    @Override
    public void remove(String key) {
        iMemCache.remove(key);
        iPersistent.remove(key);
    }

    @Override
    public void invalidate() {
        iMemCache.invalidate();
        iPersistent.invalidate();
    }

}
