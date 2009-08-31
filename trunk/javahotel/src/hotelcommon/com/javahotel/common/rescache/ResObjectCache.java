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
package com.javahotel.common.rescache;

import com.javahotel.common.command.ISignal;
import com.javahotel.common.dateutil.CalendarTable;
import com.javahotel.common.dateutil.CalendarTable.PeriodType;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.dateutil.DateUtil;
import com.javahotel.common.dateutil.PeriodT;
import com.javahotel.common.dateutil.RunPeriodLoop;
import com.javahotel.common.toobject.ResDayObjectStateP;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class ResObjectCache {

    private final Map<HKey, ResDayObjectStateP> hM =
            new HashMap<HKey, ResDayObjectStateP>();

    public void invalidate() {
        hM.clear();
    }

    public ResObjectCache(final IReadResData iRead) {
        this.iRead = iRead;
        invalidate();
    }

    public interface IReadResData {

        void getResData(ReadResParam pa, IReadResCallBack col);
    }

    public interface IReadResCallBack {

        void setCol(final List<ResDayObjectStateP> col);
    }

    private class SignalReadResCallBack implements IReadResCallBack {

        private final ISignal i;
        private final ReadResParam rP;

        SignalReadResCallBack(final ISignal i, final ReadResParam rP) {
            this.i = i;
            this.rP = rP;
        }

        public void setCol(final List<ResDayObjectStateP> col) {
            for (ResDayObjectStateP r : col) {
                setS(r);
            }
            RunPeriodLoop re = new RunPeriodLoop() {

                @Override
                protected void visit(Date d) {
                    for (final String s : rP.getResList()) {
                        ResDayObjectStateP pp = getS(s, d);
                        if (pp == null) {
                            pp = new ResDayObjectStateP();
                            pp.setFree();
                            pp.setResObject(s);
                            pp.setD(DateUtil.copyDate(d));
                            setS(pp);
                        }
                    }
                }
            };

            re.run(rP.getPe());
            i.signal();
        }
    }

    public interface ISetResState {

        void setResState(ResDayObjectStateP p);
    }
    private final IReadResData iRead;

    private class HKey {

        private final String resObject;
        private final Date rDay;
        private final int hCode;
        // TODO: remove
        private final String s;

        HKey(final String resObject, final Date rDay) {
            this.resObject = resObject;
            this.rDay = rDay;
            s = DateFormatUtil.toS(rDay) + resObject;
            hCode = s.hashCode();
        }

        @Override
        public int hashCode() {
            return hCode;
        }

        @Override
        public boolean equals(final Object o) {
            HKey h = (HKey) o;
            if (!resObject.equals(h.resObject)) {
                return false;
            }
            return DateUtil.eqDate(rDay, h.rDay);
        }
    }

    private ResDayObjectStateP getS(final String resObject, final Date rDay) {
        HKey hK = new HKey(resObject, rDay);
        return hM.get(hK);
    }

    private void setS(final ResDayObjectStateP r) {
        HKey hK = new HKey(r.getResObject(), r.getD());
        hM.put(hK, r);
    }

    private Set<String> createS(final ReadResParam rP) {
        Set<String> se = new HashSet<String>();
        for (final String s : rP.getResList()) {
            se.add(s);
        }
        return se;
    }

    private boolean isCovered(final Date dFrom, final Date dTo,
            final Set<String> resList) {
        List<Date> dLine = CalendarTable.listOfDates(dFrom, dTo, PeriodType.byDay);
        for (String s : resList) {
            for (Date d : dLine) {
                if (getS(s, d) == null) {
                    return false;
                }
            }
        }
        return true;
    }

    private class MinMax {

        Date min;
        Date max;

        MinMax() {
            min = null;
            max = null;
        }
    }

    private MinMax minD(final ReadResParam rP) {
        Set<String> se = createS(rP);
        // first run
        Map<String, MinMax> mas = new HashMap<String, MinMax>();
        for (HKey h : hM.keySet()) {
            if (!se.contains(h.resObject)) {
                continue;
            }
            MinMax ma = mas.get(h.resObject);
            if (ma == null) {
                ma = new MinMax();
                ma.max = h.rDay;
                ma.min = h.rDay;
                mas.put(h.resObject, ma);
                continue;
            }
            int c = DateUtil.compareDate(h.rDay, ma.min);
            if (c == -1) {
                ma.min = h.rDay;
            }
            c = DateUtil.compareDate(h.rDay, ma.max);
            if (c == 1) {
                ma.max = h.rDay;
            }
            mas.put(h.resObject, ma);
        }
        // all res covered
        for (final String s : mas.keySet()) {
            if (!mas.containsKey(s)) {
                return null;
            }
        }
        // second run
        MinMax mm = null;
        for (final String s : mas.keySet()) {
            MinMax m = mas.get(s);
            if (mm == null) {
                mm = m;
                continue;
            }
            int c = DateUtil.compareDate(mm.min, m.min);
            if (c == -1) {
                mm.min = m.min;
            }
            c = DateUtil.compareDate(mm.max, m.max);
            if (c == 1) {
                mm.max = m.max;
            }
        }
        if (mm == null) {
            return null;
        }
        int c = DateUtil.compareDate(mm.min, mm.max);
        if (c == 1) {
            return null;
        }
        // no holes inside
        if (!isCovered(mm.min, mm.max, se)) {
            return null;
        }
        return mm;
    }

    private ReadResParam modifResParam(final ReadResParam rP,
            final MinMax mD) {
        if (mD == null) {
            return rP;
        }
        List<String> colS = rP.getResList();
        Date dFrom = rP.getPe().getFrom();
        Date dTo = rP.getPe().getTo();
        int c1 = DateUtil.compareDate(dFrom, mD.min);
        int c2 = DateUtil.compareDate(dTo, mD.max);
        if ((c1 == -1) && (c2 == 1)) {
            return rP;
        }
        int c3 = DateUtil.compareDate(dTo, mD.min);
        if (c3 == -1) {
            return rP;
        }
        int c4 = DateUtil.compareDate(dFrom, mD.max);
        if (c4 == 1) {
            return rP;
        }
        if (c1 == -1) {
            dTo = DateUtil.copyDate(mD.min);
            DateUtil.PrevDay(dTo);
        } else {
            dFrom = DateUtil.copyDate(mD.max);
            DateUtil.NextDay(dFrom);
        }
        int c5 = DateUtil.compareDate(dFrom, dTo);
        if (c5 == 1) {
            return null;
        }
        return new ReadResParam(colS, new PeriodT(dFrom, dTo));
    }

    private class SetResStateSignal implements ISignal {

        private final String resObject;
        private final Date rDay;
        private final ISetResState i;

        SetResStateSignal(final String resObject, final Date rDay,
                final ISetResState i) {
            this.resObject = resObject;
            this.rDay = rDay;
            this.i = i;
        }

        public void signal() {
            ResDayObjectStateP p = getS(resObject, rDay);
            i.setResState(p);
        }
    }

    public void getResState(final String resObject, final Date rDay,
            final ReadResParam rP, final ISetResState i) {
        ResDayObjectStateP p = getS(resObject, rDay);
        if (p != null) {
            i.setResState(p);
            return;

        }
        SetResStateSignal se = new SetResStateSignal(resObject, rDay, i);
        ReadResState(rP, se);
    }

    public void ReadResState(final ReadResParam rP, final ISignal i) {
        Set<String> se = createS(rP);

        if (isCovered(rP.getPe().getFrom(), rP.getPe().getTo(), se)) {
            i.signal();
            return;
        }
        SignalReadResCallBack cBack = new SignalReadResCallBack(i, rP);
        MinMax mD = minD(rP);
        ReadResParam rr = modifResParam(rP, mD);
        if (rr == null) {
            i.signal();
            return;
        }
        iRead.getResData(rr, cBack);
    }

    public ResDayObjectStateP getResState(final String resObject,
            final Date rDay) {
        ResDayObjectStateP p = getS(resObject, rDay);
        return p;
    }

    public List<ResDayObjectStateP> isConflict(final ResObjectElem res) {
        final List<ResDayObjectStateP> out =
                new ArrayList<ResDayObjectStateP>();
        RunPeriodLoop re = new RunPeriodLoop() {

            @Override
            protected void visit(Date d) {
                ResDayObjectStateP p = getResState(res.getResName(), d);
                if (p.getLState() == null) {
                    return;
                }
                out.add(p);
            }
        };
        re.run(res.getPe());
        return out;
    }

    public List<ResDayObjectStateP> isConflict(
            final List<ResObjectElem> res) {
        final List<ResDayObjectStateP> out =
                new ArrayList<ResDayObjectStateP>();
        for (ResObjectElem e : res) {
            List<ResDayObjectStateP> p = isConflict(e);
            out.addAll(p);
        }
        return out;


    }
}
