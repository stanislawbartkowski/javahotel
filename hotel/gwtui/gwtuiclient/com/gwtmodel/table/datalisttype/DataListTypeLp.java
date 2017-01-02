/*
 *  Copyright 2017 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.datalisttype;

import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.common.GetMaxUtil;
import com.gwtmodel.table.common.GetMaxUtil.IGetLp;
import java.util.List;

/**
 * 
 * @author hotel
 */
class DataListTypeLp extends DataListType {

    DataListTypeLp(List<IVModelData> dList, IVField comboFie) {
        super(dList, comboFie, null, null);
    }

    void addNext(AbstractLpVModelData e) {

        IGetLp<IVModelData> iget = new IGetLp<IVModelData>() {

            @Override
            public Long getLp(IVModelData o) {
                AbstractLpVModelData t = (AbstractLpVModelData) o;
                return t.getLp();
            }
        };
        Long ma = GetMaxUtil.getNextMax(dList, iget, new Long(1));
        e.setLp(ma);
        dList.add(e); // not dList.append !
    }

    // ignore row, add element always at the end
    @Override
    public void add(int row, IVModelData vData) {
        AbstractLpVModelData e = (AbstractLpVModelData) vData;
        addNext(e);
    }

}
