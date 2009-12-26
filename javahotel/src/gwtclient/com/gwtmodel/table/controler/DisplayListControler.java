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
package com.gwtmodel.table.controler;

import java.util.List;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.listdataview.IListDataView;
import com.gwtmodel.table.listdataview.ListDataViewFactory;
import com.gwtmodel.table.panelview.IPanelView;
import com.gwtmodel.table.panelview.PanelViewFactory;
import com.gwtmodel.table.persist.IDataPersistAction;
import com.gwtmodel.table.slotmediator.ISlotMediator;
import com.gwtmodel.table.slotmediator.SlotMediatorFactory;
import com.gwtmodel.table.slotmodel.ListEventEnum;
import com.gwtmodel.table.slotmodel.SlotListContainer;
import com.gwtmodel.table.slotmodel.SlotSignalContext;
import com.gwtmodel.table.slotmodel.SlotSubscriberType;
import com.gwtmodel.table.slotmodel.SlotType;
import com.gwtmodel.table.slotmodel.SlotTypeFactory;
import com.gwtmodel.table.view.table.VListHeaderDesc;

class DisplayListControler implements IDataControler {

    private final IDataType dType;

    private int cellTableId;
    private final ISlotMediator slMediator;
    private final SlotSubscriberType startSl;

    DisplayListControler(IDataType dType, int panelId,
            int cellIdFirst, List<VListHeaderDesc> heList, IDataPersistAction persistA) {
        this.dType = dType;
        // create panel View
        IPanelView pView = PanelViewFactory.construct(panelId, cellIdFirst);
        cellTableId = pView.addCellPanel(0, 0);
        pView.createView();
        // persist layer
        // header list
        IListDataView daView = ListDataViewFactory.construct(dType,
                cellTableId, heList);
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
