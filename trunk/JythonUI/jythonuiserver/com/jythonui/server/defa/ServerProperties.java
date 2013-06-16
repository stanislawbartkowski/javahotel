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
package com.jythonui.server.defa;

import java.net.URL;

import javax.inject.Inject;

import com.jython.ui.shared.ISharedConsts;
import com.jythonui.server.IConsts;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.JythonUiServerProvider;

/**
 * @author hotel
 * 
 */
public class ServerProperties implements IJythonUIServerProperties {

    private final IsCached isC;

    @Inject
    public ServerProperties(IsCached isC) {
        this.isC = isC;
    }

    private String getResource(String dir) {
        URL u = JythonUiServerProvider.class.getClassLoader().getResource(
                ISharedConsts.RESOURCES + "/" + dir);
        if (u == null)
            return null;
        return u.getPath();
    }

    @Override
    public String getDialogDirectory() {
        return getResource(IConsts.DIALOGDIR);
    }

    @Override
    public String getPackageDirectory() {
        return getResource(IConsts.PACKAGEDIR);
    }

    @Override
    public boolean isCached() {
        if (isC == null)
            return false;
        return isC.isCached();
    }

    @Override
    public String getBundleBase() {
        return getResource(IConsts.BUNDLEDIR);
    }

}
