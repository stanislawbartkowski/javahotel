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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.gwthotel.admintest.guice.ServiceInjector;
import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.IClearHotel;
import com.gwthotel.hotel.IHotelObjectsFactory;
import com.gwthotel.hotel.bill.CustomerBill;
import com.gwthotel.hotel.bill.ICustomerBills;
import com.gwthotel.hotel.customer.HotelCustomer;
import com.gwthotel.hotel.customer.IHotelCustomers;
import com.gwthotel.hotel.mailing.IHotelMailList;
import com.gwthotel.hotel.payment.IPaymentBillOp;
import com.gwthotel.hotel.pricelist.IHotelPriceList;
import com.gwthotel.hotel.prices.IHotelPriceElem;
import com.gwthotel.hotel.reservation.IReservationForm;
import com.gwthotel.hotel.reservation.ReservationForm;
import com.gwthotel.hotel.reservation.ReservationPaymentDetail;
import com.gwthotel.hotel.reservationop.IReservationOp;
import com.gwthotel.hotel.rooms.HotelRoom;
import com.gwthotel.hotel.rooms.IHotelRooms;
import com.gwthotel.hotel.server.service.H;
import com.gwthotel.hotel.services.IHotelServices;
import com.gwtmodel.table.common.dateutil.DateFormatUtil;
import com.jython.serversecurity.OObject;
import com.jython.serversecurity.OObjectRoles;
import com.jython.serversecurity.Person;
import com.jython.serversecurity.cache.OObjectId;
import com.jythonui.server.IConsts;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.security.token.ICustomSecurity;
import com.jythonui.shared.CustomSecurity;
import com.jythonui.shared.DialogVariables;
import com.jythonui.test.CommonTestHelper;

/**
 * @author hotel
 * 
 */
public class TestHelper extends CommonTestHelper {

    protected final IHotelServices iServices;
    protected final IHotelPriceList iPrice;
    protected final IHotelPriceElem iPriceElem;
    protected final IHotelRooms iRooms;
    protected final IHotelCustomers iCustomers;
    protected final IHotelObjectsFactory hObjects;
    protected final IReservationForm iRes;
    protected final IReservationOp iResOp;
    protected final IClearHotel iClear;
    protected final ICustomerBills iBills;
    protected final IPaymentBillOp iPayOp;
    protected final IHotelMailList iHotelMail;

    @Before
    public void before() {
        clearObjects();
        createHotels();
        setTestToday(DateFormatUtil.toD(2013, 6, 13));
    }

    protected void createHotels() {
        super.createObjects();
    }

    protected final String realM = "classpath:resources/shiro/hoteluser.ini";
    protected final String adminM = "classpath:resources/shiro/admin.ini";

    public TestHelper() {
        iServices = ServiceInjector.getHotelServices();
        iPrice = ServiceInjector.getHotelPriceList();
        iPriceElem = ServiceInjector.getHotelPriceElem();
        iRooms = ServiceInjector.getHotelRooms();
        iCustomers = ServiceInjector.getHotelCustomers();
        hObjects = ServiceInjector.getHotelObjects();
        iRes = ServiceInjector.getReservationForm();
        iResOp = ServiceInjector.getReservationOp();
        iClear = H.getClearHotel();
        iBills = ServiceInjector.getCustomerBills();
        iPayOp = H.getPaymentsOp();
        iHotelMail = ServiceInjector.getHotelMail();

    }

    @BeforeClass
    public static void setUp() {
        iTest = ServiceInjector.constructITestEnhancer();
        iTest.beforeTest();
    }

    @AfterClass
    public static void tearDown() {
        iTest.afterTest();
    }

    @Before
    public void beforeTest() {
        Holder.setAuth(false);
    }

    protected void clearObjects() {
        List<OObject> aList = iAdmin.getListOfObjects(getI());
        for (OObject ho : aList) {
            OObjectId hotel = getH(ho.getName());
            iClear.clearObjects(hotel);
        }

    }

    protected Date toDate(int y, int m, int d) {
        return getD(y, m, d);
    }

    protected ICustomSecurity getSec(String hotel) {
        CustomSecurity cust = new CustomSecurity();
        cust.setAttr(IConsts.OBJECTNAME, hotel);
        cust.setAttr(IConsts.INSTANCEID, TESTINSTANCE);
        ICustomSecurity cu = Holder.getSecurityConvert().construct(cust);
        return cu;
    }

    protected void setUserPassword() {
        iAdmin.clearAll(getI());
        Person pe = new Person();
        pe.setName("user");
        pe.setDescription("user name");
        List<OObjectRoles> roles = new ArrayList<OObjectRoles>();
        iAdmin.addOrModifPerson(getI(), pe, roles);
        iAdmin.changePasswordForPerson(getI(), "user", "secret");
        OObject ho = new OObject();

        ho.setName(HOTEL);
        ho.setDescription("Pod Pieskiem");
        roles = new ArrayList<OObjectRoles>();
        pe = new Person();
        pe.setName("user");
        OObjectRoles rol = new OObjectRoles(pe);
        rol.getRoles().add("mana");
        rol.getRoles().add("acc");
        roles.add(rol);
        iAdmin.addOrModifObject(getI(), ho, roles);
    }

    protected CustomerBill createP() {

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
        b.setDateOfPayment(toDate(2010, 10, 15));
        for (ReservationPaymentDetail d : r.getResDetail()) {
            b.getPayList().add(d.getId());
        }
        b = iBills.addElem(getH(HOTEL), b);
        assertNotNull(b);
        System.out.println(b.getName());
        return b;
    }

    protected String createRes(int no) {
        HotelRoom ho = new HotelRoom();
        ho.setName("P10");
        ho.setNoPersons(3);
        iRooms.addElem(getH(HOTEL), ho);
        HotelCustomer p = (HotelCustomer) hObjects.construct(getH(HOTEL),
                HotelObjects.CUSTOMER);
        p.setGensymbol(true);
        p = iCustomers.addElem(getH(HOTEL), p);
        ReservationForm r = (ReservationForm) hObjects.construct(getH(HOTEL),
                HotelObjects.RESERVATION);
        r.setCustomerName(p.getName());
        r.setGensymbol(true);
        Date d = toDate(2013, 4, 10);
        for (int i = 0; i < no; i++) {
            ReservationPaymentDetail det = new ReservationPaymentDetail();
            det.setNoP(3);
            det.setPrice(new BigDecimal(100));
            det.setPriceList(new BigDecimal(200.0));
            det.setPriceTotal(new BigDecimal(100));
            det.setRoomName("P10");
            det.setResDate(d);
            r.getResDetail().add(det);
            d = incDay(d);
        }
        r = iRes.addElem(getH(HOTEL), r);
        String sym = r.getName();
        r = iRes.findElem(getH(HOTEL), sym);
        System.out.println(sym);
        assertNull(r.getAdvanceDeposit());
        assertNull(r.getTermOfAdvanceDeposit());
        return sym;
    }

    protected void scriptTest(String dialogName, String action) {
        setUserPassword();
        ICustomSecurity cu = getSec(HOTEL);
        String token = iSec.authenticateToken(realM, "user", "secret", cu);
        assertNotNull(token);
        DialogVariables v = new DialogVariables();
        runAction(token, v, dialogName, action);
        assertOK(v);
    }

}
