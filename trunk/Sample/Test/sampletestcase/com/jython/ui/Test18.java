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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.jythonui.shared.CheckList;
import com.jythonui.shared.DialogCheckVariables;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ListOfRows;
import com.jythonui.shared.RowContent;
import com.jythonui.shared.RowIndex;

public class Test18 extends TestHelper {

    @Test
    public void test1() {
        DialogFormat d = findDialog("test45.xml");
        assertNotNull(d);
        assertEquals(1, d.getCheckList().size());
    }

    @Test
    public void test2() {
        DialogFormat d = findDialog("test45.xml");
        assertNotNull(d);
        DialogVariables v = new DialogVariables();
        runAction(v, "test45.xml", "before");
        DialogCheckVariables var = v.getCheckVariables().get("check");
        assertNotNull(var);
        ListOfRows l = var.getLines();
        assertEquals(2, l.getRowList().size());
        int row = 0;
        for (RowContent r : l.getRowList()) {
            String id = r.getRow(0).getValueS();
            String displayname = r.getRow(1).getValueS();
            System.out.println(id + " " + displayname);
            switch (row) {
            case 0:
                assertEquals("hotel1", id);
                assertEquals("Hotel One", displayname);
                break;
            case 1:
                assertEquals("hotel2", id);
                assertEquals("Hotel Two", displayname);
                break;
            }
            row++;
        }
        l = var.getColumns();
        assertEquals(2, l.getRowList().size());
        row = 0;
        for (RowContent r : l.getRowList()) {
            String id = r.getRow(0).getValueS();
            String displayname = r.getRow(1).getValueS();
            System.out.println(id + " " + displayname);
            switch (row) {
            case 0:
                assertEquals("man", id);
                assertEquals("Manager", displayname);
                break;
            case 1:
                assertEquals("acc", id);
                assertEquals("Accountant", displayname);
                break;
            }
            row++;
        }

        l = var.getVal().get("hotel1");
        assertNotNull(l);
        assertEquals(2, l.getRowList().size());
        row = 0;
        for (RowContent r : l.getRowList()) {
            String id = r.getRow(0).getValueS();
            Boolean val = r.getRow(1).getValueB();
            System.out.println(id + " " + val);
            switch (row) {
            case 0:
                assertEquals("man", id);
                assertTrue(val);
                break;
            case 1:
                assertEquals("acc", id);
                assertFalse(val);
                break;
            }
            row++;
        }

        l = var.getVal().get("hotel2");
        assertNotNull(l);
        assertEquals(2, l.getRowList().size());
        row = 0;
        for (RowContent r : l.getRowList()) {
            String id = r.getRow(0).getValueS();
            Boolean val = r.getRow(1).getValueB();
            System.out.println(id + " " + val);
            switch (row) {
            case 0:
                assertEquals("man", id);
                assertFalse(val);
                break;
            case 1:
                assertEquals("acc", id);
                assertTrue(val);
                break;
            }
            row++;
        }

    }

    @Test
    public void test3() {
        DialogFormat d = findDialog("test45.xml");
        assertNotNull(d);
        DialogVariables v = new DialogVariables();
        ListOfRows lRows = new ListOfRows();
        CheckList cList = DialogFormat.findE(d.getCheckList(), "check");
        RowIndex rI = new RowIndex(cList.constructValLine());
        RowContent row = rI.constructRow();
        
        FieldValue val = new FieldValue();
        val.setValue("man");
        rI.setRowField(row, "id", val);
        val = new FieldValue();
        val.setValue(true);
        rI.setRowField(row, "val", val);
        lRows.addRow(row);
        
        row = rI.constructRow();
        val = new FieldValue();
        val.setValue("acc");
        rI.setRowField(row, "id", val);
        val = new FieldValue();
        val.setValue(false);
        rI.setRowField(row, "val", val);
        lRows.addRow(row);

        DialogCheckVariables var = new DialogCheckVariables();
        var.getVal().put("hotel1", lRows);
        v.getCheckVariables().put("check", var);
        runAction(v, "test45.xml", "dosth");
        FieldValue resV = v.getValue("OKTEST");
        assertNotNull(resV);
        assertTrue(resV.getValueB());
    }

}
