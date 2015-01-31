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
package com.jythonui.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.jythonui.shared.ClientProp;
import com.jythonui.shared.DialogInfo;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.CustomSecurity;
import com.jythonui.shared.RequestContext;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface JythonService extends RemoteService {

    DialogInfo getDialogFormat(RequestContext context, String name);

    DialogVariables runAction(RequestContext context, DialogVariables v,
            String name, String actionId);

    ClientProp getClientRes(RequestContext context);

    String login(String shiroRealm, String user, String password,
            CustomSecurity iCustom);

    String withoutlogin(CustomSecurity iCustom);

    void logout(String token);

}
