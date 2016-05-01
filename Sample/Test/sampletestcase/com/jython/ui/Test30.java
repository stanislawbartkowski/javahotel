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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.gwtmodel.table.common.TT;
import com.jythonui.server.semaphore.ISemaphore;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.JythonUIFatal;

public class Test30 extends TestHelper {

    @Test
    public void test1() {

        DialogFormat d = findDialog("test61.xml");
        assertNotNull(d);
        assertEquals(1, d.getLeftButtonList().size());
        assertEquals(2, d.getUpMenuList().size());
        assertTrue(d.getLeftStackList().isEmpty());
    }

    @Test
    public void test2() {

        DialogFormat d = findDialog("test62.xml");
        assertNotNull(d);
        assertEquals(1, d.getLeftButtonList().size());
        assertEquals(2, d.getUpMenuList().size());
        assertEquals(4, d.getLeftStackList().size());
    }

    @Test
    public void test3() {

        DialogFormat d = findDialog("test63.xml");
        assertNotNull(d);
        FieldItem i = d.findList("list").getColumn("id_name");
        assertEquals(TT.STRING, i.getFooterType());
        i = d.findList("list").getColumn("name");
        assertEquals("C", i.getFooterAlign());
        assertFalse(d.isClearCentre());
        assertFalse(d.isClearLeft());
    }

    @Test
    public void test4() {

        DialogFormat d = findDialog("test64.xml");
        assertNotNull(d);
        assertTrue(d.isClearCentre());
        assertTrue(d.isClearLeft());

    }

    @Test
    public void test5() {

        try {
            DialogFormat d = findDialog("test65.xml");
            fail("Should be failed as column before and boolean");
        } catch (JythonUIFatal e) {
            System.out.println("Exception as expected");
        }
    }

    @Test
    public void test6() {
        iSem.wait("SEMA", ISemaphore.DEFAULT);
        iSem.wait("SEMA", ISemaphore.DEFAULT);
        iSem.signal("SEMA");
    }

}
