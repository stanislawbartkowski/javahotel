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
package com.javahotel.client;

import java.util.Date;

import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.dateutil.DateUtil;

/**
 * @author hotel
 * Static class providing different simple utils for dialogs
 *
 */
public class DUtil {
    
    /**
     * Static class, disables contructor
     */
    private DUtil() {
        
    }
    
    /**
     * Info about number of days for reservation
     * @param from First date of the period
     * @param to Last date
     * @return String with info 
     */
    public static String getLodgingS(final Date from, final Date to) {
        Date d1 = DateUtil.copyDate(to);
        DateUtil.NextDay(d1);
        String s = DateFormatUtil.toS(from);
        String s1 = DateFormatUtil.toS(d1);
        String st = "Nocleg : " + s + " - " + s1;
        return st;
    }

    /**
     * Info with general information on reservation
     * @param from First date
     * @param to Last date
     * @return String with info
     */
    public static String getBookingS(final Date from, final Date to) {
        Date d1 = DateUtil.copyDate(to);
        DateUtil.NextDay(d1);
        String s = DateFormatUtil.toS(from);
        String s1 = DateFormatUtil.toS(d1);
        String st = "Rezerwacja, przyjazd : " + s + " wyjazd:" + s1;

        return st;
    }


}
