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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.bill.CustomerBill;
import com.gwthotel.hotel.customer.HotelCustomer;
import com.gwthotel.hotel.reservation.ReservationForm;
import com.gwthotel.hotel.reservation.ReservationPaymentDetail;
import com.gwthotel.hotel.rooms.HotelRoom;
import com.gwtmodel.table.common.dateutil.DateFormatUtil;
import com.jythonui.server.security.token.ICustomSecurity;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.ListOfRows;
import com.jythonui.shared.RowContent;
import com.jythonui.shared.RowIndex;

public class Test25 extends TestHelper {

    @Before
    public void before() {
        clearObjects();
        createHotels();
        setTestToday(DateFormatUtil.toD(2013, 6, 13));
        setUserPassword();
    }

    private CustomerBill createP() {

        HotelRoom ho = new HotelRoom();
        ho.setName("P10");
        ho.setNoPersons(3);
        iRooms.addElem(getH(HOTEL), ho);
        HotelCustomer p = (HotelCustomer) hObjects.construct(getH(HOTEL),
                HotelObjects.CUSTOMER);
        p.setGensymbol(true);
        p.setAttr("country", "gb");
        p = iCustomers.addElem(getH(HOTEL), p);
        ReservationForm r = (ReservationForm) hObjects.construct(getH(HOTEL),
                HotelObjects.RESERVATION);
        r.setCustomerName(p.getName());
        r.setGensymbol(true);
        ReservationPaymentDetail det = new ReservationPaymentDetail();
        det.setNoP(3);
        det.setPrice(new BigDecimal("100.0"));
        det.setPriceTotal(new BigDecimal("100.0"));
        det.setPriceList(new BigDecimal("200.0"));
        det.setRoomName("P10");
        det.setResDate(toDate(2013, 4, 10));
        r.getResDetail().add(det);
        r = iRes.addElem(getH(HOTEL), r);
        String sym = r.getName();

        CustomerBill b = (CustomerBill) hObjects.construct(getH(HOTEL),
                HotelObjects.BILL);
        b.setGensymbol(true);
        b.setPayer(p.getName());
        b.setReseName(sym);
        b.setIssueDate(toDate(2010, 10, 12));
        for (ReservationPaymentDetail d : r.getResDetail()) {
            b.getPayList().add(d.getId());
        }
        b = iBills.addElem(getH(HOTEL), b);
        assertNotNull(b);
        System.out.println(b.getName());
        return b;
    }

    private int noPDFs(DialogVariables v) {
        ListOfRows r = v.getList("blist");
        return r.getRowList().size();
    }
    
    private DialogVariables verifyNumberOf(CustomerBill b,String token,int no) {
        DialogVariables v = new DialogVariables();
        v.setValueS("billno", b.getName());
        runAction(v, token, "dialog5.xml", "createlist");
        assertTrue(v.getValue("OK").getValueB());
        assertEquals(no, noPDFs(v));
        return v;
    }

    @Test
    public void test1() {
        ICustomSecurity cu = getSec(HOTEL);
        String token = iSec.authenticateToken(realM, "user", "secret", cu);
        assertNotNull(token);
        CustomerBill b = createP();
        DialogVariables v = new DialogVariables();
        v.setValueS("billno", b.getName());
        runAction(v, token, "dialog5.xml", "clearlist");
        assertTrue(v.getValue("OK").getValueB());

        verifyNumberOf(b,token,0); 

        v = new DialogVariables();
        v.setValueS("billno", b.getName());
        runAction(v, token, "dialog5.xml", "addpdf");
        assertTrue(v.getValue("OK").getValueB());

        verifyNumberOf(b,token,1); 

        for (int i = 0; i < 5; i++) {
            v = new DialogVariables();
            v.setValueS("billno", b.getName());
            runAction(v, token, "dialog5.xml", "addpdf");
            assertTrue(v.getValue("OK").getValueB());
        }

        v = verifyNumberOf(b,token,6); 

        DialogFormat d = findDialog("dialog5.xml");
        ListFormat li = d.findList("blist");
        assertNotNull(li);
        RowIndex rI = new RowIndex(li.getColumns());

        ListOfRows r = v.getList("blist");
        Long remId = null;
        for (RowContent row : r.getRowList()) {
            Long id = row.getRow(rI.getInde("id")).getValueL();
            remId = id;
            System.out.println(id);
        }
        
        v = new DialogVariables();
        v.setValueS("billno", b.getName());
        v.setValueL("id", remId);
        runAction(v, token, "dialog5.xml", "changecomment");
        
        v = new DialogVariables();
        v.setValueS("billno", b.getName());
        v.setValueL("id", remId);        
        runAction(v, token, "dialog5.xml", "removepdf");

        verifyNumberOf(b,token,5); 
                
    }
    
    @Test
    public void test2() {
        Date da = DateFormatUtil.getToday();
        assertTrue(eqDate(da,2013,6,13));
        setTestToday(null);
        da = DateFormatUtil.getToday();
        assertFalse(eqDate(da,2013,6,13));
    }

}
