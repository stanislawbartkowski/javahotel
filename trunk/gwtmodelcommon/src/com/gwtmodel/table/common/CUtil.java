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
package com.gwtmodel.table.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author perseus
 */
public class CUtil {

    private CUtil() {
    }

    public static boolean EmptyS(String s) {
        if (s == null) {
            return true;
        }
        return s.trim().equals("");
    }

    public static String emptyToS(String s) {
        if (s == null) {
            return null;
        }
        return "";
    }

    public static boolean OkNumber(String s) {
        try {
            double d = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static String toAfterS(String s, int afterdot) {
        String a[] = s.split("\\.");
        String beforeS = a[0];
        String afterS = "";
        String ss;
        if (a.length > 1) {
            afterS = a[1];
        }
        while (afterS.length() < afterdot) {
            afterS += "0";
        }
        afterS = afterS.substring(0, afterdot);
        if (afterdot > 0) {
            ss = beforeS + "." + afterS;
        } else {
            ss = beforeS.trim();
        }
        return ss;
    }

    public static boolean EqNS(String p1, String p2) {
        // important: if both null that not equal
        if ((p1 == null) || (p2 == null)) {
            return false;
        }
        return p1.equalsIgnoreCase(p2);
    }

    public static boolean EqDS(Date d1, Date d2) {
        if ((d1 == null) && (d2 == null)) {
            return true;
        }
        if ((d1 == null) || (d2 == null)) {
            return false;
        }
        return d1.equals(d2);
    }

    public static Integer getInteger(String s) {
        Integer i = new Integer(s);
        return i;
    }

    public static boolean OkInteger(String s) {
        try {
            Long d = Long.parseLong(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static int getNumb(String s) {
        Integer i = getInteger(s);
        return i.intValue();
    }

    public static String NumbToS(int i) {
        return Integer.toString(i);
    }

    public static Long LToL(Object val) {
        if (val instanceof String) {
            String s = (String) val;
            return new Long(s);
        }
        return (Long) val;
    }

    public static String toNS(final int nu, final int ma) {
        String s = new Integer(nu).toString();
        while (s.length() < ma) {
            s = "0" + s;
        }
        return s;
    }

    public static <T> List<T> arrayToL(T[] l) {
        List<T> aL = new ArrayList<T>();
        for (T t : l) {
            aL.add(t);
        }
        return aL;

    }
}
