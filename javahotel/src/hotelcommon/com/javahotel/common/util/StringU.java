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
package com.javahotel.common.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class StringU {

    private StringU() {
    }

    public static boolean isEmpty(final String na) {
        if (na == null) {
            return true;
        }
        if (na.trim().equals("")) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(final List col) {
        if (col == null) {
            return true;
        }
        if (col.size() == 0) {
            return true;
        }
        return false;
    }
    private static final BigInteger zero = new BigInteger("0");

    public static boolean eqZero(final BigDecimal b) {
        BigInteger bi = b.unscaledValue();
        return zero.compareTo(bi) == 0;
    }

    public static boolean eqS(final String s1, final String s2) {
        if ((s1 == null) && (s2 == null)) {
            return true;
        }
        if ((s1 == null) || (s2 == null)) {
            return false;
        }
        return s1.equals(s2);
    }

    public static String[] toA(final List<String> li) {
        String[] a = new String[li.size()];
        int i = 0;
        for (String s : li) {
            a[i++] = s;
        }
        return a;
    }
}
