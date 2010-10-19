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
import com.javahotel.nmvc.common.AddType;
import com.javahotel.nmvc.controler.DataControlerEnum;

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
        String val = (String) sI.getLabels().PanelLabelNames().get(key);
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
            // i = new HotelPersonCommand(sI, RType.AllPersons);
            i = new NewMvcPanel(sI, RType.AllPersons);
            break;
        case HOTEL:
            // i = new HotelPersonCommand(sI, RType.AllHotels);
            i = new NewMvcPanel(sI, RType.AllHotels);
            break;
        case REMOVEDATA:
            // i = new ClearHotelDataCommand(sI);
            i = new NewMvcPanel(sI, DataControlerEnum.ClearDataHotel);
            break;
        case ROOMSADMIN:
//            i = new RoomsAdmin(sI);
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
            i = new NewMvcPanel(sI, DictType.RoomObjects);
            // i = new DictPanelCommand(sI, DictType.RoomObjects);
            break;
        case SERVICES:
            i = new NewMvcPanel(sI, DictType.ServiceDict);
            // i = new DictPanelCommand(sI, DictType.ServiceDict);
            break;
        case VAT:
            i = new NewMvcPanel(sI, DictType.VatDict);
            // i = new DictPanelCommand(sI, DictType.VatDict);
            break;
        case CUSTOMERS:
            // i = new DictPanelCommand(sI, DictType.CustomerList);
            i = new NewMvcPanel(sI, DictType.CustomerList);
            break;
        case SEASON:
//            i = new DictPanelCommand(sI, DictType.OffSeasonDict);
            i = new NewMvcPanel(sI, DictType.OffSeasonDict);
            break;
        case PRICES:
            // i = new DictPanelCommand(sI, DictType.PriceListDict);
            i = new NewMvcPanel(sI, DictType.PriceListDict);
            break;
        case STANDARD:
            i = new NewMvcPanel(sI, DictType.RoomStandard);
            // i = new DictPanelCommand(sI, DictType.RoomStandard);
            break;
        case FACILITY:
            i = new NewMvcPanel(sI, DictType.RoomFacility);
            // i = new DictPanelCommand(sI, DictType.RoomFacility);
            break;
        case TESTSCROLLSEASON:
            i = new TestSeasonScrollPanel(sI);
            break;
        case TESTSCROLLSEASONWIDGET:
            i = new TestSeasonScrollPanelWidget(sI);
            break;
        case TESTBOOKINGELEM:
            i = new TestBookingElem(sI);
            // i = new NewMvcPanel(sI,DictType.PriceListDict);
            // i = new NewMvcPanel(sI,DictType.RoomFacility);
            // i = new NewMvcPanel(sI,DictType.RoomStandard);
            // i = new NewMvcPanel(sI,DictType.RoomObjects);
            // i = new NewMvcPanel(sI,DictType.PriceListDict);
            // i = new NewMvcPanel(sI,DictType.CustomerList);
            // i = new NewMvcPanel(sI,RType.AllPersons);
            // i = new NewMvcPanel(sI, RType.AllHotels);
            // i = new NewMvcPanel(sI, DictType.BookingList);
            break;
        case TESTBOOKING:
            i = new NewMvcPanel(sI, DictType.BookingList);
            break;

        case TESTBOOKINGNEWELEM:
            i = new NewTestBookingElem(sI);
            break;

        case TESTPANEL:
            i = new TestBookingElem(sI);
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
