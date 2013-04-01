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

import java.io.InvalidClassException;

import javax.inject.Inject;

import com.gwtmodel.commoncache.ICommonCache;
import com.jythonui.server.security.ISessionCache;

public class SecurityCache implements ISessionCache {

    private final ICommonCache iCache;

    @Inject
    public SecurityCache(ICommonCache iCache) {
        this.iCache = iCache;
    }

    @Override
    public Object get(String key) throws InvalidClassException {
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

}
