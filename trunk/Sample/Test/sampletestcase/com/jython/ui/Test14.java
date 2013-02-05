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

import com.jythonui.shared.ButtonItem;
import com.jythonui.shared.DialogFormat;


public class Test14 extends TestHelper {

    @Test
    public void test1() {
        DialogFormat d = iServer.findDialog("test32.xml");
        assertNotNull(d);
        ButtonItem b = DialogFormat.findE(d.getButtonList(), "ID");
        assertNotNull(b);
        assertTrue(b.isValidateAction());
        assertEquals(2,d.getValList().size());
    }
    
}
