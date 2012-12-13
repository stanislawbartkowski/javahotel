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
package com.gwtmodel.table;

import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.rdef.IFormLineView;
import java.util.List;

/**
 * 
 * @author perseus
 */
public class FieldDataType {

    /**
     * Custom access to field value (not by IVModelData.getF and
     * IVModelData.setF
     * 
     * @author hotel
     * 
     */
    public interface IGetCustomValue {
        Object getF(IVField fie, IVModelData v);

        void setF(IVField fie, IVModelData v, Object o);

    }

    /**
     * Construct empty IGetCuctomValue interface
     * 
     * @return IGetCustomValue - which does nothing
     */
    public static IGetCustomValue constructEmptyCustomValue() {
        return new IGetCustomValue() {

            @Override
            public void setF(IVField fie, IVModelData v, Object o) {
            }

            @Override
            public Object getF(IVField fie, IVModelData v) {
                return null;
            }
        };
    }

    public interface IFormLineViewFactory {

        IFormLineView construct(IVField v);
    }

    /**
     * @return the i
     */
    public ICustomType getI() {
        return i;
    }

    /**
     * @return the e
     */
    public IEnumType getE() {
        return e;
    }

    /**
     * @return the li
     */
    public IGetListValues getLi() {
        return li;
    }

    public interface ICustomType {

        Object fromCustom(Object sou);

        Object toCustom(Object sou);

        String assertS(Object sou);

        boolean isNullCustom(Object sou);
    }

    public interface IGetListValues {

        List<IMapEntry> getList();
    }

    public interface IEnumType {

        boolean IsNullEnum(Enum<?> e);

        Enum<?> toEnum(String e);

        String assertS(Object sou);

        String getValueS(String esou);

        List<String> getValues();
    }

    public enum T {

        BOOLEAN, DATE, DATETIME, BIGDECIMAL, LONG, INT, STRING, ENUM
    };

    private final T type;
    private final int afterdot;
    private final ICustomType i;
    private final IEnumType e;
    private final IGetListValues li;
    private final IFormLineViewFactory iFactory;
    private final IGetCustomValue iGetCustom;

    private static int D(T type) {
        return type == T.BIGDECIMAL ? IConsts.defaultDecimal : 0;
    }

    public static FieldDataType constructCustom(T type, IGetCustomValue iGet) {
        return new FieldDataType(type, D(type), null, null, null, null, iGet);

    }

    /**
     * @return the iGetCustom
     */
    IGetCustomValue getiGetCustom() {
        return iGetCustom;
    }

    public static FieldDataType constructDate() {
        return new FieldDataType(T.DATE);
    }

    public static FieldDataType constructLong() {
        return new FieldDataType(T.LONG);
    }

    public static FieldDataType constructInt() {
        return new FieldDataType(T.INT);
    }

    public static FieldDataType constructString() {
        return new FieldDataType(T.STRING);
    }

    public static FieldDataType constructString(IFormLineViewFactory iFact) {
        return new FieldDataType(T.STRING, 0, null, null, null, iFact, null);
    }

    public static FieldDataType constructString(ICustomType i) {
        return new FieldDataType(T.STRING, 0, i, null, null, null, null);
    }

    public static FieldDataType constructString(ICustomType i,
            IFormLineViewFactory iFact) {
        return new FieldDataType(T.STRING, 0, i, null, null, iFact, null);
    }

    public static FieldDataType constructBoolean() {
        return new FieldDataType(T.BOOLEAN);
    }

    public static FieldDataType constructBigDecimal(int afterdot) {
        return new FieldDataType(T.BIGDECIMAL, afterdot);
    }

    public static FieldDataType constructBigDecimal() {
        return new FieldDataType(T.BIGDECIMAL);
    }

    public static FieldDataType constructEnum(IEnumType e) {
        return new FieldDataType(T.ENUM, 0, null, e, null, null, null);
    }

    public static FieldDataType constructStringList(IGetListValues gVal) {
        return new FieldDataType(T.STRING, 0, null, null, gVal, null, null);
    }

    public static FieldDataType constructLongList(IGetListValues gVal) {
        return new FieldDataType(T.LONG, 0, null, null, gVal, null, null);
    }

    private FieldDataType(T type, int afterdot, ICustomType i, IEnumType e,
            IGetListValues li, IFormLineViewFactory iFactory,
            IGetCustomValue iGet) {
        this.type = type;
        this.afterdot = afterdot;
        this.i = i;
        this.e = e;
        this.li = li;
        this.iFactory = iFactory;
        this.iGetCustom = iGet;
    }

    private FieldDataType(T type) {
        this(type, D(type), null, null, null, null, null);
    }

    private FieldDataType(T type, int afterdot) {
        this(type, afterdot, null, null, null, null, null);
    }

    /**
     * @return the iFactory
     */
    public IFormLineViewFactory getiFactory() {
        return iFactory;
    }

    /**
     * @return the type
     */
    public T getType() {
        return type;
    }

    /**
     * @return the afterdot
     */
    public int getAfterdot() {
        return afterdot;
    }

    public boolean isConvertableToString() {
        return li != null;
    }

    public String convertToString(String o) {
        if (o == null) {
            return null;
        }
        for (IMapEntry ii : li.getList()) {
            if (CUtil.EqNS(o, ii.getKey())) {
                return ii.getValue();
            }
        }
        return "unknown";
    }

    public boolean isBoolean() {
        return type == T.BOOLEAN;
    }
}
