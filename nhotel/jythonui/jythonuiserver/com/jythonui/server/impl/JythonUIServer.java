/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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
package com.jythonui.server.impl;

import com.google.inject.Inject;
import com.gwtmodel.commoncache.ICommonCache;
import com.gwtmodel.table.common.CUtil;
import com.jythonui.server.IExecuteJython;
import com.jythonui.server.IGetDialog;
import com.jythonui.server.IJythonUIServer;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.Util;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.shared.CustomMessages;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogInfo;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.RequestContext;

/**
 * @author hotel
 * 
 */
public class JythonUIServer extends UtilHelper implements IJythonUIServer {

    private final IJythonUIServerProperties p;
    private final IGetDialog iDialog;
    private final IExecuteJython iJython;

    @Inject
    public JythonUIServer(IJythonUIServerProperties p, ICommonCache mCache,
            IGetDialog iDialog, IExecuteJython iJython) {
        this.p = p;
        this.iDialog = iDialog;
        this.iJython = iJython;
    }

    @Override
    public DialogInfo findDialog(RequestContext rcontext, String dialogName) {
        Util.setContext(rcontext);
        String token = Util.getToken();
        DialogFormat d = iDialog.getDialog(token, dialogName, true);
        if (d == null)
            return null;
        CustomMessages custMess = null;
        if (!p.isCached())
            custMess = Holder.getAppMess().getCustomMess();
        return new DialogInfo(d, custMess);
    }

    @Override
    public DialogVariables runAction(RequestContext rcontext,
            DialogVariables v, String dialogName, String actionId) {
        if (CUtil.EmptyS(actionId)) {
            String mess = Holder.getM().getMess(IErrorCode.ERRORCODE70,
                    ILogMess.ACIONIDCANNOTBENULL, dialogName);
            errorLog(mess);
        }
        Util.setContext(rcontext);
        String locale = Util.getLocale();
        String securityToken = Util.getToken();
        v.setSecurityToken(securityToken);
        v.setLocale(locale);
        v.setValueS(ICommonConsts.J_DIALOGNAME, dialogName);
        DialogFormat d = iDialog.getDialog(securityToken, dialogName, false);
        if (CUtil.onTheList(actionId, d.getAsXmlList())) {
            v.setValueS(ICommonConsts.JXMLCONTENT, Holder.getXMLTransformer()
                    .toXML(dialogName, v));
        }
        iJython.executeJython(v, d, actionId);
        FieldValue b = v.getValue(ICommonConsts.JXMLSETCONTENT);
        if (b != null)
            if (b.getValueB().booleanValue()) {
                FieldValue x = v.getValue(ICommonConsts.JXMLCONTENT);
                if (x == null) {
                    String mess = Holder.getM().getMess(IErrorCode.ERRORCODE69,
                            ILogMess.XMLSETCONTENTBUTCONTENTNOTAVAILABLE,
                            d.getId(), ICommonConsts.JXMLCONTENT,
                            ICommonConsts.JXMLCONTENT);
                    errorLog(mess);
                }
                Holder.getXMLTransformer()
                        .fromXML(dialogName, v, x.getValueS());
            }

        return v;
    }

}
