/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.jython.serversecurity.OObject;
import com.jython.serversecurity.OObjectRoles;
import com.jython.serversecurity.Person;

public class Test40 extends TestHelper {
    
    @Test
    public void test1() {
        iAdmin.clearAll(getI());
        Person pe = new Person();
        pe.setName("user");
        pe.setDescription("user name");
        List<OObjectRoles> roles = new ArrayList<OObjectRoles>();
        iAdmin.addOrModifPerson(getI(),pe, roles);
        OObject ho = new OObject();
        ho.setName("hotel");
        ho.setDescription("Super hotel");
        roles = new ArrayList<OObjectRoles>();
        OObjectRoles rol = new OObjectRoles(pe);
        rol.getRoles().add("admin");
        roles.add(rol);
        iAdmin.addOrModifObject(getI(),ho, roles);
        // now check for hotel
        List<OObjectRoles> hList = iAdmin.getListOfRolesForObject(getI(),"hotel");
        assertEquals(1, hList.size());
        for (OObjectRoles hot : hList) {
            assertEquals("user", hot.getObject().getName());
            assertEquals(1, hot.getRoles().size());
            assertEquals("admin", hot.getRoles().get(0));
        }
        // now check for person
        hList = iAdmin.getListOfRolesForPerson(getI(),"user");
        assertEquals(1, hList.size());
        for (OObjectRoles hot : hList) {
            assertEquals("hotel", hot.getObject().getName());
            assertEquals(1, hot.getRoles().size());
            assertEquals("admin", hot.getRoles().get(0));
        }
    }

    
    @Test
    public void test2() {
        test1();
        OObject ho = new OObject();
        ho.setName("hotel");
        ho.setDescription("Super hotel");
        List<OObjectRoles> roles = new ArrayList<OObjectRoles>();
        iAdmin.addOrModifObject(getI(),ho, roles);
        // now check person, should be empty
        List<OObjectRoles> hList = iAdmin.getListOfRolesForPerson(getI(),"user");
        assertEquals(0, hList.size());
    }

    @Test
    public void test3() {
        iAdmin.clearAll(getI());
        Person pe = new Person();
        pe.setName("user");
        pe.setDescription("user name");
        List<OObjectRoles> roles = new ArrayList<OObjectRoles>();
        for (int i = 0; i < 100; i++) {
            OObject ho = new OObject();
            ho.setName("hotel" + i);
            ho.setDescription("Avoid this hotel " + i);
            OObjectRoles rol = new OObjectRoles(ho);
            rol.getRoles().add("admin");
            rol.getRoles().add("man");
            roles.add(rol);
            iAdmin.addOrModifObject(getI(),ho, new ArrayList<OObjectRoles>());
        }
        iAdmin.addOrModifPerson(getI(),pe, roles);
        List<OObjectRoles> hList = iAdmin.getListOfRolesForPerson(getI(),"user");
        assertEquals(100,hList.size());
    }
    
    @Test
    public void test4() {
        test3();
        List<OObject> h = iAdmin.getListOfObjects(getI());
        assertEquals(100,h.size());
        iAdmin.removeObject(getI(),"hotel10");
        List<OObjectRoles> hList = iAdmin.getListOfRolesForPerson(getI(),"user");
        assertEquals(99,hList.size());  
        h = iAdmin.getListOfObjects(getI());
        assertEquals(99,h.size());
        boolean exists = false;
        for (OObject ho : h) {
            if (ho.getName().equals("hotel10")) {
                exists = true;
            }
        }
        assertFalse(exists);
    }
    
    @Test
    public void test5() {
        test3();
        iAdmin.removePerson(getI(),"user");
        List<OObjectRoles> hList = iAdmin.getListOfRolesForPerson(getI(),"user");
        assertNull(hList);
        List<OObject> h = iAdmin.getListOfObjects(getI());
        assertEquals(100,h.size());
        List<Person> pList = iAdmin.getListOfPersons(getI());
        assertEquals(0,pList.size());
        
    }

}
