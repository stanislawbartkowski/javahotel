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
package com.javahotel.nmvc.factories.bookingpanel.invoice;

import java.util.Map;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.editc.EditChooseRecordFactory;
import com.gwtmodel.table.editc.IChooseRecordContainer;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.javahotel.common.toobject.IField;

/**
 * @author hotel
 * 
 */
class HotelDataWidget extends AbstractSlotContainer {

    private final IChooseRecordContainer iChooseHotelData;
    private final EditChooseRecordFactory ecFactory;
    private final DrawAfterSelect aSelect;

    HotelDataWidget(IDataType dType, Map<IField, String> bList,
            MapStringField mapS, ISlotable iView, IDataType iViewType) {
        aSelect = new DrawAfterSelect(dType, bList, mapS, iView, iViewType);
        this.dType = dType;
        ecFactory = GwtGiniInjector.getI().getEditChooseRecordFactory();
        iChooseHotelData = ecFactory.constructChooseRecord(dType);
        this.setSlContainer(iChooseHotelData);
        iChooseHotelData.getSlContainer().registerSubscriber(dType,
                DataActionEnum.DrawViewComposeFormAction,
                aSelect.constructDrawListener());
    }

    @Override
    public void startPublish(CellId cellId) {
        iChooseHotelData.startPublish(cellId);
    }

    /**
     * @return the aSelect
     */
    DrawAfterSelect getaSelect() {
        return aSelect;
    }

}