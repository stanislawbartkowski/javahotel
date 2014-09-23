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
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.ListFormat;

public class Test55 extends TestHelper {

    @Test
    public void test1() {

        DialogFormat d = findDialog("test97.xml");
        assertNotNull(d);
        ListFormat li = d.findList("blist");
        assertNotNull(li);
        assertFalse(li.isNoWrap());
        li = d.findList("blistnowrap");
        assertNotNull(li);
        assertTrue(li.isNoWrap());
    }

    @Test
    public void test2() {

        DialogFormat d = findDialog("test98.xml");
        assertNotNull(d);
        FieldItem i = d.findFieldItem("content");
        assertNotNull(i);
        assertEquals("10", i.getVisLines());
    }

    @Test
    public void test3() {

        DialogFormat d = findDialog("test99.xml");
        assertNotNull(d);
        DialogVariables v = new DialogVariables();
        runAction(v, "test99.xml", "testxml");
    }

    @Test
    public void test4() {
        String from = iFrom.getFrom();
        System.out.println(from);
        assertEquals("Hello.From.Jython",from);
    }
    
    @Test
    public void test5() {
        DialogFormat d = findDialog("test100.xml");
        assertNotNull(d);
        DialogVariables v = new DialogVariables();
        runAction(v, "test100.xml", "testfrom");
        assertOK(v);
    }

}
