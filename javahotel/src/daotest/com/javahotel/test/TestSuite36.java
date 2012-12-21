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

import java.util.List;

import org.junit.Test;

import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.InvoiceP;
import com.javahotel.remoteinterfaces.HotelT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author hotel
 * 
 */
public class TestSuite36 extends TestHelper {

    /* Basic test for invoice
    * Step 1: create Booking  and Customer
    * Step 2: create Invoice
    * Step 3: persist invoice
    * Step 4: retrieve
    * Verification 1: the invoice number is identical
    * Verification 2: the booking number is identical
    * Verification 3: the customer number is identical
    */
    @Test
    public void test1() {
        loginuser();
        // Step 1
        BookingP bok = createB();
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        DictionaryP a = getDict(DictType.CustomerList, HOTEL1);
        a.setName("C001");
        ReturnPersist resC = hot.persistDic(seu, DictType.CustomerList, a);
        // Step 2
        InvoiceP inv = (InvoiceP) getDict(DictType.InvoiceList, HOTEL1);
        inv.setBooking(bok.getId());
        inv.setCustomer(resC.getId());
        // Step 3
        ReturnPersist res = hot.persistDic(seu, DictType.InvoiceList, inv);
        System.out.println(res.getIdName());

        // Step 4
        List<AbstractTo> li = hot.getDicList(seu, DictType.InvoiceList,
                new HotelT(HOTEL1));
        // Verification 1
        assertEquals(1, li.size());
        InvoiceP i = (InvoiceP) li.get(0);
        assertEquals(res.getIdName(), i.getName());
        // Verification 2
        assertNotNull(i.getBooking());
        assertEquals(bok.getId(), i.getBooking());
        System.out.println(i.getCustomer());
        // Verification 3
        assertNotNull(i.getCustomer());
        assertEquals(resC.getId(), i.getCustomer());
    }

    /*
     * Purpose: test writing and reading invoices for several bookings
     * Step 1: persist booking with three invoices
     * Step 2: persist booking with one invoice
     * Step 3: read invoices for first booking
     * Verification 1: three expected
     * Step 3 : read invoices for second booking
     * Verification 2: one expected
     */
    @Test
    public void test2() {
        loginuser();
        // Step 1
        BookingP bok = createB();
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        DictionaryP a = getDict(DictType.CustomerList, HOTEL1);
        a.setName("C001");
        ReturnPersist resC = hot.persistDic(seu, DictType.CustomerList, a);
        InvoiceP inv = (InvoiceP) getDict(DictType.InvoiceList, HOTEL1);
        inv.setBooking(bok.getId());
        inv.setCustomer(resC.getId());
        ReturnPersist res = hot.persistDic(seu, DictType.InvoiceList, inv);
        System.out.println(res.getIdName());

        inv = (InvoiceP) getDict(DictType.InvoiceList, HOTEL1);
        inv.setBooking(bok.getId());
        inv.setCustomer(resC.getId());
        res = hot.persistDic(seu, DictType.InvoiceList, inv);

        inv = (InvoiceP) getDict(DictType.InvoiceList, HOTEL1);
        inv.setBooking(bok.getId());
        inv.setCustomer(resC.getId());
        res = hot.persistDic(seu, DictType.InvoiceList, inv);

        List<AbstractTo> li = hot.getDicList(seu, DictType.InvoiceList,
                new HotelT(HOTEL1));
        assertEquals(3, li.size());

        // Step 2
        BookingP bok1 = createB();
        bok1 = getpersistName(DictType.BookingList, bok, "BOK0002");
        inv = (InvoiceP) getDict(DictType.InvoiceList, HOTEL1);
        inv.setBooking(bok1.getId());
        inv.setCustomer(resC.getId());
        res = hot.persistDic(seu, DictType.InvoiceList, inv);
        System.out.println(res.getIdName());

        // Step 3
        CommandParam par = new CommandParam();
        par.setHotel(HOTEL1);
        par.setBookingId(bok.getId());
        par.setDict(DictType.InvoiceList);
        li = list.getList(seu, RType.ListDict, par);
        // Verification 1
        assertEquals(3, li.size());
        for (AbstractTo aa : li) {
            inv = (InvoiceP) aa;
            assertEquals(bok.getId(), inv.getBooking());
        }

        // Step 4
        par = new CommandParam();
        par.setHotel(HOTEL1);
        par.setBookingId(bok1.getId());
        par.setDict(DictType.InvoiceList);
        li = list.getList(seu, RType.ListDict, par);
        // Verification 2
        assertEquals(1, li.size());
        for (AbstractTo aa : li) {
            inv = (InvoiceP) aa;
            assertEquals(bok1.getId(), inv.getBooking());
        }

    }

    /* Purpose: bulk invoices testing
     * Step 1: persist booking with 100 invoices and 50 invoices
     * Verification 1: check if there are 50 invoices for second booking
     * Verification 2: check if there are 100 invoices for first booking
     */

    @Test
    public void test3() {
        loginuser();
        // Step 1
        BookingP bok = createB();
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        DictionaryP a = getDict(DictType.CustomerList, HOTEL1);
        a.setName("C001");

        ReturnPersist resC = hot.persistDic(seu, DictType.CustomerList, a);
        BookingP bok1 = createB();
        bok1 = getpersistName(DictType.BookingList, bok, "BOK0002");

        for (int i = 0; i < 100; i++) {
            InvoiceP inv = (InvoiceP) getDict(DictType.InvoiceList, HOTEL1);
            inv.setBooking(bok.getId());
            inv.setCustomer(resC.getId());
            ReturnPersist res = hot.persistDic(seu, DictType.InvoiceList, inv);
            if (i < 50) {
                inv = (InvoiceP) getDict(DictType.InvoiceList, HOTEL1);
                inv.setBooking(bok1.getId());
                inv.setCustomer(resC.getId());
                res = hot.persistDic(seu, DictType.InvoiceList, inv);
            }
        }
        List<AbstractTo> li = hot.getDicList(seu, DictType.InvoiceList,
                new HotelT(HOTEL1));
        assertEquals(150, li.size());

        // Verification 1
        CommandParam par = new CommandParam();
        par.setHotel(HOTEL1);
        par.setBookingId(bok1.getId());
        par.setDict(DictType.InvoiceList);
        li = list.getList(seu, RType.ListDict, par);
        assertEquals(50, li.size());
        for (AbstractTo aa : li) {
            InvoiceP inv = (InvoiceP) aa;
            assertEquals(bok1.getId(), inv.getBooking());
        }

        // Verification 2
        par = new CommandParam();
        par.setHotel(HOTEL1);
        par.setBookingId(bok.getId());
        par.setDict(DictType.InvoiceList);
        li = list.getList(seu, RType.ListDict, par);
        assertEquals(100, li.size());
    }

}
