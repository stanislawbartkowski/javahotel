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
package com.javahotel.client.param;

import com.javahotel.client.CommonUtil;
import com.javahotel.common.dateutil.DateUtil;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class ConfigParam {

    private ConfigParam() {
    }

    public static Date countPayAdvanceDay() {
        Date da = DateUtil.getToday();
        DateUtil.addDays(da, 3);
        return da;
    }

    public static BigDecimal coundAdvancePay(BigDecimal b) {
        if (b == null) {
            return null;
        }
        BigDecimal c1 = CommonUtil.percent0(b, new BigDecimal("20"));
        return c1;
    }

    public static String getExtyDateFormat() {
        return "Y/m/d";
    }
}
