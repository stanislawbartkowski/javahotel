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

import com.jythonui.shared.ChartFormat;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.JythonUIFatal;
import com.jythonui.shared.ListOfRows;

public class Test46 extends TestHelper {

    @Test
    public void test1() {

        try {
            DialogFormat d = findDialog("test91.xml");
            fail();
        } catch (JythonUIFatal e) {
            // as expected
        }
    }

    @Test
    public void test2() {

        DialogFormat d = findDialog("test92.xml");
        assertNotNull(d);
        assertEquals(1, d.getChartList().size());
        ChartFormat ch = d.getChartList().get(0);
        assertEquals(800, ch.getOptionsHeight());
        assertEquals(ChartFormat.ChartType.PIE, ch.getChartType());
        assertTrue(ch.isPie3D());
    }

    @Test
    public void test3() {

        DialogFormat d = findDialog("test93.xml");
        assertNotNull(d);
        DialogVariables v = new DialogVariables();
        runAction(v, "test93.xml", "before");
        assertEquals(1, v.getChartList().size());
        ListOfRows ro = v.getChartList().get("chart");
        assertNotNull(ro);
        assertEquals(1, ro.getRowList().size());
    }

}
