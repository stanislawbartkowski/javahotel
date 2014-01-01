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
package com.gwthotel.admintest.suite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.gwthotel.admin.Hotel;
import com.gwthotel.admin.HotelRoles;
import com.gwthotel.admin.Person;
import com.gwthotel.auth.HotelCustom;
import com.jythonui.server.security.token.ICustomSecurity;

public class Test5 extends TestHelper {

    private void setUser() {
        iAdmin.clearAll(getI());
        Person pe = new Person();
        pe.setName("user");
        pe.setDescription("user name");
        List<HotelRoles> roles = new ArrayList<HotelRoles>();
        iAdmin.addOrModifPerson(getI(), pe, roles);
        iAdmin.changePasswordForPerson(getI(), "user", "secret");
        Hotel ho = new Hotel();

        ho.setName("hotel");
        ho.setDescription("Pod Pieskiem");
        roles = new ArrayList<HotelRoles>();
        pe = new Person();
        pe.setName("user");
        HotelRoles rol = new HotelRoles(pe);
        rol.getRoles().add("mana");
        rol.getRoles().add("acc");
        roles.add(rol);
        iAdmin.addOrModifHotel(getI(), ho, roles);
    }

    @Test
    public void test1() {

        setUser();
        String token = iSec.authenticateToken(realM, "user", "wrong", null);
        assertNull(token);
        ICustomSecurity cu = getSec("hotel");
        token = iSec.authenticateToken(realM, "user", "wrong", cu);
        assertNull(token);

        cu = getSec("hotel");
        token = iSec.authenticateToken(realM, "user", "secret", cu);
        assertNotNull(token);

        System.out.println(token);
        assertTrue(iSec.isAuthorized(token, "sec.u('user')"));
        assertFalse(iSec.isAuthorized(token, "sec.u('stranger')"));
        assertTrue(iSec.isAuthorized(token, "sec.r('mana')"));
        assertTrue(iSec.isAuthorized(token, "sec.r('acc')"));
        assertFalse(iSec.isAuthorized(token, "sec.r('admin')"));

        ICustomSecurity cus = iSec.getCustom(token);
        assertNotNull(cus);
        HotelCustom h = (HotelCustom) cus;
        assertEquals("hotel", h.getHotelName());
    }

    @Test
    public void test2() {
        String token = iSec.authenticateToken(adminM, "admin", "admin", null);
        assertNotNull(token);
    }

    @Test
    public void test3() {
        setUser();
        ICustomSecurity cu = getSec("hotel");
        String token = iSec.authenticateToken(realM, "user", "secret", cu);
        assertNotNull(token);
        iSec.logout(token);
        token = iSec.authenticateToken(adminM, "user", "secret", null);
        assertNull(token);
        token = iSec.authenticateToken(adminM, "admin", "admin", null);
        assertNotNull(token);
    }

}
