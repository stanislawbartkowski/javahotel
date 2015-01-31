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
package com.jythonui.server.service;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.jythonui.client.service.JythonService;
import com.jythonui.server.IConsts;
import com.jythonui.server.IJythonClientRes;
import com.jythonui.server.IJythonRPCNotifier;
import com.jythonui.server.IJythonUIServer;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.security.ISecurity;
import com.jythonui.server.security.token.ICustomSecurity;
import com.jythonui.shared.ClientProp;
import com.jythonui.shared.CustomSecurity;
import com.jythonui.shared.DialogInfo;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.RequestContext;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class JythonServiceImpl extends RemoteServiceServlet implements
        JythonService {

    @Override
    public DialogInfo getDialogFormat(RequestContext context, String name) {
        IJythonUIServer iServer = Holder.getiServer();
        return iServer.findDialog(context, name);
    }

    @Override
    public DialogVariables runAction(RequestContext context, DialogVariables v,
            String name, String actionId) {
        IJythonUIServer iServer = Holder.getiServer();
        return iServer.runAction(context, v, name, actionId);
    }

    @Override
    public ClientProp getClientRes(RequestContext context) {
        IJythonClientRes iClient = Holder.getiClient();
        return iClient.getClientRes(context);

    }

    @Override
    public String login(String shiroRealm, String user, String password,
            CustomSecurity iCustom) {
        IJythonRPCNotifier iRPC = Holder.getRPC();
        iRPC.hello(IJythonRPCNotifier.BEFORELOGIN);
        ISecurity iSec = Holder.getiSec();
        ICustomSecurity iCust = Holder.getSecurityConvert().construct(iCustom);
        return iSec.authenticateToken(shiroRealm, user, password, iCust);
    }

    @Override
    public void logout(String token) {
        ISecurity iSec = Holder.getiSec();
        iSec.logout(token);
    }

    @Override
    public String withoutlogin(CustomSecurity custom) {
        ICustomSecurity cu;
        if (custom == null) {
            CustomSecurity cust = new CustomSecurity();
            cust.setAttr(IConsts.INSTANCEID, ISharedConsts.INSTANCEDEFAULT);
            cu = Holder.getPersonSecurityConvert().construct(cust);
        } else
            cu = Holder.getSecurityConvert().construct(custom);
        return Holder.getiSec().withoutlogin(cu);
    }

}
