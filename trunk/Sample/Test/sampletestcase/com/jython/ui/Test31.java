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

import static org.junit.Assert.*;

import org.junit.Test;

import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.ListFormat;

public class Test31 extends TestHelper {

    @Test
    public void test1() {

        DialogFormat d = findDialog("test66.xml");
        assertNotNull(d);
        DialogVariables v = new DialogVariables();
        runAction(v, "test66.xml", "before");
        runAction(v, "test66.xml", "after");

        runAction(v, "test66.xml", "before1");
        runAction(v, "test66.xml", "after1");

        runAction(v, "test66.xml", "before2");
        runAction(v, "test66.xml", "after2");

        runAction(v, "test66.xml", "before3");
        runAction(v, "test66.xml", "after3");

        runAction(v, "test66.xml", "before4");
        runAction(v, "test66.xml", "after4");

        runAction(v, "test66.xml", "before5");
        runAction(v, "test66.xml", "after5");

        runAction(v, "test66.xml", "before6");
        runAction(v, "test66.xml", "after6");

    }

    @Test
    public void test2() {
        DialogFormat d = findDialog("test67.xml");
        assertNotNull(d);
        ListFormat li = d.findList("list");
        assertEquals("200px", li.getWidth());
        assertEquals("40%", li.getColumn("id_name").getWidth());
        assertEquals("ee", li.getColumn("name").getEditClass());
        assertEquals("rybka", li.getColumn("date").getEditCss());
    }
}