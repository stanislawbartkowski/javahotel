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

import static org.junit.Assert.*;

import org.junit.Test;

import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.ListOfRows;
import com.jythonui.shared.RowContent;
import com.jythonui.shared.RowIndex;

/**
 * @author hotel
 * 
 */
public class Test5 extends TestHelper {

    @Test
    public void test1() {
        DialogVariables v = new DialogVariables();
        FieldValue val = new FieldValue();
        val.setValue("Firm worldwide");
        v.setValue("company", val);
        DialogFormat d = findDialog("test11.xml");
        assertNotNull(d);
        ListFormat li = d.findList("lista1");
        assertNotNull(li);
        RowIndex rI = new RowIndex(li.getColumns());
        RowContent row = rI.constructRow();
        ListOfRows list = new ListOfRows();
        val = new FieldValue();
        val.setValue("cid");
        rI.setRowField(row, "id", val);
        val = new FieldValue();
        val.setValue("Company name");
        rI.setRowField(row, "name", val);
        list.addRow(row);
        v.setRowList("lista1", list);

        runAction(v, "test11.xml", "writelist");
        FieldValue ok = v.getValue("OK");
        assertNotNull(ok);
        Boolean b = ok.getValueB();
        assertNotNull(b);
        assertTrue(b);
        ListOfRows l = v.getRowList().get("lista1");
        assertNotNull(l);
        assertEquals(1, l.getRowList().size());
        for (RowContent r : l.getRowList()) {
            for (int i = 0; i < rI.rowSize(); i++) {
                FieldValue vv = r.getRow(i);
                System.out.println(vv.getValueS());
                if (i == 0) {
                    assertEquals("cid", vv.getValueS());
                }
                if (i == 1) {
                    assertEquals("Company name", vv.getValueS());
                }
            }
        }
    }

    @Test
    public void test2() {
        FieldValue val = new FieldValue();
        DialogVariables v = new DialogVariables();
        DialogFormat d = findDialog("test11.xml");
        assertNotNull(d);
        ListFormat li = d.findList("lista1");
        assertNotNull(li);
        RowIndex rI = new RowIndex(li.getColumns());
        RowContent row = rI.constructRow();
        ListOfRows list = new ListOfRows();
        val = new FieldValue();
        val.setValue("cid");
        rI.setRowField(row, "id", val);
        list.addRow(row);
        v.setRowList("lista1", list);

        runAction(v, "test11.xml", "checkList");
        FieldValue ok = v.getValue("OK");
        assertNotNull(ok);
        Boolean b = ok.getValueB();
        assertNotNull(b);
        assertTrue(b);
        ListOfRows l = v.getRowList().get("lista1");
        assertNotNull(l);
        assertEquals(1, l.getRowList().size());
        for (RowContent r : l.getRowList()) {
            for (int i = 0; i < rI.rowSize(); i++) {
                FieldValue vv = r.getRow(i);
                System.out.println(vv.getValueS());
                if (i == 0) {
                    assertEquals("cid", vv.getValueS());
                }
                if (i == 1) {
                    assertNull(vv.getValueS());
                }
            }
        }
    }

    @Test
    public void test3() {
        DialogVariables v = new DialogVariables();
        DialogFormat d = findDialog("test12.xml");
        assertNotNull(d);
        ListFormat li = d.findList("lista");
        assertNotNull(li);
        RowIndex rI = new RowIndex(li.getColumns());

        runAction(v, "test12.xml", "createList");
        ListOfRows l = v.getRowList().get("lista");
        assertNotNull(l);
        assertEquals(100, l.getRowList().size());
        int i = 0;
        for (RowContent r : l.getRowList()) {
            Integer ii = rI.get(r, "id").getValueI();
            System.out.println(ii);
            assertEquals(i++, ii.intValue());
            assertNull(rI.get(r, "name").getValue());
        }
    }

    @Test
    public void test4() {
        DialogFormat d = findDialog("test11.xml");
        assertNotNull(d);
        assertEquals(2, d.getFieldList().size());
        for (FieldItem i : d.getFieldList()) {
            assertFalse(i.isHidden());
            assertFalse(i.isNotEmpty());
            assertFalse(i.isReadOnly());
        }

    }
}
