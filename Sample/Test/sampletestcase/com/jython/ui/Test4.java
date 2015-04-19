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

import org.junit.Test;

import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.FieldItem;

/**
 * @author hotel
 * 
 */
public class Test4 extends TestHelper {

    @Test
    public void test1() {
        DialogFormat d = findDialog("test9.xml");
        assertNotNull(d);
        assertNotNull(d.getListList());
        assertEquals(1, d.getListList().size());
        assertEquals(2, d.getListList().get(0).getColumns().size());
        int i = 0;
        for (FieldItem f : d.getListList().get(0).getColumns()) {
            String id = f.getId();
            String name = f.getDisplayName();
            System.out.println(id + " " + name);
            if (id.equals("ID")) {
                i++;
                assertEquals("XXX", name);
            }
            if (id.equals("Name")) {
                i++;
                assertNull(name);
            }
        }
        assertEquals(2, i);
    }

    @Test
    public void test2() {
        DialogFormat d = findDialog("test10.xml");
        assertNotNull(d);
        assertNotNull(d.getListList());
        assertEquals(2, d.getListList().size());
        assertEquals(2, d.getListList().get(0).getColumns().size());
        assertEquals(1, d.getListList().get(1).getColumns().size());
    }

}
