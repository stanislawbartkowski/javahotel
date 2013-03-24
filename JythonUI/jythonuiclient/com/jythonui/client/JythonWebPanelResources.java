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
package com.jythonui.client;

import java.util.HashMap;
import java.util.Map;

import com.gwtmodel.table.factories.IWebPanelResources;
import com.jythonui.shared.ICommonConsts;

class JythonWebPanelResources implements IWebPanelResources {

    private final Map<String, String> ma = new HashMap<String, String>();

    private void putRes(String key, String rkey, Map<String, String> res) {
        String val = res.get(rkey);
        if (val == null) {
            return;
        }
        ma.put(key, val);
    }

    JythonWebPanelResources(Map<String, String> res) {
        putRes(TITLE, ICommonConsts.APP_TITLE, res);
        putRes(PRODUCTNAME, ICommonConsts.APP_PRODUCTNAME, res);
        putRes(OWNERNAME, ICommonConsts.APP_OWNERNAME, res);
        putRes(VERSION, ICommonConsts.APP_VERSION, res);
        putRes(IIMAGEPRODUCT, ICommonConsts.APP_PRODUCTIMAGE, res);
    }

    public String getRes(String res) {
        String val = ma.get(res);
        return val;
    }
}