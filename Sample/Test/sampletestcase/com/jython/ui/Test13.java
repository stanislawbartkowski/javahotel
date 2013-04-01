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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.gwtmodel.table.common.TT;
import com.jythonui.shared.ButtonItem;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.ListOfRows;
import com.jythonui.shared.TypedefDescr;

public class Test13 extends TestHelper {

    @Test
    public void test1() {
        DialogFormat d = findDialog("test29.xml");
        assertNotNull(d);
        DialogVariables v = new DialogVariables();
        iServer.runAction(v, "test26.xml", ICommonConsts.BEFORE);
        ListOfRows ro = v.getEnumList().get("tenum");
        assertNotNull(ro);
        assertEquals(100, ro.getRowList().size());
        ro = v.getEnumList().get("globhelper");
        assertNull(ro);
    }

    @Test
    public void test2() {
        DialogFormat d = findDialog("test29.xml");
        assertNotNull(d);
        FieldItem f = d.findFieldItem("globhelper");
        DialogVariables v = new DialogVariables();
        iServer.runAction(v, "test26.xml", f.getTypeName());
        ListOfRows ro = v.getEnumList().get("tehelper");
        assertNotNull(ro);
        assertEquals(10, ro.getRowList().size());
    }

    @Test
    public void test3() {
        DialogFormat d = findDialog("test29.xml");
        assertNotNull(d);
        TypedefDescr ty = d.findCustomType("tehelper");
        assertNotNull(ty);
        assertEquals(3, ty.getListOfColumns().size());
    }

    @Test
    public void test4() {
        DialogFormat d = findDialog("test30.xml");
        assertNotNull(d);
        FieldItem f = d.findFieldItem("glob1");
        assertNotNull(f);
        assertEquals(TT.STRING, f.getFieldType());
        assertFalse(f.isTextArea());

        f = d.findFieldItem("glob2");
        assertNotNull(f);
        assertEquals(TT.STRING, f.getFieldType());
        assertTrue(f.isTextArea());
        assertNull(f.getFrom());

        f = d.findFieldItem("glob3");
        assertNotNull(f);
        assertEquals(TT.STRING, f.getFieldType());
        assertFalse(f.isTextArea());
        assertTrue(f.isRichText());
    }

    @Test
    public void test5() {
        DialogFormat d = findDialog("test29.xml");
        assertNotNull(d);
        FieldItem f = d.findFieldItem("glob2");
        assertEquals("glob", f.getFrom());
    }

    @Test
    public void test6() {
        DialogFormat d = findDialog("test31.xml");
        assertNotNull(d);
        ButtonItem b = DialogFormat.findE(d.getButtonList(), "ID");
        assertNotNull(b);
        assertTrue(b.isValidateAction());
    }

}
