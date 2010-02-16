/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.login;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.controlbuttonview.IControlButtonView;
import com.gwtmodel.table.datamodelview.IDataViewModel;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.TablesFactories;
import com.gwtmodel.table.panelview.IPanelView;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.slotmediator.ISlotMediator;
import com.gwtmodel.table.slotmediator.SlotMediatorFactory;
import com.gwtmodel.table.slotmodel.SlotListContainer;

class LoginDataView implements ILoginDataView {

    private final ISlotMediator slMediator;

    LoginDataView(int cellId, int firstId,IDataType dType, FormLineContainer lContainer,
            IDataModelFactory dFactory) {
        TablesFactories tFactories = GwtGiniInjector.getI()
                .getTablesFactories();
        ListOfControlDesc bControl = tFactories.getControlButtonFactory()
                .constructLoginButton();
        IControlButtonView bView = tFactories.getbViewFactory().construct(
                bControl);
        IPanelView pView = tFactories.getpViewFactory().construct(firstId);
        slMediator = SlotMediatorFactory.construct();
        int controlId = pView.addCellPanel(1, 0);
        int cellTableId = pView.addCellPanel(0, 0);
        pView.createView();
        IDataViewModel daView = tFactories.getdViewFactory().construct(dType,
                lContainer, dFactory);
        slMediator.registerSlotContainer(cellId, pView);
        slMediator.registerSlotContainer(cellTableId, daView);
        slMediator.registerSlotContainer(controlId, bView);
    }

    public SlotListContainer getSlContainer() {
        return slMediator.getSlContainer();
    }

    public void startPublish(int cellId) {
        slMediator.startPublish(cellId);
    }

}
