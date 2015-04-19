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

import java.math.BigDecimal;

import org.junit.Test;

import com.gwtmodel.table.common.DecimalUtils;
import com.jythonui.shared.CheckList;
import com.jythonui.shared.DialogCheckVariables;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ListOfRows;
import com.jythonui.shared.RowContent;
import com.jythonui.shared.RowIndex;

public class Test20 extends TestHelper {

    @Test
    public void test1() {
        DialogFormat d = findDialog("test47.xml");
        assertNotNull(d);
        DialogVariables v = new DialogVariables();
        runAction(v, "test47.xml", "before");
        DialogCheckVariables var = v.getCheckVariables().get("prices");
        assertNotNull(var);
        assertTrue(var.getErrors().getRowList().isEmpty());
        ListOfRows l = var.getLines();
        assertEquals(3, l.getRowList().size());
        for (RowContent r : l.getRowList()) {
            String id = r.getRow(0).getValueS();
            String displayname = r.getRow(1).getValueS();
            System.out.println(id + " " + displayname);
        }
        l = var.getColumns();
        assertEquals(2, l.getRowList().size());
        for (RowContent r : l.getRowList()) {
            String id = r.getRow(0).getValueS();
            String displayname = r.getRow(1).getValueS();
            System.out.println(id + " " + displayname);
        }
        l = var.getVal().get("price1");
        assertNotNull(l);
        assertEquals(2, l.getRowList().size());
        BigDecimal sum = new BigDecimal(0);
        for (RowContent r : l.getRowList()) {
            String id = r.getRow(0).getValueS();
            BigDecimal b = r.getRow(1).getValueBD();
            System.out.println(id + " : " + b);
            sum = sum.add(b);
        }
        System.out.println("sum=" + sum);
        assertEquals(DecimalUtils.DecimalToS(sum, 2), "23.88");
    }

    @Test
    public void test2() {
        DialogFormat d = findDialog("test47.xml");
        assertNotNull(d);
        DialogVariables v = new DialogVariables();

        ListOfRows lRows = new ListOfRows();
        CheckList cList = DialogFormat.findE(d.getCheckList(), "prices");
        RowIndex rI = new RowIndex(cList.constructValLine());
        RowContent row = rI.constructRow();

        FieldValue val = new FieldValue();
        val.setValue("weekend");
        rI.setRowField(row, "id", val);
        val = new FieldValue();
        val.setValue(new BigDecimal(9.12), 2);
        rI.setRowField(row, "val", val);
        lRows.addRow(row);

        row = rI.constructRow();
        val = new FieldValue();
        val.setValue("working");
        rI.setRowField(row, "id", val);
        val = new FieldValue();
        val.setValue(new BigDecimal(4.12), 2);
        rI.setRowField(row, "val", val);
        lRows.addRow(row);

        DialogCheckVariables var = new DialogCheckVariables();
        var.getVal().put("price1", lRows);
        v.getCheckVariables().put("prices", var);

        runAction(v, "test47.xml", "dosth");
        FieldValue resV = v.getValue("OKTEST");
        assertNotNull(resV);
        assertTrue(resV.getValueB());
    }

    @Test
    public void test3() {
        DialogFormat d = findDialog("test47.xml");
        assertNotNull(d);
        DialogVariables v = new DialogVariables();
        runAction(v, "test47.xml", "checkerror");
        DialogCheckVariables var = v.getCheckVariables().get("prices");
        assertNotNull(var);
        ListOfRows errorS = var.getErrors();
        assertFalse(errorS.getRowList().isEmpty());
        for (RowContent r : errorS.getRowList()) {
            String line = r.getRow(0).getValueS();
            String col = r.getRow(1).getValueS();
            String errS = r.getRow(2).getValueS();
            assertNotNull(line);
            assertNotNull(col);
            assertNotNull(errS);
            assertEquals("price1", line);
            assertEquals("working", col);
            System.out.println(line + " " + col + " =  " + errS);
        }
    }

}
