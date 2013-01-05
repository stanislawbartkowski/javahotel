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

package com.javahotel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.gwtmodel.table.common.dateutil.DateFormatUtil;
import com.javahotel.common.command.BookingEnumTypes;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.HotelOpType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.GuestP;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.types.LId;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite26 extends TestHelper {

    @Test
    public void Test1() {
        System.out.println("Add guests");
        loginuser();
        itest.setTodayDate(D("2008/12/08"));
        BookingP bok = createB();
        bok.setHotel(HOTEL1);
        bok.setName("");
        bok.setBookingType(BookingEnumTypes.Stay);
        OfferPriceP oPrice = getOfferPrice(bok.getSeason(), "Norm");
        bok.setOPrice("Norm");
        bok.setOPrice(oPrice.getName());
        BookElemP be = new BookElemP();
        ResObjectP rO = getResObject("1p");
        be.setResObject("1p");
        ServiceDictionaryP servi = (ServiceDictionaryP) getDict(
                DictType.ServiceDict, HOTEL1);
        servi = getpersistName(DictType.ServiceDict, servi, "LUX");
        be.setService("LUX");
        be.setCheckIn(DateFormatUtil.toD("2008/02/07"));
        be.setCheckOut(DateFormatUtil.toD("2008/02/08"));
        modifPaymentRow(be);

        List<BookElemP> colE = new ArrayList<BookElemP>();
        colE.add(be);
        bok.setBooklist(colE);

        ReturnPersist ret = hot.persistDic(se, DictType.BookingList, bok);
        System.out.println(ret.getIdName());

        CommandParam par = new CommandParam();
        CustomerP cust = (CustomerP) getDict(DictType.CustomerList, HOTEL1);
        cust = getpersistName(DictType.CustomerList, cust, "P001");
        GuestP guest = new GuestP();
        guest.setCheckIn(D("2009/02/07"));
        guest.setCheckOut(D("2009/02/08"));
        guest.setCustomer(cust.getId());

        List<GuestP> guests = new ArrayList<GuestP>();
        guests.add(guest);
        Map<String, List<GuestP>> ma = new HashMap<String, List<GuestP>>();
        ma.put("1p", guests);
        par.setGuests(ma);
        par.setHotel(HOTEL1);
        par.setReservName(ret.getIdName());
        hotop.hotelOp(se, HotelOpType.PersistGuests, par);

        bok = getOneNameN(DictType.BookingList, ret.getIdName());
        for (BookElemP b : bok.getBooklist()) {
            be = b;
        }
        guests = be.getGuests();
        assertNotNull(guests);
        assertEquals(1, guests.size());
        for (GuestP g : guests) {
            LId id = g.getCustomer();
            CustomerP cu = getById(DictType.CustomerList, id);
            System.out.println(cu.getName());
            assertEquals("P001", cu.getName());
        }

        par = new CommandParam();
        cust = (CustomerP) getDict(DictType.CustomerList, HOTEL1);
        cust = getpersistName(DictType.CustomerList, cust, "P003");
        guest = new GuestP();
        guest.setCustomer(cust.getId());
        guest.setCheckIn(D("2009/02/07"));
        guest.setCheckOut(D("2009/02/08"));
        guests = new ArrayList<GuestP>();
        guests.add(guest);
        ma = new HashMap<String, List<GuestP>>();
        ma.put("1p", guests);
        par.setGuests(ma);
        par.setHotel(HOTEL1);
        par.setReservName(ret.getIdName());
        hotop.hotelOp(se, HotelOpType.PersistGuests, par);

        bok = getOneNameN(DictType.BookingList, ret.getIdName());
        be = null;
        for (BookElemP b : bok.getBooklist()) {
            be = b;
        }
        guests = be.getGuests();
        assertNotNull(guests);
        assertEquals(1, guests.size());
        for (GuestP g : guests) {
            LId id = g.getCustomer();
            CustomerP cu = getById(DictType.CustomerList, id);
            System.out.println(cu.getName());
            assertEquals("P003", cu.getName());
        }

    }

}
