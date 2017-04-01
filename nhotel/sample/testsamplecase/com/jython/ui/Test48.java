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
package com.jython.ui;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.jython.serversecurity.OObjectRoles;
import com.jython.serversecurity.Person;
import com.jythonui.server.security.token.ICustomSecurity;

public class Test48 extends TestHelper {

    protected final String realM = "classpath:resources/shiro/user.ini";

    @Before
    public void setUp() {
        super.setUp();
        iPerson.clearAll(getI());
    }

    @Test
    public void test1() {
        Person p = new Person();
        p.setName("user1");
        List<OObjectRoles> rList = new ArrayList<OObjectRoles>();
        OObjectRoles ro = new OObjectRoles();
        List<String> rol = new ArrayList<String>();
        rol.add("role1");
        rol.add("role2");
        ro.getRoles().addAll(rol);
        iPerson.addOrModifPerson(getI(), p, rList);
        List<Person> pList = iPerson.getListOfPersons(getI());
        assertEquals(1,pList.size());
        p = pList.get(0);
        assertEquals("user1",p.getName());
        p = new Person();
        p.setName("user2");
        ro = new OObjectRoles();
        ro.getRoles().add("role2");
        rList = new ArrayList<OObjectRoles>();
        rList.add(ro);
        iPerson.addOrModifPerson(getI(), p, rList);
        pList = iPerson.getListOfPersons(getI());
        assertEquals(2,pList.size());
        
        iPerson.changePasswordForPerson(getI(), "user2", "secret");
        String pa = iPerson.getPassword(getI(), "user2");
        assertEquals("secret",pa);
    }
    
    @Test
    public void test2() {
        test1();
        ICustomSecurity cu = getPersonSec();
        String token = iSec.authenticateToken(realM, "user", "secret", cu);
        assertNull(token);
        token = iSec.authenticateToken(realM, "user2", "secret", cu);
        assertNotNull(token);
        token = iSec.authenticateToken(realM, "user2", "wow - invalid", cu);
        assertNull(token);
        token = iSec.authenticateToken(realM, "user1", "secret", cu);
        assertNull(token);
    }
    
    @Test
    public void test3() {
        test1();
        ICustomSecurity cu = getPersonSec();
        String token = iSec.authenticateToken(realM, "user2", "secret", cu);
        assertNotNull(token);
        assertTrue(iSec.isAuthorized(token, "sec.u('user2')"));
        assertFalse(iSec.isAuthorized(token, "sec.u('stranger')"));
        assertTrue(iSec.isAuthorized(token, "sec.r('role2')"));
        assertFalse(iSec.isAuthorized(token, "sec.r('role1')"));
    }

}
