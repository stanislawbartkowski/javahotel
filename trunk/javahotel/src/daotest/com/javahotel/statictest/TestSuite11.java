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

//import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.gwtmodel.table.common.dateutil.DateFormatUtil;
import com.gwtmodel.table.common.dateutil.DateUtil;
import com.gwtmodel.table.daytimeline.DaySeasonScrollData;
import com.gwtmodel.table.daytimeline.MonthSeasonScrollData;
import com.gwtmodel.table.daytimeline.MoveSkip;
import com.gwtmodel.table.daytimeline.PanelDesc;
import com.gwtmodel.table.daytimeline.YearMonthPe;
import com.javahotel.common.dateutil.CalendarTable;
import com.javahotel.common.dateutil.CalendarTable.PeriodType;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite11 {

    /**
     * Test scroll month for year: 01.01 - 12.20
     * Step1: create panel for tha period
     * Expected result 1:
     *   first and last in pane: 0-5
     * Expected result 2:
     *   number of days for first (January) is 31
     * Expected result 3:
     *   last (June) number of days is 30 and month is 6
     * Expected result 4:
     *   number of days December is 20 (partial period)
     * Expected result 5:
     *   Left not active, right active
     * Expected result 6:
     *   Width of moth panel is 6
     */
    @Test
    public void Test1() {
        MonthSeasonScrollData da = new MonthSeasonScrollData();
        Date dF = DateFormatUtil.toD(2009, 1, 1);
        Date dT = DateFormatUtil.toD(2009, 12, 20);
        // Step1
        List<Date> dList = CalendarTable.listOfDates(dF, dT, PeriodType.byDay);
        da.createVPanel(dList, 0);
        int firstP = da.getFirstP();
        int lastP = da.getLastP();
        // Expected result 1
        assertEquals(0, firstP);
        assertEquals(5, lastP);
        YearMonthPe f = da.getPe(0);
        // Expected result 2
        assertEquals(31, f.getNoDays());
        YearMonthPe l = da.getPe(5);
        // Expected result 3
        assertEquals(6, l.getMonth());
        assertEquals(30, l.getNoDays());
        YearMonthPe ll = da.getPe(11);
        // Expected result 4
        assertEquals(20, ll.getNoDays());
        PanelDesc s = da.getMonthScrollStatus();
        // Expected result 5
        assertFalse(s.isScrollLeftActive());
        assertTrue(s.isScrollRightActive());
        // Expected result 6
        assertEquals(6, da.getMonthPe());

    }

    /**
     * Test scroll for short period in year boundaries
     * Step 1:
     *   Create scroll for 2009/12/01 2010/01/31
     * Expected result 1:
     *   First and last are 0 and 1
     * Expected result 2:
     *   Year of the first is 2009, the last is 2010
     * Expected result 3:
     *   Both scrolls are inactive
     */
    @Test
    public void Test2() {
        MonthSeasonScrollData da = new MonthSeasonScrollData();
        Date dF = DateFormatUtil.toD(2009, 12, 1);
        Date dT = DateFormatUtil.toD(2010, 01, 31);
        // Step 1
        List<Date> dList = CalendarTable.listOfDates(dF, dT, PeriodType.byDay);
        da.createVPanel(dList, 0);
        int firstP = da.getFirstP();
        int lastP = da.getLastP();
        // Expected result 1
        assertEquals(0, firstP);
        assertEquals(1, lastP);
        YearMonthPe f = da.getPe(0);
        YearMonthPe l = da.getPe(1);
        // Expected result 2
        assertEquals(2009, f.getYear());
        assertEquals(2010, l.getYear());
        PanelDesc s = da.getMonthScrollStatus();
        // Expected result 3
        assertFalse(s.isScrollLeftActive());
        assertFalse(s.isScrollRightActive());
        assertEquals(2, da.getMonthPe());
    }

    /**
     * Test scroll going to
     * Step1:
     *   Create scroll 2009/1/1 - 2009/12/20
     *   Create for starting day 32
     * Expected result 1
     *   Actual month 1
     *   Status left : no active
     *   Status right: active
     * Step 2:
     *   Goto 2009/11/20
     * Expected result 2
     *   Panel from 6-11 index
     *   Status left: active
     *   Status right: no active  (hit 11 index, 12 month December)
     */
    @Test
    public void Test3() {
        MonthSeasonScrollData da = new MonthSeasonScrollData();
        Date dF = DateFormatUtil.toD(2009, 1, 1);
        Date dT = DateFormatUtil.toD(2009, 12, 20);
        // Step 1
        List<Date> dList = CalendarTable.listOfDates(dF, dT, PeriodType.byDay);
        da.createVPanel(dList, 31);
        // Expected result 1
        assertEquals(1, da.getIntP());
        PanelDesc s = da.getMonthScrollStatus();
        assertFalse(s.isScrollLeftActive());
        assertTrue(s.isScrollRightActive());
        Date dat = DateFormatUtil.toD(2009, 11, 20);
        int in = CalendarTable.findIndex(dList, dat);
        // Step 2
        da.gotoDate(in);
        int firstP = da.getFirstP();
        int lastP = da.getLastP();
        // Expected result 3
        assertEquals(11, lastP);
        assertEquals(6, firstP);
        s = da.getMonthScrollStatus();
        assertTrue(s.isScrollLeftActive());
        assertFalse(s.isScrollRightActive());
    }

    /**
     * Test for skipping months
     * Step 1:
     *   Create scroll table 2009/01/01 - 2009/12/20
     * Step 2:
     *   Skip BEG
     * Expected result 1:
     *   False
     * Step 3:
     *   Skip LEFT
     * Expected result 2:
     *   False
     * Step 4:
     *   Skip RIGHT twice
     * Expected result 3:
     *   True, panel 2-7
     * Step 5:
     *   Skip END
     * Expected result 4:
     *   True, panel 6-11
     * Step 6:
     *   Skip LEFT
     * Expected result 5:
     *   True, panel 5-10
     * Step 7:
     *   Skip BEG
     * Expected result 6:
     *   True, panel 0-5
     */
    @Test
    public void Test4() {
        MonthSeasonScrollData da = new MonthSeasonScrollData();
        Date dF = DateFormatUtil.toD(2009, 1, 1);
        Date dT = DateFormatUtil.toD(2009, 12, 20);
        // Step 1
        List<Date> dList = CalendarTable.listOfDates(dF, dT, PeriodType.byDay);
        da.createVPanel(dList, 10);
        // Step 2
        boolean ok = da.skipMonth(MoveSkip.BEG);
        // Expected result 1
        assertFalse(ok);
        // Step 3
        ok = da.skipMonth(MoveSkip.LEFT);
        // Expected result 2
        assertFalse(ok);
        // Step 4
        ok = da.skipMonth(MoveSkip.RIGHT);
        assertTrue(ok);
        ok = da.skipMonth(MoveSkip.RIGHT);
        assertTrue(ok);
        int firstP = da.getFirstP();
        int lastP = da.getLastP();
        // Expected result 3
        assertEquals(7, lastP);
        assertEquals(2, firstP);
        // Step 5
        ok = da.skipMonth(MoveSkip.END);
        assertTrue(ok);
        firstP = da.getFirstP();
        lastP = da.getLastP();
        // Expected result 4
        assertEquals(11, lastP);
        assertEquals(6, firstP);
        // Step 6
        ok = da.skipMonth(MoveSkip.LEFT);
        assertTrue(ok);
        firstP = da.getFirstP();
        lastP = da.getLastP();
        // Expected result 5
        assertEquals(10, lastP);
        assertEquals(5, firstP);
        // Step 7
        ok = da.skipMonth(MoveSkip.BEG);
        assertTrue(ok);
        firstP = da.getFirstP();
        lastP = da.getLastP();
        // Expected result 6
        assertEquals(5, lastP);
        assertEquals(0, firstP);

    }

    private void testP(DaySeasonScrollData da, int p, int l) {
        int firstP = da.getFirstD();
        int lastD = da.getLastD();
        assertEquals(p, firstP);
        assertEquals(l, lastD);
    }

    private void testS(DaySeasonScrollData da, boolean left, boolean right) {
        PanelDesc s = da.getDayScrollStatus();
        assertEquals(left, s.isScrollLeftActive());
        assertEquals(right, s.isScrollRightActive());
    }

    /**
     * Test DaySeasonScrollData (rough test - very similar like Month)
     * Step1:
     *   Create DayList 2009/01/01 -2009/12/20
     *   Create panel and todayC 10
     * Expected result 1:
     *   firstP and lastP: 9-18
     *   can scroll left and right
     * Step 2:
     *   skip left by 4
     * Expected result 2:
     *   skip possible
     *   can scroll left and right
     * Step 3:
     *   skip to the end
     * Expected result 3:
     *  skip possible
     *  can scroll left, cannot right
     * Expected result 4:
     *  test last date in period - should be equals to 2009/12/20
     */
//    @Test
    public void Test5() {
        // Step 1
//        DaySeasonScrollData da = new DaySeasonScrollData(14);
        DaySeasonScrollData da = new DaySeasonScrollData(DateFormatUtil.toD(2009, 1, 1));
        Date dF = DateFormatUtil.toD(2009, 1, 1);
        Date dT = DateFormatUtil.toD(2009, 12, 20);
        // Step 1
        List<Date> dList = CalendarTable.listOfDates(dF, dT, PeriodType.byDay);
        da.createSPanel(dList, 10);
        // Expected result 1
        testP(da, 9, 18);
        testS(da, true, true);
        // Step 2
        boolean ok = da.moveD(MoveSkip.LEFT, 4);
        // Expected result 2
        assertTrue(ok);
        testP(da, 5, 14);
        // Step 3
        ok = da.moveD(MoveSkip.END, 0);
        // Expected  result 3
        assertTrue(ok);
        testS(da, true, false);

        // Expected result 4
        int lastP = da.getLastD();
        Date d = da.getD(lastP);
        boolean eq = DateUtil.eqDate(d,dT);
        assertTrue(eq);

    }
}
