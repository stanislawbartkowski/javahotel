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
package com.gwtmodel.table.persist;

import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.common.PersistTypeEnum;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;

/**
 * 
 * @author hotel
 */
public class MemoryRecordPersist extends AbstractSlotContainer {

    private final IDataListType dataList;

    private class PersistRecord implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            PersistTypeEnum persistEnumType = slContext.getPersistType();
            IVModelData pData = null;
            if (persistEnumType == PersistTypeEnum.MODIF) {
                ISlotSignalContext te = getGetterContext(dType,
                        GetActionEnum.GetListLineChecked);
                pData = te.getVData();
            }
            pData = getGetterIVModelData(dType,
                    GetActionEnum.GetComposeModelToPersist, pData);
            IVModelDataEquable eData = (IVModelDataEquable) pData;
            switch (persistEnumType) {
            case ADD:
                dataList.append(pData);
                break;
            case MODIF:
                break;
            case REMOVE:
                for (int row = 0; row < FUtils.getRowNumber(dataList); row++) {
                    IVModelDataEquable eeData = (IVModelDataEquable) FUtils
                            .getRow(dataList, row);
                    if (eData.eq(eeData)) {
                        dataList.remove(row);
                        break;
                    }
                }
                break;
            } // switch
            publish(dType, DataActionEnum.PersistDataSuccessSignal,
                    persistEnumType);
        }
    }

    public MemoryRecordPersist(IDataType dType, IDataListType dataList) {
        assert dataList != null : LogT.getT().DataListCannotBeNull();
        this.dataList = dataList;
        this.dType = dType;
        registerSubscriber(dType, DataActionEnum.PersistDataAction,
                new PersistRecord());
    }
}
