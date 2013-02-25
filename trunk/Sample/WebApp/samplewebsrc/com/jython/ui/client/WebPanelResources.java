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
package com.jython.ui.client;

import java.util.HashMap;
import java.util.Map;

import com.gwtmodel.table.factories.IWebPanelResources;

class WebPanelResources implements IWebPanelResources {

    private final Map<String, String> ma = new HashMap<String, String>();

    WebPanelResources() {
        ma.put(TITLE, "Example of Jython UI");
        ma.put(PRODUCTNAME, "Jython UI demo");
        ma.put(OWNERNAME, "Demo");
        ma.put(VERSION, "Jython sample v 1.0");
    }

    public String getRes(String res) {
        String val = ma.get(res);
        return val;
    }
}