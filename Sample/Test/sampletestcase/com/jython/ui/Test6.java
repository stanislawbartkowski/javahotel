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

import static org.junit.Assert.*;

import org.junit.Test;

import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.FieldItem;

/**
 * @author hotel
 * 
 */
public class Test6 extends TestHelper {

    @Test
    public void test1() {
        DialogFormat d = findDialog("test13.xml");
        assertNotNull(d);
        DialogFormat elem = d.getListList().get(0).getfElem();
        assertNotNull(elem);
        assertNotNull(elem.getFieldList());
        assertNotNull(elem.getJythonImport());
        assertNotNull(elem.getJythonMethod());
    }

    @Test
    public void test2() {
        DialogFormat d = findDialog("test14.xml");
        assertNotNull(d);
        DialogFormat elem = d.getListList().get(0).getfElem();
        assertNotNull(elem);
        assertNotNull(elem.getFieldList());
        assertNotNull(elem.getJythonImport());
        assertNotNull(elem.getJythonMethod());
        assertEquals(1, elem.getFieldList().size());
        String imp = elem.getJythonImport();
        String me = elem.getJythonMethod();
        System.out.println(imp + " " + me);
        assertEquals("XXX", imp);
        assertEquals("YYY", me);
    }

    @Test
    public void test3() {
        DialogFormat d = findDialog("test15.xml");
        assertNotNull(d);
        assertEquals(2, d.getFieldList().size());
        int no = 0;
        for (FieldItem i : d.getFieldList()) {
            String id = i.getId();
            if (id.equals("glob1")) {
                assertTrue(i.isHidden());
                assertTrue(i.isReadOnly());
                assertFalse(i.isNotEmpty());
                no++;
            }
            if (id.equals("globtimestamp")) {
                no++;
                assertTrue(i.isNotEmpty());
                assertFalse(i.isHidden());
                assertFalse(i.isReadOnly());
            }
        }
        assertEquals(2, no);
    }

    @Test
    public void test4() {
        DialogFormat d = findDialog("test16.xml");
        assertNotNull(d);
        d = findDialog("elem3.xml");
        assertNotNull(d);
        assertNotNull(d.getFieldList());
        assertNotNull(d.getJythonImport());
        assertNotNull(d.getJythonMethod());
    }

    @Test
    public void test5() {
        DialogFormat d = findDialog("test17.xml");
        assertNotNull(d);
        d = findDialog("elem4.xml");
        assertNotNull(d);
        assertNotNull(d.getFieldList());
        assertNotNull(d.getJythonImport());
        assertNotNull(d.getJythonMethod());
        d = findDialog("elem5.xml");
        assertNotNull(d);
        assertNotNull(d.getFieldList());
        assertNotNull(d.getJythonImport());
        assertNotNull(d.getJythonMethod());
    }
}
