/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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

import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldValue;
import static org.junit.Assert.*;

/**
 * @author hotel
 * 
 */
public class Test2 extends TestHelper {

    @Test
    public void test1() {
        DialogVariables v = new DialogVariables();
        runAction(v, "test3.xml", "before");
        FieldValue val = v.getValue("glob1");
        assertNotNull(val);
        assertEquals(val.getValueS(), "aaaa");
        val = v.getValue("JCOPY_glob1");
        assertNotNull(val);
        assertNotNull(val.getValueB());
        assertTrue(val.getValueB());
    }

    @Test
    public void test2() {
        DialogVariables v = new DialogVariables();
        FieldValue val = new FieldValue();
        val.setValue((Integer) null);
        v.setValue("globint", val);
        runAction(v, "test4.xml", "before");
        val = v.getValue("globint");
        assertNotNull(val);
        assertNull(val.getValueI());
    }

    @Test
    public void test3() {
        DialogVariables v = new DialogVariables();
        FieldValue val = new FieldValue();
        val.setValue((Integer) null);
        v.setValue("globint", val);
        runAction(v, "test4.xml", "testnone");
        val = v.getValue("result");
        assertNotNull(val);
        assertEquals("IsNone", val.getValueS());
    }

    @Test
    public void test4() {
        DialogVariables v = new DialogVariables();
        FieldValue val = new FieldValue();
        val.setValue((Integer) 1);
        v.setValue("globint", val);
        runAction(v, "test4.xml", "inc");
        val = v.getValue("globint");
        assertNotNull(val);
        assertEquals((Integer) 2, val.getValueI());
    }

    @Test
    public void test5() {
        DialogVariables v = new DialogVariables();
        FieldValue val = new FieldValue();
        val.setValue((Long) 1l);
        v.setValue("globlong", val);
        runAction(v, "test5.xml", "retlong");
        val = v.getValue("globlong");
        assertNotNull(val);
        assertEquals((Long) 1l, val.getValueL());
    }

    @Test
    public void test6() {
        DialogVariables v = new DialogVariables();
        FieldValue val = new FieldValue();
        val.setValue((Long) 1l);
        v.setValue("globlong", val);
        runAction(v, "test5.xml", "setvalue");
        val = v.getValue("globlong");
        assertNotNull(val);
        assertEquals((Long) 99l, val.getValueL());
    }

}