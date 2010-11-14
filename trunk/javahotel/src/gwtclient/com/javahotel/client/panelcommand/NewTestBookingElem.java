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
package com.javahotel.client.panelcommand;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.dialog.ISetGwtWidget;
import com.javahotel.nmvc.common.AddType;
import com.javahotel.nmvc.common.DataType;
import com.javahotel.nmvc.controler.DataControlerEnum;
import com.javahotel.nmvc.controler.DataControlerFactory;

class NewTestBookingElem extends AbstractPanelCommand {

    private final IResLocator rI;
    private ISetGwtWidget iS;

    NewTestBookingElem(IResLocator rI) {
        this.rI = rI;
    }

    private class SetGwt implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContext) {
            iS.setGwtWidget(new DefaultMvcWidget(slContext.getGwtWidget()
                    .getGWidget()));
        }

    }

    @Override
    public void beforeDrawAction(ISetGwtWidget iSet) {
        this.iS = iSet;
        DataType dType = new DataType(AddType.BookElem);
        ISlotable iControler = DataControlerFactory.constructDataControler(rI,
                DataControlerEnum.DisplayList, dType,
                new CellId(0));
        iControler.getSlContainer().registerSubscriber(dType,0, new SetGwt());
        iControler.startPublish(null);

    }

    public void drawAction() {
        // TODO Auto-generated method stub

    }

}
