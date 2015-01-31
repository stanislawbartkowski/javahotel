/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.factories.IDataModelFactory;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.panelview.IPanelView;
import com.gwtmodel.table.slotmodel.*;
import java.util.ArrayList;
import java.util.List;

class ComposeController extends AbstractSlotMediatorContainer implements
        IComposeController {

    private final List<ComposeControllerType> cList = new ArrayList<ComposeControllerType>();
    private IPanelView pView;
    private final SlotTypeFactory slFactory;
    private final IDataModelFactory dFactory;

    ComposeController(IDataType dType, IDataModelFactory dFactory) {
        super();
        this.dType = dType;
        slFactory = tFactories.getSlTypeFactory();
        this.dFactory = dFactory;
    }

    private class EditCallerGetter implements ISlotCallerListener {

        private final GetActionEnum getA;

        EditCallerGetter(GetActionEnum getA) {
            this.getA = getA;
        }

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            IVModelData mData = slContext.getVData();
            if (mData == null) {
                mData = dFactory.construct(dType);
            }
            IVModelData pData = slMediator.getSlContainer()
                    .getGetterIVModelData(dType, getA, mData);
            for (ComposeControllerType cType : cList) {
                if (cType.getdType() == null) {
                    continue;
                }
                if (cType.getdType().equals(dType)) {
                    continue;
                }
                IVModelData ppData = slMediator.getSlContainer()
                        .getGetterIVModelData(cType.getdType(), getA, pData);
                if (ppData != null) {
                    pData = ppData;
                }
            }
            return slMediator.getSlContainer().getGetterContext(
                    slContext.getSlType(), pData);
        }
    }

    @Override
    public void registerControler(ComposeControllerType cType) {
        assert cType.getiSlot() != null : LogT.getT().cannotBeNull();
        cList.add(cType);
    }

    private class DrawAction implements ISlotListener {

        private final DataActionEnum dataActionEnum;

        DrawAction(DataActionEnum dataActionEnum) {
            this.dataActionEnum = dataActionEnum;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            for (ComposeControllerType cType : cList) {
                if (cType.getdType() == null) {
                    continue;
                }
                if (!cType.isPanelElem() && !cType.isCellId()) {
                    continue;
                }
                slMediator.getSlContainer().publish(cType.getdType(),
                        dataActionEnum, slContext);
            }
            if (dataActionEnum == DataActionEnum.DrawViewFormAction) {
                slMediator.getSlContainer().publish(dType,
                        DataActionEnum.AfterDrawViewFormAction, slContext);
            }
            if (dataActionEnum == DataActionEnum.ChangeViewFormModeAction) {
                slMediator.getSlContainer().publish(dType,
                        DataActionEnum.AfterChangeModeFormAction, slContext);
            }
        }

    }

    // do not remove, it overrides
    @Override
    public void startPublish(CellId cellId) {
        slMediator.startPublish(null);
    }

    private class ValidateA extends SynchronizeList {

        private final ISlotSignalContext slContext;

        ValidateA(int no, ISlotSignalContext slContext) {
            super(no);
            this.slContext = slContext;
        }

        @Override
        protected void doTask() {
            slMediator.getSlContainer().publish(slContext);
        }
    }

    private class ComposeValidateAction implements ISlotListener {

        private final int no;
        private final List<SubValidate> li;
        private final SlotType sldType;

        ComposeValidateAction(int no, List<SubValidate> li, SlotType sldType) {
            this.no = no;
            this.li = li;
            this.sldType = sldType;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            ISlotSignalContext rSl = slContextFactory.construct(sldType,
                    slContext);
            ValidateA a = new ValidateA(no, rSl);
            if (no == 0) {
                a.doTask();
                return;
            }
            for (SubValidate v : li) {
                v.li = a;
                v.publish(slContext);
            }
        }

    }

    private class SubValidate implements ISlotListener {

        private SynchronizeList li;
        private final SlotType slType;

        SubValidate(SlotType slType) {
            this.slType = slType;
        }

        void publish(ISlotSignalContext slContext) {
            ISlotSignalContext rSl = slContextFactory.construct(slType,
                    slContext);
            slMediator.getSlContainer().publish(rSl);
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            li.signalDone();
        }

    }

    @Override
    public void createComposeControler(CellId cellId) {
        pView = pViewFactory.construct(dType, cellId);
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
        slMediator.getSlContainer().registerSubscriber(dType,
                DataActionEnum.DrawViewComposeFormAction,
                new DrawAction(DataActionEnum.DrawViewFormAction));
        slMediator.getSlContainer().registerSubscriber(dType,
                DataActionEnum.ChangeViewComposeFormModeAction,
                new DrawAction(DataActionEnum.ChangeViewFormModeAction));
        slMediator.getSlContainer().registerSubscriber(dType,
                DataActionEnum.DefaultViewComposeFormAction,
                new DrawAction(DataActionEnum.DefaultViewFormAction));

        slMediator.getSlContainer().registerRedirector(
                slFactory.construct(dType,
                        DataActionEnum.PersistComposeFormAction),
                slFactory.construct(dType, DataActionEnum.PersistDataAction));

        slMediator.getSlContainer().registerRedirector(
                slFactory.construct(dType, DataActionEnum.InvalidSignal),
                slFactory.construct(dType,
                        DataActionEnum.ChangeViewFormToInvalidAction));

        int no = 0;
        List<SubValidate> li = new ArrayList<SubValidate>();
        SlotType sldType = slFactory.construct(dType,
                DataActionEnum.ValidateAction);
        for (ComposeControllerType c : cList) {
            IDataType dType = c.getdType();
            if (dType == null) {
                continue;
            }
            SlotType slType = slFactory.construct(dType,
                    DataActionEnum.ValidateAction);
            if (slType.eq(sldType)) {
                continue;
            }
            int i = slMediator.getSlContainer().noListeners(slType);
            if (i > 0) {
                no += i;
                SubValidate va = new SubValidate(slType);
                SlotType slVal = slFactory.construct(dType,
                        DataActionEnum.ValidSignal);
                c.getiSlot().getSlContainer().registerSubscriber(slVal, va);
                li.add(va);
            }
        }
        slMediator.getSlContainer().registerSubscriber(dType,
                DataActionEnum.ValidateComposeFormAction,
                new ComposeValidateAction(no, li, sldType));

        slMediator.getSlContainer().registerCaller(dType,
                GetActionEnum.GetViewComposeModelEdited,
                new EditCallerGetter(GetActionEnum.GetViewModelEdited));
        slMediator.getSlContainer().registerCaller(dType,
                GetActionEnum.GetComposeModelToPersist,
                new EditCallerGetter(GetActionEnum.GetModelToPersist));
    }
}
