/*
 *  Copyright 2016 stanislawbartkowski@gmail.com
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
import com.gwtmodel.table.IClickYesNo;
import com.gwtmodel.table.ICustomObject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.ISignal;
import com.gwtmodel.table.common.PersistTypeEnum;
import com.gwtmodel.table.composecontroller.ComposeControllerType;
import com.gwtmodel.table.composecontroller.IComposeController;
import com.gwtmodel.table.controlbuttonview.IControlButtonView;
import com.gwtmodel.table.factories.IDataCrudModifButtonAction;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.injector.TablesFactories;
import com.gwtmodel.table.slotmodel.ButtonAction;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.CustomObjectValue;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCallerListener;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.slotmodel.SlotListContainer;
import com.gwtmodel.table.slotmodel.SlotSignalContextFactory;
import com.gwtmodel.table.smessage.IGetStandardMessage;
import com.gwtmodel.table.view.util.GetActionName;
import com.gwtmodel.table.view.util.ModalDialog;
import com.gwtmodel.table.view.util.SolidPos;
import com.gwtmodel.table.view.util.YesNoDialog;

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
            if (iSlo != null) {
                iSlo.getSlContainer().publish(dType,
                        DataActionEnum.RefreshAfterPersistActionSignal,
                        persistTypeEnum);
            }
        }
    }

    static abstract class Signaller implements ISlotListener {

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

        FormDialog(VerticalPanel vp, String title, IGWidget w, boolean modal,
                ISignal aClose) {
            super(vp, title, false, modal);
            this.w = w;
            if (aClose == null) {
                create();
            } else {
                create(aClose);
            }
        }

        @Override
        protected void addVP(VerticalPanel vp) {
            vp.add(w.getGWidget());
        }
    }

    static class DrawForm implements ISlotListener {

        private final WSize wSize;
        private final String title;
        private final SolidPos sPos;
        private final ClickButtonType.StandClickEnum action;
        private FormDialog d;
        private final boolean modal;
        private final ISignal o;
        private final ISignal aClose;
        private final IGetStandardMessage iMess = GwtGiniInjector.getI()
                .getStandardMessage();

        DrawForm(WSize wSize, String title, SolidPos sPos,
                ClickButtonType.StandClickEnum action, boolean modal,
                ISignal o, ISignal aClose) {
            this.wSize = wSize;
            this.sPos = sPos;
            this.title = title;
            this.action = action;
            this.modal = modal;
            this.o = o;
            this.aClose = aClose;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            String addTitle = GetActionName.getActionName(action);
            IGWidget w = slContext.getGwtWidget();
            VerticalPanel vp = new VerticalPanel();
            String titleDialog;
            if (CUtil.EmptyS(title))
                titleDialog = addTitle;
            else
                titleDialog = LogT.getT().formRecordTitle(
                        iMess.getMessage(title), addTitle);
            d = new FormDialog(vp, titleDialog, w, modal, aClose);
            if (sPos == null)
                d.show(wSize);
            else
                d.show(wSize, sPos);
            if (o != null) {
                d.setOnClose(o);
            }
        }

        void hide() {
            d.hide();
        }
    }

    /**
     * Removes dialog from screen (important: default visibility)
     * 
     * @author hotel
     * 
     */
    static class ResignAction extends Signaller {

        private final ISignal i;
        private final String ask;

        ResignAction(DrawForm dForm, ISignal i, String ask) {
            super(dForm);
            this.i = i;
            this.ask = ask;
        }

        private void removeDialog() {
            hide();
            if (i != null) {
                i.signal();
            }

        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            ICustomObject o = slContext.getCustom();
            String pAsk = null;

            if (o != null && o instanceof CustomObjectValue) {
                CustomObjectValue<String> v = (CustomObjectValue<String>) o;
                pAsk = v.getValue();
            }

            if (pAsk == null) {
                pAsk = ask;
            }
            if (pAsk != null) {
                IGWidget w = slContext.getGwtWidget();
                IClickYesNo yes = new IClickYesNo() {

                    @Override
                    public void click(boolean yes) {
                        if (yes) {
                            removeDialog();
                        }

                    }
                };
                YesNoDialog yesD = new YesNoDialog(pAsk, yes);
                yesD.show(w);
                return;

            }
            removeDialog();
        }
    }

    private class PersistData implements ISlotListener {

        private final PersistTypeEnum persistTypeEnum;
        private final IComposeController iController;
        private final DataActionEnum dataActionEnum;
        private final String ask;

        PersistData(PersistTypeEnum persistTypeEnum,
                IComposeController iController, DataActionEnum dataActionEnum,
                String ask) {
            this.persistTypeEnum = persistTypeEnum;
            this.iController = iController;
            this.dataActionEnum = dataActionEnum;
            this.ask = ask;
        }

        private void goPersist(ISlotSignalContext slContext) {
            SlU.publishActionPersist(dType, iController, slContext,
                    dataActionEnum, persistTypeEnum);
        }

        @Override
        public void signal(final ISlotSignalContext slContext) {
            ICustomObject o = slContext.getCustom();
            String pAsk = null;
            if (o != null && o instanceof CustomObjectValue) {
                CustomObjectValue<String> v = (CustomObjectValue<String>) o;
                pAsk = v.getValue();
            }
            if (pAsk == null) {
                pAsk = ask;
            }
            if (pAsk != null) {
                IGWidget w = slContext.getGwtWidget();
                IClickYesNo yes = new IClickYesNo() {

                    @Override
                    public void click(boolean yes) {
                        if (yes) {
                            goPersist(slContext);
                        }

                    }
                };
                YesNoDialog yesD = new YesNoDialog(pAsk, yes);
                yesD.show(w);
                return;

            }
            goPersist(slContext);
        }
    }

    private class GetterGWidget implements ISlotCallerListener {

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

    private class GetterModel implements ISlotCallerListener {

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
                    dType, GetActionEnum.GetViewModelEdited, peData);
            listParam.getDataFactory()
                    .fromModelToPersist(dType, pData, perData);
            // result: perData
            return slContext;
        }
    }

    private class GetListLine implements ISlotCallerListener {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            return iSlo.getSlContainer().getGetterContext(dType,
                    GetActionEnum.GetListLineChecked);
        }
    }

    IComposeController BoxActionItem(ClickButtonType.StandClickEnum action,
            PersistTypeEnum persistTypeEnum, IVModelData peData, WSize wSize,
            boolean contentOnly) {
        ListOfControlDesc liControls;
        ICallContext iCall = GwtGiniInjector.getI().getCallContext();
        iCall.setdType(dType);
        iCall.setiSlo(iSlo);
        iCall.setPersistTypeEnum(persistTypeEnum);
        boolean addModifAction = false;
        switch (action) {
        case REMOVEITEM:
            liControls = tFactories.getControlButtonFactory()
                    .constructRemoveDesign();
            break;
        case SHOWITEM:
            liControls = tFactories.getControlButtonFactory()
                    .constructOkButton();
            break;
        default:
            liControls = tFactories.getControlButtonFactory()
                    .constructAcceptResign();
            addModifAction = true;
            break;

        }
        CellId cId = new CellId(0);
        IComposeController fController = listParam.getfControler().construct(
                iCall);
        if (listParam.getModifButtonFactory() != null) {
            IDataCrudModifButtonAction bModif = listParam
                    .getModifButtonFactory().contruct(iCall);
            ComposeControllerType mType = new ComposeControllerType(bModif);
            fController.registerControler(mType);
            bModif.modifButton(liControls);
        }
        final SlotListContainer slControlerContainer = fController
                .getSlContainer();
        if (!contentOnly) {
            IControlButtonView cView = tFactories.getbViewFactory().construct(
                    dType, liControls);
            ComposeControllerType bType = new ComposeControllerType(cView,
                    null, 1, 0);
            fController.registerControler(bType);
            fController.createComposeControler(cId);
            String title = listParam.getFormFactory().getFormTitle(iCall);
            ISignal aClose = null;
            if (addModifAction) {
                aClose = new ISignal() {

                    @Override
                    public void signal() {
                        slControlerContainer.publish(dType,
                                new ClickButtonType(
                                        ClickButtonType.StandClickEnum.RESIGN),
                                new ButtonAction(
                                        ButtonAction.Action.ForceButton));
                    }
                };
            }
            DrawForm dForm = new DrawForm(wSize, title, listParam
                    .getFormFactory().getSolidPos(iCall), action, true, null,
                    aClose);
            slControlerContainer.registerSubscriber(dType, cId, dForm);
            String resignAsk = listParam.getMenuOptions().getAskString(
                    BoxActionMenuOptions.ASK_BEFORE_RESIGN);
            String persistAsk = listParam.getMenuOptions().getAskString(
                    BoxActionMenuOptions.ASK_BEFORE_PERSIST);
            ResignAction aRes = new ResignAction(dForm, null, resignAsk);

            slControlerContainer.registerSubscriber(listParam.getMenuOptions()
                    .constructRemoveFormDialogSlotType(), aRes);

            // redirect Resign (default: remove without any question)
            slControlerContainer.registerRedirector(
                    listParam.getMenuOptions().constructResignButtonSlotType(
                            dType),
                    listParam.getMenuOptions().getSlotType(
                            BoxActionMenuOptions.REDIRECT_RESIGN));

            PersistData pData = new PersistData(persistTypeEnum, fController,
                    DataActionEnum.ValidateComposeFormAction, persistAsk);
            if (action == ClickButtonType.StandClickEnum.SHOWITEM) {
                slControlerContainer.registerSubscriber(dType,
                        ClickButtonType.StandClickEnum.ACCEPT, aRes);
            } else {
                slControlerContainer.registerSubscriber(dType,
                        ClickButtonType.StandClickEnum.ACCEPT, pData);
            }

            pData = new PersistData(persistTypeEnum, fController,
                    DataActionEnum.PersistComposeFormAction, persistAsk);
            slControlerContainer.registerSubscriber(dType,
                    DataActionEnum.ValidSignal, pData);

            slControlerContainer.registerSubscriber(dType,
                    DataActionEnum.PersistDataSuccessSignal,
                    new AfterPersistData(dForm, persistTypeEnum));

            fController.startPublish(cId);
            slControlerContainer.registerCaller(dType,
                    GetActionEnum.GetGWidget, new GetterGWidget(dForm));
        } else {
            fController.createComposeControler(cId);
        }
        slControlerContainer.publish(dType,
                DataActionEnum.DrawViewComposeFormAction, peData,
                persistTypeEnum);
        slControlerContainer.publish(dType,
                DataActionEnum.DefaultViewComposeFormAction, peData,
                persistTypeEnum);

        slControlerContainer
                .publish(dType, DataActionEnum.ChangeViewComposeFormModeAction,
                        persistTypeEnum);

        slControlerContainer.registerCaller(dType,
                GetActionEnum.GetModelToPersist, new GetterModel(
                        slControlerContainer, peData));
        slControlerContainer.registerCaller(dType,
                GetActionEnum.GetListLineChecked, new GetListLine());
        return fController;
    }

    private class ActionItem implements ISlotListener {

        private final PersistTypeEnum persistTypeEnum;

        ActionItem(PersistTypeEnum persistTypeEnum) {
            this.persistTypeEnum = persistTypeEnum;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            ClickButtonType.StandClickEnum action = slContext.getSlType()
                    .getButtonClick().getClickEnum();
            ISlotSignalContext ret = iSlo.getSlContainer().getGetterContext(
                    dType, GetActionEnum.GetListLineChecked);
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
            BoxActionItem(action, persistTypeEnum, peData, wSize, false);
        }
    }

    private void publishP(ButtonAction.Action b, boolean redirectB) {

        iSlo.getSlContainer().publish(dType,
                new ClickButtonType(ClickButtonType.StandClickEnum.ADDITEM),
                new ButtonAction(b));

        if (redirectB) {
            iSlo.getSlContainer().publish(
                    dType,
                    new ClickButtonType(
                            ClickButtonType.StandClickEnum.MODIFITEM),
                    new ButtonAction(ButtonAction.Action.RedirectButton,
                            new ClickButtonType(
                                    ClickButtonType.StandClickEnum.SHOWITEM)));
        } else {
            iSlo.getSlContainer().publish(
                    dType,
                    new ClickButtonType(
                            ClickButtonType.StandClickEnum.MODIFITEM),
                    new ButtonAction(b));
        }

        iSlo.getSlContainer().publish(dType,
                new ClickButtonType(ClickButtonType.StandClickEnum.REMOVEITEM),
                new ButtonAction(b));
    }

    private class ChangeMode implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            LogT.getLS().info(
                    LogT.getT().receivedSignalLog(
                            slContext.getSlType().toString()));
            PersistTypeEnum persistTypeEnum = slContext.getPersistType();
            publishP(
                    persistTypeEnum.readOnly() ? ButtonAction.Action.DisableButton
                            : ButtonAction.Action.EnableButton,
                    persistTypeEnum.readOnly());
        }
    }

    ISlotListener constructChangeMode() {
        return new ChangeMode();
    }

    ISlotListener constructActionItem(PersistTypeEnum persistTypeEnum) {
        return new ActionItem(persistTypeEnum);
    }
}
