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
package com.jython.ui.shared;

import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

public class MUtil {

    private MUtil() {
    }

    public static void toElem(Map<String, String> dest, Properties prop) {
        Enumeration e = prop.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            dest.put(key, prop.getProperty(key));
        }
    }

    public static BigDecimal toB(Double d) {
        if (d == null)
            return null;
        return new BigDecimal(d);
    }

    public static Double toD(BigDecimal b) {
        if (b == null)
            return null;
        return b.doubleValue();
    }
    
    public static BigDecimal roundB(BigDecimal b) {
        if (b == null)
            return null;
        return b.setScale(2, BigDecimal.ROUND_HALF_UP);
    }


}
