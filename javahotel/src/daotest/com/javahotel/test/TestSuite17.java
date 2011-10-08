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

import com.javahotel.common.command.BookingStateType;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.HotelOpType;
import com.javahotel.common.command.PaymentMethod;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.common.toobject.PaymentP;

import java.math.BigDecimal;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite17 extends TestHelper {

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
        pay.setSumOp(true);
        pay.setDatePayment(D("2008/02/02"));
        pay.setPayMethod(PaymentMethod.Cache);
        pa.setDownPayment(pay);
        hotop.hotelOp(se, HotelOpType.payDownPaymentState, pa);
        bok = getOneName(DictType.BookingList, "BOK0001");
//        col = bok.getPayments();
        col = getPay(bok);
        assertEquals(1, col.size());
        for (PaymentP p : col) {
            pay = p;
        }
        assertEquals(new Integer(1), pay.getLp());

        List<BookingStateP> colp = bok.getState();
        assertEquals(1, colp.size());

        BookingStateP bs = new BookingStateP();
        bs.setBState(BookingStateType.Canceled);
        pa = new CommandParam();
        pa.setHotel(HOTEL1);
        pa.setReservName("BOK0001");
        pa.setStateP(bs);
        hotop.hotelOp(se, HotelOpType.payDownPaymentState, pa);
        bok = getOneName(DictType.BookingList, "BOK0001");
        colp = bok.getState();
        assertEquals(2, colp.size());

        bs = new BookingStateP();
        bs.setBState(BookingStateType.Confirmed);
        pa = new CommandParam();
        pa.setHotel(HOTEL1);
        pa.setReservName("BOK0001");
        pa.setStateP(bs);
        pay = new PaymentP();
        pay.setAmount(new BigDecimal(70));
        pay.setSumOp(false);
        pay.setDatePayment(D("2008/02/05"));
        pay.setPayMethod(PaymentMethod.CreditCard);
        pa.setDownPayment(pay);
        hotop.hotelOp(se, HotelOpType.payDownPaymentState, pa);
        bok = getOneName(DictType.BookingList, "BOK0001");
        colp = bok.getState();
        assertEquals(3, colp.size());
//        col = bok.getPayments();
        col = getPay(bok);
        assertEquals(2, col.size());
        boolean is2 = false;

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

    @Test
    public void Test2() {
        loginuser();
        BookingP bok = createB();
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        List<PaymentP> col;
        List<BookingStateP> colp;
        for (int i = 0; i < 50; i++) {
            System.out.println(i);
            bok = getOneName(DictType.BookingList, "BOK0001");
            colp = bok.getState();
//            col = bok.getPayments();
            col = getPay(bok);
            CommandParam pa = new CommandParam();
            pa.setHotel(HOTEL1);
            pa.setReservName("BOK0001");
            PaymentP pay = new PaymentP();
            pay.setAmount(new BigDecimal(90));
            pay.setSumOp(true);
            pay.setDatePayment(D("2008/02/02"));
            pay.setPayMethod(PaymentMethod.Cache);
            pa.setDownPayment(pay);
            BookingStateP bs = new BookingStateP();
            bs.setBState(BookingStateType.Canceled);
            pa.setStateP(bs);
            hotop.hotelOp(se, HotelOpType.payDownPaymentState, pa);
        }
        bok = getOneName(DictType.BookingList, "BOK0001");
        colp = bok.getState();
//        col = bok.getPayments();
        col = getPay(bok);
        assertEquals(51,colp.size());
        assertEquals(50,col.size());
    }
}
