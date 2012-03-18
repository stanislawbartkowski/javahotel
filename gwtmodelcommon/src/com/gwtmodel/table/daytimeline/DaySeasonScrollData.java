/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.daytimeline;

import java.util.Date;
import java.util.List;

import com.gwtmodel.table.common.dateutil.DateFormatUtil;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class DaySeasonScrollData {

    /** List of days under display. */
    private List<Date> dList;
    /** Actual day, today */
    private final Date today;
    /** Month panel. */
    private final MonthSeasonScrollData mScroll;
    /** Pos data. */
    private final PosData pos;

    public DaySeasonScrollData(Date todayC) {
        this.today = todayC;
        mScroll = new MonthSeasonScrollData();
        pos = new PosData();
    }

    private void setD(Date d) {
        int no = CalendarTable.findIndex(dList, d);
        if (no == -1) {
            return;
        }
        setD(no);
    }

    private void setD(int d) {
        pos.setD(d);
    }

    public void createSPanel(List<Date> dList, int pWidth) {
        this.dList = dList;
        int todayC = CalendarTable.findIndex(dList, today);
        mScroll.createVPanel(dList, todayC);
        pos.createW(dList.size(), pWidth);
        pos.setD(todayC);
    }

    public void gotoD(int d) {
        setD(d);
    }

    public void gotoD(Date d) {
        setD(d);
    }

    public void gotoMonth(YearMonthPe m) {
        int day = m.getNoDays() / 2;
        int no = -1;
        boolean found = false;
        for (Date d : dList) {
            no++;
            int y = DateFormatUtil.getY(d);
            int mt = DateFormatUtil.getM(d);
            int dt = DateFormatUtil.getD(d);
            if (y != m.getYear()) {
                continue;
            }
            if (mt != m.getMonth()) {
                continue;
            }
            if (dt < day) {
                continue;
            }
            found = true;
            break;
        }
        if (!found) {
            return;
        }
        setD(no);
    }

    public PanelDesc getDayScrollStatus() {
        return pos.getDayScrollStatus();
    }

    public boolean moveD(MoveSkip m, int noD) {
        return pos.skipPos(m, noD);

    }

    public Date getD(int c) {
        return dList.get(c);
    }

    /**
     * @return the periodNo
     */
    public int getPeriodNo() {
        return pos.getPanelW();
    }

    /**
     * @return the firstD
     */
    public int getFirstD() {
        return pos.getFirstD();
    }

    /**
     * @return the lastD
     */
    public int getLastD() {
        return pos.getLastD();
    }

    /**
     * @return the todayC
     */
    public Date getTodayC() {
        return today;
    }

    public int getTodayM() {
        return mScroll.getIntP();
    }
}
