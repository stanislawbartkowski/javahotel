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
package com.jythonui.server.security.impl;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.shiro.subject.Subject;

import com.jythonui.server.IConsts;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.IStorageMemCache;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.server.security.ISecurity;
import com.jythonui.server.security.ISecurityResolver;
import com.jythonui.server.security.token.ICustomSecurity;

public class SecurityJython extends UtilHelper implements ISecurity {

    private final IGetLogMess gMess;

    private final SubjectCache cCache;
    private final ISecurityResolver iResolver;
    private final IStorageMemCache iCache;

    @Inject
    public SecurityJython(
            @Named(IConsts.SECURITYREALM) IStorageMemCache iCache,
            ISecurityResolver iResolver,
            @Named(ISharedConsts.JYTHONMESSSERVER) IGetLogMess gMess) {
        cCache = new SubjectCache(iCache, gMess);
        this.iResolver = iResolver;
        this.gMess = gMess;
        this.iCache = iCache;
    }

    @Override
    public String authenticateToken(String realm, String userName,
            String password, ICustomSecurity iCustom) {
        SessionEntry se = new SessionEntry(userName, password, realm, iCustom);
        return cCache.authenticateS(se);
    }

    @Override
    public String withoutlogin(ICustomSecurity iCustom) {
        UUID i = UUID.randomUUID();
        String tokenS = i.toString();
        SessionEntry se = new SessionEntry(null, null, null, iCustom);
        iCache.put(tokenS, se);
        return tokenS;
    }

    @Override
    public void logout(String token) {
        Subject currentUser = cCache.getSubject(token);
        if (currentUser == null)
            return; // TODO: more detailed log
        currentUser.logout();
        if (currentUser.getSession() != null)
            currentUser.getSession().stop();
        cCache.removeSubject(token);
    }

    @Override
    public boolean validToken(String token) {
        return cCache.validToken(token);
    }

    @Override
    public boolean isAuthorized(String token, String permission) {
        Subject currentUser = cCache.getSubject(token);
        if (currentUser == null) {
            severe(gMess.getMess(IErrorCode.ERRORCODE21, ILogMess.INVALIDTOKEN,
                    token));
            return false;
        }
        return iResolver.isAuthorized(currentUser, permission);
    }

    @Override
    public ICustomSecurity getCustom(String token) {
        return cCache.getCustom(token);
    }

    @Override
    public String getUserName(String token) {
        return cCache.getUserName(token);
    }

    @Override
    public String evaluateExpr(String token, String expr) {
        Subject currentUser = null;
        if (token != null) {
            currentUser = cCache.getSubject(token);
            if (currentUser == null) {
                severe(gMess.getMess(IErrorCode.ERRORCODE96,
                        ILogMess.INVALIDTOKEN, token));
                return null;
            }
        }
        return iResolver.evaluateExpr(currentUser, expr);
    }

}
