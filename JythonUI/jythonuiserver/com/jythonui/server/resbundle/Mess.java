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
package com.jythonui.server.resbundle;

import java.util.Map;

import javax.inject.Inject;

import com.jythonui.server.IConsts;
import com.jythonui.server.IGetResourceMap;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.Util;
import com.jythonui.server.getmess.GetLogMessFactory;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.resourcemulti.IReadMultiResourceFactory;
import com.jythonui.shared.CustomMessages;

public class Mess implements IAppMess {

    private final IJythonUIServerProperties iRes;
    private final IGetResourceMap iGet;
    private IGetLogMess iMess = null;
    private String lastloca = null;
    private Map<String, String> commonmess = null;
    private final IReadMultiResourceFactory iFactory;

    @Inject
    public Mess(IJythonUIServerProperties iRes, IGetResourceMap iGet,
            IReadMultiResourceFactory iFactory) {
        this.iRes = iRes;
        this.iGet = iGet;
        this.iFactory = iFactory;
    }

    private boolean localeChanged() {
        String loc = Util.getLocale();
        if ((loc == null) && (lastloca == null))
            return false;
        if ((loc == null) && (lastloca != null))
            return true;
        if ((loc != null) && (lastloca == null))
            return true;
        return !loc.equals(lastloca);
    }

    private void setMess() {
        if ((iMess == null) || !iRes.isCached() || localeChanged()) {
            // use full path
            commonmess = iGet.getResourceMap(iFactory.construct(), true,
                    "mess", "commonmess");

            Map<String, String> mess = iGet.getResourceMap(iRes.getResource(),
                    false, IConsts.BUNDLEDIR, "messages");
            iMess = GetLogMessFactory.construct(mess);
            lastloca = Util.getLocale();
        }
    }

    @Override
    public String getMess(String errCode, String key, String... params) {
        setMess();
        return iMess.getMess(errCode, key, params);
    }

    @Override
    public String getMessN(String key, String... params) {
        setMess();
        return iMess.getMessN(key, params);
    }

    @Override
    public CustomMessages getCustomMess() {
        // if (iRes.getBundleBase() == null)
        // return null;
        setMess();
        CustomMessages cust = new CustomMessages();
        // firstly common mess
        for (String key : commonmess.keySet()) {
            cust.setAttr(key, commonmess.get(key));
        }
        Map<String, String> ma = iMess.getMess();
        for (String key : ma.keySet()) {
            cust.setAttr(key, ma.get(key));
        }
        return cust;
    }

    @Override
    public Map<String, String> getMess() {
        setMess();
        return iMess.getMess();
    }

}
