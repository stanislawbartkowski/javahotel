/*
 *  Copyright 2011 stanislawbartkowski@gmail.com
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.gwtmodel.table.controler;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.ISignal;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.composecontroller.ComposeControllerType;
import com.gwtmodel.table.composecontroller.IComposeController;
import com.gwtmodel.table.controlbuttonview.IControlButtonView;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.injector.TablesFactories;
import com.gwtmodel.table.slotmodel.ButtonAction;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCaller;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlotListContainer;
import com.gwtmodel.table.slotmodel.SlotSignalContextFactory;
import com.gwtmodel.table.view.util.GetActionName;
import com.gwtmodel.table.view.util.ICloseAction;
import com.gwtmodel.table.view.util.ModalDialog;

/**
 *
 * @author hotel
 */
class DataListActionItemFactory {

    private final TablesFactories tFactories;
    private final IDataType dType;
    private final ISlotable iSlo;
    private final DataListParam listParam;
    private final SlotSignalContextFactory slFactory;

    DataListActionItemFactory(TablesFactories tFactories, IDataType dType,
            ISlotable iSlo, DataListParam listParam,
            SlotSignalContextFactory slFactory) {
        this.tFactories = tFactories;
        this.dType = dType;
        this.iSlo = iSlo;
        this.listParam = listParam;
        this.slFactory = slFactory;
    }

    private class AfterPersistData extends Signaller {

        private final PersistTypeEnum persistTypeEnum;

        AfterPersistData(DrawForm dForm, PersistTypeEnum persistTypeEnum) {
            super(dForm);
            this.persistTypeEnum = persistTypeEnum;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            hide();
            iSlo.getSlContainer().publish(DataActionEnum.RefreshAfterPersistActionSignal, dType,
                    persistTypeEnum);
        }
    }

    static abstract class Signaller implements ISlotSignaller {

        private final DrawForm dForm;

        Signaller(DrawForm dForm) {
            this.dForm = dForm;
        }

        protected void hide() {
            dForm.d.hide();
        }
    }

    static private class FormDialog extends ModalDialog {

        private final IGWidget w;

        FormDialog(VerticalPanel vp, String title, IGWidget w, boolean modal) {
            super(vp, title, modal);
            this.w = w;
            create();
        }

        @Override
        protected void addVP(VerticalPanel vp) {
            vp.add(w.getGWidget());
        }
    }

    static class DrawForm implements ISlotSignaller {

        private final WSize wSize;
        private final String title;
        private final ClickButtonType.StandClickEnum action;
        private FormDialog d;
        private final boolean modal;
        private final ICloseAction o;

        DrawForm(WSize wSize, String title,
                ClickButtonType.StandClickEnum action, boolean modal, ICloseAction o) {
            this.wSize = wSize;
            this.title = title;
            this.action = action;
            this.modal = modal;
            this.o = o;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            String addTitle = GetActionName.getActionName(action);
            IGWidget w = slContext.getGwtWidget();
            VerticalPanel vp = new VerticalPanel();
            d = new FormDialog(vp, title + " / " + addTitle, w, modal);
            d.show(wSize);
            if (o != null) {
                d.setOnClose(o);
            }
        }

        void hide() {
            d.hide();
        }
    }

    static class ResignAction extends Signaller {

        private final ISignal i;

        ResignAction(DrawForm dForm, ISignal i) {
            super(dForm);
            this.i = i;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            hide();
            if (i != null) {
                i.signal();
            }
        }
    }

    private class PersistData implements ISlotSignaller {

        private final PersistTypeEnum persistTypeEnum;
        private final IComposeController iController;
        private final DataActionEnum dataActionEnum;

        PersistData(PersistTypeEnum persistTypeEnum,
                IComposeController iController, DataActionEnum dataActionEnum) {
            this.persistTypeEnum = persistTypeEnum;
            this.iController = iController;
            this.dataActionEnum = dataActionEnum;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            iController.getSlContainer().publish(dataActionEnum, dType,
                    persistTypeEnum);
        }
    }

    private class GetterGWidget implements ISlotCaller {

        private final DrawForm d;

        GetterGWidget(DrawForm d) {
            this.d = d;
        }

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            IGWidget i = d.d.w;
            return slFactory.construct(slContext.getSlType(), i);
        }
    }

    private class GetterModel implements ISlotCaller {

        private final IVModelData peData;
        private final SlotListContainer slControlerContainer;

        public GetterModel(SlotListContainer slControlerContainer,
                IVModelData peData) {
            this.slControlerContainer = slControlerContainer;
            this.peData = peData;
        }

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            IVModelData perData = slContext.getVData(); // do nothing
            IVModelData pData = slControlerContainer.getGetterIVModelData(
                    GetActionEnum.GetViewModelEdited, dType, peData);
            listParam.getDataFactory().fromModelToPersist(dType, pData,
                    perData);
            // result: perData
            return slContext;
        }
    }

    private class ActionItem implements ISlotSignaller {

        private final PersistTypeEnum persistTypeEnum;

        ActionItem(PersistTypeEnum persistTypeEnum) {
            this.persistTypeEnum = persistTypeEnum;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            ClickButtonType.StandClickEnum action = slContext.getSlType().getButtonClick().getClickEnum();
            ISlotSignalContext ret = iSlo.getSlContainer().getGetterContext(
                    GetActionEnum.GetListLineChecked, dType);
            IVModelData mModel = listParam.getDataFactory().construct(dType);
            IVModelData peData = null;
            WSize wSize = null;
            if (action != ClickButtonType.StandClickEnum.ADDITEM) {
                peData = ret.getVData();
                if (peData == null) {
                    return;
                }
                wSize = ret.getWSize();
            }
            if (peData != null) {
                listParam.getDataFactory().copyFromPersistToModel(dType,
                        peData, mModel);
            } else {
                IGWidget wi = slContext.getGwtWidget();
                wSize = new WSize(wi.getGWidget());
                // create empty
                peData = listParam.getDataFactory().construct(dType);
            }
            ListOfControlDesc liControls;
            switch (action) {
                case REMOVEITEM:
                    liControls = tFactories.getControlButtonFactory().constructRemoveDesign();
                    break;
                case SHOWITEM:
                    liControls = tFactories.getControlButtonFactory().constructOkButton();
                    break;
                default:
                    liControls = tFactories.getControlButtonFactory().constructAcceptResign();
                    break;

            }
            CellId cId = new CellId(0);
            ICallContext iCall = GwtGiniInjector.getI().getCallContext();
            iCall.setdType(dType);
            iCall.setiSlo(iSlo);
            IComposeController fController = listParam.getfControler().construct(iCall);
            IControlButtonView cView = tFactories.getbViewFactory().construct(dType,
                    liControls);
            ComposeControllerType bType = new ComposeControllerType(cView,
                    null, 1, 0);
            fController.registerControler(bType);
            fController.createComposeControle(cId);

            SlotListContainer slControlerContainer = fController.getSlContainer();
            String title = listParam.getFormFactory().getFormTitle(dType);
            DrawForm dForm = new DrawForm(wSize, title, action, true, null);
            slControlerContainer.registerSubscriber(dType, cId, dForm);
            ResignAction aRes = new ResignAction(dForm, null);
            slControlerContainer.registerSubscriber(
                    ClickButtonType.StandClickEnum.RESIGN, aRes);
            PersistData pData = new PersistData(persistTypeEnum, fController,
                    DataActionEnum.ValidateComposeFormAction);
            if (action == ClickButtonType.StandClickEnum.SHOWITEM) {
                slControlerContainer.registerSubscriber(
                        ClickButtonType.StandClickEnum.ACCEPT, aRes);
            } else {
                slControlerContainer.registerSubscriber(
                        ClickButtonType.StandClickEnum.ACCEPT, pData);
            }

            pData = new PersistData(persistTypeEnum, fController,
                    DataActionEnum.PersistComposeFormAction);
            slControlerContainer.registerSubscriber(DataActionEnum.ValidSignal,
                    dType, pData);

            slControlerContainer.registerSubscriber(
                    DataActionEnum.PersistDataSuccessSignal, dType,
                    new AfterPersistData(dForm, persistTypeEnum));

            fController.startPublish(cId);
            slControlerContainer.publish(
                    DataActionEnum.DrawViewComposeFormAction, dType, peData, persistTypeEnum);
            slControlerContainer.publish(
                    DataActionEnum.DefaultViewComposeFormAction, dType, peData, persistTypeEnum);

            slControlerContainer.publish(
                    DataActionEnum.ChangeViewComposeFormModeAction, dType,
                    persistTypeEnum);

            slControlerContainer.registerCaller(
                    GetActionEnum.GetModelToPersist, dType, new GetterModel(
                    slControlerContainer, peData));
            slControlerContainer.registerCaller(
                    GetActionEnum.GetGWidget, dType, new GetterGWidget(dForm));
        }
    }

    private void publishP(ButtonAction b) {

        iSlo.getSlContainer().publish(dType,
                new ClickButtonType(ClickButtonType.StandClickEnum.ADDITEM), b);

        iSlo.getSlContainer().publish(dType,
                new ClickButtonType(ClickButtonType.StandClickEnum.REMOVEITEM), b);
    }

    private class ChangeMode implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            LogT.getLS().info(LogT.getT().receivedSignalLog(slContext.getSlType().toString()));
            PersistTypeEnum persistTypeEnum = slContext.getPersistType();
            switch (persistTypeEnum) {
                case ADD:
                case MODIF:
                case REMOVE:
                    publishP(ButtonAction.EnableButton);
                    break;
                case SHOWONLY:
                    publishP(ButtonAction.DisableButton);
                    break;
            }
        }
    }

    ISlotSignaller constructChangeMode() {
        return new ChangeMode();
    }

    ISlotSignaller constructActionItem(PersistTypeEnum persistTypeEnum) {
        return new ActionItem(persistTypeEnum);
    }
}
