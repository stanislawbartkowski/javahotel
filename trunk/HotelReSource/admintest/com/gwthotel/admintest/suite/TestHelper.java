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
import com.gwthotel.hotel.server.service.H;
import com.gwtmodel.testenhancer.ITestEnhancer;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.security.ISecurity;

/**
 * @author hotel
 * 
 */
public class TestHelper {

    protected final IHotelAdmin iAdmin;
    protected final IGetHotelRoles iRoles;
    private final ITestEnhancer iTest;

    public TestHelper() {
        iAdmin = ServiceInjector.constructHotelAdmin();
        iRoles = H.getHotelRoles();
        iTest = ServiceInjector.constructITestEnhancer();

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

}
