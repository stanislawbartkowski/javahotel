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

import org.junit.Test;
import static org.junit.Assert.*;

import com.jythonui.server.dict.IDictOfLocalEntries;

public class Test32 extends TestHelper {

    @Test
    public void test1() {

        assertTrue(iListC.getList().length > 0);
        String pl = null;
        for (IDictOfLocalEntries.DictEntry c : iListC.getList()) {
            System.out.println(c.getKey() + " " + c.getName());
            if (c.getKey().equals("PL"))
                pl = c.getName();
        }
        assertNotNull(pl);

    }

    @Test
    public void test2() {

        assertTrue(iListT.getList().length > 0);
        for (IDictOfLocalEntries.DictEntry c : iListT.getList()) {
            System.out.println(c.getKey() + " " + c.getName());
        }
    }

    @Test
    public void test3() {

        assertTrue(iListI.getList().length > 0);
        for (IDictOfLocalEntries.DictEntry c : iListI.getList()) {
            System.out.println(c.getKey() + " " + c.getName());
        }
    }

    @Test
    public void test4() {

        assertTrue(iListP.getList().length > 0);
        for (IDictOfLocalEntries.DictEntry c : iListP.getList()) {
            System.out.println(c.getKey() + " " + c.getName());
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

}
