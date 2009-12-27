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
import com.gwtmodel.table.DataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.WChoosedLine;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.injector.TableFactoriesContainer;
import com.gwtmodel.table.injector.TablesFactories;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.SlotListContainer;
import com.gwtmodel.table.slotmodel.SlotType;
import com.gwtmodel.table.view.util.GetActionName;
import com.gwtmodel.table.view.util.ModalDialog;
import com.gwtmodel.table.view.util.PopupUtil;

class DataListCrudControler extends AbstractSlotContainer {

    private final IDataType dType;
    private final TablesFactories tFactories;
    private final TableFactoriesContainer fContainer;

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

        DrawForm(WSize wSize, FormLineContainer lContainer,ClickButtonType.StandClickEnum action) {
            this.wSize = wSize;
            this.lContainer = lContainer;
            this.action = action;
        }

        public void signal(ISlotSignalContext slContext) {
            String addTitle = GetActionName.getActionName(action);
            IGWidget w = slContext.getGwtWidget();
            VerticalPanel vp = new VerticalPanel();
            FormDialog d = new FormDialog(vp, lContainer.getTitle() + " / " + addTitle, w);
            PopupUtil.setPos(d.getDBox(), wSize);
            d.show();
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
                wSize = new WSize(0, 0, 0, 0);
            }
            FormLineContainer lContainer = fContainer.getFormDefFactory()
                    .construct(dType);
            DisplayFormControler fControler = new DisplayFormControler(
                    tFactories, fContainer, lContainer, dType, 0, 1, mModel);
            SlotListContainer slContainer = fControler.getSlContainer();
            slContainer.registerSubscriberGwt(0,
                    new DrawForm(wSize, lContainer,action));
            fControler.startPublish();
        }

    }

    DataListCrudControler(TablesFactories tFactories,
            TableFactoriesContainer fContainer, IDataType dType) {
        this.dType = dType;
        this.tFactories = tFactories;
        this.fContainer = fContainer;
        SlotType slType = slTypeFactory
                .contructClickButton(ClickButtonType.StandClickEnum.ADDITEM);
        addSubscriber(slType, new ActionItem());
        slType = slTypeFactory
                .contructClickButton(ClickButtonType.StandClickEnum.REMOVEITEM);
        addSubscriber(slType, new ActionItem());
        slType = slTypeFactory
                .contructClickButton(ClickButtonType.StandClickEnum.MODIFITEM);
        addSubscriber(slType, new ActionItem());
    }

    public void startPublish() {
    }

}
