/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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

import com.javahotel.common.dateutil.DateUtil;
import com.javahotel.common.dateutil.PeriodT;
import com.javahotel.common.dateutil.GetPeriods.StartWeek;
import com.javahotel.common.seasonutil.CreateTableSeason;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.OfferSeasonPeriodP;
import com.javahotel.common.toobject.SeasonPeriodT;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite6 {

    private static Date createD(int year, int mo, int da) {
        return TestDUtil.createD(year, mo, da);
    }

    private void eqD(final Date d, int year, int mo, int da) {
        Date dx = createD(year, mo, da);
        assertEquals(0, DateUtil.compareDate(d, dx));
    }

    @Test
    public void Test1() throws Exception {
        System.out.println("Test create table day more");
        OfferSeasonP oP = new OfferSeasonP();
        oP.setStartP(createD(2008, 9, 1));
        oP.setEndP(createD(2008, 10,30)); // day more
        Collection<OfferSeasonPeriodP> colP = new ArrayList<OfferSeasonPeriodP>();
        OfferSeasonPeriodP sp = new OfferSeasonPeriodP();
        sp.setStartP(createD(2008, 10, 1));
        sp.setEndP(createD(2008, 10, 4));
        sp.setPeriodT(SeasonPeriodT.SPECIAL);
        colP.add(sp);
        
        oP.setPeriods(colP);
        Collection<PeriodT> col = CreateTableSeason.createTable(oP, StartWeek.onFriday);
        assertEquals(17,col.size());
        int no = 0;
        for (PeriodT p : col) {
            System.out.print(no + " ");
            TestDUtil.drawP(p);
            if (no == 9) {
                // period: 2008/10/01 - 2008/10/04
                TestDUtil.checkP(p,2008,10,1,2008,10,4);
            }
            no++;
        }
    }
}
