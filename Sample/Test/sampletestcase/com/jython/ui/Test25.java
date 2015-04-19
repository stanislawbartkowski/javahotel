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

import org.junit.Test;

import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.ListFormat;

public class Test25 extends TestHelper {

    @Test
    public void test1() {
        DialogFormat d = findDialog("test52.xml");
        assertNotNull(d);
        ListFormat list = d.findList("lista");
        assertNotNull(list);
        FieldItem fi = DialogFormat.findE(list.getColumns(),"id");
        assertNotNull(fi);
        assertFalse(fi.isFooter());
        fi = DialogFormat.findE(list.getColumns(),"name");
        assertNotNull(fi);
        assertTrue(fi.isFooter());
        assertFalse(list.isAfterRowSignal());
        assertFalse(list.isBeforeRowSignal());
        assertTrue(list.getValList().isEmpty());
    }
    
    @Test
    public void test2() {
        DialogFormat d = findDialog("test53.xml");
        assertNotNull(d);
        ListFormat list = d.findList("lista");
        assertNotNull(list);
        assertTrue(list.isAfterRowSignal());
        assertTrue(list.isBeforeRowSignal());
        assertTrue(list.getValList().isEmpty());
    }

    @Test
    public void test3() {
        DialogFormat d = findDialog("test54.xml");
        assertNotNull(d);
        ListFormat list = d.findList("lista");
        assertNotNull(list);
        assertEquals(2,list.getValList().size());
    }

}
