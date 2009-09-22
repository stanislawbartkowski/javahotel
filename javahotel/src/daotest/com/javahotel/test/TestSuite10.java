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

import org.junit.Test;

import com.javahotel.common.command.BookingStateType;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.PaymentMethod;
import com.javahotel.common.command.RType;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.AdvancePaymentP;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookRecordP;
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
        p.setSumOp(true);
        p.setLp(new Integer(1));
        p.setDatePayment(D("2008/10/01"));
        col.add(p);
        setPay(bok,col);
//        bok.setPayments(col);
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
//        col = bok.getPayments();
        col = getPay(bok);
        assertEquals(1, col.size());
        for (PaymentP pp : col) {
//            assertEquals(new Long(123), pp.getAmount().longValue());
            eqBig(new BigDecimal(123), pp.getAmount());
        }
    }

    @Test
    public void Test3() throws Exception {
        System.out.println("validation");
        loginuser();
        BookingP bok = createB();
        List<AdvancePaymentP> col = new ArrayList<AdvancePaymentP>();
        AdvancePaymentP p = new AdvancePaymentP();
        p.setLp(new Integer(1));
        p.setDateOp(DateFormatUtil.toD("2008/02/02"));
        p.setValidationDate(DateFormatUtil.toD("2008/03/02"));
        p.setAmount(new BigDecimal(12));
//        p.setClosed(false);
        p.setRemarks("rybka");
        col.add(p);
        setVal(bok,col);
//        bok.setValidations(col);
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
//        col = bok.getValidations();
        col = getVal(bok);
        assertEquals(1, col.size());
        for (AdvancePaymentP pp : col) {
//            assertEquals(new Long(12), pp.getAmount().longValue());
            eqBig(new BigDecimal(12), pp.getAmount());
            assertEquals("rybka", pp.getRemarks());
        }

        for (int i = 0; i < 10; i++) {
            p = new AdvancePaymentP();
            p.setLp(new Integer(i + 2));
            p.setDateOp(DateFormatUtil.toD("2008/02/02"));
            p.setValidationDate(DateFormatUtil.toD("2008/03/02"));
            p.setAmount(new BigDecimal(i));
            p.setRemarks("rybka");
            col.add(p);
        }

        hot.persistDic(seu, DictType.BookingList, bok);
        bok = getOneName(DictType.BookingList, "BOK0001");
//        col = bok.getValidations();
        col = getVal(bok);
        assertEquals(11, col.size());
        long sum = 0;
        for (AdvancePaymentP pp : col) {
            long a = pp.getAmount().longValue();
            sum += a;
        }
        System.out.println("sum=" + sum);
        assertEquals(57l, sum);
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
        List<BookRecordP> col = new ArrayList<BookRecordP>();
        BookRecordP p = new BookRecordP();
//        OfferSeasonP oSeason = getOfferSeason("P2008");
//        bok.setSeason("P2008");
        OfferPriceP oPrice = getOfferPrice(bok.getSeason(), "Norm");
        p.setOPrice("Norm");
        p.setCustomerPrice(new BigDecimal(999));
        p.setDataFrom(DateFormatUtil.toD("2008/02/07"));
        p.setLp(new Integer(1));
        col.add(p);
        bok.setBookrecords(col);
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        col = bok.getBookrecords();
        assertEquals(1, col.size());
        for (BookRecordP pp : col) {
//            assertEquals(new Long(999), pp.getCustomerPrice().longValue());
            eqBig(new BigDecimal(999),pp.getCustomerPrice());
            assertEquals("Norm", pp.getOPrice());
            assertEquals(new Integer(1), pp.getLp());
        }
    }

    @Test
    public void Test6() throws Exception {
        System.out.println("booking elem");
        Test5();
        BookingP bok = getOneName(DictType.BookingList, "BOK0001");
        BookRecordP p = null;
        for (BookRecordP pp : bok.getBookrecords()) {
            p = pp;
        }
        ServiceDictionaryP servi = (ServiceDictionaryP) getDict(DictType.ServiceDict, "2p2");
        servi = getpersistName(DictType.ServiceDict, servi, "2p2");

        BookElemP be = new BookElemP();
        ResObjectP rO = getResObject("1p");
        be.setResObject("1p");
        be.setService("2p2");
        be.setCheckIn(DateFormatUtil.toD("2008/02/07"));
        be.setCheckOut(DateFormatUtil.toD("2008/03/07"));
        List<BookElemP> col = new ArrayList<BookElemP>();
        col.add(be);
        p.setBooklist(col);
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        p = null;
        for (BookRecordP pp : bok.getBookrecords()) {
            p = pp;
        }
        for (BookElemP bb : p.getBooklist()) {
            be = bb;
        }
        assertEquals("1p", be.getResObject());

    }

    @Test
    public void Test7() throws Exception {
        System.out.println("booking elem");
        Test6();
        BookingP bok = getOneName(DictType.BookingList, "BOK0001");
        BookRecordP p = null;
        for (BookRecordP pp : bok.getBookrecords()) {
            p = pp;
        }
        BookElemP be = null;
        for (BookElemP bb : p.getBooklist()) {
            be = bb;
        }
        PaymentRowP pR = new PaymentRowP();
        List<PaymentRowP> col = new ArrayList<PaymentRowP>();
        pR.setCustomerPrice(new BigDecimal(123));
        pR.setOfferPrice(new BigDecimal(333));
        pR.setRowFrom(DateFormatUtil.toD("2008/02/07"));
        pR.setRowTo(DateFormatUtil.toD("2008/03/07"));
        col.add(pR);
        pR = new PaymentRowP();
        pR.setCustomerPrice(new BigDecimal(1234));
        pR.setOfferPrice(new BigDecimal(3334));
        pR.setRowFrom(DateFormatUtil.toD("2008/06/07"));
        pR.setRowTo(DateFormatUtil.toD("2008/06/07"));
        col.add(pR);
        be.setPaymentrows(col);
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        p = null;
        for (BookRecordP pp : bok.getBookrecords()) {
            p = pp;
        }
        be = null;
        for (BookElemP bb : p.getBooklist()) {
            be = bb;
        }
        col = be.getPaymentrows();
        assertEquals(2, col.size());
    }
}