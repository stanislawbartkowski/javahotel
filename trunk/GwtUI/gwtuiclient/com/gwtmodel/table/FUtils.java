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
package com.gwtmodel.table;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.ConvertTT;
import com.gwtmodel.table.common.DecimalUtils;
import com.gwtmodel.table.common.TT;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.injector.MM;

/**
 * 
 * @author perseus
 */
public class FUtils {

    // methods only, do not construct
    private FUtils() {

    }

    public interface IBooleanConversion {

        String toS(Boolean b);

        Boolean toB(String s);
    }

    private static IBooleanConversion iBool;

    public static void setB(IBooleanConversion b) {
        iBool = b;
    }

    private static class ConvB implements IBooleanConversion {

        public String toS(Boolean b) {
            return b.toString();
        }

        public Boolean toB(String s) {
            return new Boolean(s);
        }
    }

    static {
        iBool = new ConvB();
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

    /**
     * Transforms string to proper object
     * 
     * @param f
     *            Object type
     * @param s
     *            String to be tranformed (empty or null will be returned as
     *            null)
     * @return Object created
     */
    public static Object getValue(IVField f, String s) {
        if (CUtil.EmptyS(s)) {
            return null;
        }
        Object o;
        switch (f.getType().getType()) {
        case DATE:
            o = Utils.toD(s);
            break;
        case DATETIME:
            o = Utils.toDT(s);
            break;
        case ENUM:
            o = f.getType().getE().toEnum(s);
            break;
        case BOOLEAN:
            o = iBool.toB(s);
            break;
        default:
            o = ConvertTT.toO(f.getType().getType(), s);
            break;

        }
        if (f.getType().getI() != null) {
            Object oo = f.getType().getI().toCustom(o);
            o = oo;
        }
        return o;
    }

    /**
     * Get value from IVModelData regarding also custom method
     * 
     * @param ii
     *            IVModelData to get from
     * @param f
     *            IVField identifier
     * @return Object
     */
    private static Object getF(IVModelData ii, IVField f) {
        if (f.getType().getiGetCustom() != null) {
            return f.getType().getiGetCustom().getF(f, ii);
        }
        return ii.getF(f);
    }

    /**
     * Get BigDecimalValue from IVModelData
     * 
     * @param ii
     *            IVModelData to get from
     * @param f
     *            IVField identifier
     * @return Object
     */
    public static BigDecimal getValueBigDecimal(IVModelData ii, IVField f) {
        Object val = getF(ii, f);
        assertType(f, val);
        return (BigDecimal) val;
    }

    public static Long getValueLong(IVModelData ii, IVField f) {
        Object val = getF(ii, f);
        assertType(f, val);
        return (Long) val;
    }

    public static Integer getValueInteger(IVModelData ii, IVField f) {
        Object val = getF(ii, f);
        assertType(f, val);
        return (Integer) val;
    }

    public static Date getValueDate(IVModelData ii, IVField f) {
        Object val = getF(ii, f);
        assertType(f, val);
        return (Date) val;
    }

    public static Timestamp getValueTimestamp(IVModelData ii, IVField f) {
        Object val = getF(ii, f);
        assertType(f, val);
        return (Timestamp) val;
    }

    public static String getValueString(IVModelData ii, IVField f) {
        Object val = getF(ii, f);
        assertType(f, val);
        return (String) val;
    }

    public static Boolean getValueBoolean(IVModelData ii, IVField f) {
        Object val = getF(ii, f);
        assertType(f, val);
        return (Boolean) val;
    }

    public static Enum<?> getValueEnum(IVModelData ii, IVField f) {
        Object val = getF(ii, f);
        assertType(f, val);
        return (Enum<?>) val;
    }

    public static Object getValue(IVModelData ii, IVField f) {
        Object o = getF(ii, f);
        if (f.getType().getI() != null) {
            if (o == null) {
                return null;
            }
            return f.getType().getI().fromCustom(o);
        }
        return o;
    }

    private static boolean isNullBigDecimal(IVModelData ii, IVField f) {
        return getValueBigDecimal(ii, f) == null;
    }

    private static boolean isNullLong(IVModelData ii, IVField f) {
        return getValueLong(ii, f) == null;
    }

    private static boolean isNullInt(IVModelData ii, IVField f) {
        return getValueInteger(ii, f) == null;
    }

    private static boolean isNullDate(IVModelData ii, IVField f) {
        return getValueDate(ii, f) == null;
    }

    private static boolean isNullTimestamp(IVModelData ii, IVField f) {
        return getValueTimestamp(ii, f) == null;
    }

    private static boolean isNullString(IVModelData ii, IVField f) {
        return CUtil.EmptyS(getValueString(ii, f));
    }

    private static boolean isNullEnum(IVModelData ii, IVField f) {
        return f.getType().getE().IsNullEnum(getValueEnum(ii, f));
    }

    /**
     * Check is field (object) is empty
     * 
     * @param ii
     *            VModelData container
     * @param f
     *            Field description
     * @return true or false (empty, not empty)
     */
    public static boolean isNullValue(IVModelData ii, IVField f) {
        if (f.getType().getI() != null) {
            Object o = getF(ii, f);
            if (o == null) {
                return true;
            }
            return f.getType().getI().isNullCustom(o);
        }
        switch (f.getType().getType()) {
        case BIGDECIMAL:
            return isNullBigDecimal(ii, f);
        case LONG:
            return isNullLong(ii, f);
        case INT:
            return isNullInt(ii, f);
        case DATE:
            return isNullDate(ii, f);
        case DATETIME:
            return isNullTimestamp(ii, f);
        case ENUM:
            return isNullEnum(ii, f);
            // cannot set boolean as null
        case BOOLEAN:
            return false;
        default:
            break;
        }
        return isNullString(ii, f);
    }

    private static int compBoolean(IVModelData row, IVField f,
            IVModelData filter, IVField from) {
        Boolean bTow = getValueBoolean(row, f);
        Boolean bFilt = getValueBoolean(filter, from);
        return bTow.compareTo(bFilt);
    }

    private static int compString(IVModelData row, IVField f,
            IVModelData filter, IVField from, boolean first,
            boolean ignorecase, boolean onlyEqual) {
        String rS = getValueString(row, f);
        String fS = getValueString(filter, from);
        String xrs = rS;
        String xfs = fS;

        if (ignorecase) {
            rS = rS.toUpperCase();
            fS = fS.toUpperCase();
        }
        // Data: 2014/10/29 : modified
        if (first) {
            // compare beginning only
            if (!onlyEqual && rS.indexOf(fS) != -1)
                return 0;
            if (onlyEqual && rS.indexOf(fS) == 0)
                return 0;
            if (fS.indexOf('*') == 0)
                return xrs.matches(xfs.substring(1)) ? 0 : -1;
        }

        // if (fS.indexOf('*') == 0)
        // return rS.matches(fS.substring(1)) ? 0 : -1;

        return fS.compareTo(rS);
    }

    private static int compDate(IVModelData row, IVField f, IVModelData filter,
            IVField from) {
        Date rD = getValueDate(row, f);
        Date fD = getValueDate(filter, from);
        return DateUtil.compareDate(fD, rD);
    }

    private static int compTimestamp(IVModelData row, IVField f,
            IVModelData filter, IVField from) {
        Timestamp rD = getValueTimestamp(row, f);
        Timestamp fD = getValueTimestamp(filter, from);
        return DateUtil.compareTimestamp(fD, rD);
    }

    private static int compLong(IVModelData row, IVField f, IVModelData filter,
            IVField from) {
        Long rI = getValueLong(row, f);
        Long fI = getValueLong(filter, from);
        return fI.compareTo(rI);
    }

    private static int compBigDecimal(IVModelData row, IVField f,
            IVModelData filter, IVField from) {
        BigDecimal rB = getValueBigDecimal(row, f);
        BigDecimal fB = getValueBigDecimal(filter, from);
        return fB.compareTo(rB);
    }

    private static int compInteger(IVModelData row, IVField f,
            IVModelData filter, IVField from) {
        Integer rB = getValueInteger(row, f);
        Integer fB = getValueInteger(filter, from);
        return fB.compareTo(rB);
    }

    public static int compareValue(IVModelData row1, IVField f1,
            IVModelData row2, IVField f2, boolean ignorecase) {
        return compareValue(row1, f1, row2, f2, ignorecase, true);
    }

    public static int compareValue(IVModelData row1, IVField f1,
            IVModelData row2, IVField f2, boolean ignorecase, boolean first) {
        boolean empty1 = isNullValue(row1, f1);
        boolean empty2 = isNullValue(row2, f2);
        if (empty1 && empty2) {
            return 0;
        }
        if (empty1) {
            return 1;
        }
        if (empty2) {
            return -1;
        }
        int comp;
        switch (f1.getType().getType()) {
        case INT:
            comp = compInteger(row1, f1, row2, f2);
            break;
        case BIGDECIMAL:
            comp = compBigDecimal(row1, f1, row2, f2);
            break;
        case LONG:
            comp = compLong(row1, f1, row2, f2);
            break;
        case DATE:
            comp = compDate(row1, f1, row2, f2);
            break;
        case DATETIME:
            comp = compTimestamp(row1, f1, row2, f2);
            break;
        case BOOLEAN:
            comp = compBoolean(row1, f1, row2, f2);
            break;
        default:
            comp = compString(row1, f1, row2, f2, first, ignorecase, true);
            break;
        }
        return comp;
    }

    private static class TempM implements IVModelData {

        private final Object o;

        TempM(Object o) {
            this.o = o;
        }

        @Override
        public Object getF(IVField fie) {
            return o;
        }

        @Override
        public void setF(IVField fie, Object o) {
        }

        @Override
        public boolean isValid(IVField fie) {
            return false;
        }

        @Override
        public List<IVField> getF() {
            return null;
        }

        @Override
        public Object getCustomData() {
            return null;
        }

        @Override
        public void setCustomData(Object o) {
        }
    }

    private static class TempV implements IVField {

        private final FieldDataType tt;

        TempV(TT type, int afterdot) {
            tt = FieldDataType.construct(type, afterdot);
        }

        @Override
        public boolean eq(IVField o) {
            return false;
        }

        @Override
        public FieldDataType getType() {
            return tt;
        }

        @Override
        public String getId() {
            return null;
        }

    }

    public static int compareValue(Object o1, Object o2, TT type, int afterdot) {
        IVModelData v1 = new TempM(o1);
        IVModelData v2 = new TempM(o2);
        IVField v = new TempV(type, afterdot);
        return compareValue(v1, v, v2, v, false, false);
    }

    /**
     * Field comparison
     * 
     * @param row
     *            Row (container) to be compared
     * @param f
     *            Field description
     * @param filter
     *            Container with filtr (search) values
     * @param from
     *            Field in filtr container (from value)
     * @param to
     *            Field in filtr container (to value)
     * @param checkE
     *            Field in filtr keeping Boolean value for equality
     * @return true (holds true, inside filter) : false : outside
     */
    public static boolean inRange(IVModelData row, IVField f,
            IVModelData filter, IVField from, IVField to, IVField checkE) {
        boolean emptyV = isNullValue(row, f);
        boolean emptyfrom = isNullValue(filter, from);
        boolean emptyto;
        if (f.getType().getType() == TT.BOOLEAN) {
            emptyto = true;
        } else {
            emptyto = isNullValue(filter, to);
        }
        boolean checkEqual = true;
        if (checkE != null) {
            Boolean checkN = getValueBoolean(filter, checkE);
            if (checkN != null) {
                checkEqual = checkN.booleanValue();
            }
        }
        if (emptyfrom && emptyto) {
            return true;
        }
        if (emptyV) {
            return false;
        }
        int compfrom = 0;
        int compto = 0;
        if (!emptyfrom) {
            switch (f.getType().getType()) {
            case INT:
                compfrom = compInteger(row, f, filter, from);
                break;
            case BIGDECIMAL:
                compfrom = compBigDecimal(row, f, filter, from);
                break;
            case LONG:
                compfrom = compLong(row, f, filter, from);
                break;
            case DATE:
                compfrom = compDate(row, f, filter, from);
                break;
            case DATETIME:
                compfrom = compTimestamp(row, f, filter, from);
                break;
            case BOOLEAN:
                compfrom = compBoolean(row, f, filter, from);
                break;
            default:
                compfrom = compString(row, f, filter, from, true, true,
                        checkEqual || !emptyto);
                break;
            }
        }
        if (!emptyto) {
            switch (f.getType().getType()) {
            case BIGDECIMAL:
                compto = compBigDecimal(row, f, filter, to);
                break;
            case INT:
                compto = compInteger(row, f, filter, to);
                break;
            case LONG:
                compto = compLong(row, f, filter, to);
                break;
            case DATE:
                compto = compDate(row, f, filter, to);
                break;
            case DATETIME:
                compto = compTimestamp(row, f, filter, to);
                break;
            case BOOLEAN:
                compto = compBoolean(row, f, filter, to);
                break;
            default:
                compto = compString(row, f, filter, to, false, true, true);
                break;
            }
        }
        // take BOOLEAN type differently
        // remove : 2012/08/13, test only the from element
        // if (f.getType().getType() == FieldDataType.T.BOOLEAN) {
        // return (compfrom == 0 || compto == 0);
        // }
        if (emptyto) {
            // if (checkEqual) {
            return compfrom == 0;
            // }
            // return compfrom <= 0;
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
        return Utils.toS(d);
    }

    private static String getTimestampS(Object o, IVField f) {
        Timestamp d = (Timestamp) o;
        return Utils.toS(d);
    }

    private static String xgetLongS(Object o, IVField f) {
        Long l = (Long) o;
        if (l == null) {
            return "";
        }
        return l.toString();
    }

    private static String xgetIntS(Object o, IVField f) {
        Integer l = (Integer) o;
        if (l == null) {
            return "";
        }
        return l.toString();
    }

    public static String xgetBigDecimalS(Object o, IVField f) {
        BigDecimal b = (BigDecimal) o;
        if (b == null) {
            return "";
        }
        return DecimalUtils.DecimalToS(b, f.getType().getAfterdot());
    }

    private static String getS(Enum<?> e) {
        return LogT.getT().errorEnum(e.toString(), e.getClass().getName());
    }

    private static String getEnum(Object o, IVField f) {
        if (o == null) {
            return "";
        }
        Enum<?> e = (Enum<?>) o;
        String va = f.getType().getE().getValueS(e.toString());
        assert va != null : getS(e);
        return va;
    }

    private static String getBoolS(Object o, IVField f) {
        if (o == null) {
            return "";
        }
        Boolean b = (Boolean) o;
        return iBool.toS(b);
    }

    /**
     * Transforms object to string value
     * 
     * @param oo
     *            Object to be transfored
     * @param f
     *            Object (field) description
     * @return String
     */
    public static String getValueOS(Object oo, IVField f) {
        Object o;
        if (f.getType().getI() != null) {
            o = f.getType().getI().fromCustom(oo);
        } else {
            o = oo;
        }
        try {
            switch (f.getType().getType()) {
            // case BIGDECIMAL:
            // return getBigDecimalS(o, f);
            // case LONG:
            // return getLongS(o, f);
            case DATE:
                return getDateS(o, f);
            case DATETIME:
                return getTimestampS(o, f);
                // case INT:
                // return getIntS(o, f);
            case ENUM:
                return getEnum(o, f);
            case BOOLEAN:
                return getBoolS(o, f);
            default:
                return ConvertTT.toS(o, f.getType().getType(), f.getType()
                        .getAfterdot());
            }
        } catch (java.lang.ClassCastException e) {
            Utils.errAlert(LogT.getT().errorWhileReading(f.getId()), e);
            throw e;
        }
    }

    public static String getValueS(Object o, TT type, int afterdot) {
        return getValueOS(o, new TempV(type, afterdot));
    }

    /**
     * Transforms object from container to string
     * 
     * @param ii
     *            Container (row)
     * @param f
     *            Field description
     * @return String
     */
    public static String getValueS(IVModelData ii, IVField f) {
        return getValueOS(ii.getF(f), f);
    }

    private static InvalidateMess checkDate(IVField f, String s) {
        if (Utils.toD(s) != null) {
            return null;
        }
        return new InvalidateMess(f, MM.getL().DateFormNotValid());
    }

    private static InvalidateMess checkLong(IVField f, String s) {
        if (CUtil.OkInteger(s)) {
            return null;
        }
        return new InvalidateMess(f, MM.getL().NumberNotValid());
    }

    private static InvalidateMess checkBigDecimal(IVField f, String s) {
        if (CUtil.OkNumber(s)) {
            return null;
        }
        return new InvalidateMess(f, MM.getL().NumberNotValid());
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
        default:
            break;
        }
        return null;
    }

    public static String constructAssertS(Object o,
            @SuppressWarnings("rawtypes") Class cl) {
        String s = LogT.getT().assertT(cl.getName(), o.getClass().getName());
        return s;

    }

    private static String assertTypeS(Object o, Class<?> cl) {
        String s = constructAssertS(o, cl);
        Utils.errAlert(s);
        return s;
    }

    /**
     * Check if object is of type according to FieldType Very important to be
     * performed here to avoid error propagation
     * 
     * @param f
     *            Object identifier
     * @param o
     *            Object itself Throws exception if not valid
     */
    public static void assertType(IVField f, Object o) {
        if (o == null) {
            return;
        }
        if (f.getType().getI() != null) {
            String s = f.getType().getI().assertS(o);
            assert s == null : s;
            return;
        }
        switch (f.getType().getType()) {
        case BIGDECIMAL:
            assert o instanceof BigDecimal : assertTypeS(o, BigDecimal.class);
            break;
        case INT:
            assert o instanceof Integer : assertTypeS(o, Integer.class);
            break;
        case LONG:
            assert o instanceof Long : assertTypeS(o, Long.class);
            break;
        case DATE:
            assert o instanceof Date : assertTypeS(o, Date.class);
            break;
        case DATETIME:
            assert o instanceof Timestamp : assertTypeS(o, Timestamp.class);
            break;
        case BOOLEAN:
            assert o instanceof Boolean : assertTypeS(o, Boolean.class);
            break;
        case ENUM:
            String s = f.getType().getE().assertS(o);
            assert s == null : s;
            break;
        default:
            assert o instanceof String : assertTypeS(o, String.class);
            break;
        }
    }
}
