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

import com.javahotel.common.command.BookingStateType;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.PaymentRowP;
import com.javahotel.common.toobject.ResDayObjectStateP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.types.LId;

/**
 * 
 * @author hotel
 */
public class TestSuite24 extends TestHelper {

    // Test scenario: check canceled
    // Step 1 : book one room for two days
    // Verification 1: two entries
    // Step 2: cancel reservation
    // Verification 2: 0 entries
    // Step 3: again book the same room for two days
    // Verfication 3: 2 entries
    @Test
    public void Test1() {
        loginuser();
        // Step 1
        BookingP bok = createB();
        bok.setHotel(HOTEL1);
        bok.setName("BOKRYBKA");
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
        List<BookElemP> colE = new ArrayList<BookElemP>();
        colE.add(be);
        bok.setBooklist(colE);
        PaymentRowP rP = new PaymentRowP();
        rP.setCustomerRate(new BigDecimal(100));
        rP.setOfferRate(new BigDecimal(100));
        rP.setRowFrom(be.getCheckIn());
        rP.setRowTo(be.getCheckOut());
        List<PaymentRowP> rCol = new ArrayList<PaymentRowP>();
        rCol.add(rP);
        be.setPaymentrows(rCol);

        ReturnPersist ret = hot.persistDic(se, DictType.BookingList, bok);
        // end of step 2

        // start checking
        CommandParam par = new CommandParam();
        par.setHotel(HOTEL1);
        par.setDateFrom(DateFormatUtil.toD("2008/02/01"));
        par.setDateTo(DateFormatUtil.toD("2008/03/09"));
        par.setResListNo(0, "1p");
        List<AbstractTo> res = list.getList(se, RType.ResObjectState, par);
        // Verification 1
        assertEquals(2, res.size());

        // Step 2
        bok = getOneNameN(DictType.BookingList, ret.getIdName());

        System.out.println("Now cancel");
        BookingStateP bState = new BookingStateP();
        bState.setBState(BookingStateType.Canceled);
        bState.setLp(new Integer(2));
        bok.getState().add(bState);
        hot.persistDic(se, DictType.BookingList, bok);
        // end of Step 2
        res = list.getList(se, RType.ResObjectState, par);
        // Verification 2
        assertEquals(0, res.size());
        System.out.println("Add new reserv");

        // Step 3
        bok = createB();
        bok.setHotel(HOTEL1);
        bok.setOPrice("Norm");
        bok.setOPrice(oPrice.getName());
        be = new BookElemP();
        rO = getResObject("1p");
        be.setResObject("1p");
        be.setService("LUX");

        be.setCheckIn(DateFormatUtil.toD("2008/02/07"));
        be.setCheckOut(DateFormatUtil.toD("2008/02/08"));
        colE = new ArrayList<BookElemP>();
        colE.add(be);
        bok.setBooklist(colE);
        rP = new PaymentRowP();
        rP.setCustomerRate(new BigDecimal(500));
        rP.setOfferRate(new BigDecimal(500));
        rP.setRowFrom(be.getCheckIn());
        rP.setRowTo(be.getCheckOut());
        rCol = new ArrayList<PaymentRowP>();
        rCol.add(rP);
        be.setPaymentrows(rCol);

        ret = hot.persistDic(se, DictType.BookingList, bok);
        System.out.println(ret.getIdName());
        // End of Step 3

        res = list.getList(se, RType.ResObjectState, par);
        // Verification 3
        assertEquals(2, res.size());
        bok = getOneNameN(DictType.BookingList, ret.getIdName());
        for (AbstractTo a : res) {
            ResDayObjectStateP re = (ResDayObjectStateP) a;
            System.out.println(re.getBookName());
            System.out.println(re.getLState().getBState());
            System.out.println(re.getD());
            LId id = re.getPaymentRowId();
            BigDecimal bi = null;
            for (BookElemP el : bok.getBooklist()) {
                for (PaymentRowP pr : el.getPaymentrows()) {
                    if (pr.getId().equals(id)) {
                        if (bi == null) {
                            bi = pr.getCustomerPrice();
                        }
                    }
                }
            }
            eqBig(new BigDecimal(1000), bi);
        }
    }
}
