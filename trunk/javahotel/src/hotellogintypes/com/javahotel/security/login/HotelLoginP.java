/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.javahotel.security.login;

import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import com.javahotel.remoteinterfaces.HotelT;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class HotelLoginP implements Serializable {

    private final boolean admin;
    private final LoginContext lc;
    private final String user;
    private final Map<String, ArrayList<String>> hot;

    public HotelLoginP(final LoginContext lc, final String user,
            final boolean admin) {
        this.lc = lc;
        this.user = user;
        this.admin = admin;
        hot = new HashMap<String, ArrayList<String>>();
        if (admin) {
            return;
        }
        Set f = lc.getSubject().getPrincipals();
        for (Object o : f) {
//            if (o instanceof Group) {
//                Group g = (Group) o;
//                String s = g.getName();
//            }
            if (o instanceof Principal) {
                Principal p = (Principal) o;
                String s = p.getName();
                PersonHotelRules.HR h = new PersonHotelRules.HR(s);
                if (h.isNull()) {
                    continue;
                }
                setRoles(new HotelT(h.getHotel()), h.getRole());
            }
        }
    }

    public HotelLoginP(final String user, final boolean admin) {
        this.lc = null;
        this.user = user;
        this.admin = admin;
        hot = new HashMap<String, ArrayList<String>>();
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setRoles(final HotelT ho, final String role) {
        ArrayList<String> s = hot.get(ho.getName());
        if (s == null) {
            s = new ArrayList<String>();
            hot.put(ho.getName(), s);
        }
        s.add(role);
    }

    public void logout() throws LoginException {
        if (lc != null) {
            lc.logout();
        }
    }

    public String getUser() {
        return user;
    }

    public List<HotelT> getHotels() {
        Set<String> se = hot.keySet();
        ArrayList<HotelT> res = new ArrayList<HotelT>();
        for (final String ho : se) {
            res.add(new HotelT(ho));
        }
        return res;
    }

    public List<String> getRoles(final HotelT ho) {
        List<String> co = hot.get(ho.getName());
        if (co == null) {
            co = new ArrayList<String>();
        }
        return co;
    }
}
