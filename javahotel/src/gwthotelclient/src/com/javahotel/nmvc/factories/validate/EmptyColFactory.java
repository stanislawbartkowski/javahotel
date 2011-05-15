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
package com.javahotel.nmvc.factories.validate;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.IVField;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.login.LoginField;
import com.javahotel.client.MM;
import com.javahotel.client.types.DataType;
import com.javahotel.client.types.VField;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.HotelP;
import com.javahotel.common.toobject.IField;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.common.toobject.VatDictionaryP;

/**
 * @author hotel
 * 
 */
class EmptyColFactory {

    private IField[] getEmptyField(DataType dt) {

        if (dt.isRType()) {
            switch (dt.getrType()) {
            case AllHotels:
                return new IField[] { HotelP.F.name, HotelP.F.database };
            default:
                assert false : MM.M().NotSupportedError(dt.getrType().name());
            } // switch
        } else if (dt.isDictType()) {
            switch (dt.getdType()) {
            case VatDict:
                return new IField[] { DictionaryP.F.name, VatDictionaryP.F.vat };
            case ServiceDict:
                return new IField[] { DictionaryP.F.name,
                        ServiceDictionaryP.F.vat, ServiceDictionaryP.F.servtype };
            case RoomFacility:
            case RoomStandard:
                return new IField[] { DictionaryP.F.name };
            case RoomObjects:
                return new IField[] { DictionaryP.F.name,
                        ResObjectP.F.standard, ResObjectP.F.maxperson };
            case CustomerList:
                return new IField[] {};
            case OffSeasonDict:
                return new IField[] { DictionaryP.F.name,
                        OfferSeasonP.F.startp, OfferSeasonP.F.endp };
            case PriceListDict:
                return new IField[] { DictionaryP.F.name, OfferPriceP.F.season };

            default:

                assert false : MM.M().NotSupportedError(dt.getdType().name());
            }
        }
        assert false : MM.M().NotSupportedErrorS();
        return null;
    }

    List<IVField> getEmptyCol(DataType dt, PersistTypeEnum persistTypeEnum) {

        List<IVField> li = new ArrayList<IVField>();
        if (dt.isAllPersons()) {
            li.add(new LoginField(LoginField.F.LOGINNAME));
            if (persistTypeEnum == PersistTypeEnum.ADD) {
                li.add(new LoginField(LoginField.F.PASSWORD));
                li.add(new LoginField(LoginField.F.REPASSWORD));
            }
            return li;
        }
        IField[] ft = getEmptyField(dt);
        for (IField f : ft) {
            li.add(new VField(f));
        }
        return li;
    }

}
