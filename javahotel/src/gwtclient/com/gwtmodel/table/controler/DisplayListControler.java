/*
f * Copyright 2008 stanislawbartkowski@gmail.com 
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

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.controlbuttonview.ControlButtonViewFactory;
import com.gwtmodel.table.controlbuttonview.IControlButtonView;
import com.gwtmodel.table.injector.TableFactoriesContainer;
import com.gwtmodel.table.injector.TablesFactories;
import com.gwtmodel.table.listdataview.IListDataView;
import com.gwtmodel.table.listdataview.ListDataViewFactory;
import com.gwtmodel.table.panelview.IPanelView;
import com.gwtmodel.table.panelview.PanelViewFactory;
import com.gwtmodel.table.persist.IDataPersistAction;
import com.gwtmodel.table.slotmediator.ISlotMediator;
import com.gwtmodel.table.slotmediator.SlotMediatorFactory;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.ListEventEnum;
import com.gwtmodel.table.slotmodel.SlotListContainer;
import com.gwtmodel.table.slotmodel.SlotSubscriberType;
import com.gwtmodel.table.slotmodel.SlotType;
import com.gwtmodel.table.view.table.VListHeaderContainer;

class DisplayListControler implements IDataControler {

    private final int cellTableId;
    private final int controlId;
    private final ISlotMediator slMediator;
    private final SlotSubscriberType startSl;
    private final TablesFactories tFactories;

    DisplayListControler(TablesFactories tFactories,
            TableFactoriesContainer fContainer, IDataType dType, int panelId,
            int cellIdFirst, ListOfControlDesc listButton,
            ISlotable cControler) {
        this.tFactories = tFactories;
        IDataPersistAction persistA = fContainer.getPersistFactoryAction().contruct(dType);
        VListHeaderContainer heList = fContainer.getHeaderListFactory()
                .getVListHeaderContainer(dType);
        // create panel View
        PanelViewFactory pViewFactory = tFactories.getpViewFactory();
        IPanelView pView = pViewFactory.construct(panelId, cellIdFirst);
        controlId = pView.addCellPanel(0, 0);
        cellTableId = pView.addCellPanel(1, 0);
        pView.createView();
        // persist layer
        // header list
        ListDataViewFactory lDataFactory = tFactories.getlDataFactory();
        IListDataView daView = lDataFactory.construct(dType, cellTableId,
                heList);
        ControlButtonViewFactory bFactory = tFactories.getbViewFactory();
        IControlButtonView bView = bFactory.construct(controlId, listButton);
        slMediator = SlotMediatorFactory.construct();

        slMediator.registerSlotContainer(pView);
        slMediator.registerSlotContainer(persistA);
        slMediator.registerSlotContainer(daView);
        slMediator.registerSlotContainer(bView);
        slMediator.registerSlotContainer(cControler);

        SlotType slType = tFactories.getSlTypeFactory().contruct(
                ListEventEnum.ReadList, dType);
        startSl = persistA.getSlContainer().findSubscriber(slType);
    }

    public void startPublish() {
        slMediator.startPublish();
        tFactories.getSlSignalContext().signal(startSl);
    }

    public SlotListContainer getSlContainer() {
        return slMediator.getSlContainer();
    }
}
