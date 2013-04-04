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
package com.jythonui.server.service;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.jythonui.client.service.JythonService;
import com.jythonui.server.IJythonClientRes;
import com.jythonui.server.IJythonUIServer;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.security.ISecurity;
import com.jythonui.shared.ClientProp;
import com.jythonui.shared.DialogInfo;
import com.jythonui.shared.DialogVariables;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class JythonServiceImpl extends RemoteServiceServlet implements
        JythonService {

    @Override
    public DialogInfo getDialogFormat(String token, String name) {
        IJythonUIServer iServer = Holder.getiServer();
        return iServer.findDialog(token, name);
    }

    @Override
    public DialogVariables runAction(DialogVariables v, String name,
            String actionId) {
        IJythonUIServer iServer = Holder.getiServer();
        return iServer.runAction(v, name, actionId);
    }

    @Override
    public ClientProp getClientRes() {
        IJythonClientRes iClient = Holder.getiClient();
        return iClient.getClientRes();

    }

    @Override
    public String login(String shiroRealm, String user, String password) {
        ISecurity iSec = Holder.getiSec();
        return iSec.authenticateToken(shiroRealm, user, password);
    }

    @Override
    public void logout(String token) {
        ISecurity iSec = Holder.getiSec();
        iSec.logout(token);
    }

}
