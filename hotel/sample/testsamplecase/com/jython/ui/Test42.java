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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.gwtmodel.table.common.TT;
import com.gwtmodel.table.shared.JythonUIFatal;
import com.jythonui.shared.ButtonItem;
import com.jythonui.shared.DateLine;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.ListFormat;

public class Test42 extends TestHelper {

    @Test
    public void test1() {
        DialogFormat d = findDialog("test75.xml");
        assertNotNull(d);
        assertTrue(d.isAutoHideDialog());
        assertFalse(d.isModelessDialog());
    }

    @Test
    public void test2() {
        DialogFormat d = findDialog("test76.xml");
        assertNotNull(d);
        assertFalse(d.isAutoHideDialog());
        assertEquals(2, d.getDiscList().size());
        assertTrue(d.isModelessDialog());
        assertNull(d.getCssCode());
    }

    @Test
    public void test3() {
        DialogFormat d = findDialog("test77.xml");
        assertNotNull(d);
        ListFormat f = DialogFormat.findE(d.getListList(), "lista");
        assertNotNull(f);
        assertEquals("hello", f.getJSModifRow());
        assertEquals("CSS", d.getCssCode());
    }

    @Test
    public void test4() {
        DialogFormat d = findDialog("test78.xml");
        assertNotNull(d);
        DateLine dL = d.getDatelineList().get(0);
        assertNotNull(dL.getStandButt());
        assertEquals(1, d.getActionList().size());
        ButtonItem a = d.getActionList().get(0);
        assertNull(a.getJsAction());
    }

    @Test
    public void test5() {
        try {
            DialogFormat d = findDialog("test79.xml");
        } catch (JythonUIFatal e) {
            // expected
            return;
        }
        // failure
        fail();
    }

    @Test
    public void test6() {
        DialogFormat d = findDialog("test80.xml");
        assertEquals(1, d.getActionList().size());
        ButtonItem a = d.getActionList().get(0);
        assertEquals("JS.helloworld", a.getJsAction());
    }

    @Test
    public void test7() {
        DialogFormat d = findDialog("test81.xml");
        assertNotNull(d);
        FieldItem i = d.findFieldItem("spin");
        assertNotNull(i);
        assertTrue(i.isSpinner());
        assertEquals(TT.INT, i.getFieldType());
    }

    @Test
    public void test8() {
        DialogFormat d = findDialog("test82.xml");
        assertNotNull(d);
        FieldItem i = d.findFieldItem("spin");
        assertNotNull(i);
        assertTrue(i.isSpinner());
        assertEquals(TT.INT, i.getFieldType());
        assertEquals(5,i.getSpinnerMin());
        assertEquals(5,i.getSpinnerMax());
        i = d.findFieldItem("spin1");
        assertEquals(1,i.getSpinnerMin());
        assertEquals(15,i.getSpinnerMax());
        i = d.findFieldItem("spin2");
        assertEquals(24,i.getSpinnerMin());
        assertEquals(25,i.getSpinnerMax());
    }

}
