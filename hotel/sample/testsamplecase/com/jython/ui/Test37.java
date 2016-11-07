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

import org.junit.Test;

import static org.junit.Assert.*;

import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;

public class Test37 extends TestHelper {

    @Test
    public void test1() {
        DialogFormat d = findDialog("test74.xml");
        assertNotNull(d);
        DialogVariables v = new DialogVariables();
        runAction(v, "test74.xml", "test1");
        assertOK(v);
        v = new DialogVariables();
        runAction(v, "test74.xml", "test2");
        assertOK(v);
    }

    @Test
    public void test2() {
        DialogFormat d = findDialog("test74.xml");
        assertNotNull(d);
        DialogVariables v = new DialogVariables();
        v.setValueS("hello", "Rząd");
        runAction(v, "test74.xml", "test3");
        String xml = v.getValueS("xml");
        System.out.println(xml);
        String rzad = v.getValueS("key1");
        System.out.println(rzad);
        assertEquals(rzad, "Rząd");
        assertOK(v);
    }

}
