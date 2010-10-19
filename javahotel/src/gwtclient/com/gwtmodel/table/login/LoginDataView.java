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
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.controlbuttonview.IControlButtonView;
import com.gwtmodel.table.datamodelview.IDataViewModel;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.factories.IDataValidateAction;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.TablesFactories;
import com.gwtmodel.table.panelview.IPanelView;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.slotmediator.ISlotMediator;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCaller;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.SlotListContainer;
import com.gwtmodel.table.slotmodel.SlotSignalContextFactory;
import com.gwtmodel.table.slotmodel.SlotTypeFactory;

class LoginDataView implements ILoginDataView {

    private final ISlotMediator slMediator;
    private final IDataModelFactory dFactory;
    private final IDataType dType;

    private class GetCompose implements ISlotCaller {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            IVModelData perData = dFactory.construct(dType);
            IVModelData pData = slMediator.getSlContainer()
                    .getGetterIVModelData(GetActionEnum.GetViewModelEdited,
                            dType, perData);
            // result: perData
            SlotSignalContextFactory coFactory = GwtGiniInjector.getI()
                    .getSlotSignalContextFactory();
            return coFactory.construct(slContext.getSlType(), pData);
        }
    }

    LoginDataView(CellId panelId, IDataType dType,
            FormLineContainer lContainer, IDataModelFactory dFactory,
            IDataValidateAction vAction) {
        this.dFactory = dFactory;
        this.dType = dType;
        TablesFactories tFactories = GwtGiniInjector.getI()
                .getTablesFactories();
        ListOfControlDesc bControl = tFactories.getControlButtonFactory()
                .constructLoginButton();
        IControlButtonView bView = tFactories.getbViewFactory().construct(
                bControl);
        IPanelView pView = tFactories.getpViewFactory().construct(panelId);
        slMediator = tFactories.getSlotMediatorFactory().construct();
        CellId controlId = pView.addCellPanel(1, 0);
        CellId cellTableId = pView.addCellPanel(0, 0);
        pView.createView();
        IDataViewModel daView = tFactories.getdViewFactory().construct(dType,
                lContainer, dFactory);
        slMediator.registerSlotContainer(panelId, pView);
        slMediator.registerSlotContainer(cellTableId, daView);
        slMediator.registerSlotContainer(controlId, bView);
        slMediator.registerSlotContainer(vAction);
        SlotTypeFactory slFactory = tFactories.getSlTypeFactory();
        slMediator.getSlContainer().registerCaller(
                slFactory.construct(GetActionEnum.GetViewComposeModelEdited,
                        dType), new GetCompose());
        slMediator.getSlContainer().registerRedirector(
                slFactory.construct(ClickButtonType.StandClickEnum.ACCEPT),
                slFactory.construct(DataActionEnum.ValidateAction, dType));
    }

    @Override
    public SlotListContainer getSlContainer() {
        return slMediator.getSlContainer();
    }

    @Override
    public void startPublish(CellId cellId) {
        slMediator.startPublish(cellId);
    }

}
