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

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import org.junit.Test;

import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldValue;

public class Test47 extends TestHelper {

    @Test
    public void test1() {

        DialogFormat d = findDialog("test94.xml");
        assertNotNull(d);
        BigDecimal b = new BigDecimal(1234567890123456.1234);
        DialogVariables v = new DialogVariables();
        FieldValue val = new FieldValue();
        val.setValue(b, 4);
        v.setValue("bignumber", val);
        runAction(v, "test94.xml", "run");
        FieldValue val1 = v.getValue("out");
        assertNotNull(val1);
        System.out.println(val1.getValueBD());
        // fraction part is lost
        assertEqB(1234567890123456.0, val1.getValueBD());
    }

    @Test
    public void test2() {
        String t = authenticateToken(realmIni, "guest", "guest");
        assertNotNull(t);
        BigDecimal b = new BigDecimal(1234567890123456.1234);
        DialogVariables v = new DialogVariables();
        FieldValue val = new FieldValue();
        val.setValue(b, 4);
        v.setValue("bignumber", val);
        runAction(t, v, "test94.xml", "save");
        v = new DialogVariables();
        runAction(t, v, "test94.xml", "restore");
        FieldValue val1 = v.getValue("out");
        assertNotNull(val1);
        System.out.println(val1.getValueBD());
        // fraction part is lost
        assertEqB(1234567890123456.0, val1.getValueBD());
    }
}
