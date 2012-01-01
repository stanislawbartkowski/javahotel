/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.javahotel.common.command.DictType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.GuestP;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.PaymentRowP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.IHotelTest;
import com.javahotel.types.LId;

/**
 *
 * @author hotel
 */
public class TestSuite25 extends TestHelper {

    @Test
    public void Test1() {
        System.out.println("Test empty string");
        loginuser();
        itest.setTodayDate(D("2008/12/08"));
        BookingP bok = createB();
        bok.setHotel(HOTEL1);
        bok.setName("");
        OfferPriceP oPrice = getOfferPrice(bok.getSeason(), "Norm");
        bok.setCustomerPrice(new BigDecimal(999));
        bok.setOPrice("Norm");
        bok.setOPrice(oPrice.getName());
        BookElemP be = new BookElemP();
        ResObjectP rO = getResObject("1p");
        be.setResObject("1p");
        
        ServiceDictionaryP servi = (ServiceDictionaryP) getDict(DictType.ServiceDict, HOTEL1);
        servi = getpersistName(DictType.ServiceDict, servi, "LUX");
        be.setService("LUX");

        be.setCheckIn(DateFormatUtil.toD("2008/02/07"));
        be.setCheckOut(DateFormatUtil.toD("2008/02/08"));
        List<BookElemP> colE = new ArrayList<BookElemP>();
        colE.add(be);
        bok.setBooklist(colE);
        PaymentRowP rP = new PaymentRowP();
        rP.setCustomerPrice(new BigDecimal(100));
        rP.setOfferPrice(new BigDecimal(100));
        rP.setRowFrom(be.getCheckIn());
        rP.setRowTo(be.getCheckOut());
        List<PaymentRowP> rCol = new ArrayList<PaymentRowP>();
        rCol.add(rP);
        be.setPaymentrows(rCol);

        ReturnPersist ret = hot.persistDic(se, DictType.BookingList, bok);
        System.out.println(ret.getIdName());
        assertEquals("1/12/2008", ret.getIdName());
        System.out.println("Now remove ..");
        bok = getOneNameN(DictType.BookingList, ret.getIdName());
        hot.removeDic(se, DictType.BookingList, bok);
        int no = itest.getNumberOfRecord(se, IHotelTest.BOOKINGPAYMENTREGISTER, new HotelT(HOTEL1));
        assertEquals(0, no);
    }

    @Test
    public void Test2() {
        System.out.println("Test set/get guests");
        loginuser();
        itest.setTodayDate(D("2008/12/08"));
        BookingP bok = createB();
        bok.setHotel(HOTEL1);
        bok.setName("");
        OfferPriceP oPrice = getOfferPrice(bok.getSeason(), "Norm");
        bok.setCustomerPrice(new BigDecimal(999));
        bok.setOPrice("Norm");
        bok.setOPrice(oPrice.getName());

        BookElemP be = new BookElemP();
        ResObjectP rO = getResObject("1p");
        be.setResObject("1p");
        ServiceDictionaryP servi = (ServiceDictionaryP) getDict(DictType.ServiceDict, HOTEL1);
        servi = getpersistName(DictType.ServiceDict, servi, "LUX");
        be.setService("LUX");

        be.setCheckIn(D("2008/02/07"));
        be.setCheckOut(D("2008/02/08"));
        modifPaymentRow(be);

        CustomerP cust = (CustomerP) getDict(DictType.CustomerList, HOTEL1);
        cust = getpersistName(DictType.CustomerList, cust, "CUST001");
        GuestP ge = new GuestP();
        ge.setCustomer(cust.getId());
        ge.setCheckIn(D("2009/02/07"));
        ge.setCheckOut(D("2009/02/08"));
        List<GuestP> gList = new ArrayList<GuestP>();
        gList.add(ge);
        be.setGuests(gList);

        List<BookElemP> colE = new ArrayList<BookElemP>();
        colE.add(be);
        bok.setBooklist(colE);

//        PaymentRowP rP = new PaymentRowP();
//        rP.setCustomerPrice(new BigDecimal(100));
//        rP.setOfferPrice(new BigDecimal(100));
//        rP.setRowFrom(be.getCheckIn());
//        rP.setRowTo(be.getCheckOut());
//        List<PaymentRowP> rCol = new ArrayList<PaymentRowP>();
//        rCol.add(rP);
//        be.setPaymentrows(rCol);

        ReturnPersist ret = hot.persistDic(se, DictType.BookingList, bok);
        System.out.println(ret.getIdName());

        bok = getOneNameN(DictType.BookingList, ret.getIdName());
        for (BookElemP b : bok.getBooklist()) {
            be = b;
        }
        gList = be.getGuests();
        assertNotNull(gList);
        assertEquals(1, gList.size());
        for (GuestP g : gList) {
            System.out.println(g.getCheckIn());
            System.out.println(g.getCheckOut());
            eqD("2009/02/07",g.getCheckIn());
            eqD("2009/02/08",g.getCheckOut());
        }

        cust = (CustomerP) getDict(DictType.CustomerList, HOTEL1);
        cust = getpersistName(DictType.CustomerList, cust, "CUST002");
        ge = new GuestP();
        ge.setCustomer(cust.getId());
        ge.setCheckIn(D("2009/02/07"));
        ge.setCheckOut(D("2009/02/08"));
        gList.add(ge);

        cust = (CustomerP) getDict(DictType.CustomerList, HOTEL1);
        cust = getpersistName(DictType.CustomerList, cust, "CUST003");
        ge = new GuestP();
        ge.setCheckIn(D("2009/02/07"));
        ge.setCheckOut(D("2009/02/08"));
        ge.setCustomer(cust.getId());
        gList.add(ge);

        ret = hot.persistDic(se, DictType.BookingList, bok);
        System.out.println(ret.getIdName());

        bok = getOneNameN(DictType.BookingList, ret.getIdName());
        for (BookElemP b : bok.getBooklist()) {
            be = b;
        }
        gList = be.getGuests();
        assertNotNull(gList);
        assertEquals(3, gList.size());
        String[] li = new String[]{"CUST001", "CUST002", "CUST003"};
        for (GuestP g : gList) {
            LId id = g.getCustomer();
            CustomerP cu = getById(DictType.CustomerList, id);
            System.out.println(cu.getName());
            boolean found = false;
            for (int i = 0; i < li.length; i++) {
                if (li[i] == null) {
                    continue;
                }
                if (li[i].equals(cu.getName())) {
                    found = true;
                    li[i] = null;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    public void Test3() {
        System.out.println("Test null customer");
        loginuser();
        itest.setTodayDate(D("2008/12/08"));
        CustomerP cust = (CustomerP) getDict(DictType.CustomerList, HOTEL1);
        ReturnPersist ret = hot.persistDic(se, DictType.CustomerList, cust);
        System.out.println(ret.getIdName());
    }
}
