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
import java.util.logging.Logger;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

import com.jythonui.server.IStorageMemCache;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.server.security.token.ICustomSecurity;
import com.jythonui.server.security.token.PasswordSecurityToken;

class SubjectCache extends UtilHelper {

    private final IStorageMemCache iCache;
    private final IGetLogMess gMess;

    class CurrentSubject {
        SessionEntry se;
        SecurityManager sManager;
        Subject currentUser;
    }

    private static ThreadLocal<CurrentSubject> lastS = new ThreadLocal<CurrentSubject>();

    SubjectCache(IStorageMemCache iCache, IGetLogMess gMess) {
        this.iCache = iCache;
        this.gMess = gMess;
    }

    private static SecurityManager constructManager(String realm) {
        info(Holder.getM().getMessN(ILogMess.REALMINITIALZATION, realm));
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

    private static Subject buildSubject() {
        Subject currentUser = new Subject.Builder().buildSubject();
        return currentUser;
    }

    private Result authenticate(SessionEntry se, String tokenS) {
        SecurityManager securityManager = constructManager(se.getRealm());
        SecurityUtils.setSecurityManager(securityManager);
        // Subject currentUser = SecurityUtils.getSubject();
        // currentUser = new Subject.Builder().buildSubject();
        Subject currentUser = buildSubject();
        PasswordSecurityToken token = new PasswordSecurityToken(se.getUser(),
                se.getPassword(), se.getiCustom());
        info(gMess.getMessN(ILogMess.AUTHENTICATEUSER, se.getUser(),
                se.getRealm()));
        try {
            currentUser.login(token);
        } catch (UnknownAccountException uae) {
            info(gMess.getMess(IErrorCode.ERRORCODE3,
                    ILogMess.AUTHENTICATENOUSER, se.getUser()));
            return null;
        } catch (IncorrectCredentialsException ice) {
            info(gMess.getMess(IErrorCode.ERRORCODE4,
                    ILogMess.AUTHENTICATEINCORECTPASSWORD, se.getUser()));
            return null;
        } catch (LockedAccountException lae) {
            info(gMess.getMess(IErrorCode.ERRORCODE5,
                    ILogMess.AUTHENTOCATELOCKED, se.getUser()));
            return null;
        } catch (AuthenticationException ae) {
            severe(gMess.getMess(IErrorCode.ERRORCODE6,
                    ILogMess.AUTHENTICATEOTHERERROR, se.getUser(),
                    ae.getMessage()),ae);
            ae.printStackTrace();
            return null;
        } catch (UnknownSessionException ae) {
            info(gMess.getMess(IErrorCode.ERRORCODE22,
                    ILogMess.AUTHENTICATEOTHERERROR, se.getUser(),
                    ae.getMessage()));
            return null;
        }

        info(gMess.getMessN(ILogMess.OKAUTHENTICATED));
        if (tokenS == null) {
            UUID i = UUID.randomUUID();
            tokenS = i.toString();
            iCache.put(tokenS, se);
        }
        CurrentSubject subS = new CurrentSubject();
        subS.se = se;
        subS.sManager = securityManager;
        subS.currentUser = currentUser;
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
        e = (SessionEntry) iCache.get(token);
        if (e == null) {
            info(token + " token not found ");
        }
        return e;
    }

    boolean validToken(String token) {
        return get(token) != null;
    }

    ICustomSecurity getCustom(String token) {
        SessionEntry se = get(token);
        if (se == null) // TODO: more verbose log
            return null;
        return se.getiCustom();
    }

    String getUserName(String token) {
        SessionEntry se = get(token);
        if (se == null) // TODO: more verbose log
            return null;
        return se.getUser();
    }

    Subject getSubject(String token) {
        SessionEntry se = get(token);
        if (se == null)
            return null; // TODO: more detailed log
        CurrentSubject subC = lastS.get();
        if (subC != null && se.eq(subC.se)) {
            // SecurityUtils.setSecurityManager(subC.sManager);
            // Subject sub = SecurityUtils.getSubject();
            // Subject sub = buildSubject();
            return subC.currentUser;
        }
        // validate again
        info("Authenticate again");
        Result res = authenticate(se, token);
        if (res == null) {
            severe(gMess.getMess(IErrorCode.ERRORCODE23,
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
