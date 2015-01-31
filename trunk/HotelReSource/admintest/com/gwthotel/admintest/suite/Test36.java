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
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;

import org.junit.Test;

import com.gwthotel.hotel.reservation.ReservationForm;
import com.gwthotel.hotel.reservation.ReservationPaymentDetail;
import com.jythonui.server.security.token.ICustomSecurity;
import com.jythonui.shared.DialogVariables;

public class Test36 extends TestHelper {

    @Test
    public void test1() {
        String sym = createRes(1);
        ReservationForm r = iRes.findElem(getH(HOTEL), sym);
        r.setAdvanceDeposit(new BigDecimal("99"));
        r.setTermOfAdvanceDeposit(toDate(2014, 3, 4));
        iRes.changeElem(getH(HOTEL), r);
        r = iRes.findElem(getH(HOTEL), sym);
        equalB(99, r.getAdvanceDeposit(), 2);
        eqD(2014, 3, 4, r.getTermOfAdvanceDeposit());
    }

    @Test
    public void test2() {
        String sym = createRes(1);
        ReservationForm r = iRes.findElem(getH(HOTEL), sym);

        ReservationPaymentDetail det = new ReservationPaymentDetail();
        det.setNoP(3);
        det.setPrice(new BigDecimal(100));
        det.setPriceList(new BigDecimal("200.0"));
        det.setPriceTotal(new BigDecimal(100));
        det.setRoomName("P10");
        det.setVat("7%");
        det.setResDate(toDate(2013, 4, 11));
        r.getResDetail().add(det);
        iRes.changeElem(getH(HOTEL), r);

        setUserPassword();
        ICustomSecurity cu = getSec(HOTEL);
        String token = iSec.authenticateToken(realM, "user", "secret", cu);
        assertNotNull(token);
        DialogVariables v = new DialogVariables();
        v.setValueS("rese", sym);
        runAction(token, v, "dialog10.xml", "checkrese");
        assertOK(v);
    }
    

    @Test
    public void test3() {
        String sym = createRes(1);
        ReservationForm r = iRes.findElem(getH(HOTEL), sym);
        assertNull(r.getAdvancePayment());
        assertNull(r.getDateofadvancePayment());
        r.setAdvancePayment(new BigDecimal(999));
        r.setDateofadvancePayment(toDate(2014, 3, 20));
        iRes.changeElem(getH(HOTEL), r);
        r = iRes.findElem(getH(HOTEL), sym);
        equalB(999, r.getAdvancePayment(), 2);
        eqD(2014, 3, 20, r.getDateofadvancePayment());                
    }

}
