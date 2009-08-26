/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.security.glassfish.login;

import com.javahotel.remoteinterfaces.PasswordT;
import com.javahotel.security.login.HotelLoginConsts;
import com.javahotel.security.login.IHotelLoginJDBC;
import com.javahotel.security.login.HotelLoginP;
import com.javahotel.security.login.IHotelLogin;
import com.sun.enterprise.security.auth.login.PasswordCredential;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class HotelLoginImpl implements IHotelLogin {

    private HotelLoginP loginp(final String puser, final PasswordT ppassword,
            final String na, final String rea, boolean admin) {
        try {
            LoginContext lc;
            Subject su = new Subject();
            PasswordCredential pa = new PasswordCredential(puser,
                    ppassword.getPassword(), rea);
            su.getPrivateCredentials().add(pa);
            lc = new LoginContext(na, su);
            lc.login();
            HotelLoginP p = new HotelLoginP(lc, puser, admin);
            return p;
        } catch (LoginException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public HotelLoginP loginadmin(final String puser, final PasswordT ppassword,
            final Map<String, String> prop) {
        return loginp(puser, ppassword,
                prop.get(HotelLoginConsts.ADMINLOGINCONTEXT),
                prop.get(HotelLoginConsts.ADMINREALM), true);
    }

    public void setLoginJDBC(IHotelLoginJDBC i) {
    }

    @Override
    public HotelLoginP loginuser(final String user, final PasswordT password,
            final Map<String, String> prop) {
        return loginp(user, password,
                prop.get(HotelLoginConsts.USERLOGINCONTEXT),
                prop.get(HotelLoginConsts.USERREALM), false);
    }
}
