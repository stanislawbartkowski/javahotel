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

import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.composecontroller.ComposeControllerType;
import com.gwtmodel.table.composecontroller.IComposeController;
import com.gwtmodel.table.controlbuttonview.IControlButtonView;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.injector.TablesFactories;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCaller;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.SlotListContainer;
import com.gwtmodel.table.view.util.GetActionName;
import com.gwtmodel.table.view.util.ModalDialog;
import com.gwtmodel.table.view.util.PopupUtil;

class DataListCrudControler extends AbstractSlotContainer {

    private final IDataType dType;
    private final TablesFactories tFactories;
    private final ITableCustomFactories fContainer;
    private final DataListParam listParam;

    private abstract class Signaller implements ISlotSignaller {

        private final DrawForm dForm;

        Signaller(DrawForm dForm) {
            this.dForm = dForm;
        }

        protected void hide() {
            dForm.d.hide();
        }
    }

    private class AfterPersistData extends Signaller {

        private final PersistTypeEnum persistTypeEnum;

        AfterPersistData(DrawForm dForm, PersistTypeEnum persistTypeEnum) {
            super(dForm);
            this.persistTypeEnum = persistTypeEnum;
        }

        public void signal(ISlotSignalContext slContext) {
            hide();
            publish(DataActionEnum.RefreshAfterPersistActionSignal, dType,
                    persistTypeEnum);
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

        public void signal(ISlotSignalContext slContext) {
            iController.getSlContainer().publish(dataActionEnum, dType,
                    persistTypeEnum);
        }
    }

    private class ResignAction extends Signaller {

        ResignAction(DrawForm dForm) {
            super(dForm);
        }

        public void signal(ISlotSignalContext slContext) {
            hide();
        }
    }

    private class FormDialog extends ModalDialog {

        private final IGWidget w;

        FormDialog(VerticalPanel vp, String title, IGWidget w) {
            super(vp, title);
            this.w = w;
            create();
        }

        @Override
        protected void addVP(VerticalPanel vp) {
            vp.add(w.getGWidget());
        }
    }

    private class DrawForm implements ISlotSignaller {

        private final WSize wSize;
        private final String title;
        private final ClickButtonType.StandClickEnum action;
        private FormDialog d;

        DrawForm(WSize wSize, String title,
                ClickButtonType.StandClickEnum action) {
            this.wSize = wSize;
            this.title = title;
            this.action = action;
        }

        public void signal(ISlotSignalContext slContext) {
            String addTitle = GetActionName.getActionName(action);
            IGWidget w = slContext.getGwtWidget();
            VerticalPanel vp = new VerticalPanel();
            d = new FormDialog(vp, title + " / " + addTitle, w);
            PopupUtil.setPos(d.getDBox(), wSize);
            d.show();
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

        public void signal(ISlotSignalContext slContext) {
            ClickButtonType.StandClickEnum action = slContext.getSlType().getButtonClick().getClickEnum();
            ISlotSignalContext ret = getGetterContext(
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
            String title = listParam.getFormFactory().getFormTitle(dType);
            ListOfControlDesc liControls;
            if (action != ClickButtonType.StandClickEnum.REMOVEITEM) {
                liControls = tFactories.getControlButtonFactory().constructAcceptResign();
            } else {
                liControls = tFactories.getControlButtonFactory().constructRemoveDesign();
            }
            CellId cId = new CellId(0);
            IComposeController fController = listParam.getfControler().construct(dType);
            IControlButtonView cView = tFactories.getbViewFactory().construct(
                    liControls);
            ComposeControllerType bType = new ComposeControllerType(cView,
                    null, 1, 0);
            fController.registerControler(bType);
            fController.createComposeControle(cId);

            SlotListContainer slControlerContainer = fController.getSlContainer();
            DrawForm dForm = new DrawForm(wSize, title, action);
            slControlerContainer.registerSubscriber(cId, dForm);
            slControlerContainer.registerSubscriber(
                    ClickButtonType.StandClickEnum.RESIGN, new ResignAction(
                    dForm));
            PersistData pData = new PersistData(persistTypeEnum, fController,
                    DataActionEnum.ValidateComposeFormAction);
            slControlerContainer.registerSubscriber(
                    ClickButtonType.StandClickEnum.ACCEPT, pData);

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
                    DataActionEnum.ChangeViewComposeFormModeAction, dType,
                    persistTypeEnum);

            slControlerContainer.registerCaller(
                    GetActionEnum.GetModelToPersist, dType, new GetterModel(
                    slControlerContainer, peData));
        }
    }

    DataListCrudControler(TablesFactories tFactories,
            ITableCustomFactories fContainer, DataListParam listParam,
            IDataType dType) {
        this.dType = dType;
        this.tFactories = tFactories;
        this.fContainer = fContainer;
        this.listParam = listParam;
        registerSubscriber(ClickButtonType.StandClickEnum.ADDITEM,
                new ActionItem(PersistTypeEnum.ADD));
        registerSubscriber(ClickButtonType.StandClickEnum.REMOVEITEM,
                new ActionItem(PersistTypeEnum.REMOVE));
        registerSubscriber(ClickButtonType.StandClickEnum.MODIFITEM,
                new ActionItem(PersistTypeEnum.MODIF));
    }
}
