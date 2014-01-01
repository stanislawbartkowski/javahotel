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
package com.jython.ui;

import com.jythonui.server.IJythonUIServerProperties;

/**
 * @author hotel
 * 
 */
public class ServerProperties implements IJythonUIServerProperties {

    private final String RESOURCES = "resources";
    private final String DIALOGDIR = "dialogs";
    private final String PACKAGEDIR = "packages";
    private final String MESS = "bundle";

    private String getResource(String dir) {
        return TestHelper.class.getClassLoader()
                .getResource(RESOURCES + "/" + dir).getPath();
    }

    @Override
    public String getDialogDirectory() {
        return getResource(DIALOGDIR);
    }

    @Override
    public String getPackageDirectory() {
        return getResource(PACKAGEDIR);
    }

    @Override
    public boolean isCached() {
        return true;
    }

    @Override
    public String getBundleBase() {
        return getResource(MESS);
    }
}
