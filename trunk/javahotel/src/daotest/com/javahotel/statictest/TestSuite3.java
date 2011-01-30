/*
 * Copyright 2011 stanislawbartkowski@gmail.com
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
public class TestSuite3 {

    private static Date createD(int year, int mo, int da) {
        return TestDUtil.createD(year, mo, da);
    }

    @Test
    public void Test1() throws Exception {
        System.out.println("Test create table day more");
        OfferSeasonP oP = new OfferSeasonP();
        oP.setStartP(createD(2008, 9, 1));
        oP.setEndP(createD(2008, 9, 12)); // day more
        List<OfferSeasonPeriodP> colP = new ArrayList<OfferSeasonPeriodP>();
        oP.setPeriods(colP);
        List<PeriodT> col = CreateTableSeason.createTable(oP, StartWeek.onSaturday);
        for (PeriodT p : col) {
            TestDUtil.drawP(p);
        }

        PeriodT pe = new PeriodT(createD(2008, 9, 6), createD(2008, 9, 6), null);
        List<PeriodT> cou = GetPeriods.get(pe, col);
        int no = 0;
        for (PeriodT p : cou) {
            switch (no) {
                case 0:
                    TestDUtil.checkP(p, 2008, 9, 6, 2008, 9, 6);
                    break;
                default:
                    fail();
            }
        }
    }
}
