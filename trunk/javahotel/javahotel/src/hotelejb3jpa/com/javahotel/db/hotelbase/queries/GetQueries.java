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
package com.javahotel.db.hotelbase.queries;

import java.util.Collection;
import java.util.Date;

import com.javahotel.common.command.DictType;
import com.javahotel.db.context.ICommandContext;
import com.javahotel.db.hotelbase.jpa.AdvancePayment;
import com.javahotel.db.hotelbase.jpa.OfferPrice;
import com.javahotel.db.hotelbase.jpa.OfferSeasonPeriod;
import com.javahotel.db.hotelbase.jpa.ParamRegistry;
import com.javahotel.db.hotelbase.jpa.PaymentRow;
import com.javahotel.db.hotelbase.jpa.RHotel;
import com.javahotel.db.hotelbase.types.IHotelDictionary;
import com.javahotel.db.jtypes.IId;

public class GetQueries {

    private GetQueries() {
    }

    public static IHotelDictionary getD(final ICommandContext iC,
            final Class<?> cla, final RHotel hotel, final String name) {
        IHotelDictionary o = iC.getJpa().getOneWhereRecord(cla, "name", name,
                "hotel", hotel);
        return o;
    }

    public static Collection<IId> getDList(final ICommandContext iC,
            final Class<?> cla, final DictType d, boolean all) {
        String qName = null;
        switch (d) {
            case RoomStandard:
                qName = "getListStandard";
                break;
            case RoomFacility:
                qName = "getListFacilities";
                break;
            case RoomObjects:
                qName = "getRoomObjects";
                break;
            case VatDict:
                qName = "getVatDict";
                break;
            case ServiceDict:
                qName = "getListServices";
                break;
            case OffSeasonDict:
                qName = "getSeasons";
                break;
            case PriceListDict:
                qName = "getPriceList";
                break;
            case CustomerList:
                qName = "getCustomerList";
                break;
            case BookingList:
                qName = "getBookingList";
                break;
        }
        Collection<IId> c = iC.getJpa().getNamedQuery(qName, "hotel",
                iC.getHotel());
        return c;
    }

    public static Collection<IId> getDList(final ICommandContext iC,
            final Class<?> cla, final DictType d) {
        return getDList(iC, cla, d, false);
    }

    public static OfferPrice getOnePriceList(final ICommandContext iC,
            String seasonName, String offerName) {
        OfferPrice o = iC.getJpa().getNamedOneQuery("getOnePriceList", "seasonname", seasonName, "name", offerName, "hotel", iC.getRHotel());
        return o;
    }

    public static int getNumber(final ICommandContext iC, final Class<?> cla) {
        Long l = iC.getJpa().getNumber(cla);
        return l.intValue();
    }

    public static OfferSeasonPeriod getSeasonPeriod(final ICommandContext iC,
            String seasonName, Long pId) {
        OfferSeasonPeriod op;
        op = iC.getJpa().getNamedOneQuery("getSeasonPeriod", "hotel",
                iC.getRHotel(), "name", seasonName, "pId", pId);
        return op;

    }

    public static Collection<PaymentRow> getPaymentForReservation(
            final ICommandContext iC, final Date dFrom, final Date dTo,
            final String oName) {
        Collection<PaymentRow> c = iC.getJpa().getNamedQuery(
                "getObjectResState", "hotel", iC.getRHotel(), "oName", oName,
                "dFrom", dFrom, "dTo", dTo);
        return c;
    }

    public static Collection<AdvancePayment> getValidationForHotel(
            final ICommandContext iC) {
        Collection<AdvancePayment> col;
        col = iC.getJpa().getNamedQuery("getValidationsForDay", "hotel",
                iC.getRHotel());
        return col;
    }

    public static Collection<ParamRegistry> getRegistryEntries(
            final ICommandContext iC, final String keylike) {
        Collection<ParamRegistry> c = iC.getJpa().getNamedQuery(
                "getParamsLike", "hotel", iC.getRHotel(), "namelike",
                keylike + "%");
        return c;

    }
}
