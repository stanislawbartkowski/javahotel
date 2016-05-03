/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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

/**
 * Implementation of IVField based on string identifier This class is
 * implemented as factory
 * 
 * @author perseus
 */
public class VSField implements IVField {

    /** Id. */
    private final String vName;
    /** Type. */
    private final FieldDataType dType;

    /**
     * Creates FSField (private, is created through factory)
     * 
     * @param vName
     *            Id
     * @param dType
     *            Type
     */
    private VSField(String vName, FieldDataType dType) {
        this.vName = vName;
        this.dType = dType;
    }

    @Override
    public FieldDataType getType() {
        return dType;
    }

    @Override
    public String getId() {
        return vName;
    }

    @Override
    public boolean eq(IVField o) {
        if (o instanceof VSField) {
            VSField v = (VSField) o;
            return CUtil.EqNS(vName, v.vName);
        }
        return false;
    }

    /**
     * Creates VSField (type of String)
     * 
     * @param vName
     *            String id
     * @return VSField
     */
    public static IVField createVString(String vName) {
        return new VSField(vName, FieldDataType.constructString());
    }

    /**
     * Creates VSField (type of Date)
     * 
     * @param vName
     *            String id
     * @return VSField
     */
    public static IVField createVDate(String vName) {
        return new VSField(vName, FieldDataType.constructDate());
    }

    /**
     * Creates VSField (type of Integer)
     * 
     * @param vName
     *            String id
     * @return VSField
     */
    public static IVField createVInteger(String vName) {
        return new VSField(vName, FieldDataType.constructInt());
    }

    public static IVField createVDecimal(String vName) {
        return new VSField(vName, FieldDataType.constructBigDecimal());
    }

    public static IVField createVSField(String vName, FieldDataType t) {
        return new VSField(vName, t);
    }

}
