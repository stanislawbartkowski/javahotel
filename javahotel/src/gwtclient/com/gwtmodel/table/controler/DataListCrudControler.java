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

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtmodel.table.DataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.WChoosedLine;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.injector.TableFactoriesContainer;
import com.gwtmodel.table.injector.TablesFactories;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.GetActionEnum;
import com.gwtmodel.table.slotmodel.ISlotCaller;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.PersistEventEnum;
import com.gwtmodel.table.slotmodel.SlotCallerType;
import com.gwtmodel.table.slotmodel.SlotListContainer;
import com.gwtmodel.table.slotmodel.SlotPublisherType;
import com.gwtmodel.table.slotmodel.SlotType;
import com.gwtmodel.table.slotmodel.ValidateActionType;
import com.gwtmodel.table.view.util.GetActionName;
import com.gwtmodel.table.view.util.ModalDialog;
import com.gwtmodel.table.view.util.PopupUtil;

class DataListCrudControler extends AbstractSlotContainer {

    private final IDataType dType;
    private final TablesFactories tFactories;
    private final TableFactoriesContainer fContainer;

    private abstract class Signaller implements ISlotSignaller {

        private final DrawForm dForm;

        Signaller(DrawForm dForm) {
            this.dForm = dForm;
        }

        protected void hide() {
            dForm.d.hide();
        }

    }

    private class PersistData extends Signaller {
        private final SlotType puSlType;
        
        PersistData(DrawForm dForm, SlotType puSlType) {
            super(dForm);
            this.puSlType = puSlType;
        }

        public void signal(ISlotSignalContext slContext) {
            Window.alert("Persist - validate - now persis");
            findAndPublish(puSlType);
//            slContainer.publish(slPublisher);
            hide();
        }

    }

    private class AfterPersistData extends Signaller {

        AfterPersistData(DrawForm dForm) {
            super(dForm);
        }

        public void signal(ISlotSignalContext slContext) {
            Window.alert("Persist - afer persist - success");
            hide();
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
            vp.add(w.getWidget());
        }

    }

    private class DrawForm implements ISlotSignaller {

        private final WSize wSize;
        private final FormLineContainer lContainer;
        private final ClickButtonType.StandClickEnum action;
        private FormDialog d;

        DrawForm(WSize wSize, FormLineContainer lContainer,
                ClickButtonType.StandClickEnum action) {
            this.wSize = wSize;
            this.lContainer = lContainer;
            this.action = action;
        }

        public void signal(ISlotSignalContext slContext) {
            String addTitle = GetActionName.getActionName(action);
            IGWidget w = slContext.getGwtWidget();
            VerticalPanel vp = new VerticalPanel();
            d = new FormDialog(vp, lContainer.getTitle() + " / " + addTitle, w);
            PopupUtil.setPos(d.getDBox(), wSize);
            d.show();
        }

    }

    private class GetterModel implements ISlotCaller {

        private IVModelData peData;
        private SlotListContainer slControlerContainer;

        public void setSlControlerContainer(SlotListContainer slControlerContainer) {
            this.slControlerContainer = slControlerContainer;
        }

        public void setPeData(IVModelData peData) {
            this.peData = peData;
        }

        public ISlotSignalContext call(ISlotSignalContext slContext) {
            IVModelData mData = slControlerContainer.callGetterModelData(GetActionEnum.ModelVData,
                    dType);
            fContainer.getDataModelFactory().fromModelToPersist(dType, mData,
                    peData);
            return slSignalContext.returngetter(slContext, peData);
        }

    }

    private class ActionItem implements ISlotSignaller {

        private final ValidateActionType.ValidateType vType;

        ActionItem(ValidateActionType.ValidateType vType) {
            this.vType = vType;
        }

        public void signal(ISlotSignalContext slContext) {
            ClickButtonType.StandClickEnum action = slContext.getSlType()
                    .getButtonClick().getClickEnum();
            ISlotSignalContext ret = callGetterList(dType);
            DataListType dList = ret.getDataList();
            IVModelData mModel = fContainer.getDataModelFactory().construct(
                    dType);
            IVModelData peData = null;
            WSize wSize;
            if (action != ClickButtonType.StandClickEnum.ADDITEM) {
                WChoosedLine choosedLine = ret.getChoosedLine();
                if (!choosedLine.isChoosed()) {
                    return;
                }
                peData = dList.getRow(choosedLine.getChoosedLine());
                fContainer.getDataModelFactory().copyFromPersistToModel(dType,
                        peData, mModel);
                wSize = choosedLine.getwSize();
            } else {
                IGWidget wi = slContext.getGwtWidget();
                wSize = new WSize(wi.getWidget());
                // crete empty
                peData = fContainer.getDataModelFactory().construct(
                        dType);

            }
            FormLineContainer lContainer = fContainer.getFormDefFactory()
                    .construct(dType);
            ListOfControlDesc liControls;
            if (action != ClickButtonType.StandClickEnum.REMOVEITEM) {
                liControls = tFactories.getControlButtonFactory()
                        .constructAcceptResign();
            } else {
                liControls = tFactories.getControlButtonFactory()
                        .constructRemoveDesign();
            }
            DisplayFormControler fControler = new DisplayFormControler(
                    tFactories, fContainer, lContainer, liControls, dType, 0,
                    1, mModel, action);
            SlotListContainer slControlerContainer = fControler.getSlContainer();
            DrawForm dForm = new DrawForm(wSize, lContainer, action);
            slControlerContainer.registerSubscriber(0, dForm);
            SlotType slType = slTypeFactory
                    .construct(ClickButtonType.StandClickEnum.RESIGN);
            slControlerContainer.registerSubscriber(slType, new ResignAction(dForm));
            
            SlotCallerType slCaller = slContainer.findCaller(slTypeFactory.construct(GetActionEnum.ModelVDataPersist, dType));
            GetterModel ge = (GetterModel) slCaller.getSlCaller();
            ge.setPeData(peData);
            ge.setSlControlerContainer(slControlerContainer);

            PersistEventEnum eType = PersistEventEnum.AddItem;
            switch (vType) {
            case MODIF:
                eType = PersistEventEnum.ChangeItem;
                break;
            case REMOVE:
                eType = PersistEventEnum.RemoveItem;
                break;
            }
            
            

            slType = slTypeFactory.construct(new ValidateActionType(
                    ValidateActionType.ValidateActionEnum.ValidationPassed),
                    dType);
            SlotType puSlType = slTypeFactory.construct(eType,dType);
            slControlerContainer.registerSubscriber(slType, new PersistData(dForm, puSlType));

            slType = slTypeFactory.construct(new ValidateActionType(
                    ValidateActionType.ValidateActionEnum.Validate, vType),
                    dType);
            slControlerContainer.registerRedirector(slTypeFactory
                    .construct(ClickButtonType.StandClickEnum.ACCEPT), slType);

            fControler.startPublish();
        }

    }

    DataListCrudControler(TablesFactories tFactories,
            TableFactoriesContainer fContainer, IDataType dType) {
        this.dType = dType;
        this.tFactories = tFactories;
        this.fContainer = fContainer;
        registerSubscriber(ClickButtonType.StandClickEnum.ADDITEM,
                new ActionItem(ValidateActionType.ValidateType.ADD));
        registerSubscriber(ClickButtonType.StandClickEnum.REMOVEITEM,
                new ActionItem(ValidateActionType.ValidateType.REMOVE));
        registerSubscriber(ClickButtonType.StandClickEnum.MODIFITEM,
                new ActionItem(ValidateActionType.ValidateType.MODIF));
        registerPublisher(PersistEventEnum.AddItem, dType);
        registerPublisher(PersistEventEnum.ChangeItem, dType);
        registerPublisher(PersistEventEnum.RemoveItem, dType);
        registerCaller(GetActionEnum.ModelVDataPersist, dType,
                new GetterModel());
    }

    public void startPublish() {
    }

}
