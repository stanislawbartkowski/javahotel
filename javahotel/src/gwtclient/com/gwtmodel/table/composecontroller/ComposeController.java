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
package com.gwtmodel.table.composecontroller;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.injector.TablesFactories;
import com.gwtmodel.table.panelview.IPanelView;
import com.gwtmodel.table.panelview.PanelViewFactory;
import com.gwtmodel.table.slotmediator.ISlotMediator;
import com.gwtmodel.table.slotmediator.SlotMediatorFactory;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.SlotListContainer;
import com.gwtmodel.table.slotmodel.SlotTypeFactory;

class ComposeController implements IComposeController {

    private final ISlotMediator slMediator;
    private final List<ComposeControllerType> cList = new ArrayList<ComposeControllerType>();
    private IPanelView pView;
    private final PanelViewFactory pViewFactory;
    private final IDataType dType;
    private final SlotTypeFactory slFactory;

    ComposeController(TablesFactories tFactories, IDataType dType) {
        slMediator = SlotMediatorFactory.construct();
        pViewFactory = tFactories.getpViewFactory();
        this.dType = dType;
        slFactory = tFactories.getSlTypeFactory();
    }

    public void registerController(ComposeControllerType cType) {
        cList.add(cType);
    }

    public void startPublish(int cellId) {
        pView = pViewFactory.construct(cellId + 1);
        for (ComposeControllerType c : cList) {
            int cId = -1;
            if (c.isPanelElem()) {
                cId = pView.addCellPanel(c.getRow(), c.getCell());
            }
            slMediator.registerSlotContainer(cId, c.getiSlot());
        }
        pView.createView();
        slMediator.registerSlotContainer(cellId, pView);
        slMediator.getSlContainer().registerRedirector(
                slFactory.construct(DataActionEnum.DrawViewComposeFormAction,
                        dType),
                slFactory.construct(DataActionEnum.DrawViewFormAction, dType));

        slMediator.getSlContainer().registerRedirector(
                slFactory.construct(
                        DataActionEnum.ChangeViewComposeFormModeAction, dType),
                slFactory.construct(DataActionEnum.ChangeViewFormModeAction,
                        dType));
        
        slMediator.getSlContainer().registerRedirector(
                slFactory.construct(
                        DataActionEnum.PersistComposeFormAction, dType),
                slFactory.construct(DataActionEnum.PersistDataAction,
                        dType));
        
        slMediator.getSlContainer().registerRedirector(
                slFactory.construct(
                        DataActionEnum.InvalidSignal, dType),
                slFactory.construct(DataActionEnum.ChangeViewFormToInvalidAction,
                        dType));
        
        slMediator.getSlContainer().registerRedirector(
                slFactory.construct(
                        DataActionEnum.ValidateComposeFormAction, dType),
                slFactory.construct(DataActionEnum.ValidateAction,
                        dType));                
       
        slMediator.startPublish(-1);
    }

    public SlotListContainer getSlContainer() {
        return slMediator.getSlContainer();
    }

}
