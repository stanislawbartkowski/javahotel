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

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.bill.CustomerBill;
import com.gwthotel.hotel.payment.PaymentBill;
import com.gwthotel.hotel.reservation.ResStatus;
import com.gwthotel.hotel.reservation.ReservationForm;
import com.gwthotel.hotel.reservation.ReservationPaymentDetail;
import com.jythonui.server.security.token.ICustomSecurity;
import com.jythonui.shared.DialogVariables;

public class Test37 extends TestHelper {

    @Test
    public void test1() {
        String sym = createRes(1);
        setUserPassword();
        ICustomSecurity cu = getSec(HOTEL);
        String token = iSec.authenticateToken(realM, "user", "secret", cu);
        assertNotNull(token);
        DialogVariables v = new DialogVariables();
        v.setValueS("rese", sym);
        runAction(token, v, "dialog11.xml", "test1");
        assertOK(v);
        ReservationForm r = iRes.findElem(getH(HOTEL), sym);
        r.setStatus(ResStatus.SCHEDULED);
        iRes.changeElem(getH(HOTEL), r);
        v = new DialogVariables();
        v.setValueS("rese", sym);
        runAction(token, v, "dialog11.xml", "test2");
        assertOK(v);
    }

    @Test
    public void test2() {
        String sym = createRes(1);
        setUserPassword();
        ICustomSecurity cu = getSec(HOTEL);
        String token = iSec.authenticateToken(realM, "user", "secret", cu);
        assertNotNull(token);
        ReservationForm r = iRes.findElem(getH(HOTEL), sym);
        r.setAdvanceDeposit(new BigDecimal(50));
        iRes.changeElem(getH(HOTEL), r);
        DialogVariables v = new DialogVariables();
        v.setValueS("rese", sym);
        runAction(token, v, "dialog11.xml", "test3");
        assertOK(v);
        
        r = iRes.findElem(getH(HOTEL), sym);
        r.setTermOfAdvanceDeposit(toDate(2013, 4, 5));
        iRes.changeElem(getH(HOTEL), r);
        v = new DialogVariables();
        v.setValueS("rese", sym);
        runAction(token, v, "dialog11.xml", "test4");
        assertOK(v);
        
        r = iRes.findElem(getH(HOTEL), sym);
        r.setAdvancePayment(new BigDecimal(50));
        iRes.changeElem(getH(HOTEL), r);
        v = new DialogVariables();
        v.setValueS("rese", sym);
        runAction(token, v, "dialog11.xml", "test5");
        assertOK(v);
        
        v = new DialogVariables();
        v.setValueS("rese", sym);
        runAction(token, v, "dialog11.xml", "test6");
        assertOK(v);        
    }
    
    @Test
    public void test3() {
        String sym = createRes(1);
        setUserPassword();
        ICustomSecurity cu = getSec(HOTEL);
        String token = iSec.authenticateToken(realM, "user", "secret", cu);
        assertNotNull(token);
        iResOp.changeStatus(getH(HOTEL), sym, ResStatus.STAY);
        DialogVariables v = new DialogVariables();
        v.setValueS("rese", sym);
        runAction(token, v, "dialog11.xml", "test7");
        assertOK(v);        

        ReservationForm r = iRes.findElem(getH(HOTEL), sym);
        r.setAdvancePayment(new BigDecimal(50));
        iRes.changeElem(getH(HOTEL), r);
        v = new DialogVariables();
        v.setValueS("rese", sym);
        runAction(token, v, "dialog11.xml", "test8");
        assertOK(v);

        r = iRes.findElem(getH(HOTEL), sym);
        r.setAdvancePayment(new BigDecimal(100));
        iRes.changeElem(getH(HOTEL), r);
        v = new DialogVariables();
        v.setValueS("rese", sym);
        runAction(token, v, "dialog11.xml", "test9");
        assertOK(v);
    }
    
    @Test
    public void test4() {
        String sym = createRes(1);
        setUserPassword();
        ICustomSecurity cu = getSec(HOTEL);
        String token = iSec.authenticateToken(realM, "user", "secret", cu);
        assertNotNull(token);
        iResOp.changeStatus(getH(HOTEL), sym, ResStatus.STAY);
        ReservationForm r = iRes.findElem(getH(HOTEL), sym);
        r.setAdvancePayment(new BigDecimal(90));
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
        iPayOp.addPaymentForBill(getH(HOTEL), b.getName(), p);
        
        DialogVariables v = new DialogVariables();
        v.setValueS("rese", sym);
        runAction(token, v, "dialog11.xml", "test10");
        assertOK(v);
        
        List<PaymentBill> ppli = iPayOp.getPaymentsForBill(getH(HOTEL), b.getName());
        iPayOp.removePaymentForBill(getH(HOTEL), b.getName(), ppli.get(0).getId());
        p = new PaymentBill();
        p.setDateOfPayment(toDate(2013, 9, 10));
        p.setPaymentTotal(new BigDecimal(90));
        p.setPaymentMethod("cache");
        p.setDescription("Hello");
        p.setAdvancepayment(true);
        iPayOp.addPaymentForBill(getH(HOTEL), b.getName(), p);
        
        v = new DialogVariables();
        v.setValueS("rese", sym);
        runAction(token, v, "dialog11.xml", "test11");
        assertOK(v);
    }
    
    @Test
    public void test5() {
        String sym = createRes(4);
        setUserPassword();
        ICustomSecurity cu = getSec(HOTEL);
        String token = iSec.authenticateToken(realM, "user", "secret", cu);
        assertNotNull(token);
        iResOp.changeStatus(getH(HOTEL), sym, ResStatus.STAY);
        ReservationForm r = iRes.findElem(getH(HOTEL), sym);
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
        p.setPaymentTotal(new BigDecimal(150));
        p.setPaymentMethod("cache");
        p.setDescription("Hello");
        iPayOp.addPaymentForBill(getH(HOTEL), b.getName(), p);
        
        p = new PaymentBill();
        p.setDateOfPayment(toDate(2013, 9, 10));
        p.setPaymentTotal(new BigDecimal(100));
        p.setPaymentMethod("cache");
        p.setDescription("Hello");
        iPayOp.addPaymentForBill(getH(HOTEL), b.getName(), p);
        
        DialogVariables v = new DialogVariables();
        v.setValueS("rese", sym);
        runAction(token, v, "dialog11.xml", "test12");
        assertOK(v);
        
        p = new PaymentBill();
        p.setDateOfPayment(toDate(2013, 9, 10));
        p.setPaymentTotal(new BigDecimal(150));
        p.setPaymentMethod("cache");
        p.setDescription("Hello");
        iPayOp.addPaymentForBill(getH(HOTEL), b.getName(), p);
        
        v = new DialogVariables();
        v.setValueS("rese", sym);
        runAction(token, v, "dialog11.xml", "test13");
        assertOK(v);


    }

}
