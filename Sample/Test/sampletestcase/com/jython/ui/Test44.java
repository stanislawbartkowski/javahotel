/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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

public class Test44 extends TestHelper {

    @Test
    public void test1() {
        String t = authenticateToken(realmIni, "guest", "guest");
        assertNotNull(t);
        DialogVariables v = new DialogVariables();
        runAction(t, v, "test85.xml", "testmap");
        v = new DialogVariables();
        runAction(t, v, "test85.xml", "testlistmap");
    }

    @Test
    public void test2() {
        String t = authenticateToken(realmIni, "guest", "guest");
        assertNotNull(t);
        DialogVariables v = new DialogVariables();
        runAction(t, v, "test85.xml", "testxmllist");
    }

    @Test
    public void test3() {
        DialogFormat d = findDialog("test86.xml");
        assertNotNull(d);
        ListFormat l = d.findList("reslist");
        assertNotNull(l);
        System.out.println(l.getListButtonsSelected());
        assertEquals("accept,select", l.getListButtonsSelected());
        System.out.println(l.getListSelectedMess());
        assertEquals("Hello", l.getListSelectedMess());
    }

    @Test
    public void test4() {
        DialogFormat d = findDialog("test87.xml");
        assertNotNull(d);
        ListFormat l = d.findList("list1");
        assertNotNull(l);
        assertEquals(ListFormat.ToolBarType.EDIT, l.getToolBarType());
        l = d.findList("list2");
        assertNotNull(l);
        assertEquals(ListFormat.ToolBarType.LISTONLY, l.getToolBarType());
        l = d.findList("list3");
        assertNotNull(l);
        assertEquals(ListFormat.ToolBarType.EDIT, l.getToolBarType());
        l = d.findList("list4");
        assertNotNull(l);
        assertEquals(ListFormat.ToolBarType.LISTSHOWONLY, l.getToolBarType());
    }

}
