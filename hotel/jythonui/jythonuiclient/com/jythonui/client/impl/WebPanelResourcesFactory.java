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

import com.gwtmodel.table.factories.IWebPanelResources;
import com.jythonui.client.interfaces.IWebPanelResourcesFactory;
import com.jythonui.shared.ClientProp;

public class WebPanelResourcesFactory implements IWebPanelResourcesFactory {

    @Override
    public IWebPanelResources construct(ClientProp res) {
        return new JythonWebPanelResources(res);
    }

}
