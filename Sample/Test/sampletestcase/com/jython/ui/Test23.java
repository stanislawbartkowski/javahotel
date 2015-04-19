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

import java.util.Date;

import org.junit.Test;

import com.jythonui.shared.DateLine;
import com.jythonui.shared.DateLineVariables;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.RowContent;
import com.jythonui.shared.RowIndex;

public class Test23 extends TestHelper {

    @Test
    public void test1() {
        DialogFormat d = findDialog("test51.xml");
        assertNotNull(d);
        DateLine dL = d.getDatelineList().get(0);
        assertEquals("datecol", dL.getDateColId());
        assertEquals("name1", dL.getDefaFile());
        assertEquals("file", dL.getDateFile());
    }

    @Test
    public void test2() {
        DialogFormat d = findDialog("test51.xml");
        assertNotNull(d);
        DateLine dL = d.findDateLine("dateline");
        DialogVariables v = new DialogVariables();
        v.setValueS(ICommonConsts.JDATELINEQUERYID, "dateline");
        RowIndex rI = new RowIndex(dL.constructQueryLine());
        RowContent row = rI.constructRow();
        FieldValue vId = new FieldValue();
        vId.setValue(1L);
        row.setRow(0, vId);
        Date dFrom = getD(2012, 1, 1);
        Date dTo = getD(2013, 2, 2);
        FieldValue va = new FieldValue();
        va.setValue(dFrom);
        row.setRow(1, va);
        va = new FieldValue();
        va.setValue(dTo);
        row.setRow(2, va);
        v.getQueryDateLine().addRow(row);
        runAction(v, "test50.xml", "datelinevalues");
        DateLineVariables vara = v.getDatelineVariables().get("dateline");
        assertNotNull(vara);
        assertEquals(9,vara.getValues().getRowList().size());
        for (RowContent r : vara.getValues().getRowList()) {
            assertEquals(7,r.getLength());
            for (int i=0; i<r.getLength(); i++) {
                System.out.println(i + " " + r.getRow(i).getValue());
            }
            
        }
    }


    @Test
    public void test3() {
        DialogFormat d = findDialog("test69.xml");
        assertNotNull(d);
        DateLine dL = d.findDateLine("dateline");
        assertNotNull(dL);
        DialogVariables v = new DialogVariables();
        runAction(v, "test69.xml", "datelinevalues");
        DateLineVariables vara = v.getDatelineVariables().get("dateline");
        assertNotNull(vara);
        assertEquals(9,vara.getValues().getRowList().size());
        for (RowContent r : vara.getValues().getRowList()) {
            assertEquals(7,r.getLength());
            for (int i=0; i<r.getLength(); i++) {
                System.out.println(i + " " + r.getRow(i).getValue());
            }            
        }
    }
}
