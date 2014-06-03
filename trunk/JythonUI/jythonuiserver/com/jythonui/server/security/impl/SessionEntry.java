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
package com.jythonui.server.security.impl;

import java.io.Serializable;

import com.jythonui.server.security.token.ICustomSecurity;

class SessionEntry implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String user;
    private final String password;
    private final String realm;
    private final ICustomSecurity iCustom;

    SessionEntry(String user, String password, String realm,
            ICustomSecurity iCustom) {
        this.user = user;
        this.password = password;
        this.realm = realm;
        this.iCustom = iCustom;
    }

    String getUser() {
        return user;
    }

    String getPassword() {
        return password;
    }

    String getRealm() {
        return realm;
    }

    boolean eq(SessionEntry se) {
        if (!user.equals(se.user))
            return false;
        if (!realm.equals(realm))
            return false;
        if (iCustom != null && se.iCustom != null) {
            if (!iCustom.eq(se.iCustom))
                return false;
        }
        return true;
    }

    public ICustomSecurity getiCustom() {
        return iCustom;
    }

}
