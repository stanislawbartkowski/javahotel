/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
package com.gwthotel.admintest.suite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.bill.CustomerBill;
import com.gwthotel.hotel.customer.HotelCustomer;
import com.gwthotel.hotel.payment.PaymentBill;
import com.gwthotel.hotel.reservation.ReservationForm;
import com.gwthotel.hotel.reservation.ReservationPaymentDetail;
import com.gwthotel.hotel.rooms.HotelRoom;
import com.gwtmodel.table.common.dateutil.DateFormatUtil;

public class Test20 extends TestHelper {

    // @Before
    // public void before() {
    // clearObjects();
    // createHotels();
    // setTestToday(DateFormatUtil.toD(2013, 6, 13));
    // }

    @Test
    public void test1() {
        CustomerBill b = createP();
        PaymentBill p = new PaymentBill();
        p.setDateOfPayment(toDate(2013, 9, 10));
        p.setPaymentTotal(new BigDecimal(100.0));
        p.setPaymentMethod("cache");
        p.setDescription("Hello");
        iPayOp.addPaymentForBill(getH(HOTEL), b.getName(), p);
        List<PaymentBill> pList = iPayOp.getPaymentsForBill(getH(HOTEL),
                b.getName());
        assertEquals(1, pList.size());
        p = pList.get(0);
        eqD(2013, 9, 10, p.getDateOfPayment());
        assertEqB(100.0, p.getPaymentTotal());
        assertEquals("cache", p.getPaymentMethod());
        assertEquals("Hello", p.getDescription());
        // now remove
        iPayOp.removePaymentForBill(getH(HOTEL), b.getName(), p.getId());
        pList = iPayOp.getPaymentsForBill(getH(HOTEL), b.getName());
        assertTrue(pList.isEmpty());
    }

    @Test
    public void test2() {
        CustomerBill b = createP();
        for (int i = 0; i < 100; i++) {
            PaymentBill p = new PaymentBill();
            p.setDateOfPayment(toDate(2013, 9, 10));
            p.setPaymentTotal(new BigDecimal(i));
            p.setPaymentMethod("cache");
            p.setDescription("Hello");
            iPayOp.addPaymentForBill(getH(HOTEL), b.getName(), p);
        }
        List<PaymentBill> pList = iPayOp.getPaymentsForBill(getH(HOTEL),
                b.getName());
        assertEquals(100, pList.size());
        for (int i = 0; i < 70; i++) {
            iPayOp.removePaymentForBill(getH(HOTEL), b.getName(), pList.get(i)
                    .getId());
        }
        pList = iPayOp.getPaymentsForBill(getH(HOTEL), b.getName());
        assertEquals(30, pList.size());
        for (PaymentBill bb : pList) {
            System.out.println(bb.getBillName());
            assertNotNull(bb.getBillName());
        }
        // remove bill
        iBills.deleteElem(getH(HOTEL), b);
        assertNull(iBills.findElem(getH(HOTEL), b.getName()));
    }

    @Test
    public void test3() {
        CustomerBill b = createP();
        PaymentBill p = new PaymentBill();
        p.setDateOfPayment(toDate(2013, 9, 10));
        p.setPaymentTotal(new BigDecimal(100.0));
        p.setPaymentMethod("cache");
        p.setDescription("Hello");
        iPayOp.addPaymentForBill(getH(HOTEL), b.getName(), p);
        String rese = b.getReseName();
        ReservationForm r = iRes.findElem(getH(HOTEL), rese);
        iRes.deleteElem(getH(HOTEL), r);
    }

}
