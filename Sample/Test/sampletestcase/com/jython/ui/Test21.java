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

import org.junit.Test;

import com.jythonui.shared.DateLine;
import com.jythonui.shared.DateLineVariables;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.RowContent;

public class Test21 extends TestHelper {

    @Test
    public void test1() {
        DialogFormat d = findDialog("test49.xml");
        assertNotNull(d);
        assertFalse(d.getDatelineList().isEmpty());
        DateLine dL = d.getDatelineList().get(0);
        assertEquals(10, dL.getColNo());
        assertEquals(20, dL.getRowNo());
        assertFalse(dL.getColList().isEmpty());
        assertEquals(2,dL.getColList().size());
    }

    @Test
    public void test2() {
        DialogFormat d = findDialog("test50.xml");
        assertNotNull(d);
        DialogVariables v = new DialogVariables();
        runAction(v, "test50.xml", "before");
        assertEquals(1,v.getDatelineVariables().size());
        DateLineVariables vLine = v.getDatelineVariables().get("dateline");
        assertNotNull(vLine);
        assertEquals(30,vLine.getLines().getRowList().size());
        for (RowContent r : vLine.getLines().getRowList()) {
            Long id = r.getRow(0).getValueL();
            String vName = r.getRow(1).getValueS();
            assertNotNull(id);
            assertNotNull(vName);
            System.out.println(id + " : " + vName);
        }

    }
}
