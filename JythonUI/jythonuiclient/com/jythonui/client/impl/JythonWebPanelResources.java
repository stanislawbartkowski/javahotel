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
package com.jythonui.client.impl;

import java.util.HashMap;
import java.util.Map;

import com.gwtmodel.table.factories.IWebPanelResources;
import com.jythonui.client.IUIConsts;
import com.jythonui.shared.ClientProp;

class JythonWebPanelResources implements IWebPanelResources {

    private final Map<String, String> ma = new HashMap<String, String>();
    private final ClientProp res;

    private void putRes(String key, String rkey, ClientProp res) {
        String val = res.getAttr(rkey);
        if (val == null) {
            return;
        }
        ma.put(key, val);
    }

    JythonWebPanelResources(ClientProp res) {
        this.res = res;
        putRes(TITLE, IUIConsts.APP_TITLE, res);
        putRes(PRODUCTNAME, IUIConsts.APP_PRODUCTNAME, res);
        putRes(OWNERNAME, IUIConsts.APP_OWNERNAME, res);
        putRes(VERSION, IUIConsts.APP_VERSION, res);
        putRes(IIMAGEPRODUCT, IUIConsts.APP_PRODUCTIMAGE, res);
        ma.put(JUIVERSION, IUIConsts.UIVersion);
    }

    @Override
    public String getRes(String reso) {
        String val = ma.get(reso);
        if (val != null)
            return val;
        val = res.getAttr(reso);
        return val;
    }
}