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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.jython.serversecurity.OObject;
import com.jython.serversecurity.OObjectRoles;
import com.jython.serversecurity.Person;


public class Test38 extends TestHelper {
    
    @Test
    public void test1() {
        iAdmin.clearAll(getI());
        List<OObject> aList = iAdmin.getListOfObjects(getI());
        assertEquals(0, aList.size());
        OObject ho = new OObject();
        ho.setName("hotel");
        ho.setDescription("Super hotel");
        List<OObjectRoles> roles = new ArrayList<OObjectRoles>();
        iAdmin.addOrModifObject(getI(), ho, roles);
        aList = iAdmin.getListOfObjects(getI());
        assertEquals(1, aList.size());
        ho = aList.get(0);
        assertEquals("hotel", ho.getName());
        assertEquals("Super hotel", ho.getDescription());
    }

    @Test
    public void test2() {
        iAdmin.clearAll(getI());
        List<Person> aList = iAdmin.getListOfPersons(getI());
        assertEquals(0, aList.size());
        Person pe = new Person();
        pe.setName("user");
        pe.setDescription("user name");
        List<OObjectRoles> roles = new ArrayList<OObjectRoles>();
        iAdmin.addOrModifPerson(getI(), pe, roles);
        aList = iAdmin.getListOfPersons(getI());
        assertEquals(1, aList.size());
        pe = aList.get(0);
        assertEquals("user", pe.getName());
        assertEquals("user name", pe.getDescription());
    }

}
