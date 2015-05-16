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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import org.junit.Test;

import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldValue;

/**
 * @author hotel
 *
 */
public class Test3 extends TestHelper {
    
    @Test
    public void test1() {
        DialogVariables v = new DialogVariables();
        FieldValue val = new FieldValue();
        val.setValue(new BigDecimal(1.1),4);
        v.setValue("globdecimal", val);
        runAction(v, "test6.xml", "name");
        val = v.getValue("globdecimal");
        assertNotNull(val);
        equalB(1.1,val.getValueBD(),4);
        
        runAction(v, "test6.xml", "setInt");
        val = v.getValue("globdecimal");
        assertNotNull(val);
        equalB(101,val.getValueBD(),4);
        
        runAction(v, "test6.xml", "setFloat");
        val = v.getValue("globdecimal");
        assertNotNull(val);
        equalB(101.84,val.getValueBD(),4);
    }

    @Test
    public void test2() {
        DialogVariables v = new DialogVariables();
        FieldValue val = new FieldValue();
        val.setValue((Date)null);
        v.setValue("globdate", val);
        runAction(v, "test7.xml", "name");
        val = v.getValue("globdate");
        assertNotNull(val);
        assertNull(val.getValueD());
    }
    
    @Test
    public void test3() {
        DialogVariables v = new DialogVariables();
        FieldValue val = new FieldValue();
        Date da = new Date();
        da.setYear(99); da.setMonth(1); da.setDate(10);
        val.setValue(da);
        v.setValue("globdate", val);
        runAction(v, "test7.xml", "name");
        val = v.getValue("globdate");
        assertNotNull(val);
        assertNotNull(val.getValueD());
        assertEquals(99,val.getValueD().getYear());
        assertEquals(1,val.getValueD().getMonth());
        assertEquals(10,val.getValueD().getDate());
    }
    
    @Test
    public void test4() {
        DialogVariables v = new DialogVariables();
        FieldValue val = new FieldValue();
        Date da = new Date();
        da.setYear(99); da.setMonth(1); da.setDate(10);
        val.setValue(da);
        v.setValue("globdate", val);
        runAction(v, "test7.xml", "setDate");
        val = v.getValue("globdate");
        assertNotNull(val);
        assertNotNull(val.getValueD());
        assertEquals(101,val.getValueD().getYear());
        assertEquals(10,val.getValueD().getMonth());
        assertEquals(5,val.getValueD().getDate());
    }

    @Test
    public void test5() {
        DialogVariables v = new DialogVariables();
        FieldValue val = new FieldValue();
        Date da = new Date();
        da.setYear(99); da.setMonth(1); da.setDate(10);
        da.setHours(10); da.setMinutes(9); da.setSeconds(22);
        Timestamp t = new Timestamp(da.getTime());
        val.setValue(t);
        v.setValue("globtimestamp", val);
        runAction(v, "test8.xml", "name");
        val = v.getValue("globtimestamp");
        assertNotNull(val);
        assertNotNull(val.getValueT());
        assertEquals(99,val.getValueT().getYear());
        assertEquals(1,val.getValueT().getMonth());
        assertEquals(10,val.getValueT().getDate());
        
        assertEquals(10,val.getValueT().getHours());
        assertEquals(9,val.getValueT().getMinutes());
        assertEquals(22,val.getValueT().getSeconds());
    }

    @Test
    public void test6() {
        DialogVariables v = new DialogVariables();
        FieldValue val = new FieldValue();
        Date da = new Date();
        da.setYear(99); da.setMonth(1); da.setDate(10);
        Timestamp ta = new Timestamp(da.getTime());
        ta.setHours(11);
        ta.setMinutes(17);
        ta.setSeconds(31);
        val.setValue(ta);
        v.setValue("globtimestamp", val);
        runAction(v, "test8.xml", "name");
        val = v.getValue("globtimestamp");
        assertNotNull(val);
        assertNotNull(val.getValueT());
        assertEquals(99,val.getValueT().getYear());
        assertEquals(1,val.getValueT().getMonth());
        assertEquals(10,val.getValueT().getDate());
        assertEquals(11,val.getValueT().getHours());
        assertEquals(17,val.getValueT().getMinutes());
        assertEquals(31,val.getValueT().getSeconds());
    }


    @Test
    public void test7() {
        DialogVariables v = new DialogVariables();
        FieldValue val = new FieldValue();
        val.setValue((Timestamp)null);
        v.setValue("globtimestamp", val);
        runAction(v, "test8.xml", "setDateTimeOnly");
        val = v.getValue("globtimestamp");
        assertNotNull(val);
        assertNotNull(val.getValueT());
        assertEquals(113,val.getValueT().getYear());
        assertEquals(0,val.getValueT().getMonth());
        assertEquals(13,val.getValueT().getDate());
    }

    @Test
    public void test8() {
        DialogVariables v = new DialogVariables();
        FieldValue val = new FieldValue();
        val.setValue((Timestamp)null);
        v.setValue("globtimestamp", val);
        runAction(v, "test8.xml", "setTimeOnly");
        val = v.getValue("globtimestamp");
        assertNotNull(val);
        assertNotNull(val.getValueT());
        printD(val.getValueT());
        assertEquals(117,val.getValueT().getYear());
        assertEquals(0,val.getValueT().getMonth());
        assertEquals(13,val.getValueT().getDate());
        assertEquals(12,val.getValueT().getHours());
        assertEquals(13,val.getValueT().getMinutes());
        assertEquals(14,val.getValueT().getSeconds());
    }
    
    @Test
    public void test9() {
        DialogVariables v = new DialogVariables();
        FieldValue val = new FieldValue();
        Date da = new Date();
        // check hours 23
        da.setYear(99); da.setMonth(1); da.setDate(10);
        da.setHours(23); da.setMinutes(9); da.setSeconds(22);
        Timestamp t = new Timestamp(da.getTime());
        val.setValue(t);
        v.setValue("globtimestamp", val);
        runAction(v, "test8.xml", "name");
        val = v.getValue("globtimestamp");
        assertNotNull(val);
        assertNotNull(val.getValueT());
        assertEquals(99,val.getValueT().getYear());
        assertEquals(1,val.getValueT().getMonth());
        assertEquals(10,val.getValueT().getDate());
        
        assertEquals(23,val.getValueT().getHours());
        assertEquals(9,val.getValueT().getMinutes());
        assertEquals(22,val.getValueT().getSeconds());
    }
    
    @Test
    public void test10() {
        DialogVariables v = new DialogVariables();
        FieldValue val = new FieldValue();
        val.setValue((Timestamp)null);
        v.setValue("globtimestamp", val);
        runAction(v, "test8.xml", "setTimeOnly23");
//        val = v.getValue("globtimestamp1");
//        printD(val.getValueT());
        val = v.getValue("globtimestamp");
        assertNotNull(val);
        assertNotNull(val.getValueT());
        printD(val.getValueT());
        assertEquals(101,val.getValueT().getYear());
        assertEquals(9,val.getValueT().getMonth());
        assertEquals(2,val.getValueT().getDate());
        assertEquals(23,val.getValueT().getHours());
        assertEquals(4,val.getValueT().getMinutes());
        assertEquals(6,val.getValueT().getSeconds());
        
        for (int i = 1; i<=12; i++) {
        	System.out.println(i+ " = ******************");
            val = v.getValue("globtimestamp" + i);
            printD(val.getValueT());
            assertEquals(101,val.getValueT().getYear());
            assertEquals(i-1,val.getValueT().getMonth());
            assertEquals(2,val.getValueT().getDate());
            assertEquals(23,val.getValueT().getHours());
            assertEquals(4,val.getValueT().getMinutes());
            assertEquals(6,val.getValueT().getSeconds());                    	
        }
    }



}
