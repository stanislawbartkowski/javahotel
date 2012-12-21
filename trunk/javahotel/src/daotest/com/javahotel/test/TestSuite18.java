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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.javahotel.common.command.BookingStateType;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.HotelOpType;
import com.javahotel.common.command.PaymentMethod;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.common.toobject.PaymentP;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite18 extends TestHelper {
    
    // Test scenario
    // Step 1: add first payment
    // Verification 1 : check if one payment added
    // Verification 2 : one booking state
    // Step 3: add Canceled state
    // Verification 3: two booking states
    // Step 4: add Canceled state
    // Verification 4: still two states (two last CANCELED)
    // Verification 5: last cancelled

    @Test
    public void Test1() {
        loginuser();
        BookingP bok = createB();
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
//        List<PaymentP> col = bok.getPayments();
        List<PaymentP> col = getPay(bok);
        assertEquals(0, col.size());
        CommandParam pa = new CommandParam();
        pa.setHotel(HOTEL1);
        pa.setReservName("BOK0001");
        PaymentP pay = new PaymentP();
        pay.setAmount(new BigDecimal(90));
        pay.setDatePayment(D("2008/02/02"));
        pay.setPayMethod(PaymentMethod.Cache);
        col.add(pay);
        pa.setListP(col);
        hotop.hotelOp(se, HotelOpType.AddDownPayment, pa);
        
        bok = getOneName(DictType.BookingList, "BOK0001");
//        col = bok.getPayments();
        col = getPay(bok);
        // Verification 1
        assertEquals(1, col.size());
        for (PaymentP p : col) {
            pay = p;
        }
        assertEquals(new Integer(1), pay.getLp());

        List<BookingStateP> colp = bok.getState();
        // Verification 2
        assertEquals(1, colp.size());

        // Step 3
        BookingStateP bs = new BookingStateP();
        bs.setBState(BookingStateType.Canceled);
        pa = new CommandParam();
        pa.setHotel(HOTEL1);
        pa.setReservName("BOK0001");
        pa.setStateP(bs);
        hotop.hotelOp(se, HotelOpType.BookingSetNewState, pa);
        bok = getOneName(DictType.BookingList, "BOK0001");
        colp = bok.getState();
        // Verification 3
        assertEquals(2, colp.size());

        // Step 4
        bs = new BookingStateP();
        bs.setBState(BookingStateType.Canceled);
        pa = new CommandParam();
        pa.setHotel(HOTEL1);
        pa.setReservName("BOK0001");
        pa.setStateP(bs);
        hotop.hotelOp(se, HotelOpType.BookingSetNewState, pa);
        bok = getOneName(DictType.BookingList, "BOK0001");
        colp = bok.getState();
        // Verification 4
        assertEquals(2, colp.size());
        bs = null;
        for (BookingStateP s : colp) {
            bs = s;
        }
        // Verification 5
        assertEquals(BookingStateType.Canceled,bs.getBState());
    }
}
