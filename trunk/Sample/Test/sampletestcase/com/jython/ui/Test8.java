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
package com.jython.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.jython.ui.server.datastore.IPerson;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.ListOfRows;

/**
 * @author hotel
 * 
 */
public class Test8 extends TestHelper {

    @Test
    public void test1() {
        DialogVariables v = new DialogVariables();
        po.clearAll();
        v.setValueS("pnumber", "P0001");
        v.setValueS("pname", "Ivan Severe");
        runAction(v, "test18.xml", "add");

        IPerson p = po.construct();
        List<IPerson> pList = po.getAllPersons();
        assertEquals(1, pList.size());
        p = pList.get(0);
        assertEquals("Ivan Severe", p.getPersonName());
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
        p = pList.get(0);

        DialogVariables v = new DialogVariables();
        v.setValueS("pnumber", p.getPersonNumb());
        v.setValueS("pname", "Ivan Terrible");
        v.setValueL("id", p.getId());

        runAction(v, "test18.xml", "change");

        pList = po.getAllPersons();
        assertEquals(1, pList.size());
        p = pList.get(0);
        assertEquals("Ivan Terrible", p.getPersonName());
    }

    @Test
    public void test3() {
        DialogFormat d = findDialog("test18.xml");
        assertNotNull(d);

        po.clearAll();
        for (int i = 0; i < 100; i++) {
            IPerson p = po.construct();
            p.setPersonName("John with name " + i);
            p.setPersonNumb("P" + i);
            po.savePerson(p);
        }

        DialogVariables v = new DialogVariables();
        v.setValueS("pnumber", "XXX");
        v.setValueS("pname", "XXX");
        runAction(v, "test18.xml", "readall");

        ListOfRows li = v.getList("list");
        assertNotNull(li);
        assertEquals(100, li.getRowList().size());
    }
}
