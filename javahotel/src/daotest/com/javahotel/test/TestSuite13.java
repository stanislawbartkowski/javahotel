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
package com.javahotel.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.AdvancePaymentP;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.DownPaymentP;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.types.LId;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite13 extends TestHelper {

    @Test
    public void Test1() {
        loginuser();
        BookingP bok = createB();
        Collection<BookRecordP> col = new ArrayList<BookRecordP>();
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
        be.setCheckOut(DateFormatUtil.toD("2008/03/07"));
        Collection<BookElemP> colE = new ArrayList<BookElemP>();
        colE.add(be);
        p.setBooklist(colE);

        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        col = bok.getBookrecords();
        p = null;
        for (BookRecordP pp : bok.getBookrecords()) {
            p = pp;
        }
        be = null;
        for (BookElemP bb : p.getBooklist()) {
            be = bb;
        }

        CommandParam par = new CommandParam();
        par.setHotel(HOTEL1);
        par.setDateFrom(DateFormatUtil.toD("2008/03/07"));
        par.setDateTo(DateFormatUtil.toD("2008/03/09"));
        Collection<AbstractTo> res = list.getList(se, RType.DownPayments, par);
        assertEquals(0, res.size());
    }
    
    private void eq(LId d1,LId d2) {
    	assertTrue("",d1.equals(d2));    	
    }

    @Test
    public void Test2() {
        loginuser();
        BookingP bok = createB();
        Collection<AdvancePaymentP> col = new ArrayList<AdvancePaymentP>();
        AdvancePaymentP va = new AdvancePaymentP();
        va.setLp(new Integer(1));
        va.setAmount(new BigDecimal(100));
        va.setValidationDate(D("2008/03/07"));
        col.add(va);
//        bok.setValidations(col);
        setVal(bok,col);
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        CommandParam par = new CommandParam();
        par.setHotel(HOTEL1);
        par.setDateFrom(DateFormatUtil.toD("2008/03/07"));
        par.setDateTo(DateFormatUtil.toD("2008/03/09"));
        Collection<AbstractTo> res = list.getList(se, RType.DownPayments, par);
        assertEquals(1,res.size());
        for (AbstractTo a : res) {
            DownPaymentP dp = (DownPaymentP) a;
            assertEquals("BOK0001",dp.getResId());
            eq(bok.getCustomer(),dp.getCustomerId());
            eqBig(new BigDecimal(100),dp.getAmount());
            eq(bok.getCustomer(),dp.getCustomerId());
        }
        
        par = new CommandParam();
        par.setHotel(HOTEL1);
        par.setDateFrom(DateFormatUtil.toD("2008/03/08"));
        par.setDateTo(DateFormatUtil.toD("2008/03/09"));
        res = list.getList(se, RType.DownPayments, par);
        assertEquals(0, res.size());
        
//        col = bok.getValidations();
        col = getVal(bok);
        va = new AdvancePaymentP();
        va.setLp(new Integer(2));
        va.setAmount(new BigDecimal(150));
        va.setValidationDate(D("2008/03/08"));
        col.add(va);
        va = new AdvancePaymentP();
        va.setLp(new Integer(3));
        va.setAmount(new BigDecimal(200));
        va.setValidationDate(D("2008/03/08"));
        col.add(va);
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        res = list.getList(se, RType.DownPayments, par);
        assertEquals(1,res.size());
        for (AbstractTo a : res) {
            DownPaymentP dp = (DownPaymentP) a;
            assertEquals("BOK0001",dp.getResId());
            eq(bok.getCustomer(),dp.getCustomerId());
            eqBig(new BigDecimal(200),dp.getAmount());
            assertEquals(new Integer(3),dp.getLp());
            assertNotNull(dp.getCustomerId());
            System.out.println("Customerid = " + dp.getCustomerId());
        }        
    }
    
    @Test
    public void Test3() {
        Test2();
        BookingP bok = getOneName(DictType.BookingList,"BOK0001");
//        Collection<AdvancePaymentP> col = bok.getValidations();
        Collection<AdvancePaymentP> col = getVal(bok);
        AdvancePaymentP va = new AdvancePaymentP();
        va.setLp(new Integer(4));
        va.setAmount(new BigDecimal(300));
        va.setValidationDate(D("2008/03/10"));
        col.add(va);
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        CommandParam par = new CommandParam();
        par.setHotel(HOTEL1);
        par.setDateFrom(DateFormatUtil.toD("2008/03/08"));
        par.setDateTo(DateFormatUtil.toD("2008/03/09"));
        Collection<AbstractTo> res = list.getList(se, RType.DownPayments, par);
        assertEquals(0, res.size());        
        
        par = new CommandParam();
        par.setHotel(HOTEL1);
        par.setDateFrom(DateFormatUtil.toD("2008/03/10"));
        par.setDateTo(DateFormatUtil.toD("2008/03/10"));
        res = list.getList(se, RType.DownPayments, par);
        assertEquals(1, res.size());        
        
//        col = bok.getValidations();
        col = getVal(bok);
        va = new AdvancePaymentP();
        va.setLp(new Integer(5));
        va.setAmount(new BigDecimal(400));
        va.setValidationDate(D("2008/03/01"));
        col.add(va);
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        
        par = new CommandParam();
        par.setHotel(HOTEL1);
        par.setDateFrom(DateFormatUtil.toD("2008/03/10"));
        par.setDateTo(DateFormatUtil.toD("2008/03/10"));
        res = list.getList(se, RType.DownPayments, par);
        assertEquals(0, res.size());        
        
        par = new CommandParam();
        par.setHotel(HOTEL1);
        par.setDateFrom(DateFormatUtil.toD("2008/03/01"));
        par.setDateTo(DateFormatUtil.toD("2008/03/10"));
        res = list.getList(se, RType.DownPayments, par);
        assertEquals(1, res.size());        
        for (AbstractTo a : res) {
            DownPaymentP dp = (DownPaymentP) a;
            assertEquals("BOK0001",dp.getResId());
            eq(bok.getCustomer(),dp.getCustomerId());
            eqBig(new BigDecimal(400),dp.getAmount());
        }
    }
    
    @Test
    public void Test4() {
        Test2();
        
        BookingP bok = createB();
        Collection<AdvancePaymentP> col = new ArrayList<AdvancePaymentP>();
        AdvancePaymentP va = new AdvancePaymentP();
        va.setLp(new Integer(1));
        va.setAmount(new BigDecimal(100));
        va.setValidationDate(D("2008/03/07"));
        col.add(va);
//        bok.setValidations(col);
        setVal(bok,col);
        bok = getpersistName(DictType.BookingList, bok, "BOK0002");
        
        CommandParam par = new CommandParam();
        par.setHotel(HOTEL1);
        par.setDateFrom(DateFormatUtil.toD("2008/03/07"));
        par.setDateTo(DateFormatUtil.toD("2008/03/09"));
        Collection<AbstractTo> res = list.getList(se, RType.DownPayments, par);
        assertEquals(2, res.size());           
    }

}

