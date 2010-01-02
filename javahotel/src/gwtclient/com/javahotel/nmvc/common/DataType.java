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
package com.javahotel.nmvc.common;

import com.javahotel.common.command.DictType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.Utils;

public class DataType implements IDataType {

    private final DictDataEnum dcType;

    private final DataTypeSubEnum dSub;

    private final DictType dType;

    public DataType(DictType dType) {
        this.dType = dType;
        dcType = DictDataEnum.DictType;
        dSub = null;
    }

    public DataType(DictType dType, DataTypeSubEnum dSub) {
        this.dType = dType;
        dcType = DictDataEnum.DictType;
        this.dSub = dSub;
    }

    public DictType getdType() {
        return dType;
    }

    public boolean eq(IDataType dt) {
        DataType p = (DataType) dt;
        if (dType != p.getdType()) {
            return false;
        }
        return Utils.eqE(dSub, p.dSub);
    }

}
