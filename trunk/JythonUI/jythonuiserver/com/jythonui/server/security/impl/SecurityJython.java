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
package com.jythonui.server.security.impl;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.shiro.subject.Subject;

import com.jythonui.server.IConsts;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.server.security.ISecurity;
import com.jythonui.server.security.ISecurityResolver;
import com.jythonui.server.security.ISecuritySessionCache;
import com.jythonui.server.security.token.ICustomSecurity;

public class SecurityJython implements ISecurity {

    private static final Logger log = Logger.getLogger(SecurityJython.class
            .getName());
    private final IGetLogMess gMess;

    private final SubjectCache cCache;
    private final ISecurityResolver iResolver;

    @Inject
    public SecurityJython(ISecuritySessionCache iCache,
            ISecurityResolver iResolver,
            @Named(IConsts.JYTHONMESSSERVER) IGetLogMess gMess) {
        cCache = new SubjectCache(iCache, gMess);
        this.iResolver = iResolver;
        this.gMess = gMess;
    }

    @Override
    public String authenticateToken(String realm, String userName,
            String password, ICustomSecurity iCustom) {
        SessionEntry se = new SessionEntry(userName, password, realm, iCustom);
        return cCache.authenticateS(se);
    }

    @Override
    public void logout(String token) {
        Subject currentUser = cCache.getSubject(token);
        if (currentUser == null)
            return; // TODO: more detailed log
        currentUser.logout();
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
            log.severe(gMess.getMess(IErrorCode.ERRORCODE21,
                    ILogMess.INVALIDTOKEN, token));
            return false;
        }
        return iResolver.isAuthorized(currentUser, permission);
    }

    @Override
    public ICustomSecurity getCustom(String token) {
        return cCache.getCustom(token);
    }

}
