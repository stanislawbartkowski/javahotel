/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.jython.ui.server.datastore.IPerson;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldValue;

/**
 * @author hotel
 * 
 */
public class Test10 extends TestHelper {

    @Test
    public void test1() {
        po.clearAll();
        IPerson p = po.construct();
        p.setPersonName("John Kovalsky");
        p.setPersonNumb("P0002");
        po.savePerson(p);
        p = po.findPersonByNumb("aaa");
        assertNull(p);
        p = po.findPersonByNumb("P0002");
        assertNotNull(p);
    }

    @Test
    public void test2() {
        po.clearAll();
        IPerson p = po.construct();
        p.setPersonName("John Kovalsky");
        p.setPersonNumb("P0002");
        po.savePerson(p);
        for (int i = 0; i < 100; i++) {
            p = po.construct();
            p.setPersonName("Name " + i);
            p.setPersonNumb("P" + i);
            po.savePerson(p);
        }

        for (int i = 99; i >= 0; i--) {
            p = po.findPersonByNumb("P" + i);
            assertNotNull(p);
            assertEquals("Name " + i, p.getPersonName());
        }

    }

    @Test
    public void test3() {
        po.clearAll();
        DialogFormat d = iServer.findDialog("test18.xml");
        assertNotNull(d);
        IPerson p = po.construct();
        p.setPersonName("John Kovalsky");
        p.setPersonNumb("P0002");
        po.savePerson(p);

        DialogVariables v = new DialogVariables();
        iServer.runAction(v, "test18.xml", "checkifexist");
        FieldValue val = v.getValue("OK");
        assertTrue(val.getValueB());
    }

}
