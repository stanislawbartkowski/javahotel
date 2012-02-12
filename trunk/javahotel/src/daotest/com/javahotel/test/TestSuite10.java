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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.javahotel.common.command.BookingStateType;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.PaymentMethod;
import com.javahotel.common.command.RType;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.PaymentP;
import com.javahotel.common.toobject.PaymentRowP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.types.LId;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite10 extends TestHelper {

    @Test
    public void Test1() throws Exception {
        loginuser();
        BookingP bok = createB();
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        assertEquals("CUR", bok.getSeason());
        LId id = bok.getCustomer();
        assertNotNull(id);
        CommandParam p = new CommandParam();
        p.setHotel(HOTEL1);
        p.setRecId(id);
        p.setDict(DictType.CustomerList);
        AbstractTo pp = list.getOne(se, RType.ListDict, p);
        CustomerP cust = (CustomerP) pp;
        assertEquals("K001", cust.getName());
    }

    @Test
    public void Test2() throws Exception {
        System.out.println("Payments");
        loginuser();
        BookingP bok = createB();
        List<PaymentP> col = new ArrayList<PaymentP>();
        PaymentP p = new PaymentP();
        p.setAmount(new BigDecimal(123));
        p.setDateOp(DateFormatUtil.toD("2008/02/02"));
        p.setPayMethod(PaymentMethod.Cache);
        p.setLp(new Integer(1));
        p.setDatePayment(D("2008/10/01"));
        col.add(p);
        setPay(bok, col);
        // bok.setPayments(col);
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        // col = bok.getPayments();
        col = getPay(bok);
        assertEquals(1, col.size());
        for (PaymentP pp : col) {
            // assertEquals(new Long(123), pp.getAmount().longValue());
            eqBig(new BigDecimal(123), pp.getAmount());
        }
    }

    @Test
    public void Test3() throws Exception {
        System.out.println("validation");
        loginuser();
        BookingP bok = createB();
        bok.setValidationDate(DateFormatUtil.toD("2008/03/02"));
        bok.setValidationAmount(new BigDecimal(12));
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        eqBig(new BigDecimal(12), bok.getValidationAmount());

    }

    @Test
    public void Test4() throws Exception {
        System.out.println("states");
        loginuser();
        BookingP bok = createB();
        List<BookingStateP> col = new ArrayList<BookingStateP>();
        BookingStateP p = new BookingStateP();
        p.setLp(new Integer(1));
        p.setDateOp(DateFormatUtil.toD("2008/02/02"));
        p.setRemarks("rybka");
        p.setBState(BookingStateType.Canceled);

        col.add(p);
        bok.setState(col);
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        col = bok.getState();
        assertEquals(1, col.size());
        for (BookingStateP pp : col) {
            assertEquals("rybka", pp.getRemarks());
            assertEquals(BookingStateType.Canceled, pp.getBState());
        }

        for (int i = 0; i < 100; i++) {
            p = new BookingStateP();
            p.setLp(new Integer(i + 2));
            p.setDateOp(DateFormatUtil.toD("2008/02/02"));
            p.setRemarks("rybka");
            p.setBState(BookingStateType.Canceled);
            col.add(p);
        }

        hot.persistDic(seu, DictType.BookingList, bok);
        bok = getOneName(DictType.BookingList, "BOK0001");
        col = bok.getState();
        assertEquals(101, col.size());
    }

    @Test
    public void Test5() throws Exception {
        System.out.println("booking record");
        loginuser();
        BookingP bok = createB();
        // OfferSeasonP oSeason = getOfferSeason("P2008");
        // bok.setSeason("P2008");
        OfferPriceP oPrice = getOfferPrice(bok.getSeason(), "Norm");
        bok.setOPrice("Norm");
        BookElemP be = new BookElemP();
        ResObjectP rO = getResObject("1p");
        be.setResObject("1p");
        ServiceDictionaryP servi = (ServiceDictionaryP) getDict(
                DictType.ServiceDict, HOTEL1);
        servi = getpersistName(DictType.ServiceDict, servi, HOTEL1);
        be.setService("LUX");

        be.setCheckIn(DateFormatUtil.toD("2008/02/07"));
        be.setCheckOut(DateFormatUtil.toD("2008/03/07"));
        List<BookElemP> colE = new ArrayList<BookElemP>();
        colE.add(be);
        modifPaymentRow(be);
        bok.setBooklist(colE);

        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        BigDecimal sum = (BigDecimal) bok.getF(BookingP.F.customerPrice);
        eqBig(new BigDecimal(3000), sum);
        assertEquals("Norm", bok.getOPrice());
    }

    @Test
    public void Test6() throws Exception {
        System.out.println("booking elem");
        Test5();
        BookingP bok = getOneName(DictType.BookingList, "BOK0001");
        ServiceDictionaryP servi = (ServiceDictionaryP) getDict(
                DictType.ServiceDict, HOTEL1);
        servi = getpersistName(DictType.ServiceDict, servi, "2p2");

        BookElemP be = new BookElemP();
        ResObjectP rO = getResObject("1p");
        be.setResObject("1p");
        be.setService("2p2");
        be.setCheckIn(DateFormatUtil.toD("2008/02/07"));
        be.setCheckOut(DateFormatUtil.toD("2008/03/07"));
        List<BookElemP> col = new ArrayList<BookElemP>();
        col.add(be);
        modifPaymentRow(be);
        bok.setBooklist(col);
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        be = null;
        for (BookElemP bb : bok.getBooklist()) {
            be = bb;
        }
        assertEquals("1p", be.getResObject());

    }

    @Test
    public void Test7() throws Exception {
        System.out.println("booking elem");
        Test6();
        BookingP bok = getOneName(DictType.BookingList, "BOK0001");
        BookElemP be = null;
        for (BookElemP bb : bok.getBooklist()) {
            be = bb;
        }
        PaymentRowP pR = new PaymentRowP();
        List<PaymentRowP> col = new ArrayList<PaymentRowP>();
        pR.setCustomerRate(new BigDecimal(123));
        pR.setOfferRate(new BigDecimal(333));
        pR.setRowFrom(DateFormatUtil.toD("2008/02/07"));
        pR.setRowTo(DateFormatUtil.toD("2008/03/07"));
        col.add(pR);
        pR = new PaymentRowP();
        pR.setCustomerRate(new BigDecimal(1234));
        pR.setOfferRate(new BigDecimal(3334));
        pR.setRowFrom(DateFormatUtil.toD("2008/06/07"));
        pR.setRowTo(DateFormatUtil.toD("2008/06/07"));
        col.add(pR);
        be.setPaymentrows(col);
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        be = null;
        for (BookElemP bb : bok.getBooklist()) {
            be = bb;
        }
        col = be.getPaymentrows();
        assertEquals(2, col.size());
    }
}