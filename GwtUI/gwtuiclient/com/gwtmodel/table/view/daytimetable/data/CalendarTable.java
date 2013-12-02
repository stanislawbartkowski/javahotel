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
package com.gwtmodel.table.view.daytimetable.data;

import com.gwtmodel.table.common.dateutil.DateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class CalendarTable {

    private CalendarTable() {
    }

    private static boolean endOfWeek(final Date cal) {
        int dOfWeek = cal.getDay();
        return (dOfWeek == 0);
    }

    private static boolean endOfMonth(final Date cal) {
        Date d = DateUtil.NextDayD(cal);
        int da = d.getDate();
        return da == 1;
    }

    public enum PeriodType {

        byMonth, byWeek, byDay;
    }

    public static int findIndex(List<Date> dList, Date d) {
        int i = 0;
        for (Date dd : dList) {
            if (DateUtil.eqDate(dd, d)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static List<Date> listOfDates(final Date first, final Date last,
            final PeriodType pType) {
        // get beginning of first week
        Date actC = first;
        List<Date> cDays = new ArrayList<Date>();
        boolean ladded = false;

        while (DateUtil.compareDate(actC, last) != 1) {
            boolean addd = false;
            switch (pType) {
            case byMonth:
                addd = endOfMonth(actC);
                break;
            case byWeek:
                addd = endOfWeek(actC);
                break;
            case byDay:
                addd = true;
                break;
            }
            if (addd) {
                cDays.add(actC);
                if (DateUtil.eqDate(actC, last)) {
                    ladded = true;
                }
            }
            actC = DateUtil.NextDayD(actC);
        }
        if (!ladded) {
            cDays.add(last);
        }
        return cDays;
    }
}
