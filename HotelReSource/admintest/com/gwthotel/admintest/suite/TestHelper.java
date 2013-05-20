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

import org.junit.After;
import org.junit.Before;

import com.gwthotel.admin.IGetHotelRoles;
import com.gwthotel.admin.IHotelAdmin;
import com.gwthotel.admintest.guice.ServiceInjector;
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

    protected final String realM = "classpath:resources/shiro/hoteluser.ini";
    protected final String adminM = "classpath:resources/shiro/admin.ini";

    public TestHelper() {
        iAdmin = ServiceInjector.constructHotelAdmin();
        iTest = ServiceInjector.constructITestEnhancer();
        iRoles = ServiceInjector.constructHotelRoles();
        iServer = ServiceInjector.contructJythonUiServer();
        iSec = ServiceInjector.constructSecurity();
        iServices = ServiceInjector.getHotelServices();
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

}
