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
package com.jythonui.server.objectauth;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.gwtmodel.table.common.CUtil;
import com.jython.serversecurity.IOObjectAdmin;
import com.jython.serversecurity.OObjectRoles;
import com.jython.serversecurity.cache.IGetInstanceOObjectIdCache;
import com.jythonui.server.IConsts;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.server.security.token.PasswordSecurityToken;

abstract public class ObjectAuthRealm extends AuthorizingRealm {

    private IRealmResources iRes;
    private IGetInstanceOObjectIdCache iGet;

    /**
     * Injected through 'ini' file
     * 
     * @param iRes
     */
    protected void setiRes(IRealmResources iRes) {
        this.iRes = iRes;
    }

    /**
     * Injected through 'ini' file
     * 
     * @param iGet
     */
    protected void setiGet(IGetInstanceOObjectIdCache iGet) {
        this.iGet = iGet;
    }

    public ObjectAuthRealm() {
        super();
        setName(IConsts.OBJECTREALM);
    }

    private IOObjectAdmin getI() {
        return iRes.getAdmin();
    }

    private IGetInstanceOObjectIdCache getG() {
        return iGet;
    }

    private void throwNotExist(String errMess, String logMess, String person) {
        String mess = iRes.getLogMess().getMess(errMess, logMess, person);
        throw new AuthenticationException(mess);
    }

    private class User {
        private List<String> roles;
        private String userName;

        @Override
        public String toString() {
            return userName;
        }

    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken at)
            throws AuthenticationException {
        PasswordSecurityToken token = (PasswordSecurityToken) at;
        ObjectCustom ho = (ObjectCustom) token.getiCustom();
        String instanceId = ho.getInstanceId();
        String person = token.getUsername();
        String password = getI().getPassword(
                getG().getInstance(instanceId, person), token.getUsername());
        if (CUtil.EmptyS(password))
            throwNotExist(IErrorCode.ERRORCODE99,
                    ILogMess.AUTHUSERDOESNOTEXIST, person);
        String hotel = ho.getObjectName();
        if (hotel == null) {
            // TODO: not expected, more verbose
            throwNotExist(IErrorCode.ERRORCODE100, ILogMess.AUTHHOTELISNULL,
                    person);
        }
        List<OObjectRoles> roles = getI().getListOfRolesForObject(
                getG().getInstance(instanceId, person), hotel);
        if (roles == null) {
            String mess = iRes.getLogMess().getMess(IErrorCode.ERRORCODE101,
                    ILogMess.AUTHCANNOTGETROLES, hotel, person);
            throw new AuthenticationException(mess);
        }
        List<String> hotelroles = null;
        for (OObjectRoles ro : roles) {
            if (ro.getObject().getName().equals(person)) {
                hotelroles = ro.getRoles();
                break;
            }
        }
        if (hotelroles == null) {
            String mess = iRes.getLogMess().getMess(IErrorCode.ERRORCODE102,
                    ILogMess.AUTHUSERDOESNOTHAVEROLEINHOTEL, person, hotel);
            throw new AuthenticationException(mess);
        }
        User user = new User();
        user.roles = hotelroles;
        user.userName = person;
        return new SimpleAuthenticationInfo(user, password, getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {
        User user = (User) getAvailablePrincipal(pc);
        Set roleNames = new HashSet();
        Set permissions = new HashSet();

        for (String role : user.roles) {
            roleNames.add(role);
        }

        SimpleAuthorizationInfo simpleAuthInfo = new SimpleAuthorizationInfo(
                roleNames);
        simpleAuthInfo.setStringPermissions(permissions);
        return simpleAuthInfo;
    }
}
