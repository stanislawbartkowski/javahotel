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
package com.gwtmodel.table;

import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.persist.IVModelDataEquable;
import java.util.List;

/**
 *
 * @author hotel
 */
abstract public class AbstractLpVModelData implements IVModelDataEquable {

    private Long lp;
    private Object o;

    @Override
    public Object getF(IVField fie) {
        return lp;
    }

    @Override
    public void setF(IVField fie, Object o) {
        lp = CUtil.LToL(o);
    }

    @Override
    public boolean isValid(IVField fie) {
        if (fie instanceof L) {
            return true;
        }
        return false;
    }

    protected List<IVField> addV(List<IVField> li) {
        li.add(new L());
        return li;
    }

    Object getL(IVField f) {
        return lp;
    }

    void setL(IVField f, Object o) {
        lp = (Long) o;
    }

    static private class L implements IVField {

        @Override
        public boolean eq(IVField o) {
            if (o instanceof L) {
                return true;
            }
            return false;
        }

        @Override
        public FieldDataType getType() {
            return FieldDataType.constructDate();
        }

        @Override
        public String getId() {
            return "XX";
        }
    }

    /**
     * @return the lp
     */
    long getLp() {
        return lp;
    }

    /**
     * @param lp the lp to set
     */
    void setLp(long lp) {
        this.lp = lp;
    }

    @Override
    public boolean eq(IVModelDataEquable o) {
        AbstractLpVModelData aa = (AbstractLpVModelData) o;
        return aa.lp.equals(lp);
    }

    @Override
    public Object getCustomData() {
        return o;
    }

    @Override
    public void setCustomData(Object o) {
        this.o = o;
    }
}
