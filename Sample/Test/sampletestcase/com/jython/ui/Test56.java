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

import com.jythonui.shared.DateLine;
import com.jythonui.shared.DialogFormat;

public class Test56 extends TestHelper {

    @Test
    public void test1() {
        DialogFormat d = findDialog("test101.xml");
        assertNotNull(d);    
        DateLine dl = d.findDateLine("dateline");
        assertNotNull(dl);
        assertEquals(10,dl.getCurrentPos());        
    }
    
    @Test
    public void test2() {
        DialogFormat d = findDialog("test102.xml");
        assertNotNull(d);  
        System.out.println(d.getTop());
        assertEquals(20,d.getTop());
        System.out.println(d.getLeft());
        assertEquals(10,d.getLeft());
        System.out.println(d.getMaxTop());
        assertEquals(2,d.getMaxTop());
        System.out.println(d.getMaxLeft());
        assertEquals(1,d.getMaxLeft());
    }

}
