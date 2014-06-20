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
import com.jythonui.shared.FieldItem;

public class Test45  extends TestHelper {
    
    @Test
    public void test1() {
        
        DialogFormat d = findDialog("test88.xml");
        assertNotNull(d);
        FieldItem f = d.findFieldItem("hello");
        assertNotNull(f);
        System.out.println(f.getDisplayName());
        assertEquals("Hello custom par",f.getDisplayName());        
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

}

