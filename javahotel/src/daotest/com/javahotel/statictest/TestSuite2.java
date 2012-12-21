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

import com.javahotel.common.dateutil.GetPeriods;
import com.javahotel.common.dateutil.PeriodT;
import com.javahotel.common.dateutil.GetPeriods.StartWeek;
import com.javahotel.common.seasonutil.CreateTableSeason;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.OfferSeasonPeriodP;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite2 {

    private static Date createD(int year, int mo, int da) {
        return TestDUtil.createD(year, mo, da);
    }

    @Test
    public void Test1() throws Exception {
        System.out.println("Test create table");
        OfferSeasonP oP = new OfferSeasonP();
        oP.setStartP(createD(2008, 9, 1));
        oP.setEndP(createD(2008, 10, 25));
        List<OfferSeasonPeriodP> colP = new ArrayList<OfferSeasonPeriodP>();
        oP.setPeriods(colP);
        List<PeriodT> col = CreateTableSeason.createTable(oP,StartWeek.onSaturday);
//2008/09/01 - 2008/09/05
//2008/09/06 - 2008/09/07
//2008/09/08 - 2008/09/12
//2008/09/13 - 2008/09/14
//2008/09/15 - 2008/09/19
//2008/09/20 - 2008/09/21
//2008/09/22 - 2008/09/26
//2008/09/27 - 2008/09/28
//2008/09/29 - 2008/10/03
//2008/10/04 - 2008/10/05
//2008/10/06 - 2008/10/10
//2008/10/11 - 2008/10/12
//2008/10/13 - 2008/10/17
//2008/10/18 - 2008/10/19
//2008/10/20 - 2008/10/25

        int no = 0;
        for (PeriodT p : col) {
            switch (no) {
                case 0:
                    TestDUtil.checkP(p, 2008, 9, 1, 2008, 9, 5);
                    break;
                case 1:
                    TestDUtil.checkP(p, 2008, 9, 6, 2008, 9, 7);
                    break;
                case 2:
                    TestDUtil.checkP(p, 2008, 9, 8, 2008, 9, 12);
                    break;
                case 3:
                    TestDUtil.checkP(p, 2008, 9, 13, 2008, 9, 14);
                    break;
                case 4:
                    TestDUtil.checkP(p, 2008, 9, 15, 2008, 9, 19);
                    break;
                case 5:
                    TestDUtil.checkP(p, 2008, 9, 20, 2008, 9, 21);
                    break;
                case 6:
                    TestDUtil.checkP(p, 2008, 9, 22, 2008, 9, 26);
                    break;
                case 7:
                    TestDUtil.checkP(p, 2008, 9, 27, 2008, 9, 28);
                    break;
                case 8:
                    TestDUtil.checkP(p, 2008, 9, 29, 2008, 10, 3);
                    break;
                case 9:
                    TestDUtil.checkP(p, 2008, 10, 4, 2008, 10, 5);
                    break;
                case 10:
                    TestDUtil.checkP(p, 2008, 10, 6, 2008, 10, 10);
                    break;
                case 11:
                    TestDUtil.checkP(p, 2008, 10, 11, 2008, 10, 12);
                    break;
                case 12:
                    TestDUtil.checkP(p, 2008, 10, 13, 2008, 10, 17);
                    break;
                case 13:
                    TestDUtil.checkP(p, 2008, 10, 18, 2008, 10, 19);
                    break;
                case 14:
                    TestDUtil.checkP(p, 2008, 10, 20, 2008, 10, 24);
                    break;
                case 15:
                    TestDUtil.checkP(p, 2008, 10, 25, 2008, 10, 25);
                    break;
                default:
                    fail();
            }
            no++;
        }
    }

    @Test
    public void Test2() throws Exception {
        System.out.println("Test create table day more");
        OfferSeasonP oP = new OfferSeasonP();
        oP.setStartP(createD(2008, 9, 1));
        oP.setEndP(createD(2008, 10, 26)); // day more
        List<OfferSeasonPeriodP> colP = new ArrayList<OfferSeasonPeriodP>();
        oP.setPeriods(colP);
        List<PeriodT> col = CreateTableSeason.createTable(oP,StartWeek.onSaturday);
//2008/09/01 - 2008/09/05
//2008/09/06 - 2008/09/07
//2008/09/08 - 2008/09/12
//2008/09/13 - 2008/09/14
//2008/09/15 - 2008/09/19
//2008/09/20 - 2008/09/21
//2008/09/22 - 2008/09/26
//2008/09/27 - 2008/09/28
//2008/09/29 - 2008/10/03
//2008/10/04 - 2008/10/05
//2008/10/06 - 2008/10/10
//2008/10/11 - 2008/10/12
//2008/10/13 - 2008/10/17
//2008/10/18 - 2008/10/19
//2008/10/20 - 2008/10/25

        int no = 0;
        for (PeriodT p : col) {
            TestDUtil.drawP(p);
            switch (no) {
                case 0: TestDUtil.checkP(p, 2008, 9, 1, 2008, 9, 5); break;
                case 1: TestDUtil.checkP(p, 2008, 9, 6, 2008, 9, 7); break;
                case 2: TestDUtil.checkP(p, 2008, 9, 8, 2008, 9, 12); break;
                case 3: TestDUtil.checkP(p, 2008, 9, 13, 2008, 9, 14); break;
                case 4: TestDUtil.checkP(p, 2008, 9, 15, 2008, 9, 19); break;
                case 5: TestDUtil.checkP(p, 2008, 9, 20, 2008, 9, 21); break;
                case 6: TestDUtil.checkP(p, 2008, 9, 22, 2008, 9, 26); break;
                case 7: TestDUtil.checkP(p, 2008, 9, 27, 2008, 9, 28); break;
                case 8: TestDUtil.checkP(p, 2008, 9, 29, 2008, 10,3); break;
                case 9: TestDUtil.checkP(p, 2008, 10,4, 2008, 10,5); break;
                case 10: TestDUtil.checkP(p, 2008, 10,6, 2008, 10,10); break;
                case 11: TestDUtil.checkP(p, 2008, 10,11, 2008, 10,12); break;
                case 12: TestDUtil.checkP(p, 2008, 10,13, 2008, 10,17); break;
                case 13: TestDUtil.checkP(p, 2008, 10,18, 2008, 10,19); break;
                case 14: TestDUtil.checkP(p, 2008, 10,20, 2008, 10,24); break;
                case 15: TestDUtil.checkP(p, 2008, 10,25, 2008, 10,26); break;
                default: fail();
            }
            no++;
        }
    }

    @Test
    public void Test3() throws Exception {
        System.out.println("Test create table");
        OfferSeasonP oP = new OfferSeasonP();
        oP.setStartP(createD(2008, 10, 20));
        oP.setEndP(createD(2008, 10, 25));
        List<OfferSeasonPeriodP> colP = new ArrayList<OfferSeasonPeriodP>();
        oP.setPeriods(colP);
        List<PeriodT> col = CreateTableSeason.createTable(oP,StartWeek.onSaturday);
        int no = 0;
        for (PeriodT p : col) {
            TestDUtil.drawP(p);
            switch (no) {
                case 0:
                    TestDUtil.checkP(p, 2008, 10, 20, 2008, 10, 24);
                    break;
                case 1:
                    TestDUtil.checkP(p, 2008, 10, 25, 2008, 10, 25);
                    break;
                default:
                    fail();
            }
            no++;
        }
    }

    @Test
    public void Test4() {
        System.out.println("List of weeks");
        List<PeriodT> out = GetPeriods.listOfWeekends(new PeriodT(TestDUtil.createD(2008, 9, 1), TestDUtil.createD(2008, 10, 26), null),StartWeek.onSaturday);
        int no = 0;
//2008/09/06 - 2008/09/07
//2008/09/13 - 2008/09/14
//2008/09/20 - 2008/09/21
//2008/09/27 - 2008/09/28
//2008/10/04 - 2008/10/05
//2008/10/11 - 2008/10/12
//2008/10/18 - 2008/10/19
//2008/10/25 - 2008/10/26

        for (PeriodT p : out) {
//            TestDUtil.drawP(pe);
            switch (no) {
                case 0: TestDUtil.checkP(p, 2008, 9, 6, 2008, 9, 7); break;
                case 1: TestDUtil.checkP(p, 2008, 9, 13, 2008, 9, 14); break;
                case 2: TestDUtil.checkP(p, 2008, 9, 20, 2008, 9, 21); break;
                case 3: TestDUtil.checkP(p, 2008, 9, 27, 2008, 9, 28); break;
                case 4: TestDUtil.checkP(p, 2008, 10, 4, 2008, 10, 5); break;
                case 5: TestDUtil.checkP(p, 2008, 10, 11, 2008, 10, 12); break;
                case 6: TestDUtil.checkP(p, 2008, 10, 18, 2008, 10, 19); break;
                case 7: TestDUtil.checkP(p, 2008, 10, 25, 2008, 10, 26); break;
                default: fail();
            }
            no++;

        }
    }
}
