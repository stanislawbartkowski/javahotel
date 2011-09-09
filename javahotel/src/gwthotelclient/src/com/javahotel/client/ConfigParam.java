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

import com.javahotel.common.dateutil.DateUtil;
import com.javahotel.common.dateutil.GetPeriods.StartWeek;

/**
 * 
 * @author stanislawbartkowski@gmail.com Some general configuration parameters
 */

public class ConfigParam {

    private ConfigParam() {
    }

    /**
     * Default number of days for advance payment
     * 
     * @return Number of days
     */
    public static Date countPayAdvanceDay() {
        Date da = DateUtil.getToday();
        DateUtil.addDays(da, 3);
        return da;
    }

    /**
     * Default day is regarded as the start of the weekend
     * 
     * @return The day of the week as the start of the weekend
     */
    public static StartWeek getStartWeek() {
        return StartWeek.onFriday;
    }
}
