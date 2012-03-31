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
package com.javahotel.nmvc.factories.bookingpanel.invoice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.common.PersistTypeEnum;
import com.gwtmodel.table.datamodelview.SignalChangeMode;
import com.gwtmodel.table.editc.EditChooseRecordFactory;
import com.gwtmodel.table.editc.IChangeObject;
import com.gwtmodel.table.editc.IEditChooseRecordContainer;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.IField;
import com.javahotel.nmvc.factories.booking.util.BookingCustInfo;
import com.javahotel.nmvc.factories.booking.util.ReceiveChange;

/**
 * @author hotel
 * 
 */
class BuyerWidget extends AbstractSlotContainer {

    private final IEditChooseRecordContainer cContainer;
    private final EditChooseRecordFactory ecFactory;
    private final Map<IField, String> bList;
    private final MapStringField mapS;
    private final ISlotable iView;
    private final IDataType iViewType;
    private final DrawAfterSelect aSelect;
    
    private void changeMode(PersistTypeEnum pe) {
        List<IVField> fLi = new ArrayList<IVField>();
        for (Entry<IField, String> e : bList.entrySet()) {
            IVField fie = mapS.get(e.getValue());
            fLi.add(fie);
        }
        SignalChangeMode si = new SignalChangeMode(fLi, pe);
        iView.getSlContainer().publish(
                SignalChangeMode.constructSlot(iViewType), si);
    }
    
    void ChangeModeToShow() {
        changeMode(PersistTypeEnum.SHOWONLY);
        cContainer.SetNewChange(false, false);
    }
    
    private class ChangeModeAfterSelect implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            PersistTypeEnum pe = slContext.getPersistType();
            changeMode(pe);
           }
    }

    BuyerWidget(IDataType dType, Map<IField, String> bList,
            MapStringField mapS, ISlotable iView, IDataType iViewType) {
        aSelect = new DrawAfterSelect(dType, bList, mapS, iView, iViewType);
        this.dType = dType;
        this.bList = bList;
        this.mapS = mapS;
        this.iView = iView;
        this.iViewType = iViewType;
        ecFactory = GwtGiniInjector.getI().getEditChooseRecordFactory();
        ICallContext ii = GwtGiniInjector.getI().getCallContext();
        ii.setdType(dType);
        cContainer = ecFactory.constructEditChooseRecordWithoutForm(ii, dType);

        cContainer.getSlContainer().registerSubscriber(
                IChangeObject.signalString, new ReceiveChange(cContainer));
        cContainer.getSlContainer().registerSubscriber(dType,
                DataActionEnum.DrawViewComposeFormAction,
                aSelect.constructDrawListener());
        cContainer.getSlContainer().registerSubscriber(dType,
                DataActionEnum.ChangeViewComposeFormModeAction,
                new ChangeModeAfterSelect());

        this.setSlContainer(cContainer);

    }

    @Override
    public void startPublish(CellId cellId) {
        cContainer.startPublish(cellId);
    }

    /**
     * @return the aSelect
     */
    DrawAfterSelect getaSelect() {
        return aSelect;
    }

    void setBuyer(CustomerP c) {
        aSelect.setaObject(c);
    }

    BookingCustInfo constructC(IVModelData p) {
        CustomerP cust;
        if (cContainer.getNewCheck()) {
            cust = new CustomerP();
            aSelect.copyFields(p, cust);
        } else {
            cust = (CustomerP) aSelect.getaObject();
        }
        return new BookingCustInfo(cContainer, cust);
    }

}
