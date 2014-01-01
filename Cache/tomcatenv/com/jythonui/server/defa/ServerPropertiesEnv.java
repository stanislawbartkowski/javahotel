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
package com.jythonui.server.defa;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.jython.ui.shared.ISharedConsts;
import com.jythonui.server.IConsts;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.shared.JythonUIFatal;

public class ServerPropertiesEnv implements IJythonUIServerProperties {

    private final IGetLogMess gMess;
    static final private Logger log = Logger
            .getLogger(ServerPropertiesEnv.class.getName());

    private class EnvVar {
        boolean resL;
        String resS;
    }

    static private void error(String mess, Throwable t) {
        if (t == null)
            log.log(Level.SEVERE, mess);
        else
            log.log(Level.SEVERE, mess, t);
        throw new JythonUIFatal(mess);
    }

    private final IGetResourceJNDI getJNDI;

    @Inject
    public ServerPropertiesEnv(
            @Named(ISharedConsts.JYTHONMESSSERVER) IGetLogMess gMess,
            IGetResourceJNDI getJNDI) {
        this.gMess = gMess;
        this.getJNDI = getJNDI;
    }

    private EnvVar getEnvString(String name, boolean logVal) {
        Context initCtx;
        EnvVar r = new EnvVar();
        try {
            initCtx = new InitialContext();
            // Context envCtx = (Context) initCtx.lookup("java:comp/env");
            Object res = initCtx.lookup(name);
            if (res == null) {
                error(gMess.getMess(IErrorCode.ERRORCODE44,
                        ILogMess.CANNOTFINDRESOURCEVARIABLE, name), null);
            }
            if (logVal)
                r.resL = (Boolean) res;
            else
                r.resS = (String) res;
            return r;
        } catch (NamingException e) {
            error(gMess.getMess(IErrorCode.ERRORCODE43,
                    ILogMess.ERRORWHILEREADINGCONTEXT, name), e);
        }
        return null;
    }

    private String getResourceDirectory(String dir) {
        EnvVar e = getEnvString(getJNDI.getResourceDir(), false);
        return e.resS + "/" + ISharedConsts.RESOURCES + "/" + dir;
    }

    @Override
    public String getDialogDirectory() {
        return getResourceDirectory(IConsts.DIALOGDIR);
    }

    @Override
    public String getPackageDirectory() {
        return getResourceDirectory(IConsts.PACKAGEDIR);
    }

    @Override
    public boolean isCached() {
        EnvVar e = getEnvString(getJNDI.getCachedValue(), true);
        return e.resL;
    }

    @Override
    public String getBundleBase() {
        return getResourceDirectory(IConsts.BUNDLEDIR);
    }

}
