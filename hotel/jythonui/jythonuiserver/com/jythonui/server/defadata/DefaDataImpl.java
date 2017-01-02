/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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
package com.jythonui.server.defadata;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import com.gwtmodel.table.common.CUtil;
import com.jythonui.server.IConsts;
import com.jythonui.server.IDefaultData;
import com.jythonui.server.IGetAppProp;
import com.jythonui.server.IGetEnvDefaultData;
import com.jythonui.server.IStorageMemCache;
import com.jythonui.server.Util;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.security.ISecurity;

public class DefaDataImpl implements IDefaultData {

    private final IStorageMemCache mCache;
    private final IGetAppProp iApp;
    private Map<String, String> mMess = null;
    private final IGetEnvDefaultData iGet;

    private String getLocalized(String key) {
        String lang = Util.getLocale();
        if (CUtil.EmptyS(lang))
            return key;
        return key + "_" + lang;
    }

    private String getUserLocalized(String key) {
        String keyL = getLocalized(key);
        ISecurity iSec = Holder.getiSec();
        if (iSec == null)
            return keyL;
        if (CUtil.EmptyS(Util.getToken()))
            return keyL;
        String user = iSec.getUserName(Util.getToken());
        if (CUtil.EmptyS(user))
            return keyL;
        return user + "_" + keyL;
    }

    @Inject
    public DefaDataImpl(IGetAppProp iApp,
            @Named(IConsts.DEFADATAREALM) IStorageMemCache mCache,
            IGetEnvDefaultData iGet) {
        this.mCache = mCache;
        this.iApp = iApp;
        this.iGet = iGet;
    }

    private void setMess() {
        if (mMess != null)
            return;
        mMess = iApp.get();
    }

    private String getValueS(String key) {
        String val = (String) mCache.get(getUserLocalized(key));
        if (!CUtil.EmptyS(val))
            return val;
        String defV = null;
        setMess();
        if (!CUtil.EmptyS(Util.getLocale()))
            defV = mMess.get(getLocalized(key));
        if (!CUtil.EmptyS(defV))
            return defV;
        return mMess.get(key);
    }

    @Override
    public String getValue(String key) {
        String res = getValueS(key);
        if (CUtil.EmptyS(res))
            return res;
        if (CUtil.EqNS(res, IConsts.VALUEFROMENV))
            return iGet.getVal(key);
        return res;
    }

    @Override
    public void putValue(String key, String value) {
        if (CUtil.EmptyS(value))
            mCache.remove(getUserLocalized(key));
        else
            mCache.put(getUserLocalized(key), value);
    }

    @Override
    public void clear() {
        mCache.invalidate();
    }

}
