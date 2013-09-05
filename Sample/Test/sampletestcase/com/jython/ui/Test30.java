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

public class Test30 extends TestHelper {

    @Test
    public void test1() {

        DialogFormat d = findDialog("test61.xml");
        assertNotNull(d);
        assertEquals(1, d.getLeftButtonList().size());
        assertEquals(2, d.getUpMenuList().size());
        assertTrue(d.getLeftStackList().isEmpty());
    }

    @Test
    public void test2() {

        DialogFormat d = findDialog("test62.xml");
        assertNotNull(d);
        assertEquals(1, d.getLeftButtonList().size());
        assertEquals(2, d.getUpMenuList().size());
        assertEquals(4, d.getLeftStackList().size());
    }

}
