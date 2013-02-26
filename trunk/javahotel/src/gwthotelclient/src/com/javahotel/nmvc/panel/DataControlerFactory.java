/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
package com.javahotel.nmvc.panel;

import com.gwtmodel.table.Empty;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.controler.DisplayListControlerParam;
import com.gwtmodel.table.controler.TableDataControlerFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.javahotel.client.IResLocator;
import com.javahotel.client.M;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.start.panel.EPanelCommand;
import com.javahotel.client.types.DataType;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.nmvc.factories.advancepayment.AdvancePayment;
import com.javahotel.nmvc.factories.bookingpanel.BookingPanel;
import com.javahotel.nmvc.factories.cleardatahotel.ClearHotelData;

public class DataControlerFactory {

    private DataControlerFactory() {
    }

    public static void runDataControler(EPanelCommand e, CellId panelId,
            ISlotListener iS) {
        IDataType d = null;
        ISlotable i = null;
        IResLocator rI = HInjector.getI().getI();
        // clear all caches to force reading from database directly
        // refresh
        rI.getR().invalidateCacheList();

        switch (e) {
        case PERSON:
            d = new DataType(RType.AllPersons);
            break;
        case HOTEL:
            d = new DataType(RType.AllHotels);
            break;
        case REMOVEDATA:
            d = new DataType(RType.AllHotels);
            i = new ClearHotelData(panelId);
            break;
        case VAT:
            d = new DataType(DictType.VatDict);
            break;
        case FACILITY:
            d = new DataType(DictType.RoomFacility);
            break;
        case SERVICES:
            d = new DataType(DictType.ServiceDict);
            break;
        case STANDARD:
            d = new DataType(DictType.RoomStandard);
            break;
        case ROOMS:
            d = new DataType(DictType.RoomObjects);
            break;
        case CUSTOMERS:
            d = new DataType(DictType.CustomerList);
            break;
        case SEASON:
            d = new DataType(DictType.OffSeasonDict);
            break;
        case PRICES:
            d = new DataType(DictType.PriceListDict);
            break;
        case BOOKING:
            d = new DataType(DictType.BookingList);
            break;
        case INVOICEISSUER:
            d = new DataType(DictType.IssuerInvoiceList);
            break;
        case BOOKINGPANEL:
            // invalidate/refresh cache at the beginning
            // force reading data from database
            rI.getR().invalidateResCache();
            d = Empty.getDataType();
            i = new BookingPanel(d, panelId);
            break;
        case ADVANCEPAYMENT:
            rI.getR().invalidateCache(RType.DownPayments);
            d = new DataType(RType.DownPayments);
            i = new AdvancePayment(d, panelId);
            break;
        default:
            assert false : M.M().NotSupportedErrorS();
            break;
        }
        if (i == null) {
            TableDataControlerFactory tFactory = GwtGiniInjector.getI()
                    .getTableDataControlerFactory();
            DisplayListControlerParam dList = tFactory.constructParam(d, null,
                    panelId);
            i = tFactory.constructDataControler(dList);
        }
        i.getSlContainer().registerSubscriber(d, 0, iS);
        i.startPublish(null);
    }
}