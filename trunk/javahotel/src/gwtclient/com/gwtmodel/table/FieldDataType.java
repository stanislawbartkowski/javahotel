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

/**
 *
 * @author perseus
 */
public class FieldDataType {

    public enum T {

        BOOLEAN, DATE, DATETIME, BIGDECIMAL,
        LONG, STRING
    };
    private final T type;
    private final int afterdot;

    public static FieldDataType contructDate() {
        return new FieldDataType(T.DATE);
    }

    public static FieldDataType contructLong() {
        return new FieldDataType(T.LONG);
    }

    public static FieldDataType contructString() {
        return new FieldDataType(T.STRING);
    }

    public static FieldDataType contructBoolean() {
        return new FieldDataType(T.BOOLEAN);
    }

    public static FieldDataType constructBigDecimal(int afterdot) {
        return new FieldDataType(T.BIGDECIMAL, afterdot);
    }

    private FieldDataType(T type, int afterdot) {
        this.type = type;
        this.afterdot = afterdot;
    }

    private FieldDataType(T type) {
        this.type = type;
        this.afterdot = 0;
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
