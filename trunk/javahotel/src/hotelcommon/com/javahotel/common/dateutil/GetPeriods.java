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
package com.javahotel.common.dateutil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.gwtmodel.table.common.PeriodT;
import com.gwtmodel.table.common.dateutil.DateUtil;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class GetPeriods {

    private GetPeriods() {
    }

    private static class ICompare implements Comparator<PeriodT> {

        public int compare(final PeriodT o1, final PeriodT o2) {
            return DateUtil.compareDate(o1.getFrom(), o2.getFrom());
        }
    }

    private static void sort(final ArrayList<PeriodT> col) {
        Collections.sort(col, new ICompare());
    }

    private static List<PeriodT> cut(final PeriodT pe, final List<PeriodT> sou) {
        ArrayList<PeriodT> pout = new ArrayList<PeriodT>();
        Date pefrom = pe.getFrom();
        Date peto = pe.getTo();
        for (PeriodT p : sou) {
            Date pfrom = p.getFrom();
            Date pto = p.getTo();
            if (DateUtil.compareDate(pto, pefrom) == -1) {
                continue;
            }
            if (DateUtil.compareDate(pfrom, peto) == 1) {
                continue;
            }
            Date nfrom = DateUtil.getMaxDate(pfrom, pefrom);
            Date nto = DateUtil.getMinDate(pto, peto);
            PeriodT npe = new PeriodT(nfrom, nto, p.getI());
            pout.add(npe);
        }
        sort(pout);
        return pout;
    }

    public static List<PeriodT> get(final PeriodT pe, final List<PeriodT> sou) {

        List<PeriodT> pout = cut(pe, sou);
        ArrayList<PeriodT> out = new ArrayList<PeriodT>();
        PeriodT beg = null;
        for (PeriodT pr : pout) {
            Date lfrom;
            if (beg != null) {
                // lfrom = DateUtil.copyDate(beg.getTo());
                lfrom = DateUtil.NextDayD(beg.getTo());
            } else {
                lfrom = pe.getFrom();
            }
            // Date lto = DateUtil.copyDate(pr.getFrom());
            Date lto = DateUtil.PrevDayD(pr.getFrom());
            beg = pr;
            if (DateUtil.compareDate(lto, lfrom) == -1) {
                out.add(pr);
                continue;
            }
            out.add(new PeriodT(lfrom, lto, pe.getI()));
            out.add(pr);
        }
        if (beg == null) {
            out.add(pe);
        } else {
            // Date lto = DateUtil.copyDate(beg.getTo());
            Date lto = DateUtil.NextDayD(beg.getTo());
            if (DateUtil.compareDate(lto, pe.getTo()) != 1) {
                out.add(new PeriodT(lto, pe.getTo(), pe.getI()));
            }
        }
        return out;
    }

    public static List<PeriodT> cutPeriods(final PeriodT pe,
            final List<PeriodT> sou) {
        List<PeriodT> out = cut(pe, sou);
        return out;
    }

    public interface IEqPeriodT {

        boolean eq(PeriodT p1, PeriodT p2);
    }

    public static List<PeriodT> consolidatePeriods(final List<PeriodT> sou,
            final IEqPeriodT i) {
        PeriodT prev = null;
        List<PeriodT> out = new ArrayList<PeriodT>();
        for (PeriodT p : sou) {
            if (prev != null) {
                if (i.eq(prev, p)) {
                    // Date dto = DateUtil.copyDate(prev.getTo());
                    Date dto = DateUtil.NextDayD(prev.getTo());
                    Date dfrom = p.getFrom();

                    if (DateUtil.compareDate(dto, dfrom) != -1) {
                        if (prev.getI() == p.getI()) {
                            // break final, getter, setter
                            // consolidate
                            prev.setTo(p.getTo());
                            continue;
                        }
                    }
                }
            }
            out.add(p);
            prev = p;
        }
        return out;

    }

    public enum StartWeek {

        onFriday, onSaturday
    };

    private static boolean startW(final int dOfWeek, final StartWeek sWeek) {
        switch (sWeek) {
        case onFriday:
            return dOfWeek == 5;
        case onSaturday:
            return dOfWeek == 6;
        }
        return false;
    }

    private static boolean endW(final int dOfWeek, final StartWeek sWeek) {
        switch (sWeek) {
        case onFriday:
            return dOfWeek == 6;
        case onSaturday:
            return dOfWeek == 0;
        }
        return false;
    }

    public static List<PeriodT> listOfWeekends(final PeriodT pe,
            final StartWeek sWeek) {
        Date first = pe.getFrom();
        Date last = pe.getTo();
        // Date actC = DateUtil.copyDate(first);
        Date actC = first;
        List<PeriodT> cDays = new ArrayList<PeriodT>();
        Date begW = null;
        while (DateUtil.compareDate(actC, last) != 1) {
            int dOfWeek = actC.getDay();
            if (startW(dOfWeek, sWeek)) {
                // begW = DateUtil.copyDate(actC);
                begW = actC;
            }
            if (endW(dOfWeek, sWeek)) {
                if (begW == null) {
                    // begW = DateUtil.copyDate(first);
                    begW = first;
                }
                cDays.add(new PeriodT(begW, actC, pe.getI()));
                begW = null;
            }
            actC = DateUtil.NextDayD(actC);
        }
        if (begW != null) {
            cDays.add(new PeriodT(begW, last, pe.getI()));
        }
        return cDays;
    }

    public static PeriodT findPeriod(final Date d, final List<PeriodT> sou) {
        for (PeriodT p : sou) {
            if (DateUtil.compPeriod(d, p) == 0) {
                return p;
            }
        }
        return null;
    }
}
