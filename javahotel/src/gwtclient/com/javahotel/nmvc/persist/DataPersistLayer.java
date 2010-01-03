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
package com.javahotel.nmvc.persist;

import java.util.List;

import com.gwtmodel.table.DataListType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.factories.IDataPersistAction;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.javahotel.client.IResLocator;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.rdata.RData.IVectorList;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.nmvc.common.DataType;
import com.javahotel.nmvc.common.DataUtil;
import com.javahotel.nmvc.persist.dict.IPersistRecord;
import com.javahotel.nmvc.persist.dict.IPersistResult;
import com.javahotel.nmvc.persist.dict.PersistRecordDict;

public class DataPersistLayer extends AbstractSlotContainer implements
        IDataPersistAction {

    private final IResLocator rI;
    private final DataType dType;

    private class ReadListDict implements IVectorList {

        public void doVList(final List<? extends AbstractTo> val) {
            DataListType dataList = DataUtil.construct(val);
            publish(DataActionEnum.ListReadSuccessSignal, dType, dataList);
        }
    }

    private class AfterPersist implements IPersistResult {

        private final PersistTypeEnum persistTypeEnum;

        AfterPersist(PersistTypeEnum persistTypeEnum) {
            this.persistTypeEnum = persistTypeEnum;
        }

        public void success(PersistResultContext re) {
            publish(DataActionEnum.PersistDataSuccessSignal, dType,
                    persistTypeEnum);
        }

    }

    private class PersistRecord implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            IVModelData pData = getGetterIVModelData(
                    GetActionEnum.GetComposeModelToPersist, dType);
            IPersistRecord persist = new PersistRecordDict(rI, dType.getdType());
            int action = DataUtil.vTypetoAction(slContext.getPersistType());
            RecordModel mo = DataUtil.toRecordModel(pData);
            persist.persist(action, mo, new AfterPersist(slContext
                    .getPersistType()));
        }

    }

    private class ReadList implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            CommandParam co = rI.getR().getHotelCommandParam();
            co.setDict(dType.getdType());
            rI.getR().getList(RType.ListDict, co, new ReadListDict());
        }
    }

    public DataPersistLayer(IResLocator rI, DataType dType) {
        this.rI = rI;
        this.dType = dType;
        // create subscribers - ReadList
        registerSubscriber(DataActionEnum.ReadListAction, dType, new ReadList());
        registerSubscriber(DataActionEnum.PersistDataAction, dType,
                new PersistRecord());
        // persist subscriber
    }

    public void startPublish(int cellId) {
    }

}
