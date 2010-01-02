/*
 * Copyright 2009 stanislawbartkowski@gmail.com 
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

import com.javahotel.client.IResLocator;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class PanelCommandFactory {

    private PanelCommandFactory() {
    }

    public static String getPanelCommandLabel(IResLocator sI,
            EPanelCommand command) {
        String key = "PANEL" + command.toString();
        String val = sI.getLabels().PanelLabelNames().get(key);
        if (val == null) {
            return "???";
        }
        return val;
    }

    public static IPanelCommand getPanelCommand(IResLocator sI,
            EPanelCommand command) {

        IPanelCommandBeforeCheck iV = PanelCommandBeforeCheckFactory
                .getPanelCheck(sI, command);
        IPanelCommand i = null;
        switch (command) {
        case PERSON:
            i = new HotelPersonCommand(sI, RType.AllPersons);
            break;
        case HOTEL:
            i = new HotelPersonCommand(sI, RType.AllHotels);
            break;
        case REMOVEDATA:
            i = new ClearHotelDataCommand(sI);
            break;
        case ROOMSADMIN:
            // i = new RoomsAdmin(sI);
            i = new RoomsAdminSt(sI);
            break;
        case BOOKINGPANEL:
            i = new CommandBookingPanel(sI);
            break;
        case BOOKING:
            i = new DoBooking(sI);
            break;
        case PREPAID:
            i = new PrePaid(sI);
            break;
        case ROOMS:
            i = new DictPanelCommand(sI, DictType.RoomObjects);
            break;
        case SERVICES:
            i = new DictPanelCommand(sI, DictType.ServiceDict);
            break;
        case VAT:
            i = new DictPanelCommand(sI, DictType.VatDict);
            break;
        case CUSTOMERS:
            i = new DictPanelCommand(sI, DictType.CustomerList);
            break;
        case SEASON:
            i = new DictPanelCommand(sI, DictType.OffSeasonDict);
            break;
        case PRICES:
            i = new DictPanelCommand(sI, DictType.PriceListDict);
            break;
        case STANDARD:
            i = new DictPanelCommand(sI, DictType.RoomStandard);
            break;
        case FACILITY:
            i = new DictPanelCommand(sI, DictType.RoomFacility);
            break;
        case TESTSCROLLSEASON:
            i = new TestSeasonScrollPanel(sI);
            break;
        case TESTSCROLLSEASONWIDGET:
            i = new TestSeasonScrollPanelWidget(sI);
            break;
        case TESTBOOKINGELEM:
//            i = new TestBookingElem(sI);
//            i = new NewMvcPanel(sI,DictType.PriceListDict);
//            i = new NewMvcPanel(sI,DictType.RoomFacility);
            i = new NewMvcPanel(sI,DictType.RoomStandard);
            break;
        }
        if (i == null) {
            return null;
        }
        if (iV == null) {
            return i;
        }
        iV.setIPanelCommand(i);
        return iV;
    }
}
