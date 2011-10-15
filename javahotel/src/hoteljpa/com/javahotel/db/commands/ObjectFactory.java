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
package com.javahotel.db.commands;

import java.util.logging.Level;

import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.HotelP;
import com.javahotel.common.toobject.PersonP;
import com.javahotel.db.authentication.jpa.Hotel;
import com.javahotel.db.authentication.jpa.Person;
import com.javahotel.db.hotelbase.jpa.Booking;
import com.javahotel.db.hotelbase.jpa.Customer;
import com.javahotel.db.hotelbase.jpa.InvoiceIssuer;
import com.javahotel.db.hotelbase.jpa.OfferPrice;
import com.javahotel.db.hotelbase.jpa.OfferSeason;
import com.javahotel.db.hotelbase.jpa.ParamRegistry;
import com.javahotel.db.hotelbase.jpa.PaymentRow;
import com.javahotel.db.hotelbase.jpa.ResObject;
import com.javahotel.db.hotelbase.jpa.RoomFacilities;
import com.javahotel.db.hotelbase.jpa.RoomStandard;
import com.javahotel.db.hotelbase.jpa.ServiceDictionary;
import com.javahotel.db.hotelbase.jpa.VatDictionary;
import com.javahotel.db.hotelbase.types.IPureDictionary;
import com.javahotel.dbres.log.HLog;

/**
 * Factory for constructing DicType objects.
 * 
 * @author stanislawbartkowski@gmail.com
 */
class ObjectFactory {

    /**
     * Construct DicType object
     * 
     * @param d
     *            DictType
     * @return Object requested
     */
    static IPureDictionary constructADict(final DictType d) {
        try {
            Class<?> cla = getC(d);
            return (IPureDictionary) cla.newInstance();
        } catch (InstantiationException ex) {
            HLog.getL().getL().log(Level.SEVERE, "", ex);
        } catch (IllegalAccessException ex) {
            HLog.getL().getL().log(Level.SEVERE, "", ex);
        }
        return null;
    }

    static Class<?> getC(final DictType d) {
        switch (d) {
        case RegistryParam:
            return ParamRegistry.class;
        case RoomStandard:
            return RoomStandard.class;
        case RoomFacility:
            return RoomFacilities.class;
        case RoomObjects:
            return ResObject.class;
        case VatDict:
            return VatDictionary.class;
        case ServiceDict:
            return ServiceDictionary.class;
        case OffSeasonDict:
            return OfferSeason.class;
        case PriceListDict:
            return OfferPrice.class;
        case CustomerList:
            return Customer.class;
        case BookingList:
            return Booking.class;
        case PaymentRowList:
            return PaymentRow.class;
        case IssuerInvoiceList:
            return InvoiceIssuer.class;
        }
        return null;
    }

    static Class<?> getP(final Class<?> aClass) {
        if (aClass == HotelP.class) {
            return Hotel.class;
        }
        if (aClass == PersonP.class) {
            return Person.class;
        }
        return null;
    }
}
