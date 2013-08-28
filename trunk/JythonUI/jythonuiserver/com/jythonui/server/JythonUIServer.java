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

import java.util.logging.Level;
import java.util.logging.Logger;

import com.gwtmodel.commoncache.ICommonCache;
import com.gwtmodel.table.common.CUtil;
import com.jythonui.server.dialog.GetDialog;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.jython.RunJython;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.server.security.ISecurity;
import com.jythonui.server.xml.IXMLTransformer;
import com.jythonui.shared.CustomMessages;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogInfo;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.JythonUIFatal;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.RequestContext;
import com.jythonui.shared.SecurityInfo;

/**
 * @author hotel
 * 
 */
class JythonUIServer implements IJythonUIServer {

    private final IJythonUIServerProperties p;
    private final MCached mCached;
    private final ISecurity iSec;
    static final private Logger log = Logger.getLogger(JythonUIServer.class
            .getName());

    static private void error(String mess) {
        log.log(Level.SEVERE, mess);
        throw new JythonUIFatal(mess);
    }

    JythonUIServer(IJythonUIServerProperties p, ICommonCache mCache,
            ISecurity iSec) {
        this.p = p;
        this.mCached = new MCached(p, mCache);
        this.iSec = iSec;
    }

    @Override
    public DialogInfo findDialog(RequestContext rcontext, String dialogName) {
        Util.setContext(rcontext);
        String token = Util.getToken();
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
    public DialogVariables runAction(RequestContext rcontext,
            DialogVariables v, String dialogName, String actionId) {
        Util.setContext(rcontext);
        String locale = Util.getLocale();
        String securityToken = Util.getToken();
        v.setSecurityToken(securityToken);
        v.setLocale(locale);
        v.setValueS(ICommonConsts.J_DIALOGNAME, dialogName);
        DialogFormat d = GetDialog.getDialog(p, mCached, securityToken,
                dialogName, false);
        if (CUtil.onTheList(actionId, d.getAsXmlList())) {
            v.setValueS(ICommonConsts.JXMLCONTENT, Holder.getXMLTransformer()
                    .toXML(dialogName, v));
        }
        RunJython.executeJython(p, mCached, v, d, actionId);
        FieldValue b = v.getValue(ICommonConsts.JXMLSETCONTENT);
        if (b != null)
            if (b.getValueB().booleanValue()) {
                FieldValue x = v.getValue(ICommonConsts.JXMLCONTENT);
                if (x == null) {
                    String mess = Holder.getM().getMess(IErrorCode.ERRORCODE69,
                            ILogMess.XMLSETCONTENTBUTCONTENTNOTAVAILABLE,
                            d.getId(), ICommonConsts.JXMLCONTENT,
                            ICommonConsts.JXMLCONTENT);
                    error(mess);
                }
                Holder.getXMLTransformer().fromXML(dialogName, v,
                        x.getValueS());
            }

        return v;
    }

}
