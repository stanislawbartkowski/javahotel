/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.editc;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.ChooseDictList;
import com.gwtmodel.table.ChooseDictList.ICallBackWidget;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.slotmodel.AbstractSlotListener;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlotType;
import com.gwtmodel.table.view.util.ModalDialog;

/**
 * @author hotel
 * 
 */
abstract class AbstractChooseListener extends AbstractSlotListener {

    AbstractChooseListener(IDataType dType, ISlotable iSlo) {
        super(dType, iSlo);
    }

    abstract void modifAfterSelect();

    private class SelectC implements ICallBackWidget<IVModelData> {

        private D d;

        private class D extends ModalDialog {

            private final Widget w;

            D(VerticalPanel vp, Widget w) {
                super(vp, "");
                this.w = w;
                create();
            }

            @Override
            protected void addVP(VerticalPanel vp) {
                vp.add(w);
            }
        }

        @Override
        public void setWidget(WSize ws, IGWidget w) {
            VerticalPanel vp = new VerticalPanel();
            d = new D(vp, w.getGWidget());
            d.show(ws);
        }

        @Override
        public void setChoosed(IVModelData vData, IVField comboFie) {
            assert vData != null : LogT.getT().cannotBeNull();
            LogT.getL().info(LogT.getT().choosedEdit(vData.toString()));
            iSlo.getSlContainer().publish(dType,
                    DataActionEnum.DrawViewComposeFormAction, vData);
            modifAfterSelect();
            d.hide();
            // send signal : object was chose and set
            SlotType slType = slTypeFactory
                    .construct(IChangeObject.choosedString);
            ISlotSignalContext con = slContextFactory.construct(slType, vData);
            // iSlo.getSlContainer().publish(IChangeObject.choosedString);
            iSlo.getSlContainer().publish(con);
        }

        @Override
        public void setResign() {
            d.hide();
        }
    }

    @Override
    public void signal(ISlotSignalContext slContext) {
        WSize w = new WSize(slContext.getGwtWidget().getGWidget());
        @SuppressWarnings("unused")
        ChooseDictList<IVModelData> c = new ChooseDictList<IVModelData>(dType,
                w, new SelectC());
    }
}
