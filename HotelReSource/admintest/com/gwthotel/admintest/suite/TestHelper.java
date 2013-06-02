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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;

import com.gwthotel.admin.Hotel;
import com.gwthotel.admin.HotelRoles;
import com.gwthotel.admin.IGetHotelRoles;
import com.gwthotel.admin.IGetVatTaxes;
import com.gwthotel.admin.IHotelAdmin;
import com.gwthotel.admintest.guice.ServiceInjector;
import com.gwthotel.hotel.pricelist.IHotelPriceList;
import com.gwthotel.hotel.prices.IHotelPriceElem;
import com.gwthotel.hotel.services.IHotelServices;
import com.gwtmodel.testenhancer.ITestEnhancer;
import com.jythonui.server.IJythonUIServer;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.security.ISecurity;
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
    private final ITestEnhancer iTest;
    protected final IJythonUIServer iServer;
    protected final ISecurity iSec;
    protected final IHotelServices iServices;
    protected final IGetVatTaxes iTaxes;
    protected final IHotelPriceList iPrice;
    protected final IHotelPriceElem iPriceElem;

    protected static final String HOTEL = "hotel";
    protected static final String HOTEL1 = "hotel1";

    protected void createHotels() {
        iAdmin.clearAll();
        String[] hNames = new String[] { HOTEL, HOTEL1 };
        for (String s : hNames) {
            Hotel ho = new Hotel();
            ho.setName(s);
            ho.setDescription("Pod Pieskiem");
            List<HotelRoles> roles = new ArrayList<HotelRoles>();
            iAdmin.addOrModifHotel(ho, roles);
        }
    }

    protected final String realM = "classpath:resources/shiro/hoteluser.ini";
    protected final String adminM = "classpath:resources/shiro/admin.ini";

    public TestHelper() {
        iAdmin = ServiceInjector.constructHotelAdmin();
        iTest = ServiceInjector.constructITestEnhancer();
        iRoles = ServiceInjector.constructHotelRoles();
        iServer = ServiceInjector.contructJythonUiServer();
        iSec = ServiceInjector.constructSecurity();
        iServices = ServiceInjector.getHotelServices();
        iTaxes = ServiceInjector.getVatTaxes();
        iPrice = ServiceInjector.getHotelPriceList();
        iPriceElem = ServiceInjector.getHotelPriceElem();
    }

    @Before
    public void setUp() {
        iTest.beforeTest();
        Holder.setAuth(false);
    }

    @After
    public void tearDown() {
        iTest.afterTest();
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

}
