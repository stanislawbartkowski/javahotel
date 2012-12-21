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
package com.javahotel.statictest;

import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.dateutil.DateUtil;
import com.javahotel.common.dateutil.PeriodT;
import com.javahotel.types.DateP;
import java.util.Date;
import static org.junit.Assert.*;


/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class TestDUtil {

    static Date createD(int year, int mo, int da) {
        DateP de = new DateP();
        de.setYear(year);
        de.setMonth(mo);
        de.setDay(da);
        return de.getD();
    }
    
    static void drawP(PeriodT p) {
        String s1 = DateFormatUtil.toS(p.getFrom());
        String s2 = DateFormatUtil.toS(p.getTo());
        System.out.println(s1 + " - " + s2);
    }
    
   static void checkP(PeriodT p, int y1, int m1, int d1, int y2, int m2, int d2) {
        Date dd1 = createD(y1, m1, d1);
        Date dd2 = createD(y2, m2, d2);
        assertEquals(0, DateUtil.compareDate(dd1, p.getFrom()));
        assertEquals(0, DateUtil.compareDate(dd2, p.getTo()));
    }


}
