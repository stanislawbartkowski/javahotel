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

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.jython.ui.server.datastore.IDateRecord;

public class Test26 extends TestHelper {

    @Test
    public void test1() {
        dOp.clearAll();
        assertTrue(dOp.getList().isEmpty());
        IDateRecord iRec = dOp.construct();
        Date d1 = getD(2013, 10, 2);
        iRec.setDates(d1, null);
        Long id = dOp.addRecord(iRec);
        assertNotNull(id);
        System.out.println(id);
        iRec = dOp.findRecord(id);
        assertNotNull(iRec);
        assertNull(iRec.getD2());
        assertNotNull(iRec.getD1());
        Date d2 = getD(2013, 10, 3);
        iRec.setDates(d1, d2);
        dOp.changeRecord(iRec);
        iRec = dOp.findRecord(id);
        assertNotNull(iRec);
        assertNotNull(iRec.getD1());        
        assertNotNull(iRec.getD2());
    }
    
    @Test
    public void test2() {
        dOp.clearAll();
        for (int i=1; i<=20; i++) {
            Date d1 = getD(2013, 10, i);
            Date d2 = getD(2013, 10, i+1);
            IDateRecord iRec = dOp.construct();
            iRec.setDates(d1, d2);
            dOp.addRecord(iRec);
        }
        List<IDateRecord> dList = dOp.getList();
        assertEquals(20,dList.size());
        Long id = dList.get(10).getId();
        dOp.removeRecord(id);
        dList = dOp.getList();
        assertEquals(19,dList.size());
        IDateRecord re = dOp.findRecord(id);
        assertNull(re);
    }
   
}
