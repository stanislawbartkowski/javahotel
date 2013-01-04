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
package com.mygwt.client.impl.find;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.datalisttype.DataListTypeFactory;
import com.gwtmodel.table.factories.IDataPersistListAction;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.mygwt.client.RemoteService;
import com.mygwt.common.data.TOItemRecord;

/**
 * @author hotel
 * 
 */
class ItemListPersistAction extends AbstractSlotContainer implements
        IDataPersistListAction {

    private final DataListTypeFactory tFactory;

    private class ReadBack implements AsyncCallback<List<TOItemRecord>> {

        @Override
        public void onFailure(Throwable caught) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onSuccess(List<TOItemRecord> result) {
            List<IVModelData> vList = new ArrayList<IVModelData>();
            for (TOItemRecord to : result) {
                IVModelData v = new ItemVData(to);
                vList.add(v);
            }
            publish(dType, DataActionEnum.ListReadSuccessSignal,
                    tFactory.construct(vList, null, ItemVData.fLEVEL));

        }
    }

    private class ReadList implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            RemoteService.getA().getItemList(new ReadBack());
        }
    }

    public ItemListPersistAction(IDataType dType) {
        this.dType = dType;
        registerSubscriber(dType, DataActionEnum.ReadListAction, new ReadList());
        tFactory = GwtGiniInjector.getI().getDataListTypeFactory();
    }

}
