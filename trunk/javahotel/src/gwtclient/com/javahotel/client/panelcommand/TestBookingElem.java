/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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

import com.google.gwt.user.client.ui.VerticalPanel;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.ISetGwtWidget;
import com.javahotel.client.dialog.user.booking.BookingCommunicator;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.mvc.apanel.GwtPanel;
import com.javahotel.client.mvc.apanel.IPanel;
import com.javahotel.client.mvc.controller.onearecord.IOneARecord;
import com.javahotel.client.mvc.controller.onearecord.OneRecordFactory;
import com.javahotel.client.mvc.crud.controler.ICrudControler;
import com.javahotel.client.mvc.crud.controler.ICrudRecordFactory;
import com.javahotel.client.mvc.crud.controler.ICrudView;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.dictcrud.controler.DictCrudControlerFactory;
import com.javahotel.client.mvc.dictcrud.controler.RecordAuxParam;
import com.javahotel.client.mvc.record.view.IRecordView;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.BookingP;

class TestBookingElem extends AbstractPanelCommand {

    private ICrudControler cPan;
    private final IResLocator rI;
    private final IPanel custV = new GwtPanel(new VerticalPanel());

    TestBookingElem(IResLocator rI) {
        this.rI = rI;

    }

    public void beforeDrawAction(ISetGwtWidget iSet) {
//        BookResRoom b = new BookResRoom(rI, null);
        RecordAuxParam pa = new RecordAuxParam();
        BookingCommunicator c = new BookingCommunicator(
                BookingCommunicator.ViewType.RESERV);
        pa.setiCon(c);
        cPan = HInjector.getI().getDictCrudControlerFactory().getCrud(new DictData(
                DictType.BookingList), pa, null);
        ICrudRecordFactory conI;
        ICrudView cView;
        conI = cPan.getF();
        BookingP p = new BookingP();
//        RecordModel mo = new RecordModel(null,null);
//        mo.setA(p);        
        cView = conI.getView(null, null, 0, custV);
        IRecordView iView = conI.getRView(cView);
        IOneARecord oRecord;
        oRecord = OneRecordFactory.getR(rI, new DictData(DictType.BookingList),
                conI, iView);
        oRecord.setFields(p);
        iSet.setGwtWidget(new DefaultMvcWidget(custV.getPanel()));
        conI.show(cView);
    }

    public void drawAction() {
//        cPan.drawTable();
    }
}
