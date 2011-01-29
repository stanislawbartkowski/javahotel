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
package com.javahotel.common.seasonutil;

import com.javahotel.common.dateutil.GetPeriods;
import com.javahotel.common.dateutil.GetPeriods.IEqPeriodT;
import com.javahotel.common.dateutil.PeriodT;
import com.javahotel.common.dateutil.GetPeriods.StartWeek;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.OfferSeasonPeriodP;
import com.javahotel.common.toobject.SeasonPeriodT;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class CreateTableSeason {

    private CreateTableSeason() {
    }

    private static PeriodT createP(final OfferSeasonPeriodP p) {
        PeriodT pe = new PeriodT(p.getStartP(), p.getEndP(), p);
        return pe;
    }

    private static List<PeriodT> createC(final OfferSeasonP oP,
            final SeasonPeriodT t) {
        List<PeriodT> out = new ArrayList<PeriodT>();
        for (OfferSeasonPeriodP p : oP.getPeriods()) {
            if (p.getPeriodT() == t) {
                out.add(createP(p));
            }
        }
        return out;
    }

    public static List<PeriodT> createTable(
            final OfferSeasonP oP, final StartWeek sWeek) {
        List<PeriodT> co = createC(oP, SeasonPeriodT.LOW);
        List<PeriodT> cous = GetPeriods.get(
                new PeriodT(oP.getStartP(), oP.getEndP(), null), co);

        Integer sign = new Integer(0);
        List<PeriodT> wCo = GetPeriods.listOfWeekends(
                new PeriodT(oP.getStartP(), oP.getEndP(), sign), sWeek);

        List<PeriodT> cou = new ArrayList<PeriodT>();
        for (PeriodT p : cous) {
            List<PeriodT> pp = GetPeriods.get(p, wCo);
            for (PeriodT pp1 : pp) {
                if (pp1.getI() == sign) {
                    OfferSeasonPeriodP oPP = new OfferSeasonPeriodP();
                    if (p.getI() == null) {
                        oPP.setPeriodT(SeasonPeriodT.HIGHWEEKEND);
                    } else {
                        oPP.setPeriodT(SeasonPeriodT.LOWWEEKEND);
                    }
                    pp1.setI(oPP);
                }
                cou.add(pp1);
            }
        }


        List<PeriodT> coS = createC(oP, SeasonPeriodT.SPECIAL);
        List<PeriodT> out = new ArrayList<PeriodT>();
        for (PeriodT p : cou) {
            List<PeriodT> pp = GetPeriods.get(p, coS);
            out.addAll(pp);
        }

        IEqPeriodT ie = new IEqPeriodT() {

            public boolean eq(PeriodT p1, PeriodT p2) {
                Object o1 = p1.getI();
                Object o2 = p2.getI();
                if ((o1 == null) || (o2 == null)) {
                    return true;
                }
                OfferSeasonPeriodP pp1 = (OfferSeasonPeriodP) o1;
                OfferSeasonPeriodP pp2 = (OfferSeasonPeriodP) o2;
                return pp1.getPeriodT() == pp2.getPeriodT();
            }
        };

        List<PeriodT> cout = GetPeriods.consolidatePeriods(out, ie);
        return cout;
    }
}
