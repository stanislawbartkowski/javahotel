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
package com.jythonui.server.envvar.impl;

import java.io.Serializable;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.gwtmodel.commoncache.ICommonCache;
import com.gwtmodel.containertype.ContainerInfo;
import com.gwtmodel.mapcache.ICommonCacheFactory;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.envvar.IGetEnvVariable;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;

public class GetEnvVariables extends UtilHelper implements IGetEnvVariable {

    private final IGetLogMess gMess;
    private final ICommonCache iCache;

    private final static String COMP = "java:comp/env/";
    private final static String GLOBAL = "java:global/";

    @Inject
    public GetEnvVariables(
            @Named(ISharedConsts.JYTHONMESSSERVER) IGetLogMess gMess,
            ICommonCacheFactory cFactory) {
        this.gMess = gMess;
        this.iCache = cFactory.construct(ISharedConsts.JYTHONENVCACHE);
    }

    private class EnvVar implements Serializable, IEnvVar {
        private static final long serialVersionUID = 1L;
        boolean resL;
        String resS;
        boolean empty = true;
        Session session;

        String toS() {
            if (empty)
                return "empty";
            if (resS != null)
                return resS;
            if (session != null)
                return session.toString();
            return new Boolean(resL).toString();
        }

        @Override
        public boolean getL() {
            return resL;
        }

        @Override
        public String getS() {
            return resS;
        }

        @Override
        public boolean isEmpty() {
            return empty;
        }

        @Override
        public Session getSession() {
            return session;
        }
    }

    @SuppressWarnings("incomplete-switch")
    private EnvVar getEnvStringDirect(String tName, ResType rType,
            boolean throwerror) {
        Context initCtx;
        EnvVar r = new EnvVar();
        String eName = tName;
        switch (ContainerInfo.getContainerType()) {
        case TOMCAT:
        case JETTY:
            eName = COMP + tName;
            break;
        case JBOSS:
            eName = GLOBAL + tName;
            break;
        }

        try {
            infoMess(gMess, ILogMess.LOOKFORENVVARIABLE, eName);

            initCtx = new InitialContext();
            Object res = initCtx.lookup(eName);
            if (res == null)
                if (throwerror)
                    errorLog(gMess.getMess(IErrorCode.ERRORCODE44,
                            ILogMess.CANNOTFINDRESOURCEVARIABLE, eName));
                else
                    return r;
            r.empty = false;
            switch (rType) {
            case LOG:
                r.resL = (Boolean) res;
                break;
            case STRING:
                r.resS = (String) res;
                break;
            case MAIL:
                r.session = (Session) res;
                break;
            }
            // ENVVARIABLEFOUND
            infoMess(gMess, ILogMess.ENVVARIABLEFOUND, eName, r.toS());
            initCtx.close();
            return r;
        } catch (NamingException e) {
            if (throwerror)
                errorLog(gMess.getMess(IErrorCode.ERRORCODE43,
                        ILogMess.ERRORWHILEREADINGCONTEXT, eName), e);
            // ENVVARIABLENOTFOUND
            infoMess(gMess, ILogMess.ENVVARIABLENOTFOUND, eName);
            return r;
        }
    }

    @Override
    public IEnvVar getEnvString(String name, ResType rType, boolean throwerror) {
        Object o = iCache.get(name);
        EnvVar r = (EnvVar) o;
        if (r != null) {

            traceLog(name + " already cached " + r.toS());
            return r;
        }
        r = getEnvStringDirect(name, rType, throwerror);
        iCache.put(name, r);
        traceLog(name + " to cache " + r.toS());
        return r;
    }

}
