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
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.gwthotel.admin.AppInstanceId;
import com.gwthotel.admin.Hotel;
import com.gwthotel.admin.HotelId;
import com.gwthotel.admin.HotelRoles;
import com.gwthotel.admin.IGetHotelRoles;
import com.gwthotel.admin.IGetVatTaxes;
import com.gwthotel.admin.IHotelAdmin;
import com.gwthotel.admin.Person;
import com.gwthotel.admintest.guice.ServiceInjector;
import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.IClearHotel;
import com.gwthotel.hotel.IGetInstanceHotelId;
import com.gwthotel.hotel.IHotelObjectGenSym;
import com.gwthotel.hotel.IHotelObjectsFactory;
import com.gwthotel.hotel.bill.CustomerBill;
import com.gwthotel.hotel.bill.ICustomerBills;
import com.gwthotel.hotel.customer.HotelCustomer;
import com.gwthotel.hotel.customer.IHotelCustomers;
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
import com.gwthotel.shared.IHotelConsts;
import com.gwtmodel.table.common.dateutil.DateFormatUtil;
import com.gwtmodel.table.common.dateutil.ISetTestToday;
import com.gwtmodel.testenhancer.ITestEnhancer;
import com.jythonui.server.IJythonUIServer;
import com.jythonui.server.IXMLToMap;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.holder.SHolder;
import com.jythonui.server.newblob.IAddNewBlob;
import com.jythonui.server.registry.IStorageRegistryFactory;
import com.jythonui.server.security.ISecurity;
import com.jythonui.server.security.token.ICustomSecurity;
import com.jythonui.server.semaphore.ISemaphore;
import com.jythonui.server.storage.blob.IBlobHandler;
import com.jythonui.server.storage.seq.ISequenceRealmGen;
import com.jythonui.shared.CustomSecurity;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogInfo;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.RequestContext;
import com.jythonui.test.CommonTestHelper;

/**
 * @author hotel
 * 
 */
public class TestHelper extends CommonTestHelper {

    protected final IHotelAdmin iAdmin;
    protected final IGetHotelRoles iRoles;
    // for some reason it is necessary
    private static final ITestEnhancer iTest = ServiceInjector
            .constructITestEnhancer();
    protected final IHotelServices iServices;
    protected final IGetVatTaxes iTaxes;
    protected final IHotelPriceList iPrice;
    protected final IHotelPriceElem iPriceElem;
    protected final IHotelRooms iRooms;
    protected final IHotelCustomers iCustomers;
    protected final IGetInstanceHotelId iGetI;
    protected final IHotelObjectGenSym iHGen;
    protected final IHotelObjectsFactory hObjects;
    protected final IReservationForm iRes;
    protected final IReservationOp iResOp;
    protected final IClearHotel iClear;
    protected final ICustomerBills iBills;
    protected final IPaymentBillOp iPayOp;

    protected static final String HOTEL = "hotel";
    protected static final String HOTEL1 = "hotel1";

    protected static final String TESTINSTANCE = IHotelConsts.INSTANCETEST;
    
    @Before
    public void before() {
        clearObjects();
        createHotels();
        setTestToday(DateFormatUtil.toD(2013, 6, 13));
    }

    protected AppInstanceId getI() {
        return iGetI.getInstance(TESTINSTANCE, "user");
    }

    protected HotelId getH(String hotel) {
        return iGetI.getHotel(TESTINSTANCE, hotel, "user");
    }

    protected HotelId getH1(String hotel) {
        return iGetI.getHotel(TESTINSTANCE, hotel, "modifuser");
    }

    protected void createHotels() {
        iGetI.invalidateCache();
        iAdmin.clearAll(getI());
        String[] hNames = new String[] { HOTEL, HOTEL1 };
        for (String s : hNames) {
            Hotel ho = new Hotel();
            ho.setName(s);
            ho.setDescription("Pod Pieskiem");
            List<HotelRoles> roles = new ArrayList<HotelRoles>();
            iAdmin.addOrModifHotel(getI(), ho, roles);
        }
    }

    protected final String realM = "classpath:resources/shiro/hoteluser.ini";
    protected final String adminM = "classpath:resources/shiro/admin.ini";

    public TestHelper() {
        iAdmin = H.getHotelAdmin();
        iRoles = ServiceInjector.constructHotelRoles();
        iServices = ServiceInjector.getHotelServices();
        iTaxes = ServiceInjector.getVatTaxes();
        iPrice = ServiceInjector.getHotelPriceList();
        iPriceElem = ServiceInjector.getHotelPriceElem();
        iRooms = ServiceInjector.getHotelRooms();
        iCustomers = ServiceInjector.getHotelCustomers();
        iGetI = H.getInstanceHotelId();
        iHGen = ServiceInjector.getHotelGenSym();
        hObjects = ServiceInjector.getHotelObjects();
        iRes = ServiceInjector.getReservationForm();
        iResOp = ServiceInjector.getReservationOp();
        iClear = H.getClearHotel();
        iBills = ServiceInjector.getCustomerBills();
        iPayOp = H.getPaymentsOp();

    }

    @BeforeClass
    public static void setUp() {
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
        List<Hotel> aList = iAdmin.getListOfHotels(getI());
        for (Hotel ho : aList) {
            HotelId hotel = getH(ho.getName());
            iClear.clearObjects(hotel);
            iHGen.clearAll(hotel);
        }

    }

     protected Date toDate(int y, int m, int d) {
         return getD(y,m,d);
     }
    protected ICustomSecurity getSec(String hotel) {
        CustomSecurity cust = new CustomSecurity();
        cust.setAttr(IHotelConsts.HOTELNAME, hotel);
        cust.setAttr(IHotelConsts.INSTANCEID, TESTINSTANCE);
        ICustomSecurity cu = Holder.getSecurityConvert().construct(cust);
        return cu;
    }

    protected void setUserPassword() {
        iAdmin.clearAll(getI());
        Person pe = new Person();
        pe.setName("user");
        pe.setDescription("user name");
        List<HotelRoles> roles = new ArrayList<HotelRoles>();
        iAdmin.addOrModifPerson(getI(), pe, roles);
        iAdmin.changePasswordForPerson(getI(), "user", "secret");
        Hotel ho = new Hotel();

        ho.setName(HOTEL);
        ho.setDescription("Pod Pieskiem");
        roles = new ArrayList<HotelRoles>();
        pe = new Person();
        pe.setName("user");
        HotelRoles rol = new HotelRoles(pe);
        rol.getRoles().add("mana");
        rol.getRoles().add("acc");
        roles.add(rol);
        iAdmin.addOrModifHotel(getI(), ho, roles);
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
    
}
