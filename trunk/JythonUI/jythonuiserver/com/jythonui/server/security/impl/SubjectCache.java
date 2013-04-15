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

import java.io.InvalidClassException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.server.logmess.LogMess;
import com.jythonui.server.security.ISecuritySessionCache;

class SubjectCache {

    private final ISecuritySessionCache iCache;

    class CurrentSubject {
        SessionEntry se;
        SecurityManager sManager;
    }

    private static ThreadLocal<CurrentSubject> lastS = new ThreadLocal<CurrentSubject>();

    private static final Logger log = Logger.getLogger(SubjectCache.class
            .getName());

    SubjectCache(ISecuritySessionCache iCache) {
        this.iCache = iCache;
    }

    private static SecurityManager constructManager(String realm) {
        log.info(LogMess.getMessN(ILogMess.REALMINITIALZATION, realm));
        Factory<SecurityManager> factory = new IniSecurityManagerFactory(realm);
        SecurityManager securityManager = factory.getInstance();
        return securityManager;
    }

    private class Result {
        Subject sub;
        String tokenS;

        Result(Subject sub, String tokenS) {
            this.sub = sub;
            this.tokenS = tokenS;
        }
    }

    private Result authenticate(SessionEntry se, String tokenS) {
        SecurityManager securityManager = constructManager(se.getRealm());
        SecurityUtils.setSecurityManager(securityManager);
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(se.getUser(),
                se.getPassword());
        log.info(LogMess.getMessN(ILogMess.AUTHENTICATEUSER, se.getUser(),
                se.getRealm()));
        try {
            currentUser.login(token);
        } catch (UnknownAccountException uae) {
            log.info(LogMess.getMess(IErrorCode.ERRORCODE3,
                    ILogMess.AUTHENTICATENOUSER, se.getUser()));
            return null;
        } catch (IncorrectCredentialsException ice) {
            log.info(LogMess.getMess(IErrorCode.ERRORCODE4,
                    ILogMess.AUTHENTICATEINCORECTPASSWORD, se.getUser()));
            return null;
        } catch (LockedAccountException lae) {
            log.info(LogMess.getMess(IErrorCode.ERRORCODE5,
                    ILogMess.AUTHENTOCATELOCKED, se.getUser()));
            return null;
        } catch (AuthenticationException ae) {
            log.info(LogMess.getMess(IErrorCode.ERRORCODE6,
                    ILogMess.AUTHENTICATEOTHERERROR, se.getUser()));
            return null;
        } catch (UnknownSessionException ae) {
            log.info(LogMess.getMess(IErrorCode.ERRORCODE22,
                    ILogMess.AUTHENTICATEOTHERERROR, se.getUser()));
            return null;
        }

        log.info(LogMess.getMessN(ILogMess.OKAUTHENTICATED));
        if (tokenS == null) {
            UUID i = UUID.randomUUID();
            tokenS = i.toString();
            iCache.put(tokenS, se);
        }
        CurrentSubject subS = new CurrentSubject();
        subS.se = se;
        subS.sManager = securityManager;
        lastS.set(subS);
        return new Result(currentUser, tokenS);
    }

    String authenticateS(SessionEntry se) {
        Result res = authenticate(se, null);
        if (res == null)
            return null;
        return res.tokenS;
    }

    private SessionEntry get(String token) {
        SessionEntry e = null;
        try {
            e = (SessionEntry) iCache.get(token);
        } catch (InvalidClassException ee) {
            log.log(Level.SEVERE, LogMess.getMess(IErrorCode.ERRORCODE24,
                    ILogMess.CANNOTCASTENTRY), e);
            iCache.remove(token);
        }
        if (e == null) {
            log.info(token + " token not found ");
        }
        return e;
    }

    boolean validToken(String token) {
        return get(token) != null;
    }

    Subject getSubject(String token) {
        SessionEntry se = get(token);
        if (se == null)
            return null; // TODO: more detailed log
        CurrentSubject subC = lastS.get();
        if (subC != null && se.eq(subC.se)) {
            SecurityUtils.setSecurityManager(subC.sManager);
            Subject sub = SecurityUtils.getSubject();
            return sub;
        }
        // validate again
        log.info("Authenticate again");
        Result res = authenticate(se, token);
        if (res == null) {
            log.severe(LogMess.getMess(IErrorCode.ERRORCODE23,
                    ILogMess.CANNOTAUTHENTICATEAGAIN, se.getUser(),
                    se.getRealm()));
            return null;
        }
        return res.sub;
    }

    void removeSubject(String token) {
        iCache.remove(token);
        lastS.remove();
    }

}
