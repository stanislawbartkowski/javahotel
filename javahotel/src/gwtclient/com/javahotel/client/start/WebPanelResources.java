/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.start;

import java.util.HashMap;
import java.util.Map;

import com.gwtmodel.table.view.webpanel.IWebPanelResources;
import com.javahotel.client.IImageGallery;
import com.javahotel.client.IResLocator;

class WebPanelResources implements IWebPanelResources {

    private final Map<String, String> ma = new HashMap<String, String>();

    WebPanelResources(IResLocator iS) {
        ma.put(TITLE, "Hotel na Javie");
        ma.put(IMAGELOGOUT, IImageGallery.LOGOUT);
        ma.put(IIMAGEPRODCUT, IImageGallery.LOGHOTEL);
        ma.put(PRODUCTNAME, iS.getLabels().productName());
        ma.put(OWNERNAME, "Wersja demonstracyjna");
        ma.put(WERSJA, iS.getLabels().Wersja());
        ma.put(LOGOUTQUESTION, iS.getLabels().LogoutQuestion());
        ma.put(COUNTER, "");
    }

    public String getRes(String res) {
        String val = ma.get(res);
        return val;
    }
}