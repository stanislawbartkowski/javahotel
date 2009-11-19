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
package com.javahotel.client.mvc.recordviewdef;

import java.util.List;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.IPersistAction;
import com.javahotel.client.dialog.MvcWindowSize;
import com.javahotel.client.mvc.record.model.RecordField;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class GetRecordDefFactory {

    private GetRecordDefFactory() {
    }

    public static List<RecordField> getDef(final IResLocator rI,
            final DictData da) {
        return rI.getView().getDef(rI, da);
    }

    public static String getTitle(final IResLocator rI, final DictData da) {
        if (da.getSE() != null) {
            switch (da.getSE()) {
            case SpecialPeriod:
                switch (da.getSpeT()) {
                case LOW:
                    return "Okres poza sezonem";
                case SPECIAL:
                    return "Okres specjalny";
                default:
                    return null;
                }
            case CustomerPhone:
                return "Telefon";
            case CustomerAccount:
                return "Konto";
            case BookingHeader:
            case ValidationHeader:
            case RowPaymentElem:
                return ""; // empty
            case BookingElem:
                return "Rezerwacja pokoju";
            case AddPayment:
                return "Wpłata zaliczki";
            case ResGuestList:
                return "Zameldowanie";
            case BillsList:
                return "Rachunek";
            case AddPaymentList:
                return "Dodatkowa usługa";
            case LoginUser:
                return "Wejście do hotelu";
            case LoginAdmin:
                return "Administrator";
            }
        }
        if (da.getD() == null) {
            switch (da.getRt()) {
            case AllPersons:
                return "Osoba";
            case AllHotels:
                return "Hotel";
            default:
                assert false : da.getRt() + " : invalid value";
            }
        }
        switch (da.getD()) {
        case RoomFacility:
            return "Wyposażenie pokoju";
        case RoomStandard:
            return "Standard pokojów";
        case RoomObjects:
            return "Pokoje";
        case VatDict:
            return "Stawki vat";
        case ServiceDict:
            return "Słownik usług";
        case OffSeasonDict:
            return "Sezony";
        case PriceListDict:
            return "Cenniki";
        case CustomerList:
            return "Kontrahenci";
        case BookingList:
            return "Rezerwacje";
        default:
            assert false : da.getD() + " : invalid value";
        }
        return null;
    }

    public static String getActionName(final IResLocator rI, final int action) {
        switch (action) {
        case IPersistAction.ADDACION:
            return "Dodanie";
        case IPersistAction.MODIFACTION:
            return "Zmiana";
        case IPersistAction.DELACTION:
            return "Usunięcie";
        }
        return null;
    }

    public static MvcWindowSize getSize(IResLocator rI, DictData da) {
        return rI.getView().getSize(da);
    }
}
