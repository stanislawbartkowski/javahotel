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
package com.gwtmodel.table.composecontroller;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.panelview.IPanelView;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCaller;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.PanelSlotContainer;
import com.gwtmodel.table.slotmodel.SlotTypeFactory;

class ComposeController extends PanelSlotContainer implements
        IComposeController {

    private final List<ComposeControllerType> cList = new ArrayList<ComposeControllerType>();
    private IPanelView pView;
    private final IDataType dType;
    private final SlotTypeFactory slFactory;
    private final IDataModelFactory dFactory;

    ComposeController(IDataType dType, IDataModelFactory dFactory) {
        super();
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
                assert pData != null : "Cannot be null";
            }
            return slMediator.getSlContainer().getGetterContext(
                    slContext.getSlType(), pData);
        }

    }

    public void registerControler(ComposeControllerType cType) {
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
                if (!cType.isPanelElem() && !cType.isCellId()) {
                    continue;
                }
                slMediator.getSlContainer().publish(dataActionEnum,
                        cType.getdType(), slContext);
            }
        }

    }

    public void startPublish(CellId cellId) {
        slMediator.startPublish(null);
    }

    public void createComposeControle(CellId cellId) {
        pView = pViewFactory.construct(cellId);
        for (ComposeControllerType c : cList) {
            CellId cId = null;
            if (c.isPanelElem()) {
                cId = pView.addCellPanel(c.getRow(), c.getCell());
            }
            if (c.isCellId()) {
                cId = c.getCellId();
            }
            slMediator.registerSlotContainer(cId, c.getiSlot());
        }
        pView.createView();
        slMediator.registerSlotContainer(cellId, pView);
        slMediator.getSlContainer().registerSubscriber(
                DataActionEnum.DrawViewComposeFormAction, dType,
                new DrawAction(DataActionEnum.DrawViewFormAction));
        slMediator.getSlContainer().registerSubscriber(
                DataActionEnum.DefaultViewComposeFormAction, dType,
                new DrawAction(DataActionEnum.DefaultViewFormAction));
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
    }

}
