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

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.controlbuttonview.IControlButtonView;
import com.gwtmodel.table.datamodelview.DataViewModelFactory;
import com.gwtmodel.table.datamodelview.IDataViewModel;
import com.gwtmodel.table.factories.IDataValidateAction;
import com.gwtmodel.table.injector.TableFactoriesContainer;
import com.gwtmodel.table.injector.TablesFactories;
import com.gwtmodel.table.panelview.IPanelView;
import com.gwtmodel.table.panelview.PanelViewFactory;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.slotmediator.ISlotMediator;
import com.gwtmodel.table.slotmediator.SlotMediatorFactory;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlotListContainer;

class DisplayFormControler implements ISlotable {

    private final int formId;
    private final int coId;
    private final ISlotMediator slMediator;

    DisplayFormControler(TablesFactories tFactories,
            TableFactoriesContainer fContainer, FormLineContainer lContainer,
            ListOfControlDesc listButton, IDataType dType, int panelId,
            int cellIdFirst, IVModelData vData,
            ClickButtonType.StandClickEnum eAction) {
        PanelViewFactory pViewFactory = tFactories.getpViewFactory();
        IPanelView pView = pViewFactory.construct(panelId, cellIdFirst);
        formId = pView.addCellPanel(0, 0);
        coId = pView.addCellPanel(1, 0);
        pView.createView();
        DataViewModelFactory dFactory = tFactories.getdViewFactory();
        IDataViewModel vModel = dFactory.construct(dType, formId, lContainer);
        IDataValidateAction vAction = fContainer.getDataValidateFactory()
                .construct(dType);
        for (FormField fie : lContainer.getfList()) {
            IFormLineView vie = fie.getELine();
            switch (eAction) {
            case ADDITEM:
                break;
            case MODIFITEM:
                if (fie.isReadOnlyIfModif()) {
                    vie.setReadOnly(true);
                }
                break;
            case REMOVEITEM:
                vie.setReadOnly(true);
                break;
            }
        }
        IControlButtonView cView = tFactories.getbViewFactory().construct(coId,
                listButton);
        vModel.fromDataToView(vData);
        slMediator = SlotMediatorFactory.construct();
        slMediator.registerSlotContainer(pView);
        slMediator.registerSlotContainer(vModel);
        slMediator.registerSlotContainer(cView);
        slMediator.registerSlotContainer(vAction);
    }

    public SlotListContainer getSlContainer() {
        return slMediator.getSlContainer();
    }

    public void startPublish() {
        slMediator.startPublish();
    }

}
