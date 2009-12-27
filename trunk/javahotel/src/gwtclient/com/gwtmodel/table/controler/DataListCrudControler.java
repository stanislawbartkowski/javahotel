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
import com.gwtmodel.table.slotmodel.SlotListContainer;
import com.gwtmodel.table.slotmodel.SlotType;
import com.gwtmodel.table.slotmodel.ValidateActionEnum;
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

        PersistData(DrawForm dForm) {
            super(dForm);
        }

        public void signal(ISlotSignalContext slContext) {
            Window.alert("Persist - validate");
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

        private final IVModelData mModel;

        GetterModel(IVModelData mModel) {
            this.mModel = mModel;
        }

        public ISlotSignalContext call(ISlotSignalContext slContext) {
            return slSignalContext.returngetter(slContext, mModel);
        }

    }

    private class ActionItem implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            ClickButtonType.StandClickEnum action = slContext.getSlType()
                    .getButtonClick().getClickEnum();
            ISlotSignalContext ret = callGetterList(dType);
            DataListType dList = ret.getDataList();
            IVModelData mModel = fContainer.getDataModelFactory().construct(
                    dType);
            WSize wSize;
            if (action != ClickButtonType.StandClickEnum.ADDITEM) {
                WChoosedLine choosedLine = ret.getChoosedLine();
                if (!choosedLine.isChoosed()) {
                    return;
                }
                IVModelData mData = dList.getRow(choosedLine.getChoosedLine());
                fContainer.getDataModelFactory().copyFromPersistToModel(dType,
                        mData, mModel);
                wSize = choosedLine.getwSize();
            } else {
                IGWidget wi = slContext.getGwtWidget();
                wSize = new WSize(wi.getWidget());
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
            SlotListContainer slContainer = fControler.getSlContainer();
            DrawForm dForm = new DrawForm(wSize, lContainer, action);
            slContainer.registerSubscriberGwt(0, dForm);
            SlotType slType;
            slType = slTypeFactory
                    .constructClickButton(ClickButtonType.StandClickEnum.RESIGN);
            slContainer.registerSubscriber(slType, new ResignAction(dForm));

            slType = slTypeFactory.construct(GetActionEnum.ModelVData, dType);
            slContainer.addCaller(slType, new GetterModel(mModel));

            slType = slTypeFactory.construct(
                    ValidateActionEnum.ValidationPassed, dType);
            slContainer.registerSubscriber(slType, new PersistData(dForm));

            slType = slTypeFactory
                    .construct(ValidateActionEnum.Validate, dType);
            slContainer
                    .addRedirector(
                            slTypeFactory
                                    .constructClickButton(ClickButtonType.StandClickEnum.ACCEPT),
                            slType);
            fControler.startPublish();
        }

    }

    DataListCrudControler(TablesFactories tFactories,
            TableFactoriesContainer fContainer, IDataType dType) {
        this.dType = dType;
        this.tFactories = tFactories;
        this.fContainer = fContainer;
        addSubscriber(ClickButtonType.StandClickEnum.ADDITEM, new ActionItem());
        addSubscriber(ClickButtonType.StandClickEnum.REMOVEITEM,
                new ActionItem());
        addSubscriber(ClickButtonType.StandClickEnum.MODIFITEM,
                new ActionItem());
    }

    public void startPublish() {
    }

}
