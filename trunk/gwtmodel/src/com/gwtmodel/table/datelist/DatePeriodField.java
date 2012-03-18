/*
 *  Copyright 2012 stanislawbartkowski@gmail.com
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.gwtmodel.table.datelist;

import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.IVField;

/**
 *
 * @author hotel
 */
public class DatePeriodField implements IVField {

    /**
     * @return the fie
     */
    public F getFie() {
        return fie;
    }

    @Override
    public FieldDataType getType() {
        if (fie == F.COMMENT) {
            return FieldDataType.constructString();
        }
        return FieldDataType.constructDate();
    }

    @Override
    public String getId() {
        return fie.toString();
    }

    public enum F {

        DATEFROM, DATETO, COMMENT
    };
    private final F fie;

    @Override
    public boolean eq(IVField o) {
        DatePeriodField d = (DatePeriodField) o;
        return d.getFie() == getFie();
    }

    public DatePeriodField(F fie) {
        this.fie = fie;
    }
}
