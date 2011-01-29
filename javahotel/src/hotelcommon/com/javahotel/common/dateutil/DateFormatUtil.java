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
package com.javahotel.common.dateutil;

import com.javahotel.types.DateP;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class DateFormatUtil {

    private static int toI(final String s, final int len)
            throws NumberFormatException {
        if (s.length() != len) {
            throw new NumberFormatException();
        }
        try {
            Integer i = new Integer(s);
            return i.intValue();
        } catch (NumberFormatException e) {
            throw e;
        }
    }

    public static String toNS(final int nu, final int ma) {
        String s = new Integer(nu).toString();
        while (s.length() < ma) {
            s = "0" + s;
        }
        return s;
    }

    public static int getY(final Date d) {
        int year = d.getYear() + 1900;
        return year;
    }

    public static int getM(final Date d) {
        int mo = d.getMonth() + 1;
        return mo;
    }

    public static int getD(final Date d) {
        int da = d.getDate();
        return da;
    }

    public static String toS(final Date d) {
        if (d == null) {
            return "";
        }
        int year = getY(d);
        int mo = getM(d);
        int da = getD(d);
        String s = toNS(year, 4) + "/" + toNS(mo, 2) + "/" + toNS(da, 2);
        return s;
    }

    public static String toTS(final Timestamp d) {
        if (d == null) {
            return "";
        }
        String s = toS(d);
        int h = d.getHours();
        int m = d.getMinutes();
        int ss = d.getSeconds();
        String s1 = toNS(h, 2) + ":" + toNS(m, 2) + ":" + toNS(ss, 2);

        return s + " " + s1;
    }

    private static boolean setD(final Date dd, final String s) {
        String a[] = s.split("/");
        if (a.length != 3) {
            return false;
        }
        String yS = a[0];
        String mS = a[1];
        String dS = a[2];
        try {
            int y = toI(yS, 4);
            if (y <= 1900) {
                return false;
            }
            int d = toI(dS, 2);
            int m = toI(mS, 2);
            if ((m < 1) || (m > 12)) {
                return false;
            }
            toD(dd, y, m, d);

            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    private static boolean setM(final Date dd, final String ss) {
        String a[] = ss.split(":");
        if (a.length != 3) {
            return false;
        }
        String hS = a[0];
        String mS = a[1];
        String sS = a[2];
        try {
            int h = toI(hS, 2);
            int m = toI(mS, 2);
            int s = toI(sS, 2);
            if ((h > 23) || (m > 63) || (s > 63)) {
                return false;
            }
            dd.setHours(h);
            dd.setMinutes(m);
            dd.setSeconds(s);

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Date toD(final String s) {
        Date d = new Date();
        if (!setD(d, s)) {
            return null;
        }
        return d;
    }

    private static void toD(Date dd, int y, int m, int d) {
        dd.setYear(y - 1900);
        dd.setMonth(m - 1);
        dd.setDate(d);

        dd.setHours(DateP.DEFH);
        dd.setMinutes(DateP.DEFM);
        dd.setSeconds(DateP.DEFS);
    }

    public static Date toD(int y, int m, int d) {
        Date dd = new Date();
        toD(dd, y, m, d);
        return dd;
    }

    public static Timestamp toT(final String s) {
        String a[] = s.split(" ");
        if ((a.length == 0) || (a.length > 2)) {
            return null;
        }
        Date d = new Date();
        if (!setD(d, a[0])) {
            return null;
        }
        if (a.length == 1) {
            return new Timestamp(d.getTime());
        }
        if (!setM(d, a[1])) {
            return null;
        }
        return new Timestamp(d.getTime());
    }
}
