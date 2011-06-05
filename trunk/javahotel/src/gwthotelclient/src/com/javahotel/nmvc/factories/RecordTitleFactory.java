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

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.factories.IFormTitleFactory;
import com.javahotel.client.MM;
import com.javahotel.client.types.DataType;
import com.javahotel.common.command.DictType;

/**
 * @author hotel
 * 
 */
class RecordTitleFactory implements IFormTitleFactory {

    @Override
    public String getFormTitle(IDataType dType) {
        DataType dd = (DataType) dType;
        if (dd.isRType())
            switch (dd.getrType()) {
            case AllPersons:
                return "Użytkownik";
            case AllHotels:
                return "Hotel";
            default:
                assert false : MM.M().NotSupportedError(dd.getrType().name());
            }

        DictType d = dd.getdType();
        switch (d) {
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
            assert false : MM.M().NotSupportedError(d.name());
        }
        assert false : MM.M().NotSupportedErrorS();
        return null;
    }

}
