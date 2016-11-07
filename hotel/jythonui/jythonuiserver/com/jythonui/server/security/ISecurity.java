/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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
package com.jythonui.server.security;

import com.jythonui.server.security.token.ICustomSecurity;

public interface ISecurity {

    String authenticateToken(String realm, String userName, String password,
            ICustomSecurity iCustom);

    void logout(String token);

    boolean validToken(String token);

    boolean isAuthorized(String token, String permission);

    ICustomSecurity getCustom(String token);

    String getUserName(String token);

    String evaluateExpr(String token, String expr);

    String withoutlogin(ICustomSecurity iCustom);

}
