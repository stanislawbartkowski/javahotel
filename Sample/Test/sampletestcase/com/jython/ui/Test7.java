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

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.jython.ui.server.datastore.IPerson;

/**
 * @author hotel
 * 
 */
public class Test7 extends TestHelper {

    @Test
    public void test1() {
        po.clearAll();
        IPerson p = po.construct();
        p.setPersonName("John Kovalsky");
        p.setPersonNumb("P0002");
        po.savePerson(p);
        List<IPerson> pList = po.getAllPersons();
        assertEquals(1, pList.size());
        po.removePerson(p);
        pList = po.getAllPersons();
        assertEquals(0, pList.size());
    }

    @Test
    public void test2() {
        po.clearAll();
        IPerson p = po.construct();
        p.setPersonName("John Kovalsky");
        p.setPersonNumb("P0002");
        po.savePerson(p);
        List<IPerson> pList = po.getAllPersons();
        assertEquals(1, pList.size());
        po.clearAll();
        pList = po.getAllPersons();
        assertEquals(0, pList.size());
    }

    @Test
    public void test3() {
        po.clearAll();
        IPerson p = po.construct();
        p.setPersonName("John Kovalsky");
        p.setPersonNumb("P0002");
        po.savePerson(p);
        List<IPerson> pList = po.getAllPersons();
        assertEquals(1, pList.size());
        p = pList.get(0);
        assertEquals("P0002", p.getPersonNumb());
        assertEquals("John Kovalsky", p.getPersonName());
        p.setPersonName("John Malenkov");
        po.changePerson(p);
        pList = po.getAllPersons();
        assertEquals(1, pList.size());
        p = pList.get(0);
        assertEquals("P0002", p.getPersonNumb());
        assertEquals("John Malenkov", p.getPersonName());
    }

}
