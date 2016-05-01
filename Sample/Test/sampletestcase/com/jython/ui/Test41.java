/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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

import com.jython.serversecurity.OObjectRoles;
import com.jython.serversecurity.Person;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;

public class Test41 extends TestHelper {

    @Test
    public void test1() {
        iAdmin.clearAll(getI());
        assertFalse(iAdmin.validatePasswordForPerson(getI(),"user", "secret"));
        Person pe = new Person();
        pe.setName("user");
        pe.setDescription("user name");
        List<OObjectRoles> roles = new ArrayList<OObjectRoles>();
        iAdmin.addOrModifPerson(getI(),pe, roles);
        assertFalse(iAdmin.validatePasswordForPerson(getI(),"user", "secret"));
        iAdmin.changePasswordForPerson(getI(),"user", "secret");
        assertTrue(iAdmin.validatePasswordForPerson(getI(),"user", "secret"));
    }

    @Test
    public void test2() {
        iAdmin.clearAll(getI());
        DialogFormat d = findDialog("dialog1.xml");
        assertNotNull(d);
        DialogVariables v = new DialogVariables();
        v.setValueS("name", "hotel1");
        v.setValueS("descr", "Pod pieskiem");
        runAction(v, "dialog1.xml", "modif");
        List<OObjectRoles> hList = iAdmin.getListOfRolesForObject(getI(),"hotel1");
        assertNotNull(hList);
        assertEquals(1,hList.size());
        for (OObjectRoles rol : hList) {
            System.out.println(rol.getObject().getName());
            assertEquals(1,rol.getRoles().size());
            for (String s : rol.getRoles()) {
                System.out.println(s);
                assertEquals("man",s);
            }
        }

    }

}
