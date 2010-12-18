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
package com.gwtmodel.table;

import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.DateFormatUtil;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author perseus
 */
public class FUtils {

    private FUtils() {
    }

    public static int getRowNumber(IDataListType tList) {
        if (tList == null) {
            return 0;
        }
        if (tList.getList() == null) {
            return 0;
        }
        return tList.getList().size();
    }

    public static IVModelData getRow(IDataListType tList, int rowNo) {
        if (getRowNumber(tList) == 0) {
            return null;
        }
        return tList.getList().get(rowNo);
    }

    public static Object getValue(IVField f, String s) {
        if (CUtil.EmptyS(s)) {
            return null;
        }
        switch (f.getType().getType()) {
            case BIGDECIMAL:
                return Utils.toBig(s);
            case LONG:
                return Utils.toLong(s);
            case DATE:
                return DateFormatUtil.toD(s);
        }
        return s;
    }

    public static BigDecimal getValueBigDecimal(IVModelData ii, IVField f) {
        Object val = ii.getF(f);
        assertType(f, val);
        return (BigDecimal) val;
    }

    public static Long getValueLong(IVModelData ii, IVField f) {
        Object val = ii.getF(f);
        assertType(f, val);
        return (Long) val;
    }

    public static Date getValueDate(IVModelData ii, IVField f) {
        Object val = ii.getF(f);
        assertType(f, val);
        return (Date) val;
    }

    public static String getValueString(IVModelData ii, IVField f) {
        Object val = ii.getF(f);
        assertType(f, val);
        return (String) val;
    }

    public static Boolean getValueBoolean(IVModelData ii, IVField f) {
        Object val = ii.getF(f);
        assertType(f, val);
        return (Boolean) val;
    }

    private static boolean isNullBigDecimal(IVModelData ii, IVField f) {
        return getValueBigDecimal(ii, f) == null;
    }

    private static boolean isNullLong(IVModelData ii, IVField f) {
        return getValueLong(ii, f) == null;
    }

    private static boolean isNullDate(IVModelData ii, IVField f) {
        return getValueDate(ii, f) == null;
    }

    private static boolean isNullString(IVModelData ii, IVField f) {
        return CUtil.EmptyS(getValueString(ii, f));
    }

    public static boolean isNullValue(IVModelData ii, IVField f) {
        switch (f.getType().getType()) {
            case BIGDECIMAL:
                return isNullBigDecimal(ii, f);
            case LONG:
                return isNullLong(ii, f);
            case DATE:
                return isNullDate(ii, f);
        }
        return isNullString(ii, f);
    }

    private static int compString(IVModelData row, IVField f, IVModelData filter, IVField from) {
        String rS = getValueString(row, f);
        String fS = getValueString(filter, from);
        if (fS.indexOf('*') != -1) {
            // regular expression
        }
        return fS.compareTo(rS);
    }

    private static int compDate(IVModelData row, IVField f, IVModelData filter, IVField from) {
        Date rD = getValueDate(row, f);
        Date fD = getValueDate(filter, from);
        return fD.compareTo(rD);
    }

    private static int compLong(IVModelData row, IVField f, IVModelData filter, IVField from) {
        Long rI = getValueLong(row, f);
        Long fI = getValueLong(filter, from);
        return fI.compareTo(rI);
    }

    private static int compBigDecimal(IVModelData row, IVField f, IVModelData filter, IVField from) {
        BigDecimal rB = getValueBigDecimal(row, f);
        BigDecimal fB = getValueBigDecimal(filter, from);
        return fB.compareTo(rB);
    }

    public static boolean inRange(IVModelData row, IVField f, IVModelData filter, IVField from, IVField to) {
        if (isNullValue(row, f)) {
            return true;
        }
        boolean emptyfrom = isNullValue(filter, from);
        boolean emptyto = isNullValue(filter, to);
        int compfrom = 0;
        int compto = 0;
        if (!emptyfrom) {
            switch (f.getType().getType()) {
                case BIGDECIMAL:
                    compfrom = compBigDecimal(row, f, filter, from);
                    break;
                case LONG:
                    compfrom = compLong(row, f, filter, from);
                    break;
                case DATE:
                    compfrom = compDate(row, f, filter, from);
                    break;
                default:
                    compfrom = compString(row, f, filter, from);
                    break;
            }
        }
        if (!emptyto) {
            switch (f.getType().getType()) {
                case BIGDECIMAL:
                    compto = compBigDecimal(row, f, filter, to);
                    break;
                case LONG:
                    compto = compLong(row, f, filter, to);
                    break;
                case DATE:
                    compto = compDate(row, f, filter, to);
                    break;
                default:
                    compto = compString(row, f, filter, to);
                    break;
            }
        }
        if (emptyto) {
            return compfrom == 0;
        }
        if (emptyfrom) {
            return true;
        }
        return ((compfrom <= 0) && (compto >= 0));
    }

    private static String getStringS(Object o, IVField f) {
        String s = (String) o;
        return s;
    }

    private static String getDateS(Object o, IVField f) {
        Date d = (Date) o;
        return DateFormatUtil.toS(d);
    }

    private static String getLongS(Object o, IVField f) {
        Long l = (Long) o;
        if (l == null) {
            return "";
        }
        return l.toString();
    }

    private static String getBigDecimalS(Object o, IVField f) {
        BigDecimal b = (BigDecimal) o;
        if (b == null) {
            return "";
        }
        return Utils.DecimalToS(b, f.getType().getAfterdot());
    }

    public static String getValueOS(Object o, IVField f) {
        try {
            switch (f.getType().getType()) {
                case BIGDECIMAL:
                    return getBigDecimalS(o, f);
                case LONG:
                    return getLongS(o, f);
                case DATE:
                    return getDateS(o, f);
                default:
                    return getStringS(o, f);
            }
        } catch (java.lang.ClassCastException e) {
            Utils.errAlert("Błąd podczas odczytyania danych", e);
            throw e;
        }
    }

    public static String getValueS(IVModelData ii, IVField f) {
        return getValueOS(ii.getF(f), f);
    }

    private static InvalidateMess checkDate(IVField f, String s) {
        if (DateFormatUtil.toD(s) != null) {
            return null;
        }
        return new InvalidateMess(f, "Niepoprawny format daty");
    }

    private static InvalidateMess checkLong(IVField f, String s) {
        if (CUtil.OkInteger(s)) {
            return null;
        }
        return new InvalidateMess(f, "Niepoprawna liczba");
    }

    private static InvalidateMess checkBigDecimal(IVField f, String s) {
        if (CUtil.OkNumber(s)) {
            return null;
        }
        return new InvalidateMess(f, "Niepoprawna liczba");
    }

    public static InvalidateMess checkValue(IVField f, String s) {
        if (s == null) {
            return null;
        }
        switch (f.getType().getType()) {
            case BIGDECIMAL:
                return checkBigDecimal(f, s);
            case LONG:
                return checkLong(f, s);
            case DATE:
                return checkDate(f, s);
        }
        return null;
    }

    private static String assertTypeS(Object o, Class cl) {
        String s = "Expected : " + cl.getName() + " given: " + o.getClass().getName();
        Utils.errAlert(s);
        return s;
    }

    public static void assertType(IVField f, Object o) {
        if (o == null) {
            return;
        }
        switch (f.getType().getType()) {
            case BIGDECIMAL:
                assert o instanceof BigDecimal : assertTypeS(o, BigDecimal.class);
                break;
            case LONG:
                assert o instanceof Long : assertTypeS(o, Long.class);
                break;
            case DATE:
                assert o instanceof Date : assertTypeS(o, Date.class);
                break;
            case BOOLEAN:
                assert o instanceof Boolean : assertTypeS(o, Boolean.class);
                break;
            default:
                assert o instanceof String : assertTypeS(o, String.class);
                break;
        }
    }
}
