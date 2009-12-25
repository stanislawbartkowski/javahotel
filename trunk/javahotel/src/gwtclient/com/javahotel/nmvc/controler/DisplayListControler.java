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
package com.javahotel.nmvc.controler;

import java.util.List;

import com.javahotel.client.IResLocator;
import com.javahotel.nmvc.common.AbstractSlotContainer;
import com.javahotel.nmvc.common.DataType;
import com.javahotel.nmvc.listdataview.IListDataView;
import com.javahotel.nmvc.listdataview.ListDataViewFactory;
import com.javahotel.nmvc.listheadermodel.ListHeaderData;
import com.javahotel.nmvc.listheadermodel.ListHeaderDesc;
import com.javahotel.nmvc.modeldatat.HeaderModelDataFactory;
import com.javahotel.nmvc.panelview.IPanelView;
import com.javahotel.nmvc.panelview.PanelViewFactory;
import com.javahotel.nmvc.persist.DataPersistActionFactory;
import com.javahotel.nmvc.persist.DataPersistEnum;
import com.javahotel.nmvc.persist.IDataPersistAction;
import com.javahotel.nmvc.slotmediator.ISlotMediator;
import com.javahotel.nmvc.slotmediator.SlotMediatorFactory;
import com.javahotel.nmvc.slotmodel.ListEventEnum;
import com.javahotel.nmvc.slotmodel.SlotListContainer;
import com.javahotel.nmvc.slotmodel.SlotSignalContext;
import com.javahotel.nmvc.slotmodel.SlotSubscriberType;
import com.javahotel.nmvc.slotmodel.SlotType;
import com.javahotel.nmvc.slotmodel.SlotTypeFactory;

class DisplayListControler implements IDataControler {

    private final IResLocator rI;
    private final DataType dType;

    private int cellTableId;
    private final ISlotMediator slMediator;
    private final SlotSubscriberType startSl;

    DisplayListControler(IResLocator rI, DataType dType, int panelId,
            int cellIdFirst) {
        this.rI = rI;
        this.dType = dType;
        // create panel View
        IPanelView pView = PanelViewFactory.construct(panelId, cellIdFirst);
        cellTableId = pView.addCellPanel(0, 0);
        pView.createView();
        // persist layer
        IDataPersistAction persistA = DataPersistActionFactory
                .contructDataPersis(rI, DataPersistEnum.PersistanceLayer);
        // header list
        List<ListHeaderDesc> heList = HeaderModelDataFactory
                .constructList(dType);
        ListHeaderData hData = new ListHeaderData(heList);
        IListDataView daView = ListDataViewFactory.construct(rI, dType,
                cellTableId, hData);
        slMediator = SlotMediatorFactory.construct();

        slMediator.registerSlotContainer(pView);
        slMediator.registerSlotContainer(persistA);
        slMediator.registerSlotContainer(daView);

        SlotType slType = SlotTypeFactory.contruct(ListEventEnum.ReadList);
        startSl = persistA.getSlContainer().findSubscriber(slType);
    }

    public void startPublish() {
        slMediator.startPublish();
        SlotSignalContext.signal(startSl, dType);
    }

    public SlotListContainer getSlContainer() {
        return slMediator.getSlContainer();
    }
}
