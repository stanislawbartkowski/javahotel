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
package com.gwtmodel.table.controler;

import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.tabledef.VListHeaderDesc;

/**
 * 
 * @author perseus
 */
class FField implements IVField {

    private final IVField fie;
    private final boolean from;
    private final VListHeaderDesc v;
    private final boolean checkField;

    FField(IVField fie, boolean from, VListHeaderDesc v, boolean checkField) {
        this.fie = fie;
        this.from = from;
        this.v = v;
        this.checkField = checkField;
    }

    @Override
    public boolean eq(IVField o) {
        FField f = (FField) o;
        if (checkField != f.isCheckField()) {
            return false;
        }
        if (isFrom() != f.isFrom()) {
            return false;
        }
        return getFie().eq(f.getFie());
    }

    /**
     * @return the fie
     */
    public IVField getFie() {
        return fie;
    }

    /**
     * @return the from
     */
    public boolean isFrom() {
        return from;
    }

    /**
     * @return the v
     */
    public VListHeaderDesc getV() {
        return v;
    }

    @Override
    public FieldDataType getType() {
        if (checkField) {
            return FieldDataType.constructBoolean();
        }
        return v.getFie().getType();
    }

    @Override
    public String getId() {
        assert v.getFie().getId() != null : LogT.getT().cannotBeNull();
        return v.getFie().getId();
    }

    /**
     * @return the checkField
     */
    public boolean isCheckField() {
        return checkField;
    }

}
