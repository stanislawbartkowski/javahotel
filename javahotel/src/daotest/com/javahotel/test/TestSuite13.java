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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.ServiceDictionaryP;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite13 extends TestHelper {

    @Test
    public void Test1() {
        loginuser();
        BookingP bok = createB();
        OfferPriceP oPrice = getOfferPrice(bok.getSeason(), "Norm");
        bok.setOPrice("Norm");
        bok.setOPrice(oPrice.getName());
        BookElemP be = new BookElemP();
        ResObjectP rO = getResObject("1p");
        
        be.setResObject("1p");
        ServiceDictionaryP servi = (ServiceDictionaryP) getDict(DictType.ServiceDict, HOTEL1);
        servi = getpersistName(DictType.ServiceDict, servi, "LUX");
        be.setService("LUX");

        be.setCheckIn(DateFormatUtil.toD("2008/02/07"));
        be.setCheckOut(DateFormatUtil.toD("2008/03/07"));
        modifPaymentRow(be);
        List<BookElemP> colE = new ArrayList<BookElemP>();
        colE.add(be);
        bok.setBooklist(colE);

        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        be = null;
        for (BookElemP bb : bok.getBooklist()) {
            be = bb;
        }

        CommandParam par = new CommandParam();
        par.setHotel(HOTEL1);
        par.setDateFrom(DateFormatUtil.toD("2008/03/07"));
        par.setDateTo(DateFormatUtil.toD("2008/03/09"));
        List<AbstractTo> res = list.getList(se, RType.DownPayments, par);
        assertEquals(0, res.size());
    }
    

    @Test
    public void Test2() {
        loginuser();
        BookingP bok = createB();
        bok.setValidationAmount(new BigDecimal(100));
        bok.setValidationDate(D("2008/03/07"));
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        CommandParam par = new CommandParam();
        par.setHotel(HOTEL1);
        par.setDateFrom(DateFormatUtil.toD("2008/03/07"));
        par.setDateTo(DateFormatUtil.toD("2008/03/09"));
        List<AbstractTo> res = list.getList(se, RType.DownPayments, par);
        assertEquals(1,res.size());
        for (AbstractTo a : res) {
            BookingP dp = (BookingP) a;
            assertEquals("BOK0001",dp.getName());
            eqBig(new BigDecimal(100),dp.getValidationAmount());
        }
        
        par = new CommandParam();
        par.setHotel(HOTEL1);
        par.setDateFrom(DateFormatUtil.toD("2008/03/08"));
        par.setDateTo(DateFormatUtil.toD("2008/03/09"));
        res = list.getList(se, RType.DownPayments, par);
        assertEquals(0, res.size());
        
        bok.setValidationAmount(new BigDecimal(200));
        bok.setValidationDate(D("2008/03/08"));
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        res = list.getList(se, RType.DownPayments, par);
        assertEquals(1,res.size());
        for (AbstractTo a : res) {
            BookingP dp = (BookingP) a;
            assertEquals("BOK0001",dp.getName());
            eqBig(new BigDecimal(200),dp.getValidationAmount());
        }        
    }
    
    @Test
    public void Test3() {
        Test2();
        BookingP bok = getOneName(DictType.BookingList,"BOK0001");
        bok.setValidationAmount(new BigDecimal(300));
        bok.setValidationDate(D("2008/03/10"));
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        CommandParam par = new CommandParam();
        par.setHotel(HOTEL1);
        par.setDateFrom(DateFormatUtil.toD("2008/03/08"));
        par.setDateTo(DateFormatUtil.toD("2008/03/09"));
        List<AbstractTo> res = list.getList(se, RType.DownPayments, par);
        assertEquals(0, res.size());        
        
        par = new CommandParam();
        par.setHotel(HOTEL1);
        par.setDateFrom(DateFormatUtil.toD("2008/03/10"));
        par.setDateTo(DateFormatUtil.toD("2008/03/10"));
        res = list.getList(se, RType.DownPayments, par);
        assertEquals(1, res.size());        
        
        bok.setValidationAmount(new BigDecimal(400));
        bok.setValidationDate(D("2008/03/01"));
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
            BookingP dp = (BookingP) a;
            assertEquals("BOK0001",dp.getName());
            eqBig(new BigDecimal(400),dp.getValidationAmount());
        }
    }
    
    @Test
    public void Test4() {
        Test2();
        
        BookingP bok = createB();
        bok.setValidationAmount(new BigDecimal(100));
        bok.setValidationDate(D("2008/03/07"));
        bok = getpersistName(DictType.BookingList, bok, "BOK0002");
        
        CommandParam par = new CommandParam();
        par.setHotel(HOTEL1);
        par.setDateFrom(DateFormatUtil.toD("2008/03/07"));
        par.setDateTo(DateFormatUtil.toD("2008/03/09"));
        List<AbstractTo> res = list.getList(se, RType.DownPayments, par);
        assertEquals(2, res.size());           
    }

}

