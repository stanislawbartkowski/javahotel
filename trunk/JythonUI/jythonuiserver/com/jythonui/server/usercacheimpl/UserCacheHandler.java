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
package com.jythonui.server.usercacheimpl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.inject.Inject;
import com.gwtmodel.commoncache.ICommonCache;
import com.gwtmodel.mapcache.ICommonCacheFactory;
import com.gwtmodel.table.common.CUtil;
import com.jythonui.server.IConsts;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.IUserCacheHandler;
import com.jythonui.server.security.ISecurity;

public class UserCacheHandler implements IUserCacheHandler {

    private final IJythonUIServerProperties p;
    private final ConcurrentMap<String, ICommonCache> maCache = new ConcurrentHashMap<String, ICommonCache>();
    private final ISecurity iSec;
    private final ICommonCacheFactory iCacheFactory;

    @Inject
    public UserCacheHandler(IJythonUIServerProperties p, ISecurity iSec,
            ICommonCacheFactory iCacheFactory) {
        this.p = p;
        this.iSec = iSec;
        this.iCacheFactory = iCacheFactory;
    }

    private ICommonCache getC(String token) {
        String user = null;
        if (token != null) user = iSec.getUserName(token);
        String cacheName = IConsts.USERCOMMONCACHENAME;
        if (!CUtil.EmptyS(user))
            cacheName = cacheName + "-" + user;
        ICommonCache i = maCache.get(cacheName);
        if (i == null) {
            i = iCacheFactory.construct(cacheName);
            maCache.put(cacheName, i);
        }
        return i;
    }

    @Override
    public Object get(String token, String key) {
        if (!p.isCached())
            return null;
        return getC(token).get(key);
    }

    @Override
    public void put(String token, String key, Object o) {
        if (!p.isCached())
            return;
        getC(token).put(key, o);
    }

}
