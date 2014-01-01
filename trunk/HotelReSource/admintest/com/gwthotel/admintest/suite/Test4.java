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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Test;

import com.gwthotel.admin.Hotel;
import com.gwthotel.admin.HotelRoles;
import com.gwthotel.admin.Person;
import com.jythonui.server.security.token.ICustomSecurity;
import com.jythonui.server.security.token.PasswordSecurityToken;

public class Test4 extends TestHelper {

    private PasswordSecurityToken construct(String person, String password,
            String hotel) {
        ICustomSecurity cu = getSec(hotel);
        PasswordSecurityToken tok = new PasswordSecurityToken(person, password,
                cu);
        return tok;
    }

    private void testShiro() {
        Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory(
                realM);
        org.apache.shiro.mgt.SecurityManager securityManager = factory
                .getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject currentUser = SecurityUtils.getSubject();
        PasswordSecurityToken token = construct("user", "secret", null);
        try {
            currentUser.login(token);
            fail("Not expected here");
        } catch (Exception e) {
            System.out.println("Is expected, hotel is null");
        }
        token = construct("user", "secret", "hotel");
        try {
            currentUser.login(token);
            fail("Not expected here");
        } catch (Exception e) {
            System.out.println("Is expected, hotel does not exist");
        }
        // now create a hotel
        Hotel ho = new Hotel();
        ho.setName("hotel");
        ho.setDescription("Grzyb");
        List<HotelRoles> roles = new ArrayList<HotelRoles>();
        iAdmin.addOrModifHotel(getI(), ho, roles);
        try {
            currentUser.login(token);
            fail("Not expected here");
        } catch (Exception e) {
            System.out.println("Is expected, user do not have any role");
        }
        roles = new ArrayList<HotelRoles>();
        Person pe = new Person();
        pe.setName("user");
        pe.setDescription("user name");
        HotelRoles role = new HotelRoles(pe);
        role.getRoles().add("man");
        roles.add(role);
        iAdmin.addOrModifHotel(getI(), ho, roles);
        currentUser.login(token);
        System.out.println("Welcome ..");
        assertTrue("Man role expected", currentUser.hasRole("man"));
        assertFalse("Admin role not expected", currentUser.hasRole("admin"));
        currentUser.logout();
    }

    @Test
    public void test1() {
        iAdmin.clearAll(getI());
        Person pe = new Person();
        pe.setName("user");
        pe.setDescription("user name");
        List<HotelRoles> roles = new ArrayList<HotelRoles>();
        iAdmin.addOrModifPerson(getI(), pe, roles);
        assertFalse(iAdmin.validatePasswordForPerson(getI(), "user", "secret"));
        iAdmin.changePasswordForPerson(getI(), "user", "secret");
        assertEquals("secret", iAdmin.getPassword(getI(), "user"));
        testShiro();
    }

    private void testShiro1() {
        Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory(
                realM);
        org.apache.shiro.mgt.SecurityManager securityManager = factory
                .getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject currentUser = SecurityUtils.getSubject();
        PasswordSecurityToken token = construct("user", "secret", "hotel");
        currentUser.login(token);
        assertTrue("Man role expected", currentUser.hasRole("mana"));
        assertTrue("Acc role expected", currentUser.hasRole("acc"));
        assertFalse("Rybka role not expected", currentUser.hasRole("rybka"));
    }

    @Test
    public void test2() {
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
        testShiro1();

    }
}
