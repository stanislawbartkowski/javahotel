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
package com.javahotel.nmvc.factories;

import com.gwtmodel.table.factories.IFormTitleFactory;
import com.gwtmodel.table.injector.ICallContext;
import com.javahotel.client.M;
import com.javahotel.client.types.DataType;

/**
 * @author hotel
 * 
 */
class RecordTitleFactory implements IFormTitleFactory {

    @Override
    public String getFormTitle(ICallContext iContext) {
        DataType dd = (DataType) iContext.getDType();
        if (dd.isRType())
            switch (dd.getrType()) {
            case AllPersons:
                return "Użytkownik";
            case AllHotels:
                return "Hotel";
            default:
                assert false : M.M().NotSupportedError(dd.getrType().name());
            }

        if (dd.isDictType()) {
            switch (dd.getdType()) {
            case CustomerList:
                return "Kontrahent";
            case OffSeasonDict:
                return "Sezony";
            case VatDict:
                return "Stawka Vat";
            case RoomFacility:
                return "Wyposażenie";
            case ServiceDict:
                return "Usługa";
            case RoomStandard:
                return "Standard pokoju";
            case RoomObjects:
                return "Pokój";
            case PriceListDict:
                return "Cennik";
            case BookingList:
                return "Rezerwacja";
            default:
                assert false : M.M().NotSupportedError(dd.getdType().name());
            }
        }
        if (dd.isAddType()) {
            switch (dd.getAddType()) {
            case BookElem:
                return "Rezerwacja pokoju";
            case BookRecord:
            case AdvanceHeader:
                return "";
            default:
                assert false : M.M().NotSupportedError(dd.getdType().name());
            }
        }

        assert false : M.M().NotSupportedErrorS();
        return null;
    }

}
