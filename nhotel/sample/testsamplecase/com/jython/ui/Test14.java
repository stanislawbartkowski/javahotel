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

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.gwtmodel.table.common.TT;
import com.jythonui.shared.ButtonItem;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.ListOfRows;

public class Test14 extends TestHelper {

    @Test
    public void test1() {
        DialogFormat d = findDialog("test32.xml");
        assertNotNull(d);
        ButtonItem b = DialogFormat.findE(d.getButtonList(), "ID");
        assertNotNull(b);
        assertTrue(b.isValidateAction());
        assertEquals(2, d.getValList().size());
    }

    @Test
    public void test2() {
        DialogFormat d = findDialog("test33.xml");
        assertNotNull(d);
        ListFormat li = d.findList("list");
        assertNotNull(li);
        assertEquals(15, li.getPageSize());
        assertNull(li.getWidth());

        List<FieldItem> col = li.getColumns();
        FieldItem f = DialogFormat.findE(col, "id");
        assertNotNull(f);
        assertEquals("15%", f.getWidth());
        assertNull(f.getAlign());
        f = DialogFormat.findE(col, "pnumber");
        assertNotNull(f);
        assertNull(f.getWidth());
        assertEquals("L", f.getAlign());

        li = d.findList("list1");
        assertEquals("95%", li.getWidth());
    }

    @Test
    public void test3() {
        DialogFormat d = findDialog("test35.xml");
        assertNotNull(d);
        DialogVariables v = new DialogVariables();
        runAction(v, "test35.xml", "before");
        ListOfRows ro = v.getList("list");
        assertNotNull(ro);
        assertEquals(20, ro.getSize());
        assertTrue(ro.getRowList().isEmpty());
        v = new DialogVariables();
        v.setValueL("JLIST_FROM", 0);
        v.setValueL("JLIST_LENGTH", 30);
        runAction(v, "test35.xml", "readchunk");
        ro = v.getList("list");
        assertEquals(30, ro.getRowList().size());
    }

    @Test
    public void test4() {
        DialogFormat d = findDialog("test35.xml");
        assertNotNull(d);
        DialogVariables v = new DialogVariables();
        runAction(v, "test35.xml", "listgetsize");
        ListOfRows ro = v.getList("list");
        assertNotNull(ro);
        assertEquals(74, ro.getSize());
        assertTrue(ro.getRowList().isEmpty());
    }

    @Test
    public void test5() {
        DialogFormat d = findDialog("test36.xml");
        assertNotNull(d);
        FieldItem f = d.findFieldItem("pass");
        assertNotNull(f);
        assertTrue(f.isPassword());
        assertEquals(TT.STRING, f.getFieldType());
    }
    
    @Test
    public void test6() {
        DialogFormat d = findDialog("test37.xml");
        assertNotNull(d);
        FieldItem f = d.findFieldItem("glob");
        assertNotNull(f);
        assertFalse(f.isHtmlId());
        f = d.findFieldItem("globenum");
        assertTrue(f.isHtmlId());
        assertEquals("globhtmlid",f.getHtmlId());
    }
}
