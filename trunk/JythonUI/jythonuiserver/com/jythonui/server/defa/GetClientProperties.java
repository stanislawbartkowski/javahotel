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
package com.jythonui.server.defa;

import java.util.Properties;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;

import com.gwtmodel.table.common.CUtil;
import com.jython.ui.shared.ISharedConsts;
import com.jython.ui.shared.MUtil;
import com.jythonui.server.IJythonClientRes;
import com.jythonui.server.Util;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.resbundle.IAppMess;
import com.jythonui.shared.ClientProp;
import com.jythonui.shared.CustomMessages;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.RequestContext;

public class GetClientProperties implements IJythonClientRes {

    static final private Logger log = Logger
            .getLogger(GetClientProperties.class.getName());
    private final IGetLogMess gMess;

    @Inject
    public GetClientProperties(
            @Named(ISharedConsts.JYTHONMESSSERVER) IGetLogMess gMess) {
        this.gMess = gMess;
    }

    @Override
    public ClientProp getClientRes(RequestContext context) {
        String locale = context.getLocale();
        if (!CUtil.EmptyS(locale)) {
            Holder.SetLocale(locale);
        }
        // InputStream i = GetClientProperties.class.getClassLoader()
        // .getResourceAsStream(ICommonConsts.APP_FILENAME);
        // if (i == null) {
        // log.log(Level.SEVERE, gMess
        // .getMess(IErrorCode.ERRORCODE1,
        // ILogMess.CANNOTFINDRESOURCEFILE,
        // ICommonConsts.APP_FILENAME));
        // return null;
        // }
        // Properties prop = new Properties();
        // try {
        // prop.load(i);
        // } catch (IOException e) {
        // log.log(Level.SEVERE, gMess.getMess(IErrorCode.ERRORCODE2,
        // ILogMess.ERRORWHILEREADINGRESOURCEFILE,
        // ICommonConsts.APP_FILENAME), e);
        // return null;
        // }
        Properties prop = Util.getProperties(ICommonConsts.APP_FILENAME);
        if (prop == null)
            return null;
        ClientProp map = new ClientProp();
        MUtil.toElem(map.getMap(), prop);
        Holder.setAuth(map.isAuthenticate());
        IAppMess custMess = Holder.getAppMess();
        CustomMessages mess = custMess.getCustomMess();
        if (mess != null)
            map.setCustomM(mess);
        return map;
    }
}