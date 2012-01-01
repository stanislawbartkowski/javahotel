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
package com.javahotel.nmvc.factories.bookingpanel.checkinguest;

import java.util.List;

import com.gwtmodel.table.IGetSetVField;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.editc.EditChooseRecordFactory;
import com.gwtmodel.table.editc.IChangeObject;
import com.gwtmodel.table.editc.IEditChooseRecordContainer;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.view.util.ClickPopUp;
import com.gwtmodel.table.view.util.FormUtil;
import com.gwtmodel.table.view.util.SetVPanelGwt;
import com.javahotel.client.types.DataType;
import com.javahotel.client.types.HModelData;
import com.javahotel.client.types.VField;
import com.javahotel.client.types.VModelDataFactory;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.nmvc.factories.booking.util.BookingCustInfo;
import com.javahotel.nmvc.factories.booking.util.ReceiveChange;

/**
 * @author hotel
 * 
 */
class DrawGuest {

    private final DataType dType;
    private final IEditChooseRecordContainer cContainer;
    private final ChoosedCustFromList choosedSignal = new ChoosedCustFromList();
    private final EditChooseRecordFactory ecFactory;

    /**
     * @return the cContainer
     */
    IEditChooseRecordContainer getcContainer() {
        return cContainer;
    }

    private final List<IGetSetVField> vList;
    private final SetVPanelGwt vP;

    private class ChangeValue implements ISlotListener {

        private final IGetSetVField iGet;

        ChangeValue(IGetSetVField iGet) {
            this.iGet = iGet;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            boolean afterFocus = SlU.afterFocus(slContext);
            if (!afterFocus) {
                return;
            }
            // apply changes cause only by focus change
            Object o = slContext.getChangedValue().getValObj();
            iGet.setValObj(o);
        }

    }

    /**
     * Raised when user chose to select customer from the list
     * 
     * @author hotel
     * 
     */
    private class ChoosedCustFromList implements ISlotListener {

        HModelData hCust;

        @Override
        public void signal(ISlotSignalContext slContext) {
            // get FormLineContainer (data have been put there)
            FormLineContainer formL = SlU.getFormLineContainer(dType,
                    cContainer);
            // retrieve to hCust
            FormUtil.copyFromViewToData(formL, hCust);
            // move to line view
            FormUtil.copyFromDataToView(hCust, vList);
        }

    }

    DrawGuest(AbstractToCheckGuest a, List<IGetSetVField> vList) {
        this.vList = vList;
        dType = new DataType(DictType.CustomerList);
        DataType dType = new DataType(DictType.CustomerList);
        ICallContext ii = GwtGiniInjector.getI().getCallContext();
        ii.setdType(dType);
        ecFactory = GwtGiniInjector.getI().getEditChooseRecordFactory();
        cContainer = ecFactory.constructEditChooseRecord(ii, dType);
        if (a.getO3().getCustomer() != null) {
            cContainer.SetNewChange(false, true);
        } else {
            cContainer.SetNewChange(true, true);
        }

        vP = new SetVPanelGwt();
        SlU.registerWidgetListener0(dType, cContainer, vP.constructSetGwt());
        cContainer.getSlContainer().registerSubscriber(
                IChangeObject.signalString, new ReceiveChange(cContainer));
        cContainer.getSlContainer().registerSubscriber(
                IChangeObject.choosedString, choosedSignal);
        SlU.startPublish0(cContainer);
        for (IGetSetVField iGet : vList) {
            SlU.registerChangeFormSubscriber(dType, cContainer, iGet.getV(),
                    new ChangeValue(iGet));
        }
    }

    void drawGuest(HModelData ha, WSize w) {
        FormUtil.copyFromViewToModel(vList, ha);
        AbstractToCheckGuest a = (AbstractToCheckGuest) ha.getA();
        choosedSignal.hCust = VModelDataFactory.construct(a.getO2());
        cContainer.getSlContainer().publish(dType,
                DataActionEnum.DrawViewComposeFormAction, choosedSignal.hCust);
        if (a.isEditable()) {
            cContainer.ChangeViewForm(PersistTypeEnum.ADD);
        } else {
            cContainer.ChangeViewForm(PersistTypeEnum.SHOWONLY);
        }
        SlU.VWidgetChangeReadOnly(dType, cContainer, new VField(
                CustomerP.F.cType), true);

        ClickPopUp p = new ClickPopUp(w, vP.constructGWidget().getGWidget());
    }

    BookingCustInfo constructCustInfo(CustomerP p) {
        return new BookingCustInfo(cContainer, p);
    }

}
