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

import static org.junit.Assert.*;

import org.junit.Test;

import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.ListFormat;

public class Test42 extends TestHelper {

    @Test
    public void test1() {
        DialogFormat d = findDialog("test75.xml");
        assertNotNull(d);
        assertTrue(d.isAutoHideDialog());
        assertFalse(d.isModelessDialog());
    }

    @Test
    public void test2() {
        DialogFormat d = findDialog("test76.xml");
        assertNotNull(d);
        assertFalse(d.isAutoHideDialog());
        assertEquals(2, d.getDiscList().size());
        assertTrue(d.isModelessDialog());
        assertNull(d.getCssCode());
    }

    @Test
    public void test3() {
        DialogFormat d = findDialog("test77.xml");
        assertNotNull(d);
        ListFormat f = DialogFormat.findE(d.getListList(), "lista");
        assertNotNull(f);
        assertEquals("hello", f.getJSModifRow());
        assertEquals("CSS", d.getCssCode());
    }

}
