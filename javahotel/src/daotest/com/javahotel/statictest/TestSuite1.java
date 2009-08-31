/*
 * Copyright 2009 stanislawbartkowski@gmail.com
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

import com.javahotel.common.dateutil.CalendarTable;
import com.javahotel.common.dateutil.CalendarTable.PeriodType;
import com.javahotel.common.dateutil.CountPixel;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.dateutil.DateUtil;
import com.javahotel.common.dateutil.GetPeriods;
import com.javahotel.common.dateutil.PeriodT;
import com.javahotel.common.dateutil.GetPeriods.StartWeek;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestSuite1 {

    public TestSuite1() {
    }

    private static Date createD(int year, int mo, int da) {
        return TestDUtil.createD(year, mo, da);
    }

    private void eqD(final Date d, int year, int mo, int da) {
        Date dx = createD(year, mo, da);
        assertEquals(0, DateUtil.compareDate(d, dx));
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void Test1() throws Exception {
        System.out.println("Basic Test");
        Date dfrom = createD(2008, 8, 1);
        Date to = createD(2009, 7, 31);
        List<Date> col = CalendarTable.listOfDates(dfrom, to, PeriodType.byMonth);
        int no = 0;
        for (final Date d : col) {
            String s = DateFormatUtil.toS(d);
            System.out.println(s);
            switch (no) {
                case 0:
                    eqD(d, 2008, 8, 31);
                    break;
                case 1:
                    eqD(d, 2008, 9, 30);
                    break;
                case 2:
                    eqD(d, 2008, 10, 31);
                    break;
                case 3:
                    eqD(d, 2008, 11, 30);
                    break;
                case 4:
                    eqD(d, 2008, 12, 31);
                    break;
                case 5:
                    eqD(d, 2009, 1, 31);
                    break;
                case 6:
                    eqD(d, 2009, 2, 28);
                    break;
                case 7:
                    eqD(d, 2009, 3, 31);
                    break;
                case 8:
                    eqD(d, 2009, 4, 30);
                    break;
                case 9:
                    eqD(d, 2009, 5, 31);
                    break;
                case 10:
                    eqD(d, 2009, 6, 30);
                    break;
                case 11:
                    eqD(d, 2009, 7, 31);
                    break;
                default:
                    fail();
            }
            no++;
        }
    }

    private static PeriodT createP(int y1, int m1, int d1, int y2, int m2, int d2) {
        Date dd1 = createD(y1, m1, d1);
        Date dd2 = createD(y2, m2, d2);
        PeriodT p = new PeriodT(dd1, dd2, dd1); // to be not null
        return p;
    }
    
    private void drawP(PeriodT p) {
        TestDUtil.drawP(p);
    }

    private void checkP(PeriodT p, int y1, int m1, int d1, int y2, int m2, int d2) {
        TestDUtil.checkP(p, y1, m1, d1, y2, m2, d2);
    }

    @Test
    public void Test2() throws Exception {
        PeriodT pe = new PeriodT(createD(2008, 8, 1), createD(2008, 8, 31), null);
        List<PeriodT> col = new ArrayList<PeriodT>();
        col.add(createP(2008, 8, 1, 2008, 8, 2));
        List<PeriodT> out = GetPeriods.get(pe, col);
        int no = 0;
        for (PeriodT p : out) {
            drawP(p);
            switch (no) {
                case 0:
                    assertNotNull(p.getI());
                    checkP(p, 2008, 8, 1, 2008, 8, 2);
                    break;
                case 1:
                    assertNull(p.getI());
                    checkP(p, 2008, 8, 3, 2008, 8, 31);
                    break;
                default:
                    fail();
                    break;
            }
            no++;
        }
    }

    @Test
    public void Test3() throws Exception {
        PeriodT pe = new PeriodT(createD(2008, 8, 1), createD(2008, 8, 31), null);
        List<PeriodT> col = new ArrayList<PeriodT>();
        col.add(createP(2008, 8, 10, 2008, 8, 19));
        List<PeriodT> out = GetPeriods.get(pe, col);
        int no = 0;
        for (PeriodT p : out) {
            drawP(p);
            switch (no) {
                case 0:
                    assertNull(p.getI());
                    checkP(p, 2008, 8, 1, 2008, 8, 9);
                    break;
                case 1:
                    assertNotNull(p.getI());
                    checkP(p, 2008, 8, 10, 2008, 8, 19);
                    break;
                case 2:
                    assertNull(p.getI());
                    checkP(p, 2008, 8, 20, 2008, 8, 31);
                    break;
                default:
                    fail();
                    break;
            }
            no++;
        }
    }
    
    @Test
    public void Test4() throws Exception {
        List<Integer> inte = new ArrayList<Integer>();
        inte.add(new Integer(7));
        inte.add(new Integer(24));
        int[] res = CountPixel.countP(91, 31, inte);
        for (int i = 0; i < res.length; i++) {
            System.out.println(i + " = " + res[i]);
            switch (i) {
                case 0:
                    assertEquals(21, res[i]);
                    break;
                case 1:
                    assertEquals(70, res[i]);
                    break;
                default:
                    fail();
            }
        }
        
    }
    
    @Test
    public void Test5() throws Exception {
        List<PeriodT> out = GetPeriods.listOfWeekends(new PeriodT(createD(2008, 8, 1), createD(2008, 8, 31), null),StartWeek.onSaturday);
        int i = 0;
        for (PeriodT pe : out) {
            drawP(pe);
            switch (i) {
                case 0:
                    checkP(pe, 2008, 8, 2, 2008, 8, 3);
                    break;
                case 1:
                    checkP(pe, 2008, 8, 9, 2008, 8, 10);
                    break;
                case 2:
                    checkP(pe, 2008, 8, 16, 2008, 8, 17);
                    break;
                case 3:
                    checkP(pe, 2008, 8, 23, 2008, 8, 24);
                    break;
                case 4:
                    checkP(pe, 2008, 8, 30, 2008, 8, 31);
                    break;
                default:
                    fail();
            }
            i++;
        }
        assertEquals(5, out.size());

        System.out.println("--");
        out = GetPeriods.listOfWeekends(new PeriodT(createD(2008, 8, 3), createD(2008, 8, 30), null),StartWeek.onSaturday);
        i = 0;
        for (PeriodT pe : out) {
            drawP(pe);
            switch (i) {
                case 0:
                    checkP(pe, 2008, 8, 3, 2008, 8, 3);
                    break;
                case 1:
                    checkP(pe, 2008, 8, 9, 2008, 8, 10);
                    break;
                case 2:
                    checkP(pe, 2008, 8, 16, 2008, 8, 17);
                    break;
                case 3:
                    checkP(pe, 2008, 8, 23, 2008, 8, 24);
                    break;
                case 4:
                    checkP(pe, 2008, 8, 30, 2008, 8, 30);
                    break;
                default:
                    fail();
            }
            i++;
        }
        assertEquals(5, out.size());
    }
    
    @Test
    public void Test6() {
        System.out.println("by Day");
        Date dfrom = createD(2008, 8, 1);
        Date to = createD(2008, 8, 5);
        List<Date> col = CalendarTable.listOfDates(dfrom, to, PeriodType.byDay);
        for (final Date d : col) {
            String s = DateFormatUtil.toS(d);
            System.out.println(s);
        }
        assertEquals(5, col.size());

    }
}
