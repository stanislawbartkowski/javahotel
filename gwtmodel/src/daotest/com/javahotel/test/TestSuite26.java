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

package com.javahotel.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.javahotel.common.command.BookingEnumTypes;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.HotelOpType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.GuestP;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.common.util.GetMaxUtil;
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
        List<BookRecordP> col = new ArrayList<BookRecordP>();
        BookRecordP p = new BookRecordP();
        OfferPriceP oPrice = getOfferPrice(bok.getSeason(), "Norm");
        p.setCustomerPrice(new BigDecimal(999));
        p.setDataFrom(DateFormatUtil.toD("2008/02/07"));
        p.setLp(new Integer(1));
        p.setOPrice("Norm");
        p.setOPrice(oPrice.getName());
        col.add(p);
        bok.setBookrecords(col);
        BookElemP be = new BookElemP();
        ResObjectP rO = getResObject("1p");
        be.setResObject("1p");
        ServiceDictionaryP servi = (ServiceDictionaryP) getDict(DictType.ServiceDict, "LUX");
        servi = getpersistName(DictType.ServiceDict, servi, "LUX");
        be.setService("LUX");
        be.setCheckIn(DateFormatUtil.toD("2008/02/07"));
        be.setCheckOut(DateFormatUtil.toD("2008/02/08"));
        List<BookElemP> colE = new ArrayList<BookElemP>();
        colE.add(be);
        p.setBooklist(colE);

        ReturnPersist ret = hot.persistDicReturn(se, DictType.BookingList, bok);
        System.out.println(ret.getIdName());


        CommandParam par = new CommandParam();
        CustomerP cust = (CustomerP) getDict(DictType.CustomerList,HOTEL1);
        cust = getpersistName(DictType.CustomerList,cust,"P001");
        GuestP guest = new GuestP();
        guest.setCustomer(cust.getId());
        List<GuestP> guests = new ArrayList<GuestP>();
        guests.add(guest);
        Map<String,List<GuestP>> ma = new HashMap<String,List<GuestP>>();
        ma.put("1p",guests);
        par.setGuests(ma);
        par.setHotel(HOTEL1);
        par.setReservName(ret.getIdName());
        hotop.hotelOp(se, HotelOpType.PersistGuests,par);

        bok = getOneNameN(DictType.BookingList, ret.getIdName());
        p = GetMaxUtil.getLastBookRecord(bok);
        be = null;
        for (BookElemP b : p.getBooklist()) {
            be = b;
        }
        guests = be.getGuests();
        assertNotNull(guests);
        assertEquals(1, guests.size());
        for (GuestP g : guests) {
            LId id = g.getCustomer();
            CustomerP cu = getById(DictType.CustomerList, id);
            System.out.println(cu.getName());
            assertEquals("P001",cu.getName());
        }

        par = new CommandParam();
        cust = (CustomerP) getDict(DictType.CustomerList,HOTEL1);
        cust = getpersistName(DictType.CustomerList,cust,"P003");
        guest = new GuestP();
        guest.setCustomer(cust.getId());
        guests = new ArrayList<GuestP>();
        guests.add(guest);
        ma = new HashMap<String,List<GuestP>>();
        ma.put("1p",guests);
        par.setGuests(ma);
        par.setHotel(HOTEL1);
        par.setReservName(ret.getIdName());
        hotop.hotelOp(se, HotelOpType.PersistGuests,par);

        bok = getOneNameN(DictType.BookingList, ret.getIdName());
        p = GetMaxUtil.getLastBookRecord(bok);
        be = null;
        for (BookElemP b : p.getBooklist()) {
            be = b;
        }
        guests = be.getGuests();
        assertNotNull(guests);
        assertEquals(1, guests.size());
        for (GuestP g : guests) {
            LId id = g.getCustomer();
            CustomerP cu = getById(DictType.CustomerList, id);
            System.out.println(cu.getName());
            assertEquals("P003",cu.getName());
        }
    
    }

}
