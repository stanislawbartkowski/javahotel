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

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class MonthSeasonScrollData {

    /** Max width (number of) months displayed. */
    private final static int maxmonthPe = 6;
    /** List of months corresponding to dList. */
    private List<YearMonthPe> dMonth;
    /** Index of dMonth keeping actC. */
    private int intP;
    /** posdata. */
    private final PosData pos;

    public YearMonthPe getPe(int no) {
        return dMonth.get(no);
    }

    public MonthSeasonScrollData() {
        pos = new PosData();
    }

    private int setMonth(int c) {
        int iP = 0;
        boolean found = false;
        for (YearMonthPe y : dMonth) {
            int p = y.getStartT();
            int l = y.getNoDays();
            if ((c >= p) && (c < p + l)) {
                found = true;
                break;
            }
            iP++;
        }
        /* display in the middle */
        if (!found) {
            iP = 0;
        }
        /* set beg and end keeping actual in the middle */
        pos.setD(iP);
        return iP;
    }

    public void createVPanel(List<Date> dList, int actC) {
        dMonth = CreateMonthPeList.createLi(dList);
        pos.createW(dMonth.size(), maxmonthPe);
        intP = setMonth(actC);
    }

    public void gotoDate(int intD) {
        setMonth(intD);
    }

    public boolean skipMonth(MoveSkip move) {
        return pos.skipPos(move, 1);
    }

    /**
     * @return the monthPe
     */
    public int getMonthPe() {
        return pos.getPanelW();
    }

    /**
     * @return the firstP
     */
    public int getFirstP() {
        return pos.getFirstD();
    }

    /**
     * @return the lastP
     */
    public int getLastP() {
        return pos.getLastD();
    }

    public PanelDesc getMonthScrollStatus() {
        return pos.getDayScrollStatus();
    }

    /**
     * @return the intP
     */
    public int getIntP() {
        return intP;
    }

}
