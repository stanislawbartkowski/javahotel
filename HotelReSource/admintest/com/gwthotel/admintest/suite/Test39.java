/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import org.junit.Test;

import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.bill.CustomerBill;
import com.gwthotel.hotel.payment.PaymentBill;
import com.gwthotel.hotel.reservation.ResStatus;
import com.gwthotel.hotel.reservation.ReservationForm;
import com.gwthotel.hotel.reservation.ReservationPaymentDetail;
import com.jythonui.server.security.token.ICustomSecurity;
import com.jythonui.shared.DialogVariables;

public class Test39 extends TestHelper {
    
    private String myRes() {
        String sym = createRes(4);
        ReservationForm r = iRes.findElem(getH(HOTEL), sym);
        r.setStatus(ResStatus.STAY);
        iRes.changeElem(getH(HOTEL), r);
        return sym;        
    }
    
    @Test
    public void test1() {
//        String sym = myRes();
        setUserPassword();
        ICustomSecurity cu = getSec(HOTEL);
        String token = iSec.authenticateToken(realM, "user", "secret", cu);
        assertNotNull(token);
        String sym = myRes();
        DialogVariables v = new DialogVariables();
        v.setValueS("rese", sym);
        runAction(token, v, "dialog39.xml", "test1");
        assertOK(v);
    }
    
    @Test
    public void test2() {
        String sym = myRes();
        setUserPassword();
        ICustomSecurity cu = getSec(HOTEL);
        String token = iSec.authenticateToken(realM, "user", "secret", cu);
        assertNotNull(token);
        
        ReservationForm r = iRes.findElem(getH(HOTEL), sym);
        r.setAdvancePayment(new BigDecimal(250));
        iRes.changeElem(getH(HOTEL), r);
        CustomerBill b = (CustomerBill) hObjects.construct(getH(HOTEL),
                HotelObjects.BILL);
        b.setGensymbol(true);
        b.setPayer(r.getCustomerName());
        b.setReseName(sym);
        b.setIssueDate(toDate(2014, 10, 12));
        b.setDateOfPayment(toDate(2014, 10, 15));
        for (ReservationPaymentDetail d : r.getResDetail()) {
            b.getPayList().add(d.getId());
        }
        b = iBills.addElem(getH(HOTEL), b);
        
        PaymentBill p = new PaymentBill();
        p.setDateOfPayment(toDate(2013, 9, 10));
        p.setPaymentTotal(new BigDecimal(90));
        p.setPaymentMethod("cache");
        p.setDescription("Hello");
        p.setAdvancepayment(true);
        iPayOp.addPaymentForBill(getH(HOTEL), b.getName(), p);
        
        p = new PaymentBill();
        p.setDateOfPayment(toDate(2013, 9, 15));
        p.setPaymentTotal(new BigDecimal(50));
        p.setPaymentMethod("cache");
        p.setDescription("Hello");
        iPayOp.addPaymentForBill(getH(HOTEL), b.getName(), p);

        DialogVariables v = new DialogVariables();
        v.setValueS("rese", sym);
        runAction(token, v, "dialog39.xml", "test2");
        assertOK(v);
    }


}
