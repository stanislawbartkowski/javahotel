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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.ListOfRows;
import com.jythonui.shared.RowContent;
import com.jythonui.shared.RowIndex;

public class Test27 extends TestHelper {

    @Test
    public void test1() {
        DialogFormat d = findDialog("test55.xml");
        assertNotNull(d);
        ListFormat fo = d.findList("list");
        assertNotNull(fo);
        System.out.println(fo.getListButtonsValidate());
        System.out.println(fo.getListButtonsWithList());
        assertEquals("accept", fo.getListButtonsValidate());
        assertEquals("accept,check", fo.getListButtonsWithList());
    }

    @Test
    public void test2() {
        DialogFormat d = findDialog("test56.xml");
        assertNotNull(d);
        ListFormat fo = d.findList("list");
        assertNotNull(fo);
        DialogVariables v = new DialogVariables();
        RowIndex rI = new RowIndex(fo.getColumns());
        RowContent row = rI.constructRow();
        ListOfRows li = new ListOfRows();
        li.addRow(row);
        FieldValue val = new FieldValue();
        val.setValue(5);
        rI.setRowField(row, "id_name", val);
        val = new FieldValue();
        val.setValue(getD(2013,10,3));
        rI.setRowField(row, "date", val);
        val = new FieldValue();
        val.setValue("hello");
        rI.setRowField(row, "name", val);
        v.getRowList().put("list", li);

        runAction(v, "test56.xml", "test1");
    }
    
    @Test
    public void test3() {
        DialogFormat d = findDialog("test56.xml");
        assertNotNull(d);
        ListFormat fo = d.findList("list");
        assertNotNull(fo);
        DialogVariables v = new DialogVariables();
        RowIndex rI = new RowIndex(fo.getColumns());
        ListOfRows li = new ListOfRows();
        for (int i=0; i<100; i++) {
            RowContent row = rI.constructRow();
            li.addRow(row);
            FieldValue val = new FieldValue();
            val.setValue(i);
            rI.setRowField(row, "id_name", val);
            val = new FieldValue();
            val.setValue(getD(2013,10,3));
            rI.setRowField(row, "date", val);
            val = new FieldValue();
            val.setValue("hello" + i);
            rI.setRowField(row, "name", val);


        }
        v.getRowList().put("list", li);
        runAction(v, "test56.xml", "test2");

    }

}
