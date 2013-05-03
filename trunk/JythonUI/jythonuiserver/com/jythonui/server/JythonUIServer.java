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
package com.jythonui.server;

import com.gwtmodel.commoncache.ICommonCache;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.security.ISecurity;
import com.jythonui.shared.CustomMessages;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogInfo;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.SecurityInfo;

/**
 * @author hotel
 * 
 */
class JythonUIServer implements IJythonUIServer {

    private final IJythonUIServerProperties p;
    private final MCached mCached;
    private final ISecurity iSec;

    JythonUIServer(IJythonUIServerProperties p, ICommonCache mCache,
            ISecurity iSec) {
        this.p = p;
        this.mCached = new MCached(p, mCache);
        this.iSec = iSec;
    }

    @Override
    public DialogInfo findDialog(String token, String dialogName) {
        DialogFormat d = GetDialog.getDialog(p, mCached, token, dialogName,
                true);
        if (d == null)
            return null;
        SecurityInfo si = AddSecurityInfo.create(iSec, token, d);
        CustomMessages custMess = null;
        if (!p.isCached())
            custMess = Holder.getAppMess().getCustomMess();
        for (ListFormat li : d.getListList()) {
            // columns
            SecurityInfo sList = AddSecurityInfo.createForColumns(iSec, token,
                    li);
            si.getListSecur().put(li.getId(), sList);
            // crud dialog
            DialogFormat dElem = li.getfElem();
            // ignore if not exist
            if (dElem == null)
                continue;
            SecurityInfo sl = AddSecurityInfo.create(iSec, token, dElem);
            si.getlSecur().put(li.getId(), sl);
        }
        return new DialogInfo(d, si, custMess);
    }

    @Override
    public DialogVariables runAction(DialogVariables v, String dialogName,
            String actionId) {
        String securityToken = v.getValueS(ICommonConsts.SECURITYTOKEN);
        DialogFormat d = GetDialog.getDialog(p, mCached, securityToken,
                dialogName, false);
        RunJython.executeJython(p, mCached, v, d, actionId);
        return v;
    }

}
