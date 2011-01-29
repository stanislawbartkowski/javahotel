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
package com.gwtmodel.table;

import java.util.List;

/**
 *
 * @author perseus
 */
public class FieldDataType {

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

        boolean IsNullEnum(Enum e);

        Enum toEnum(String e);

        String assertS(Object sou);

        String getValueS(String esou);

        List<String> getValues();
    }

    public enum T {

        BOOLEAN, DATE, DATETIME, BIGDECIMAL,
        LONG, INT, STRING, ENUM
    };
    private final T type;
    private final int afterdot;
    private final ICustomType i;
    private final IEnumType e;
    private final IGetListValues li;

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

    public static FieldDataType constructString(ICustomType i) {
        return new FieldDataType(T.STRING, 0, i, null, null);
    }

    public static FieldDataType constructBoolean() {
        return new FieldDataType(T.BOOLEAN);
    }

    public static FieldDataType constructBigDecimal(int afterdot) {
        return new FieldDataType(T.BIGDECIMAL, afterdot);
    }

    public static FieldDataType constructBigDecimal() {
        return new FieldDataType(T.BIGDECIMAL, IConsts.defaultDecimal);
    }

    public static FieldDataType constructEnum(IEnumType e) {
        return new FieldDataType(T.ENUM, 0, null, e, null);
    }

    public static FieldDataType constructStringList(IGetListValues gVal) {
        return new FieldDataType(T.STRING, 0, null, null, gVal);
    }

    public static FieldDataType constructLongList(IGetListValues gVal) {
        return new FieldDataType(T.LONG, 0, null, null, gVal);
    }

    private FieldDataType(T type, int afterdot, ICustomType i, IEnumType e,
            IGetListValues li) {
        this.type = type;
        this.afterdot = afterdot;
        this.i = i;
        this.e = e;
        this.li = li;
    }

    private FieldDataType(T type) {
        this(type, 0, null, null, null);
    }

    private FieldDataType(T type, int afterdot) {
        this(type, afterdot, null, null, null);
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
}
