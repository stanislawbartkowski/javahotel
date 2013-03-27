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
package com.jythonui.server.holder;

import javax.inject.Inject;

import com.jythonui.server.IJythonClientRes;
import com.jythonui.server.IJythonUIServer;

public class Holder {

    @Inject
    private static IJythonUIServer iServer;

    @Inject
    private static IJythonClientRes iClient;

    private static boolean auth = false;

    public static boolean isAuth() {
        return auth;
    }

    public static void setAuth(boolean pauth) {
        auth = pauth;
    }

    public static IJythonUIServer getiServer() {
        return iServer;
    }

    public static IJythonClientRes getiClient() {
        return iClient;
    }

}
