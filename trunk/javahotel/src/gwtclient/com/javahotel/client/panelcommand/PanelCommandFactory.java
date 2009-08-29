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

    public static String getPanelCommandLabel(IResLocator sI, EPanelCommand command) {
        String key = "PANEL" + command.toString();
        String val = sI.getLabels().PanelLabelNames().get(key);
        if (val == null) {
            return "???";
        }
        return val;
    }

    public static IPanelCommand getPanelCommand(IResLocator sI, EPanelCommand command) {
        switch (command) {
            case PERSON:
                return new HotelPersonCommand(sI, RType.AllPersons);
            case HOTEL:
                return new HotelPersonCommand(sI, RType.AllHotels);
            case REMOVEDATA:
                return new ClearHotelDataCommand(sI);
            case ROOMSADMIN:
                return new RoomsAdmin(sI);
            case BOOKINGPANEL:
                return new CommandBookingPanel(sI);
            case BOOKING:
                return new DoBooking(sI);
            case PREPAID:
                return new PrePaid(sI);
            case ROOMS:
                return new DictPanelCommand(sI, DictType.RoomObjects);
            case SERVICES:
                return new DictPanelCommand(sI, DictType.ServiceDict);
            case VAT:
                return new DictPanelCommand(sI, DictType.VatDict);
            case CUSTOMERS:
                return new DictPanelCommand(sI, DictType.CustomerList);
            case SEASON:
                return new DictPanelCommand(sI, DictType.OffSeasonDict);
            case PRICES:
                return new DictPanelCommand(sI, DictType.PriceListDict);
            case STANDARD:
                return new DictPanelCommand(sI, DictType.RoomStandard);
            case FACILITY:
                return new DictPanelCommand(sI, DictType.RoomFacility);
        }
        return null;
    }
}
