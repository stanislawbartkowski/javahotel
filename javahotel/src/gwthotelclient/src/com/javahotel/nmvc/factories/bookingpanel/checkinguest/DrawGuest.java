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
package com.javahotel.nmvc.factories.bookingpanel.checkinguest;

import java.util.List;

import com.gwtmodel.table.IGetSetVField;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.editc.EditChooseRecordFactory;
import com.gwtmodel.table.editc.IEditChooseRecordContainer;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.view.util.ClickPopUp;
import com.gwtmodel.table.view.util.FormUtil;
import com.gwtmodel.table.view.util.SetVPanelGwt;
import com.javahotel.client.types.DataType;
import com.javahotel.client.types.DataTypeSubEnum;
import com.javahotel.client.types.HModelData;
import com.javahotel.client.types.VField;
import com.javahotel.client.types.VModelDataFactory;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.CustomerP;

/**
 * @author hotel
 * 
 */
class DrawGuest {

    private DrawGuest() {

    }

    private static class ChangeValue implements ISlotSignaller {

        private final IGetSetVField iGet;

        ChangeValue(IGetSetVField iGet) {
            this.iGet = iGet;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            Object o = slContext.getChangedValue().getValObj();
            iGet.setValObj(o);
        }

    }

    static void drawGuest(IVField vie, IVModelData v, WSize w,
            List<IGetSetVField> vList) {
        DataType dType = new DataType(DictType.CustomerList);
        ICallContext ii = GwtGiniInjector.getI().getCallContext();
        ii.setdType(dType);
        // ii.setiSlo(this);
        DataType subType = new DataType(dType.getdType(), DataTypeSubEnum.Sub1);
        IEditChooseRecordContainer cContainer = EditChooseRecordFactory
                .constructEditChooseRecord(ii, dType, subType);
        SetVPanelGwt vP = new SetVPanelGwt();
        cContainer.getSlContainer().registerSubscriber(dType, 0,
                vP.constructSetGwt());
        SlU.VWidgetChangeReadOnly(dType, cContainer, new VField(
                CustomerP.F.cType), true);
        cContainer.startPublish(new CellId(0));
        HModelData ha = (HModelData) v;
        FormUtil.copyFromViewToModel(vList, ha);
        AbstractToCheckGuest a = (AbstractToCheckGuest) ha.getA();
        HModelData hCust = VModelDataFactory.construct(a.getO2());
        cContainer.getSlContainer().publish(dType,
                DataActionEnum.DrawViewComposeFormAction, hCust);
        for (IGetSetVField iGet : vList) {
            SlU.registerChangeFormSubscriber(dType, cContainer, iGet.getV(),
                    new ChangeValue(iGet));
        }
        ClickPopUp p = new ClickPopUp(w, vP.constructGWidget().getGWidget());
    }

}
