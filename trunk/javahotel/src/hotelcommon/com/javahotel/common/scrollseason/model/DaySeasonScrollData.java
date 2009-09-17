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
package com.javahotel.common.scrollseason.model;

import com.javahotel.common.dateutil.CalendarTable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class DaySeasonScrollData {

    /** List of days under display. */
    private List<Date> dList;
    /** Actual day, today */
    private final int todayC;
    /** Month panel. */
    private final MonthSeasonScrollData mScroll;
    /** Pos data. */
    private final PosData pos;

    public DaySeasonScrollData(int todayC) {
        this.todayC = todayC;
        mScroll = new MonthSeasonScrollData();
        pos = new PosData();
    }

    private void setD(Date d) {
        int no = CalendarTable.findIndex(dList, d);
        if (no == -1) { return; }
        setD(no);
    }

    private void setD(int d) {
        pos.setD(d);
    }

    public void createSPanel(List<Date> dList, int pWidth) {
        this.dList = dList;
        mScroll.createVPanel(dList, getTodayC());
        pos.createW(dList.size(), pWidth);
        pos.setD(todayC);
    }

    public void gotoD(int d) {
        setD(d);
    }

    public void gotoD(Date d) {
        setD(d);
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
    public int getTodayC() {
        return todayC;
    }
}
