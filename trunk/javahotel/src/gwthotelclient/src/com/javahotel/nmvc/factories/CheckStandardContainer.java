/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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
package com.javahotel.nmvc.factories;

import java.util.List;

import com.gwtmodel.table.Empty;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGetDataList;
import com.gwtmodel.table.IGetDataListCallBack;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.ReadDictList;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCaller;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.view.checkstring.CheckDictModelFactory;
import com.gwtmodel.table.view.checkstring.ICheckDictModel;

class CheckStandardContainer extends AbstractSlotContainer {

    private final ICheckDictModel iCheck;
    private IDataListType dataList;
    private final InfoExtract infoExtract;
    private final GetDataList getDataList;
    private final IDataType checkType;

    interface InfoExtract {

        List<String> getString(IVModelData mData);

        void setStrings(IVModelData mData, List<String> strings,
                IDataListType dataList);
    }

    private class ChangeModeModel implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            PersistTypeEnum persistTypeEnum = slContext.getPersistType();
            iCheck.setReadOnly(persistTypeEnum.readOnly());
        }
    }

    private class SetGetter implements ISlotCaller {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            IVModelData mData = slContext.getVData();
            infoExtract.setStrings(mData, iCheck.getValues(), dataList);
            return slContext;
        }
    }

    private class DrawModel implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            IVModelData mData = slContext.getVData();
            List<String> strings = infoExtract.getString(mData);
            iCheck.setValues(strings);
        }
    }

    private class R implements ReadDictList.IListCallBack<IDataListType> {

        private final CellId cellId;

        R(CellId cellId) {
            this.cellId = cellId;
        }

        @Override
        public void setList(IDataListType dList) {
            dataList = dList;
            getDataList.setDataList(dataList);
            publish(dType, cellId, iCheck);

        }
    }

    private class GetDataList extends SynchronizeList implements IGetDataList {

        GetDataList() {
            super(2);
        }

        private IDataListType dataListType;
        private IGetDataListCallBack iCallBack;

        void setDataList(IDataListType dList) {
            dataListType = dList;
            signalDone();
        }

        @Override
        public void call(IGetDataListCallBack iCallBack) {
            this.iCallBack = iCallBack;
            signalDone();
        }

        @Override
        protected void doTask() {
            iCallBack.set(dataListType);
        }
    }

    CheckStandardContainer(IDataType publishType, IDataType cType,
            IDataType checkType, InfoExtract infoExtract) {
        this.dType = publishType;
        this.checkType = checkType;
        this.infoExtract = infoExtract;
        CheckDictModelFactory cFactory = GwtGiniInjector.getI()
                .getCheckDictModelFactory();
        getDataList = new GetDataList();
        iCheck = cFactory.construct(Empty.getFieldType(), getDataList);
        registerSubscriber(cType, DataActionEnum.DrawViewFormAction,
                new DrawModel());
        registerSubscriber(cType, DataActionEnum.ChangeViewFormModeAction,
                new ChangeModeModel());
        registerCaller(cType, GetActionEnum.GetModelToPersist, new SetGetter());
        registerCaller(cType, GetActionEnum.GetViewModelEdited, new SetGetter());
    }

    @Override
    public void startPublish(CellId cellId) {
        new ReadDictList<IDataListType>().readList(checkType, new R(cellId));
    }
}
