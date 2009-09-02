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
package com.javahotel.client;

import java.math.BigDecimal;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.javahotel.client.dialog.IPersistAction;
import com.javahotel.common.command.PersistType;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.dateutil.DateUtil;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class CommonUtil {

    private CommonUtil() {
    }

    public static HTML createHTML(final String s) {
        HTML ha = new HTML("<a href='javascript:;'>" + s + "</a>");
        return ha;
    }

    public static String getImageAdr(final String image) {
        String path;
        path = "com.javahotel.web";
        return path + "/img/" + image;
    }

    public static String getResAdr(final String res) {
        String path;
        path = "com.javahotel.web";
        return path + "/res/" + res;
    }

    public static boolean IsScript() {
        return GWT.isScript();
    }

    public static String getImageHTML(final String imageUrl) {
        String s = "<td><img src='" + getImageAdr(imageUrl) + "'></td>";
        return s;
    }

    public static String getCookieVal(final String pName) {
        String v = Cookies.getCookie(pName);
        if (v == null) {
            return null;
        }
        if (v.trim().equals("")) {
            return null;
        }
        return v.trim();
    }

    public static void setCookieVal(final String pName, final String pVal) {
        Date now = new Date();
        long nowLong = now.getTime();
        nowLong = nowLong + (1000 * 60 * 60 * 24 * 7);// seven days
        now.setTime(nowLong);
        Cookies.setCookie(pName, pVal, now);
    }

    public static void removeCookie(final String pName) {
        Cookies.removeCookie(pName);
    }
    public static final int BADNUMBER = -1;

    public static int getNum(final String s) {
        if ((s == null) || s.equals("")) {
            return BADNUMBER;
        }
        int i;
        try {
            i = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return BADNUMBER;
        }
        return i;
    }

    public static BigDecimal toBig(final String s) {
        if (s == null) {
            return null;
        }
        if (s.equals("")) {
            return null;
        }
        return new BigDecimal(s);
    }

    public static BigDecimal toBig(final Double d) {
        if (d == null) {
            return null;
        }
        String s = Double.toString(d);
        return new BigDecimal(s);
    }

    public static Double toDouble(final BigDecimal b) {
        if (b == null) {
            return null;
        }
        String s = DecimalToS(b);
        return new Double(s);
    }

    public static String DecimalToS(final BigDecimal c) {
        int l = c.intValue();
        String sl = Integer.toString(l);
        BigDecimal re = new BigDecimal(sl);
        int rest = c.subtract(re).intValue();
        String sr = DateFormatUtil.toNS(rest, 2);
        String st = sl + "." + sr;
        return st;
    }

    public static BigDecimal percent0(final BigDecimal c,
            final BigDecimal percent) {
        BigDecimal res = c.multiply(percent);
        BigDecimal l100 = new BigDecimal("100");
        BigDecimal res1 = res.divide(l100, 2, 0);
        // int l = res1.intValue();
        // return new BigDecimal(Integer.toString(l));
        return res1;
    }

    public static void adjustCols(final FlexTable ftable, final int row,
            final int aCols) {

        int aRows = ftable.getCellCount(row);
        if (aCols < aRows) {
            ftable.removeCells(row, aCols, aRows - aCols);
        }
    }

    public static String getLodgingS(final Date from, final Date to) {
        Date d1 = DateUtil.copyDate(to);
        DateUtil.NextDay(d1);
        String s = DateFormatUtil.toS(from);
        String s1 = DateFormatUtil.toS(d1);
        String st = "Nocleg : " + s + " - " + s1;
        return st;
    }

    public static String getBookingS(final Date from, final Date to) {
        Date d1 = DateUtil.copyDate(to);
        DateUtil.NextDay(d1);
        String s = DateFormatUtil.toS(from);
        String s1 = DateFormatUtil.toS(d1);
        String st = "Rezerwacja, przyjazd : " + s + " wyjazd:" + s1;

        return st;
    }

    public static String noLodgings(final Date from, final Date to) {
        int noD = DateUtil.noLodgings(from, to);
        String st = "Liczba noclegów:" + noD;
        return st;
    }

    public static int getWidthPix(String s) {
        int len = s.length();
        return len * 10;
    }

    public static PersistType getPType(int pAction) {
        switch (pAction) {
            case IPersistAction.ADDACION:
                return PersistType.ADD;
            case IPersistAction.DELACTION:
                return PersistType.REMOVE;
            case IPersistAction.MODIFACTION:
                return PersistType.CHANGE;
        }
        return null;
    }
}
