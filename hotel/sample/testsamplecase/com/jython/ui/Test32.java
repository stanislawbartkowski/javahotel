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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.jythonui.server.dict.DictEntry;

public class Test32 extends TestHelper {

    @Test
    public void test1() {

        assertFalse(iListC.getList().isEmpty());
        String pl = null;
        for (DictEntry c : iListC.getList()) {
            System.out.println(c.getName() + " " + c.getDescription());
            if (c.getName().equals("PL"))
                pl = c.getDescription();
        }
        assertNotNull(pl);

    }

    @Test
    public void test2() {

        assertFalse(iListT.getList().isEmpty());
        for (DictEntry c : iListT.getList()) {
            System.out.println(c.getName() + " " + c.getDescription());
        }
    }

    @Test
    public void test3() {

        assertFalse(iListI.getList().isEmpty());
        for (DictEntry c : iListI.getList()) {
            System.out.println(c.getName() + " " + c.getDescription());
        }
    }

    @Test
    public void test4() {

        assertFalse(iListP.getList().isEmpty());
        for (DictEntry c : iListP.getList()) {
            System.out.println(c.getName() + " " + c.getDescription());
        }
    }

    @Test
    public void test5() {
        String val = dData.getValue("keyM");
        assertNull(val);
        dData.putValue("keyM", "hello");
        val = dData.getValue("keyM");
        assertEquals("hello", val);
        putLocale(null);
        val = dData.getValue("keyM");
        assertNull(val);
        putLocale("pl");
        val = dData.getValue("keyM");
        assertEquals("hello", val);
    }

    @Test
    public void test6() {
        String val = dData.getValue("locM");
        assertEquals("pl", val);
        putLocale(null);
        val = dData.getValue("locM");
        assertNull(val);

        val = dData.getValue("noLoc");
        assertEquals("mniam", val);
        putLocale(null);
        val = dData.getValue("noLoc");
        assertEquals("mniam", val);

        putLocale("pl");
        val = dData.getValue("locM");
        assertEquals("pl", val);
        dData.putValue("locM", "hello");
        val = dData.getValue("locM");
        assertEquals("hello", val);
    }

    @Test
    public void test7() {
        assertEquals(3, iListR.getList().size());
        for (DictEntry r : iListR.getList()) {
            assertNotNull(r.getName());
            assertNotNull(r.getDescription());
        }
    }

}
