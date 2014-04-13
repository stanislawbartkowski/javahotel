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
package com.jythonui.server.defa;

import javax.inject.Inject;

import com.jythonui.server.IConsts;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.Util;

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

    @Override
    public String getDialogDirectory() {
        return Util.getResourceAsDirectory(IConsts.DIALOGDIR);
    }

    @Override
    public String getPackageDirectory() {
        return Util.getResourceAsDirectory(IConsts.PACKAGEDIR);
    }

    @Override
    public boolean isCached() {
        if (isC == null)
            return false;
        return isC.isCached();
    }

    @Override
    public String getBundleBase() {
        return Util.getResourceAsDirectory(IConsts.BUNDLEDIR);
    }

    @Override
    public String getResourceDirectory() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getEJBHost() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getEJBPort() {
        // TODO Auto-generated method stub
        return null;
    }

}
