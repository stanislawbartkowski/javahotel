/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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

import com.gwtmodel.table.common.dateutil.DateFormatUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class CreateMonthPeList {

    private CreateMonthPeList() {
    }

    static List<YearMonthPe> createLi(List<Date> dList) {
        List<YearMonthPe> li = new ArrayList<YearMonthPe>();
        int year = -1;
        int month = -1;
        int startT = -1;
        int noD = -1;
        int inde = 0;
        for (Date d : dList) {
            int y = DateFormatUtil.getY(d);
            int m = DateFormatUtil.getM(d);
            if ((y != year) || (m != month)) {
                if (year != -1) {
                    YearMonthPe pe = new YearMonthPe(year, month, noD, startT);
                    li.add(pe);
                }
                noD = 0;
                startT = inde;
            }
            noD++;
            inde++;
            year = y;
            month = m;
        }
        if (year != -1) {
            YearMonthPe pe = new YearMonthPe(year, month, noD, startT);
            li.add(pe);
        }
        return li;
    }
}
