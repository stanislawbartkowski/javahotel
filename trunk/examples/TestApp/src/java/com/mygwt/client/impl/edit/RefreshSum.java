/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
package com.mygwt.client.impl.edit;

import java.math.BigDecimal;

import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.VModelData;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlU;

/**
 * @author hotel
 * 
 */
class RefreshSum {

    static void refreshSum(IDataType dType, ISlotable iSlo) {
        IDataListType dList = SlU.getIDataListType(dType, iSlo);
        BigDecimal sum = new BigDecimal(0);
        for (IVModelData v : dList.getList()) {
            ItemVData va = (ItemVData) v;
            BigDecimal s = (BigDecimal) va.getF(ItemVData.fNUMB);
            sum = sum.add(s);
        }
        VModelData va = new VModelData();
        va.setF(ItemVData.fNUMB, sum);
        SlU.drawFooter(dType, iSlo, va);
    }

}
