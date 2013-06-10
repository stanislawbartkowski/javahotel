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
package com.gwthotel.auth;

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

import com.gwthotel.admin.HotelRoles;
import com.gwthotel.admin.IHotelAdmin;
import com.gwthotel.admin.AppInstanceId;
import com.gwthotel.mess.IHError;
import com.gwthotel.mess.IHMess;
import com.gwthotel.shared.IHotelConsts;
import com.gwtmodel.table.common.CUtil;
import com.jythonui.server.security.token.PasswordSecurityToken;

public class HotelAuthRealm extends AuthorizingRealm {

    private IRealmResources iRes;
    private final AppInstanceId i;

    public void setiRes(IRealmResources iRes) {
        this.iRes = iRes;
    }

    public HotelAuthRealm(AppInstanceId i) {
        super();
        setName(i.getInstanceName());
        this.i = i;
    }

    private IHotelAdmin getI() {
        return iRes.getAdmin();
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
        String person = token.getUsername();
        String password = getI().getPassword(i, token.getUsername());
        if (CUtil.EmptyS(password))
            throwNotExist(IHError.HERROR003, IHMess.AUTHUSERDOESNOTEXIST,
                    person);
        HotelCustom ho = (HotelCustom) token.getiCustom();
        String hotel = ho.getHotelName();
        if (hotel == null) {
            // TODO: not expected, more verbose
            throwNotExist(IHError.HERROR002, IHMess.AUTHHOTELISNULL, person);
        }
        List<HotelRoles> roles = getI().getListOfRolesForHotel(i, hotel);
        List<String> hotelroles = null;
        for (HotelRoles ro : roles) {
            if (ro.getObject().getName().equals(person)) {
                hotelroles = ro.getRoles();
                break;
            }
        }
        if (hotelroles == null) {
            String mess = iRes.getLogMess().getMess(IHError.HERROR004,
                    IHMess.AUTHUSERDOESNOTHAVEROLEINHOTEL, person, hotel);
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
