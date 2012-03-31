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
package com.mygwt.client.impl.find;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.common.PersistTypeEnum;
import com.gwtmodel.table.factories.IDataPersistAction;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.mygwt.client.RemoteService;

/**
 * @author hotel
 * 
 */
class ItemDataPersistAction extends AbstractSlotContainer implements
        IDataPersistAction {


    private class ReadBackPersist implements AsyncCallback<Void> {

        private final PersistTypeEnum e;

        ReadBackPersist(PersistTypeEnum e) {
            this.e = e;
        }

        @Override
        public void onFailure(Throwable caught) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSuccess(Void result) {
            publish(dType, DataActionEnum.PersistDataSuccessSignal, e);
        }
    }

    private class PersistRecord implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {

            PersistTypeEnum e = slContext.getPersistType();
            IVModelData mData = null;
            if (e == PersistTypeEnum.MODIF || e == PersistTypeEnum.REMOVE) {
                mData = getGetterIVModelData(dType,
                        GetActionEnum.GetListLineChecked);
            }
            IVModelData pData = getGetterIVModelData(dType,
                    GetActionEnum.GetComposeModelToPersist, mData);
            ItemVData va = (ItemVData) pData;

            RemoteService.getA().ItemRecordOp(e, va.getRe(),
                    new ReadBackPersist(e));
        }

    }

    ItemDataPersistAction(IDataType dType) {
        this.dType = dType;
        registerSubscriber(dType, DataActionEnum.PersistDataAction,
                new PersistRecord());

    }

}
