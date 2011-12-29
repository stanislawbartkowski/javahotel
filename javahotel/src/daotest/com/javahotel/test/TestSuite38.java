/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.javahotel.common.command.DictType;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.PaymentRowP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.IHotelTest;

import static org.junit.Assert.assertEquals;

/**
 * @author hotel
 * 
 */
/*
 * Test if number of payment rows in database are handled properly
 * Step 1: Create booking with one payment row
 * Verification 1: check if one payment row in database
 * Step 2: Create booking with 50 payment row 
 * Verification 3: check if 50 payment row in database
 * Step 3: Create booking with one payment row again
 * Verification 3: check if 1 payment row in database
 */
public class TestSuite38 extends TestHelper {

    @Test
    public void test1() {
        loginuser();
        int n = itest.getNumberOfRecord(se, IHotelTest.BOOKINTPAYMENTROWS,
                new HotelT(HOTEL1));
        assertEquals(0, n);
        // Step 1
        BookingP bok = createB();
        OfferPriceP oPrice = getOfferPrice(bok.getSeason(), "Norm");
        bok.setCustomerPrice(new BigDecimal(999));
        bok.setOPrice("Norm");
        bok.setOPrice(oPrice.getName());
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
        bok.setBooklist(colE);
        modifPaymentRow(be);

        bok.setValidationAmount(new BigDecimal(100));
        bok.setValidationDate(D("2008/03/08"));
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        assertEquals(1, bok.getBooklist().size());
        n = itest.getNumberOfRecord(se, IHotelTest.BOOKINTPAYMENTROWS,
                new HotelT(HOTEL1));
        // Verification 1
        assertEquals(1, n);
        // eqD
        for (BookElemP ee : bok.getBooklist()) {
            assertEquals(1,ee.getPaymentrows().size());
            for (PaymentRowP r : ee.getPaymentrows()) {
                eqD("2008/02/07",r.getRowFrom());
                eqD("2008/03/07",r.getRowTo());                
            }
        }

        // Step 2
        colE = new ArrayList<BookElemP>();
        colE.add(be);
        List<PaymentRowP> rCol = new ArrayList<PaymentRowP>();
        be.setPaymentrows(rCol);
        bok.setBooklist(colE);
        for (int i = 0; i < 50; i++) {
            PaymentRowP rP = new PaymentRowP();
            rP.setCustomerPrice(new BigDecimal(4));
            rP.setOfferPrice(new BigDecimal(4));
            rP.setRowFrom(be.getCheckIn());
            rP.setRowTo(be.getCheckOut());
            rCol.add(rP);
        }
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        assertEquals(1, bok.getBooklist().size());
        n = itest.getNumberOfRecord(se, IHotelTest.BOOKINTPAYMENTROWS,
                new HotelT(HOTEL1));
        // Verification 2
        assertEquals(50, n);

        // Step 3
        colE = new ArrayList<BookElemP>();
        colE.add(be);
        be.setCheckIn(DateFormatUtil.toD("2011/02/07"));
        be.setCheckOut(DateFormatUtil.toD("2011/03/07"));
        bok.setBooklist(colE);
        modifPaymentRow(be);
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        assertEquals(1, bok.getBooklist().size());
        n = itest.getNumberOfRecord(se, IHotelTest.BOOKINTPAYMENTROWS,
                new HotelT(HOTEL1));
        for (BookElemP ee : bok.getBooklist()) {
            assertEquals(1,ee.getPaymentrows().size());
            for (PaymentRowP r : ee.getPaymentrows()) {
                eqD("2011/02/07",r.getRowFrom());
                eqD("2011/03/07",r.getRowTo());                
            }
        }
        // Verification 3
        assertEquals(1, n);

    }

}
