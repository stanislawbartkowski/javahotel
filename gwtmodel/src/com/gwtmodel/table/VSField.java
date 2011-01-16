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

/**
 *
 * @author perseus
 */
public class VSField implements IVField {

    private final String vName;
    private final FieldDataType dType;

    private VSField(String vName, FieldDataType dType) {
        this.vName = vName;
        this.dType = dType;
    }

    public FieldDataType getType() {
        return dType;
    }

    public String getId() {
        return vName;
    }

    public boolean eq(IVField o) {
        VSField v = (VSField) o;
        return CUtil.EqNS(vName, v.vName);
    }

    public static IVField createVString(String vName) {
        return new VSField(vName, FieldDataType.constructString());
    }
}
