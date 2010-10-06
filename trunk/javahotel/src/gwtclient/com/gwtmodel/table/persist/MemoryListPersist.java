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
package com.gwtmodel.table.persist;

import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;

public class MemoryListPersist extends AbstractSlotContainer implements
        IMemoryListModel {

    private final IDataType dType;
    private IDataListType dataList;

    public IDataListType getDataList() {
        return dataList;
    }

    public void setDataList(IDataListType dataList) {
        this.dataList = dataList;
    }

    private class PersistRecord implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            PersistTypeEnum persistEnumType = slContext.getPersistType();
            IVModelData pData = getGetterIVModelData(
                    GetActionEnum.GetComposeModelToPersist, dType);
            IVModelDataEquable eData = (IVModelDataEquable) pData;
            switch (persistEnumType) {
            case ADD:
                dataList.append(pData);
                break;
            case MODIF:
                break;
            case REMOVE:
                for (int row = 0; row < dataList.rowNo(); row++) {
                    IVModelDataEquable eeData = (IVModelDataEquable) dataList
                            .getRow(row);
                    if (eData.eq(eeData)) {
                        dataList.remove(row);
                        break;
                    }
                }
                break;
            } // switch
            publish(DataActionEnum.PersistDataSuccessSignal, dType,
                    persistEnumType);
        }

    }

    private class ReadList implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            publish(DataActionEnum.ListReadSuccessSignal, dType, dataList);
        }
    }

    public MemoryListPersist(IDataType dType) {
        this.dType = dType;
        // create subscribers - ReadList
        registerSubscriber(DataActionEnum.ReadListAction, dType, new ReadList());
        registerSubscriber(DataActionEnum.PersistDataAction, dType,
                new PersistRecord());
        // persist subscriber
    }

    public void startPublish(CellId cellId) {
    }

}
