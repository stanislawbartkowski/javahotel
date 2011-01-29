/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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
package com.javahotel.db.authentication.impl;

import com.javahotel.db.authentication.jpa.GroupD;
import com.javahotel.db.authentication.jpa.Person;
import com.javahotel.db.util.CommonHelper;
import com.javahotel.dbjpa.ejb3.JpaEntity;
import com.javahotel.dbres.exceptions.HotelException;
import com.javahotel.dbres.resources.GetMess;
import com.javahotel.dbres.resources.IMess;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.PasswordT;
import com.javahotel.security.login.IHotelLoginJDBC;
import com.javahotel.security.login.HotelLoginP;
import com.javahotel.security.login.PersonHotelRules;
import java.util.Map;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class AuthLoginUser implements IHotelLoginJDBC {

    public HotelLoginP loginuser(final String user, final PasswordT password,
            final Map<String, String> p) {
        JpaEntity jpa = CommonHelper.getAutJPA();
        Person pe = AutUtil.getPerson(jpa, user);
        if (pe == null) {
            throw new HotelException(GetMess.getM(IMess.USERPASSWORDNOTVALID));
        }
        if (!pe.getPassword().equals(password.getPassword())) {
            throw new HotelException(GetMess.getM(IMess.USERPASSWORDNOTVALID));
        }
        HotelLoginP hp = new HotelLoginP(user, false);
        for (GroupD d : pe.getGroup()) {
            String ro = d.getGroupname();
            PersonHotelRules.HR hr = new PersonHotelRules.HR(ro);
            hp.setRoles(new HotelT(hr.getHotel()), hr.getRole());
        }
        return hp;
    }
}
