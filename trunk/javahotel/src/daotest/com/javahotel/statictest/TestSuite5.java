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
package com.javahotel.statictest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.gwtmodel.table.common.ISignal;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.dateutil.PeriodT;
import com.javahotel.common.rescache.ReadResParam;
import com.javahotel.common.rescache.ResObjectCache;
import com.javahotel.common.rescache.ResObjectCache.IReadResCallBack;
import com.javahotel.common.rescache.ResObjectCache.IReadResData;
import com.javahotel.common.rescache.ResObjectCache.ISetResState;
import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.common.toobject.ResDayObjectStateP;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite5 {

    @Test
    public void Test1() {
        Date d1 = TestDUtil.createD(2008, 10, 1);
        Date d2 = TestDUtil.createD(2008, 10, 2);
        List<String> col = new ArrayList<String>();
        col.add("1p");
        ReadResParam r = new ReadResParam(col, new PeriodT(d1,d2));
        IReadResData ir = new IReadResData() {

            public int no = 0;

            public void getResData(ReadResParam pa, IReadResCallBack col) {
                assertEquals(0, no);
                System.out.println(pa.getResList());
                TestDUtil.drawP(pa.getPe());
                String sx = null;
                for (final String s : pa.getResList()) {
                    sx = s;
                }
                assertEquals("1p", sx);
                assertEquals("2008/10/01", DateFormatUtil.toS(pa.getPe().getFrom()));
                assertEquals("2008/10/02", DateFormatUtil.toS(pa.getPe().getTo()));
                List<ResDayObjectStateP> out = new ArrayList<ResDayObjectStateP>();
                ResDayObjectStateP re = new ResDayObjectStateP();
                re.setD(TestDUtil.createD(2008, 10, 1));
                re.setResObject("1p");
                BookingStateP bo = new BookingStateP();
                out.add(re);
                re.setLState(bo);
                col.setCol(out);
                no++;
            }
        };

        ResObjectCache ca = new ResObjectCache(ir);

        ISetResState iget = new ISetResState() {

            public void setResState(ResDayObjectStateP p) {
                System.out.println(p.getLState());
                assertNotNull(p.getLState());
            }
        };

        ISetResState igetn = new ISetResState() {

            public void setResState(ResDayObjectStateP p) {
                System.out.println(p.getLState());
                assertNull(p.getLState());
            }
        };

        ca.getResState("1p", d1, r, iget);
        ca.getResState("1p", d2, r, igetn);
    }

    private class readRes implements IReadResData {

        public int no;
        String checkFrom;
        String checkTo;

        public void getResData(ReadResParam pa, IReadResCallBack col) {
            switch (no) {
                case 0:
                    System.out.println(pa.getResList());
                    TestDUtil.drawP(pa.getPe());
                    String sx = null;
                    for (final String s : pa.getResList()) {
                        sx = s;
                    }
                    assertEquals("1p", sx);
                    assertEquals("2008/10/01", DateFormatUtil.toS(pa.getPe().getFrom()));
                    assertEquals("2008/10/02", DateFormatUtil.toS(pa.getPe().getTo()));
                    List<ResDayObjectStateP> out = new ArrayList<ResDayObjectStateP>();
                    ResDayObjectStateP re = new ResDayObjectStateP();
                    re.setD(TestDUtil.createD(2008, 10, 1));
                    re.setResObject("1p");
                    BookingStateP bo = new BookingStateP();
                    out.add(re);
                    re.setLState(bo);
                    col.setCol(out);
                    break;
                case 1:
                    System.out.println(pa.getResList());
                    TestDUtil.drawP(pa.getPe());
                    assertEquals("2008/10/03", DateFormatUtil.toS(pa.getPe().getFrom()));
                    assertEquals("2008/10/10", DateFormatUtil.toS(pa.getPe().getTo()));
                    out = new ArrayList<ResDayObjectStateP>();
                    re = new ResDayObjectStateP();
                    re.setD(TestDUtil.createD(2008, 10, 9));
                    re.setResObject("1p");
                    bo = new BookingStateP();
                    out.add(re);
                    re.setLState(bo);
                    col.setCol(out);
                    break;
                case 2:
                    System.out.println(pa.getResList());
                    System.out.println(pa.getPe().getFrom());
                    System.out.println(pa.getPe().getTo());
                    assertEquals(checkFrom, DateFormatUtil.toS(pa.getPe().getFrom()));
                    assertEquals(checkTo, DateFormatUtil.toS(pa.getPe().getTo()));
                    out = new ArrayList<ResDayObjectStateP>();
                    col.setCol(out);
                    break;
            }
        }
    }

    @Test
    public void Test2() {
        Date d1 = TestDUtil.createD(2008, 10, 1);
        Date d2 = TestDUtil.createD(2008, 10, 2);
        List<String> col = new ArrayList<String>();
        col.add("1p");
        ReadResParam r = new ReadResParam(col, new PeriodT(d1,d2));
        readRes ir = new readRes();
        ir.no = 0;
        ResObjectCache ca = new ResObjectCache(ir);

        ISetResState iget = new ISetResState() {

            public void setResState(ResDayObjectStateP p) {
                System.out.println(p.getLState());
                assertNotNull(p.getLState());
            }
        };

        ISetResState igetn = new ISetResState() {

            public void setResState(ResDayObjectStateP p) {
                System.out.println(p.getLState());
                assertNull(p.getLState());
            }
        };

        ca.getResState("1p", d1, r, iget);
        ca.getResState("1p", d2, r, igetn);
        
        
        Date d3 = TestDUtil.createD(2008, 10, 10);
        r = new ReadResParam(col, new PeriodT(d1,d3));
        ir.no = 1;
        ca.getResState("1p", d3, r, igetn);
        ca.getResState("1p", TestDUtil.createD(2008, 10, 9), r, iget);
        
        d1 = TestDUtil.createD(2008, 9, 20);
        d2 = TestDUtil.createD(2008, 10, 11);
        r = new ReadResParam(col, new PeriodT(d1,d2));
        ir.no = 2;
        ir.checkFrom = "2008/09/20";
        ir.checkTo = "2008/10/11";
        ca.getResState("1p", d2, r, igetn);        
        
        d1 = TestDUtil.createD(2008, 9, 15);
        d2 = TestDUtil.createD(2008, 10, 11);
        r = new ReadResParam(col, new PeriodT(d1,d2));
        ir.no = 2;
        ir.checkFrom = "2008/09/15";
        ir.checkTo = "2008/09/19";
        ca.getResState("1p", d1, r, igetn);        

    }
    
    private class readRes3 implements IReadResData {

        public int no;
        String checkFrom;
        String checkTo;
        String lastFrom = null;
        String lastTo = null;
        
        void checkDa() {
            assertNotNull(lastFrom);
            assertNotNull(lastTo);
            assertEquals(checkFrom,lastFrom);
            assertEquals(checkTo,lastTo);
        }

        public void getResData(ReadResParam pa, IReadResCallBack col) {
            switch (no) {
                case 0:
                    System.out.println(pa.getResList());
                    System.out.println(pa.getPe().getFrom());
                    System.out.println(pa.getPe().getTo());
                    String sx = null;
                    for (final String s : pa.getResList()) {
                        sx = s;
                    }
                    List<ResDayObjectStateP> out = new ArrayList<ResDayObjectStateP>();
                    ResDayObjectStateP re = new ResDayObjectStateP();
                    re.setD(TestDUtil.createD(2008, 10, 1));
                    re.setResObject("1p");
                    BookingStateP bo = new BookingStateP();
                    out.add(re);
                    re.setLState(bo);
                    col.setCol(out);
                    break;
                case 1:
                    System.out.println(pa.getResList());
                    System.out.println(pa.getPe().getFrom());
                    System.out.println(pa.getPe().getTo());
                    out = new ArrayList<ResDayObjectStateP>();
                    re = new ResDayObjectStateP();
                    re.setD(TestDUtil.createD(2008, 10, 6));
                    re.setResObject("2p");
                    bo = new BookingStateP();
                    out.add(re);
                    re.setLState(bo);
//                    re = new ResDayObjectStateP();
//                    re.setD(TestDUtil.createD(2008, 10, 9));
//                    re.setResObject("1p");
//                    bo = new BookingStateP();
//                    out.add(re);
                    re.setLState(bo);
                    col.setCol(out);
                    break;
                case 2:
                    System.out.println(pa.getResList());
                    System.out.println(pa.getPe().getFrom());
                    System.out.println(pa.getPe().getTo());
                    lastFrom = DateFormatUtil.toS(pa.getPe().getFrom());
                    lastTo = DateFormatUtil.toS(pa.getPe().getTo());
                    assertEquals(checkFrom, DateFormatUtil.toS(pa.getPe().getFrom()));
                    assertEquals(checkTo, DateFormatUtil.toS(pa.getPe().getTo()));
                    out = new ArrayList<ResDayObjectStateP>();
                    col.setCol(out);
                    break;
                case 3:
                    fail("Cannot be here");
                    break;
            }
        }
    }
    
   @Test
    public void Test3() {
        Date d1 = TestDUtil.createD(2008, 10, 1);
        Date d2 = TestDUtil.createD(2008, 10, 2);
        List<String> col = new ArrayList<String>();
        col.add("1p");
        ReadResParam r = new ReadResParam(col, new PeriodT(d1,d2));
        readRes3 ir = new readRes3();
        ir.no = 0;
        ResObjectCache ca = new ResObjectCache(ir);

        ISetResState iget = new ISetResState() {

            public void setResState(ResDayObjectStateP p) {
                System.out.println(p.getLState());
                assertNotNull(p.getLState());
            }
        };

        ISetResState igetn = new ISetResState() {

            public void setResState(ResDayObjectStateP p) {
                System.out.println(p.getLState());
                assertNull(p.getLState());
            }
        };
        ca.getResState("1p", d1, r, iget);
        ca.getResState("1p", d2, r, igetn);
        
        col = new ArrayList<String>();
        col.add("2p");
        d1 = TestDUtil.createD(2008, 10,5);
        d2 = TestDUtil.createD(2008, 10, 6);
        r = new ReadResParam(col, new PeriodT(d1,d2));
        ir.no = 1;
        ca.getResState("2p", d1, r, igetn);
        // TODO
        col = new ArrayList<String>();
        col.add("1p");
        col.add("2p");
        d1 = TestDUtil.createD(2008, 10, 2);
        d2 = TestDUtil.createD(2008, 10, 6);
        r = new ReadResParam(col, new PeriodT(d1,d2));
        ir.no = 2;
        ir.checkFrom = "2008/10/02";
        ir.checkTo = "2008/10/06";
        ca.getResState("1p", d2, r, igetn);
        
        d1 = TestDUtil.createD(2008, 9,15);
        d2 = TestDUtil.createD(2008, 10, 6);
        r = new ReadResParam(col, new PeriodT(d1,d2));
        ir.no = 2;
        ir.checkFrom = "2008/09/15";
        ir.checkTo = "2008/10/01";
        ca.getResState("1p", d1, r, igetn);
    }
    
    @Test
    public void Test4() {
        Date d1 = TestDUtil.createD(2008, 10, 1);
        Date d2 = TestDUtil.createD(2008, 10, 2);
        List<String> col = new ArrayList<String>();
        col.add("1p");
        ReadResParam r = new ReadResParam(col, new PeriodT(d1,d2));
        readRes3 ir = new readRes3();
        ResObjectCache ca = new ResObjectCache(ir);
        ISignal i = new ISignal() {

            public void signal() {
                System.out.println("Signal");
            }
            
        };
        ir.no = 2;
        ir.checkFrom = "2008/10/01";
        ir.checkTo = "2008/10/02";
        ca.ReadResState(r, i);
        ir.no = 3;
        ca.ReadResState(r, i);
        
    }
    
    @Test
    public void Test5() {
        Date d1 = TestDUtil.createD(2008, 10, 1);
        Date d2 = TestDUtil.createD(2008, 10, 2);
        List<String> col = new ArrayList<String>();
        col.add("1p");
        ReadResParam r = new ReadResParam(col, new PeriodT(d1,d2));
        readRes3 ir = new readRes3();
        ResObjectCache ca = new ResObjectCache(ir);
        ISignal i = new ISignal() {

            public void signal() {
                System.out.println("Signal");
            }
            
        };
        ir.no = 2;
        ir.checkFrom = "2008/10/01";
        ir.checkTo = "2008/10/02";
        ca.ReadResState(r, i);
        
        ir.no = 2;
        ir.checkFrom = "2008/10/09";
        ir.checkTo = "2008/10/10";
        d1 = TestDUtil.createD(2008, 10, 9);
        d2 = TestDUtil.createD(2008, 10, 10);
        r = new ReadResParam(col, new PeriodT(d1,d2));
        ca.ReadResState(r, i);
        
        ir.no = 2;
        ir.checkFrom = "2008/10/05";
        ir.checkTo = "2008/10/06";
        ir.lastFrom = null;
        ir.lastTo = null;                
        d1 = TestDUtil.createD(2008, 10, 5);
        d2 = TestDUtil.createD(2008, 10, 6);
        r = new ReadResParam(col, new PeriodT(d1,d2));
        ca.ReadResState(r, i);
        ir.checkDa();
        
        ir.no = 2;
        ir.checkFrom = "2008/10/05";
        ir.checkTo = "2008/10/06";
        ir.lastFrom = null;
        ir.lastTo = null;                
        d1 = TestDUtil.createD(2008, 10, 5);
        d2 = TestDUtil.createD(2008, 10, 6);
        r = new ReadResParam(col, new PeriodT(d1,d2));
        ca.ReadResState(r, i);
        assertNull(ir.lastFrom);
        assertNull(ir.lastTo);
        
    }
}
