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

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.controlbuttonview.ControlButtonViewFactory;
import com.gwtmodel.table.controlbuttonview.IControlButtonView;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.MM;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.slotmodel.ISlotable;

/**
 * @author hotel
 * 
 */
class ChooseFromList extends AbstractSlotContainer implements IChooseRecordContainer {

    private final static String LIST_BUTTON = "HOTEL-LIST-BUTTON";
    private final IControlButtonView bView;

    private class ChooseC extends AbstractChooseListener {

        /**
         * @param dType
         * @param iSlo
         */
        ChooseC(IDataType dType, ISlotable iSlo) {
            super(dType, iSlo);
        }

        @Override
        void modifAfterSelect() {
        }
    }

    ChooseFromList(IDataType dType, IDataType publishType) {
        this.dType = dType;
        ClickButtonType sChoose = new ClickButtonType(LIST_BUTTON);
        ControlButtonDesc bChoose = new ControlButtonDesc(MM.getL().ChooseFromList(), sChoose);
        List<ControlButtonDesc> bList = new ArrayList<ControlButtonDesc>();
        bList.add(bChoose);
        ListOfControlDesc cList = new ListOfControlDesc(bList);
        ControlButtonViewFactory bFactory = GwtGiniInjector.getI().getControlButtonViewFactory();
        bView = bFactory.construct(dType, cList);
        bView.getSlContainer().registerSubscriber(sChoose,
                new ChooseC(dType, this));
        this.setSlContainer(bView);
    }

    @Override
    public void startPublish(CellId cellId) {
        bView.startPublish(cellId);
    }
}
