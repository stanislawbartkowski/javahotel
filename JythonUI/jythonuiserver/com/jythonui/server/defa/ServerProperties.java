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

import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.JythonUiServerProvider;

/**
 * @author hotel
 * 
 */
public class ServerProperties implements IJythonUIServerProperties {

    private final String RESOURCES = "resources";
    private final String DIALOGDIR = "dialogs";
    private final String PACKAGEDIR = "packages";
    private final IsCached isC;

    @Inject
    public ServerProperties(IsCached isC) {
        this.isC = isC;
    }

    @Override
    public URL getDialogDirectory() {
        return JythonUiServerProvider.class.getClassLoader().getResource(
                RESOURCES + "/" + DIALOGDIR);
    }

    @Override
    public URL getPackageDirectory() {
        return JythonUiServerProvider.class.getClassLoader().getResource(
                RESOURCES + "/" + PACKAGEDIR);
    }

    @Override
    public boolean isCached() {
        if (isC == null)
            return false;
        return isC.isCached();
    }

}
