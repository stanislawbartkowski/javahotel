/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.javahotel.common.math;

import java.math.BigDecimal;

public class MathUtil {

    private MathUtil() {
    }

    public static BigDecimal addB(final BigDecimal b1, final BigDecimal b2) {
        return b1.add(b2);
    }

    public static BigDecimal multI(final BigDecimal b, int no) {
        BigDecimal n = new BigDecimal(no);
        return n.multiply(b);
    }

    public static BigDecimal getO() {
        return new BigDecimal(0);
    }
}
