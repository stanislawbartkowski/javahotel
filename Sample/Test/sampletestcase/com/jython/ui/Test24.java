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

import java.util.Date;

import org.junit.Test;

import com.jython.ui.server.datastore.IDateLineElem;

public class Test24 extends TestHelper {

    @Test
    public void test1() {
        iOp.clearAll();
        Long id = new Long(1);
        Date da = getD(2012, 2, 4);
        IDateLineElem e = iOp.findElem(id, da);
        assertNull(e);
        iOp.addormodifElem(id, da, 5, "rybka");
        e = iOp.findElem(id, da);
        assertNotNull(e);
        assertEquals(5, e.getNumb());
        assertEquals("rybka", e.getInfo());
        iOp.removeElem(id, da);
        e = iOp.findElem(id, da);
        assertNull(e);
    }

    @Test
    public void test2() {
        iOp.clearAll();
        Date da = getD(2012, 2, 4);
        Date dt = da;
        for (int i = 0; i < 100; i++) {
            Long id = new Long(1);
            iOp.addormodifElem(id, dt, i * 2, "Hello");
            dt = incDay(dt);
        }
        dt = da;
        int no = 0;
        for (int i = 0; i < 100; i++) {
            Long id = new Long(1);
            IDateLineElem el = iOp.findElem(id, dt);
            dt = incDay(dt);
            assertNotNull(el);
            no = i;
        }
        assertEquals(99, no);
        Long id = new Long(1);
        IDateLineElem el = iOp.findElem(id, da);
        assertNotNull(el);
        iOp.addormodifElem(id, da, el.getNumb(), "Ciao");
        el = iOp.findElem(id, da);
        assertNotNull(el);
        assertEquals("Ciao", el.getInfo());
    }

}
