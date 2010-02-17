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
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.injector.TablesFactories;
import com.gwtmodel.table.panelview.IPanelView;
import com.gwtmodel.table.panelview.PanelViewFactory;
import com.gwtmodel.table.slotmediator.ISlotMediator;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCaller;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.SlotListContainer;
import com.gwtmodel.table.slotmodel.SlotTypeFactory;

class ComposeController implements IComposeController {

    private final ISlotMediator slMediator;
    private final List<ComposeControllerType> cList = new ArrayList<ComposeControllerType>();
    private IPanelView pView;
    private final PanelViewFactory pViewFactory;
    private final IDataType dType;
    private final SlotTypeFactory slFactory;
    private final IDataModelFactory dFactory;

    ComposeController(TablesFactories tFactories, IDataType dType,
            IDataModelFactory dFactory) {
        slMediator = tFactories.getSlotMediatorFactory().construct();
        pViewFactory = tFactories.getpViewFactory();
        this.dType = dType;
        slFactory = tFactories.getSlTypeFactory();
        this.dFactory = dFactory;
    }

    private class EditCallerGetter implements ISlotCaller {

        private final GetActionEnum getA;

        EditCallerGetter(GetActionEnum getA) {
            this.getA = getA;
        }

        public ISlotSignalContext call(ISlotSignalContext slContext) {
            IVModelData mData = dFactory.construct(dType);
            IVModelData pData = slMediator.getSlContainer()
                    .getGetterIVModelData(getA, dType, mData);
            for (ComposeControllerType cType : cList) {
                if (cType.getdType() == null) {
                    continue;
                }
                if (cType.getdType().equals(dType)) {
                    continue;
                }
                if (!cType.isPanelElem()) {
                    continue;
                }
                pData = slMediator.getSlContainer().getGetterIVModelData(getA,
                        cType.getdType(), pData);
            }
            return slMediator.getSlContainer().getGetterContext(
                    slContext.getSlType(), pData);
        }

    }

    public void registerController(ComposeControllerType cType) {
        cList.add(cType);
    }

    private class DrawAction implements ISlotSignaller {

        private final DataActionEnum dataActionEnum;

        DrawAction(DataActionEnum dataActionEnum) {
            this.dataActionEnum = dataActionEnum;
        }

        public void signal(ISlotSignalContext slContext) {
            for (ComposeControllerType cType : cList) {
                if (cType.getdType() == null) {
                    continue;
                }
                if (!cType.isPanelElem()) {
                    continue;
                }
                slMediator.getSlContainer().publish(dataActionEnum,
                        cType.getdType(), slContext);
            }
        }

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
        slMediator.getSlContainer().registerSubscriber(
                DataActionEnum.DrawViewComposeFormAction, dType,
                new DrawAction(DataActionEnum.DrawViewFormAction));
        slMediator.getSlContainer().registerSubscriber(
                DataActionEnum.ChangeViewComposeFormModeAction, dType,
                new DrawAction(DataActionEnum.ChangeViewFormModeAction));

        slMediator.getSlContainer().registerRedirector(
                slFactory.construct(DataActionEnum.PersistComposeFormAction,
                        dType),
                slFactory.construct(DataActionEnum.PersistDataAction, dType));

        slMediator.getSlContainer().registerRedirector(
                slFactory.construct(DataActionEnum.InvalidSignal, dType),
                slFactory.construct(
                        DataActionEnum.ChangeViewFormToInvalidAction, dType));

        slMediator.getSlContainer().registerRedirector(
                slFactory.construct(DataActionEnum.ValidateComposeFormAction,
                        dType),
                slFactory.construct(DataActionEnum.ValidateAction, dType));

        slMediator.getSlContainer().registerCaller(
                GetActionEnum.GetViewComposeModelEdited, dType,
                new EditCallerGetter(GetActionEnum.GetViewModelEdited));
        slMediator.getSlContainer().registerCaller(
                GetActionEnum.GetComposeModelToPersist, dType,
                new EditCallerGetter(GetActionEnum.GetModelToPersist));

        slMediator.startPublish(-1);
    }

    public SlotListContainer getSlContainer() {
        return slMediator.getSlContainer();
    }

}
