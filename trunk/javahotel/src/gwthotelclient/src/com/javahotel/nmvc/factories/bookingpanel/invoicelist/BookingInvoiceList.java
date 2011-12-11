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
package com.javahotel.nmvc.factories.bookingpanel.invoicelist;

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.controler.DisplayListControlerParam;
import com.gwtmodel.table.controler.TableDataControlerFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.view.util.ClickPopUp;
import com.javahotel.client.IResLocator;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.types.DataType;
import com.javahotel.common.command.DictType;

/**
 * @author hotel
 * 
 */
public class BookingInvoiceList {

    private final IResLocator rI;
    private final IDataType iType;

    private class GetWidget implements ISlotListener {

        private final WSize wSize;

        GetWidget(WSize wSize) {
            this.wSize = wSize;
        }

        @Override
        public void signal(ISlotSignalContext slContext) {
            Widget w = slContext.getGwtWidget().getGWidget();
            new ClickPopUp(wSize, w).setVisible(true);
        }

    }

    public BookingInvoiceList(String resName, WSize w) {
        iType = new DataType(DictType.InvoiceList, resName);
        rI = HInjector.getI().getI();
        TableDataControlerFactory tFactory = GwtGiniInjector.getI()
                .getTableDataControlerFactory();
        CellId panelId = new CellId(0);
        DisplayListControlerParam dList = tFactory.constructParam(iType, null,
                panelId);
        ISlotable i = tFactory.constructDataControler(dList);
        i.getSlContainer().registerSubscriber(iType, 0, new GetWidget(w));
        i.startPublish(null);
    }

}
