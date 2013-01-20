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
import com.jythonui.shared.FieldItem;

/**
 * @author hotel
 *
 */
public class Test11 extends TestHelper {
    
    @Test
    public void test1() {
        DialogFormat d = iServer.findDialog("test20.xml");
        assertNotNull(d);
        assertNotNull(d.getDisplayName());
        assertEquals("Dialog title",d.getDisplayName());

    }

    @Test
    public void test2() {
        DialogFormat d = iServer.findDialog("test21.xml");
        assertNotNull(d);
        FieldItem f = d.findFieldItem("glob1");
        assertTrue(f.isReadOnlyChange());
        f = d.findFieldItem("glob2");
        assertTrue(f.isReadOnlyAdd());
    }

}
