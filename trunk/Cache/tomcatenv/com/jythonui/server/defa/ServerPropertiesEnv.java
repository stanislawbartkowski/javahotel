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

import java.io.Serializable;

import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.gwtmodel.commoncache.ICommonCache;
import com.gwtmodel.mapcache.ICommonCacheFactory;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.server.resource.IReadResource;
import com.jythonui.server.resource.IReadResourceFactory;

public class ServerPropertiesEnv extends AbstractServerProperties {

    private final IGetLogMess gMess;
    private final ICommonCache iCache;

    private class EnvVar implements Serializable {
        private static final long serialVersionUID = 1L;
        boolean resL;
        String resS;
        boolean empty = true;

        String toS() {
            if (empty)
                return "empty";
            if (resS != null)
                return resS;
            return new Boolean(resL).toString();
        }
    }

    private final IGetResourceJNDI getJNDI;

    @Inject
    public ServerPropertiesEnv(
            @Named(ISharedConsts.JYTHONMESSSERVER) IGetLogMess gMess,
            IGetResourceJNDI getJNDI, IReadResourceFactory iFactory,
            ICommonCacheFactory cFactory) {
        super(iFactory);
        this.gMess = gMess;
        this.getJNDI = getJNDI;
        this.iCache = cFactory.construct(ISharedConsts.JYTHONENVCACHE);
    }

    private EnvVar getEnvStringDirect(String name, boolean logVal,
            boolean throwerror) {
        Context initCtx;
        EnvVar r = new EnvVar();
        try {
            infoMess(gMess, ILogMess.LOOKFORENVVARIABLE, name);
            initCtx = new InitialContext();
            Object res = initCtx.lookup(name);
            if (res == null)
                if (throwerror)
                    errorLog(gMess.getMess(IErrorCode.ERRORCODE44,
                            ILogMess.CANNOTFINDRESOURCEVARIABLE, name));
                else
                    return r;
            r.empty = false;
            if (logVal)
                r.resL = (Boolean) res;
            else
                r.resS = (String) res;
            // ENVVARIABLEFOUND
            infoMess(gMess, ILogMess.ENVVARIABLEFOUND, name, r.toS());
            initCtx.close();
            return r;
        } catch (NamingException e) {
            if (throwerror)
                errorLog(gMess.getMess(IErrorCode.ERRORCODE43,
                        ILogMess.ERRORWHILEREADINGCONTEXT, name), e);
            // ENVVARIABLENOTFOUND
            infoMess(gMess, ILogMess.ENVVARIABLENOTFOUND, name);
            return r;
        }
    }

    private EnvVar getEnvString(String name, boolean logVal, boolean throwerror) {
        Object o = iCache.get(name);
        EnvVar r = (EnvVar) o;
        if (r != null) {
            
            traceLog(name + " already cached " + r.toS());
            return r;
        }
        r = getEnvStringDirect(name, logVal, throwerror);
        iCache.put(name, r);
        traceLog(name + " to cache " + r.toS());
        return r;
    }

    @Override
    public IReadResource getResource() {
        EnvVar e = getEnvString(getJNDI.getResourceDir(), false, false);
        if (!e.empty)
            return iFactory.constructDir(e.toS());
        return iFactory.constructLoader(this.getClass().getClassLoader());
    }

    @Override
    public boolean isCached() {
        EnvVar e = getEnvString(getJNDI.getCachedValue(), true, false);
        if (e.empty)
            return true;
        return e.resL;
    }

    @Override
    public String getEJBHost() {
        EnvVar e = getEnvString(getJNDI.getEJBHost(), false, false);
        if (e.empty)
            return null;
        return e.resS;
    }

    @Override
    public String getEJBPort() {
        EnvVar e = getEnvString(getJNDI.getEJBPort(), false, false);
        if (e.empty)
            return null;
        return e.resS;
    }

}
