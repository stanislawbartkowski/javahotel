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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.gwtmodel.table.shared.JythonUIFatal;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.FieldItem;

public class Test43 extends TestHelper {

    @Test
    public void test1() {
        String t = authenticateToken(realmIni, "darkhelmet", "ludicrousspeed");
        assertNotNull(t);
        DialogFormat d = findDialog(t, "test83.xml").getDialog();
        assertNotNull(d);
        FieldItem i = d.findFieldItem("name");
        assertNotNull(i);
        System.out.println(i.getDisplayName());
        assertEquals("Hello Dolly", i.getDisplayName());
        i = d.findFieldItem("spin2");
        System.out.println(i.getSpinnerMax());
    }
    
    @Test
    public void test2() {
        try {
            DialogFormat d = findDialog("test84.xml");
        } catch (JythonUIFatal e) {
            // expected
            return;
        }
        // failure
        fail();
    }

}
