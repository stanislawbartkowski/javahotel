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
package com.gwtmodel.table.factories.customvalues;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.factories.IWebPanelResources;

public class WebPanelResources implements IWebPanelResources {

    private final ITableCustomFactories iFactories;
    private final Map<String, String> ma = new HashMap<String, String>();

    @Inject
    public WebPanelResources(ITableCustomFactories iFactories) {
        this.iFactories = iFactories;
        ma.put(TITLE, "Default title");
        ma.put(IMAGELOGOUT, "default_logout.png");
        ma.put(IIMAGEPRODUCT, "default_logo.png");
        ma.put(PRODUCTNAME, "Default application");
        ma.put(OWNERNAME, "Default owner");
        ma.put(VERSION, "Version default");
        ma.put(STATUSHTML, "default_header.html");
        ma.put(SCROLLWITHDATE, "default_scrollWithDate.html");
        ma.put(SCROLLWITHOUTDATE, "default_scrollWithoutDate.html");
        ma.put(PROGRESSICON, "default_progressicon");
    }

    @Override
    public String getRes(String res) {
        String val = null;
        if (iFactories.getWebPanelResourcesNotDefault() != null) {
            val = iFactories.getWebPanelResourcesNotDefault().getRes(res);
        }
        if (val != null)
            return val;
        return ma.get(res);

    }

}
