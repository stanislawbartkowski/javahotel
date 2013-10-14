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
package com.gwthotel.admintest.suite;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.gwthotel.admin.IXMLToMap;
import com.gwthotel.admintest.guice.ServiceInjector;
import com.gwthotel.hotel.IClearHotel;
import com.gwthotel.hotel.IGetInstanceHotelId;
import com.gwthotel.hotel.IHotelObjectGenSym;
import com.gwthotel.hotel.IHotelObjectsFactory;
import com.gwthotel.hotel.bill.ICustomerBills;
import com.gwthotel.hotel.customer.IHotelCustomers;
import com.gwthotel.hotel.payment.IPaymentBillOp;
import com.gwthotel.hotel.pricelist.IHotelPriceList;
import com.gwthotel.hotel.prices.IHotelPriceElem;
import com.gwthotel.hotel.reservation.IReservationForm;
import com.gwthotel.hotel.reservationop.IReservationOp;
import com.gwthotel.hotel.rooms.IHotelRooms;
import com.gwthotel.hotel.server.service.H;
import com.gwthotel.hotel.services.IHotelServices;
import com.gwthotel.shared.IHotelConsts;
import com.gwtmodel.testenhancer.ITestEnhancer;
import com.jythonui.server.IJythonUIServer;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.holder.SHolder;
import com.jythonui.server.security.ISecurity;
import com.jythonui.server.security.token.ICustomSecurity;
import com.jythonui.server.semaphore.ISemaphore;
import com.jythonui.server.storage.seq.ISequenceRealmGen;
import com.jythonui.shared.CustomSecurity;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogInfo;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.RequestContext;

/**
 * @author hotel
 * 
 */
public class TestHelper {

    protected final IHotelAdmin iAdmin;
    protected final IGetHotelRoles iRoles;
    private static final ITestEnhancer iTest = ServiceInjector
            .constructITestEnhancer();
    protected final IJythonUIServer iServer;
    protected final ISecurity iSec;
    protected final IHotelServices iServices;
    protected final IGetVatTaxes iTaxes;
    protected final IHotelPriceList iPrice;
    protected final IHotelPriceElem iPriceElem;
    protected final IHotelRooms iRooms;
    protected final IHotelCustomers iCustomers;
    protected final IGetInstanceHotelId iGetI;
    protected final ISequenceRealmGen iSeq;
    protected final IHotelObjectGenSym iHGen;
    protected final IHotelObjectsFactory hObjects;
    protected final IReservationForm iRes;
    protected final IReservationOp iResOp;
    protected final IClearHotel iClear;
    protected final IXMLToMap ixMap;
    protected final ICustomerBills iBills;
    protected final ISemaphore iSem;
    protected final IPaymentBillOp iPayOp;

    protected static final String HOTEL = "hotel";
    protected static final String HOTEL1 = "hotel1";

    protected static final String TESTINSTANCE = IHotelConsts.INSTANCETEST;

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
        iServer = ServiceInjector.contructJythonUiServer();
        iSec = ServiceInjector.constructSecurity();
        iServices = ServiceInjector.getHotelServices();
        iTaxes = ServiceInjector.getVatTaxes();
        iPrice = ServiceInjector.getHotelPriceList();
        iPriceElem = ServiceInjector.getHotelPriceElem();
        iRooms = ServiceInjector.getHotelRooms();
        iCustomers = ServiceInjector.getHotelCustomers();
        iGetI = H.getInstanceHotelId();
        iSeq = ServiceInjector.getSequenceRealmGen();
        iHGen = ServiceInjector.getHotelGenSym();
        hObjects = ServiceInjector.getHotelObjects();
        iRes = ServiceInjector.getReservationForm();
        iResOp = ServiceInjector.getReservationOp();
        iClear = H.getClearHotel();
        ixMap = ServiceInjector.getXMLToMap();
        iBills = ServiceInjector.getCustomerBills();
        iSem = SHolder.getSem();
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

    protected DialogFormat findDialog(String dialogName) {
        DialogInfo d = iServer.findDialog(new RequestContext(), dialogName);
        if (d == null)
            return null;
        return d.getDialog();
    }

    protected void runAction(DialogVariables v, String dialogName,
            String actionId) {
        iServer.runAction(new RequestContext(), v, dialogName, actionId);
    }

    protected Date toDate(int y, int m, int d) {
        Date da = new Date(y - 1900, m, d);
        return da;
    }

    protected boolean eqDate(Date da, int y, int m, int d) {
        if (da.getYear() + 1900 != y)
            return false;
        if (da.getMonth() != m)
            return false;
        int day = da.getDate();
        if (day != d)
            return false;
        return true;
    }

    protected ICustomSecurity getSec(String hotel) {
        CustomSecurity cust = new CustomSecurity();
        cust.setAttr(IHotelConsts.HOTELNAME, hotel);
        cust.setAttr(IHotelConsts.INSTANCEID, TESTINSTANCE);
        ICustomSecurity cu = Holder.getSecurityConvert().construct(cust);
        return cu;
    }

    protected void assertEqB(double b1, BigDecimal b2) {
        assertEquals(b1, b2.doubleValue(),2);

    }

}
