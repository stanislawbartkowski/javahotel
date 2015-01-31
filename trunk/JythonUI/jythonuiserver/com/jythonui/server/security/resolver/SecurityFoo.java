/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
package com.jythonui.server.security.resolver;

import org.apache.shiro.subject.Subject;

// must be public
public class SecurityFoo {

    private final Subject currentUser;

    SecurityFoo(Subject currentUser) {
        this.currentUser = currentUser;
    }

    public boolean u(String user) {
        Object loggedUser = currentUser.getPrincipal();
        String userN = loggedUser.toString();
        return user.equals(userN);
    }

    public boolean r(String role) {
        return currentUser.hasRole(role);
    }

    public boolean p(String permission) {
        return currentUser.isPermitted(permission);
    }

}
