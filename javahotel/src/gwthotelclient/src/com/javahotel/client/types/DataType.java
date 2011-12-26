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
package com.javahotel.client.types;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.Utils;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.types.LId;

public class DataType implements IDataType {

    private final DataTypeSubEnum dSub;

    private final DictType dType;
    private final RType rType;
    private final AddType aType;
    private final LId lParam;

    public DataType(DictType dType, LId param) {
        this.dType = dType;
        dSub = null;
        rType = null;
        aType = null;
        this.lParam = param;
    }

    /**
     * @return the param
     */
    public LId getLParam() {
        return lParam;
    }

    public DataType(DictType dType) {
        this(dType, (LId) null);
    }

    public DataType(DictType dType, DataTypeSubEnum dSub) {
        this.dType = dType;
        this.dSub = dSub;
        rType = null;
        aType = null;
        this.lParam = null;
    }

    public DataType(RType rType) {
        dType = null;
        dSub = null;
        this.rType = rType;
        aType = null;
        this.lParam = null;
    }

    public DataType(AddType aType) {
        dType = null;
        dSub = null;
        this.rType = null;
        this.aType = aType;
        this.lParam = null;
    }

    public DictType getdType() {
        return dType;
    }

    public boolean isDictType() {
        return dType != null;
    }

    public boolean isDictType(DictType d) {
        if (!isDictType()) {
            return false;
        }
        return dType == d;
    }

    public boolean isAddType() {
        return aType != null;
    }

    public boolean isRType() {
        return rType != null;
    }

    public AddType getAddType() {
        return aType;
    }

    public boolean eq(IDataType dt) {
        if (!(dt instanceof DataType)) {
            return false;
        }
        DataType p = (DataType) dt;
        if (isRType()) {
            if (!p.isRType()) {
                return false;
            }
            return rType == p.rType;
        }
        if (p.isRType()) {
            return false;
        }
        if (isDictType()) {
            if (!p.isDictType()) {
                return false;
            }
            if (dType != p.getdType()) {
                return false;
            }
            return Utils.eqE(dSub, p.dSub);
        }
        return aType == p.aType;
    }

    public RType getrType() {
        return rType;
    }

    public boolean isAllPersons() {
        return isRType() && (rType == RType.AllPersons);
    }

    public boolean isAllHotels() {
        return isRType() && (rType == RType.AllHotels);
    }

}
