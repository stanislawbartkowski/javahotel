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
package com.jythonui.server.security.cache.impl;

import java.io.InvalidClassException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import com.jythonui.server.logmess.ILogMess;
import com.jythonui.server.logmess.LogMess;
import com.jythonui.server.security.ISecuritySessionCache;
import com.jythonui.server.security.cache.ISecuritySessionMemCache;
import com.jythonui.server.security.cache.ISecuritySessionPersistent;

public class SecuritySessionStore implements ISecuritySessionCache {

    private final ISecuritySessionMemCache iMemCache;
    private final ISecuritySessionPersistent iPersistent;

    private static final Logger log = Logger
            .getLogger(SecuritySessionStore.class.getName());

    @Inject
    public SecuritySessionStore(ISecuritySessionMemCache iMemCache,
            ISecuritySessionPersistent iPersistent) {
        this.iMemCache = iMemCache;
        this.iPersistent = iPersistent;
    }

    @Override
    public Object get(String key) throws InvalidClassException {
        Object val = iMemCache.get(key);
        if (val != null)
            return val;
        val = iPersistent.get(key);
        if (val == null)
            return null;
        // add value to cache again
        log.log(Level.FINE, LogMess.getMessN(ILogMess.PUTINCACHEAGAIN, key));
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

}
