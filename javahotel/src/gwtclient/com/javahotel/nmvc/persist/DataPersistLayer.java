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
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.IDataPersistAction;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.persistrecord.IPersistRecord;
import com.javahotel.client.mvc.persistrecord.IPersistResult;
import com.javahotel.client.mvc.persistrecord.PersistRecordFactory;
import com.javahotel.client.rdata.RData.IVectorList;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.nmvc.common.DataType;
import com.javahotel.nmvc.common.DataUtil;
import com.javahotel.nmvc.common.VModelData;

public class DataPersistLayer extends AbstractSlotContainer implements
        IDataPersistAction {

    private final IResLocator rI;
    private final DataType dType;
    private final PersistRecordFactory pFactory;
    private final IDataModelFactory dFactory;

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
            IVModelData mData = dFactory.construct(dType);
            IVModelData pData = getGetterIVModelData(
                    GetActionEnum.GetModelToPersist, dType, mData);
            // IVModelData mData = callGetterModelData(
            // GetActionEnum.ModelVDataPersist, dType);
            // IMPORTANT: DictType.RoomFacility - get the basic persist
            // TODO: change later, extract basic service
            IPersistRecord persist = pFactory.getPersistDict(new DictData(DictType.RoomFacility));
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

    public DataPersistLayer(IResLocator rI, PersistRecordFactory pFactory,
            IDataModelFactory dFactory, DataType dType) {
        this.pFactory = pFactory;
        this.dFactory = dFactory;
        this.rI = rI;
        this.dType = dType;
        // create subscribers - ReadList
        registerSubscriber(DataActionEnum.ReadListAction, dType, new ReadList());
        registerSubscriber(DataActionEnum.PersistDataAction, dType,
                new PersistRecord());
        // persist subscriber
        // registerSubscriber(PersistEventEnum.AddItem, dType, new
        // PersistRecord(
        // ValidateActionType.ValidateType.ADD,
        // PersistEventEnum.AddItemSuccess));
        // registerSubscriber(PersistEventEnum.RemoveItem, dType,
        // new PersistRecord(ValidateActionType.ValidateType.REMOVE,
        // PersistEventEnum.RemoveItemSuccess));
        // registerSubscriber(PersistEventEnum.ChangeItem, dType,
        // new PersistRecord(ValidateActionType.ValidateType.MODIF,
        // PersistEventEnum.ChangeItemSuccess));

        // create publisher - ListRead
        // registerPublisher(PersistEventEnum.ReadListSuccess, dType);
        // registerPublisher(PersistEventEnum.AddItemSuccess, dType);
        // registerPublisher(PersistEventEnum.ChangeItemSuccess, dType);
        // registerPublisher(PersistEventEnum.RemoveItemSuccess, dType);
    }

    public void startPublish(int cellId) {
    }

}
