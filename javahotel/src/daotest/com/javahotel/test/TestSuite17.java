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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
public class TestSuite17 extends TestHelper {

    // Test scenario
    // Step1: create booking entry
    // Verification 1: number of payments is 0 (at the beginning)
    // Step2 : add first payment
    // Verification2: one payment
    // Verification 3: sequence number is equal to 1
    // Verification 4: number of booking state is 1 at the beginning
    // Step 3: add Canceled state
    // Verification 5: number of booking states is 2
    // Step 4: add Confirmed state
    // Verification 6: number of booking states is 3
    // Verification 7 : number of payments is 2
    // Verification 8: check the payments if as added
    
    @Test
    public void Test1() {
        loginuser();
        BookingP bok = createB();
        // Step 1
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        List<PaymentP> col = getPay(bok);
        // Verification 1
        assertEquals(0, col.size());

        // Prepare first payment2
        CommandParam pa = new CommandParam();
        pa.setHotel(HOTEL1);
        pa.setReservName("BOK0001");
        PaymentP pay = new PaymentP();
        pay.setAmount(new BigDecimal(90));
        pay.setDatePayment(D("2008/02/02"));
        pay.setPayMethod(PaymentMethod.Cache);
        List<PaymentP> paList = new ArrayList<PaymentP>();
        paList.add(pay);
        pa.setListP(paList);
        // Step 2
        hotop.hotelOp(se, HotelOpType.AddDownPayment, pa);
        bok = getOneName(DictType.BookingList, "BOK0001");
        // col = bok.getPayments();
        col = getPay(bok);
        // Verification 2
        assertEquals(1, col.size());
        for (PaymentP p : col) {
            pay = p;
        }
        // Verification 3
        assertEquals(new Integer(1), pay.getLp());

        List<BookingStateP> colp = bok.getState();
        // Verification 4
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
        // Verification 5
        assertEquals(2, colp.size());

        // Step 3
        bs = new BookingStateP();
        bs.setBState(BookingStateType.Confirmed);
        pa = new CommandParam();
        pa.setHotel(HOTEL1);
        pa.setReservName("BOK0001");
        pa.setStateP(bs);
        hotop.hotelOp(se, HotelOpType.BookingSetNewState, pa);

        // Step 4
        pay = new PaymentP();
        pay.setAmount(new BigDecimal(70));
        pay.setDatePayment(D("2008/02/05"));
        pay.setPayMethod(PaymentMethod.CreditCard);
        col.add(pay);
        // pa.setDownPayment(pay);
        pa.setListP(col);
        hotop.hotelOp(se, HotelOpType.AddDownPayment, pa);
        bok = getOneName(DictType.BookingList, "BOK0001");
        colp = bok.getState();
        // Verification 6
        assertEquals(3, colp.size());
        col = getPay(bok);
        // Verification 7
        assertEquals(2, col.size());
        boolean is2 = false;

        // Verification 8
        for (PaymentP p : col) {
            Integer lp = p.getLp();
            switch (lp.intValue()) {
            case 1:
                eqD("2008/02/02", p.getDatePayment());
                break;
            case 2:
                eqD("2008/02/05", p.getDatePayment());
                is2 = true;
                break;
            default:
                fail();
            }
        }
        assertTrue(is2);
    }

    // Scenario (bulk adding)
    // Step 1: add 50 times payments and booking states
    //  Warning: every time different then before
    // Verification 1: number of payments is 50
    // Verification 2 : number of states should be 51
    
    @Test
    public void Test2() {
        loginuser();
        BookingP bok = createB();
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        List<PaymentP> col;
        List<BookingStateP> colp;
        // Step 1
        boolean canc = true;
        for (int i = 0; i < 50; i++) {
            System.out.println(i);
            bok = getOneName(DictType.BookingList, "BOK0001");
            colp = bok.getState();
            // col = bok.getPayments();
            col = getPay(bok);
            CommandParam pa = new CommandParam();
            pa.setHotel(HOTEL1);
            pa.setReservName("BOK0001");
            PaymentP pay = new PaymentP();
            pay.setAmount(new BigDecimal(90));
            pay.setDatePayment(D("2008/02/02"));
            col.add(pay);
            pay.setPayMethod(PaymentMethod.Cache);
            pa.setListP(col);
            hotop.hotelOp(se, HotelOpType.AddDownPayment, pa);
            pa = new CommandParam();
            pa.setHotel(HOTEL1);
            pa.setReservName("BOK0001");
            BookingStateP bs = new BookingStateP();
            if (canc) {
              bs.setBState(BookingStateType.Canceled);
            }
            else {
                bs.setBState(BookingStateType.Confirmed);                
            }
            canc = !canc;
            pa.setStateP(bs);
            hotop.hotelOp(se, HotelOpType.BookingSetNewState, pa);
        }
        bok = getOneName(DictType.BookingList, "BOK0001");
        colp = bok.getState();
        col = getPay(bok);
        // Verification 1
        assertEquals(50, col.size());
        // Verification 2
        assertEquals(51, colp.size());
    }
}
