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

import com.jython.serversecurity.OObject;
import com.jython.serversecurity.OObjectRoles;
import com.jython.serversecurity.Person;
import com.jythonui.server.objectauth.ObjectCustom;
import com.jythonui.server.security.token.ICustomSecurity;

public class Test5 extends TestHelper {

    private void setUser() {
        iAdmin.clearAll(getI());
        Person pe = new Person();
        pe.setName("user");
        pe.setDescription("user name");
        List<OObjectRoles> roles = new ArrayList<OObjectRoles>();
        iAdmin.addOrModifPerson(getI(), pe, roles);
        iAdmin.changePasswordForPerson(getI(), "user", "secret");
        OObject ho = new OObject();

        ho.setName("hotel");
        ho.setDescription("Pod Pieskiem");
        roles = new ArrayList<OObjectRoles>();
        pe = new Person();
        pe.setName("user");
        OObjectRoles rol = new OObjectRoles(pe);
        rol.getRoles().add("mana");
        rol.getRoles().add("acc");
        roles.add(rol);
        iAdmin.addOrModifObject(getI(), ho, roles);
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
        ObjectCustom h = (ObjectCustom) cus;
        assertEquals("hotel", h.getObjectName());
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
