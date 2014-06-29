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
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.gwtmodel.table.common.TT;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.FieldValue;

public class Test45 extends TestHelper {

    @Test
    public void test1() {

        DialogFormat d = findDialog("test88.xml");
        assertNotNull(d);
        FieldItem f = d.findFieldItem("hello");
        assertNotNull(f);
        System.out.println(f.getDisplayName());
        assertEquals("Hello custom par", f.getDisplayName());
    }

    @Test
    public void test2() {

        DialogFormat d = findDialog("test89.xml");
        assertNotNull(d);
        FieldItem f = d.findFieldItem("forme");
        assertNotNull(f);
        System.out.println(f.isHidden());
        assertFalse(f.isHidden());
        f = d.findFieldItem("forgroup");
        assertNotNull(f);
        System.out.println(f.isHidden());
        assertFalse(f.isHidden());
        f = d.findFieldItem("forworld");
        assertNotNull(f);
        System.out.println(f.isHidden());
        assertFalse(f.isHidden());
    }

    @Test
    public void test3() {

        DialogFormat d = findDialog("test90.xml");
        assertNotNull(d);
        DialogVariables v = new DialogVariables();
        runAction(v, "test90.xml", "set");
        FieldValue val = v.getValue("id");
        assertNotNull(val);
        assertEquals(TT.STRING, val.getType());
        assertNull(val.getValue());
    }

    @Test
    public void test4() {

        String t = authenticateToken(realmIni, "guest", "guest");
        assertNotNull(t);
        DialogFormat d = findDialog("test90.xml");
        assertNotNull(d);
        DialogVariables v = new DialogVariables();
        runAction(t, v, "test90.xml", "setmap");
        v = new DialogVariables();
        runAction(t, v, "test90.xml", "getmap");
        assertOK(v);
    }
}
