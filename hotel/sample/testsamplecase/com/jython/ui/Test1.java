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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.jythonui.shared.DialogFormat;

/**
 * @author hotel
 * 
 */
public class Test1 extends TestHelper {

    @Test
    public void test() {
        DialogFormat d = null;
        try {
            d = findDialog("startnonexisting.xml");
        } catch (Exception e) {
        }
        ;
        assertNull(d);
    }

    @Test
    public void test1() {
        DialogFormat d = findDialog("start1.xml");
        assertNotNull(d);
        assertTrue(d.isBefore());
        assertNotNull(d.getLeftButtonList());
    }

    @Test
    public void test2() {
        DialogFormat d = null;
        try {
            d = findDialog("test2.xml");
        } catch (Exception e) {
        }
        ;
        assertNull(d);
    }

}
