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
package com.jython.ui;

import static org.junit.Assert.assertEquals;
import guice.ServiceInjector;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;

import com.gwtmodel.testenhancer.ITestEnhancer;
import com.jython.ui.server.datastore.IPersonOp;
import com.jythonui.server.IJythonUIServer;
import com.jythonui.server.security.ISecurity;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogInfo;

/**
 * @author hotel
 * 
 */
public class TestHelper {

    protected final IJythonUIServer iServer;
    protected final ITestEnhancer iTest;
    protected final IPersonOp po;
    protected final ISecurity iSec;

    protected final static String realmIni = "classpath:resources/shiro/shiro.ini";
    protected final static String derbyIni = "classpath:resources/shiro/shiroderby.ini";

    public TestHelper() {
        iServer = ServiceInjector.contructJythonUiServer();
        iTest = ServiceInjector.constructITestEnhancer();
        po = ServiceInjector.constructPersonOp();
        iSec = ServiceInjector.constructSecurity();
    }

    @Before
    public void setUp() {
        iTest.beforeTest();
    }

    @After
    public void tearDown() {
        iTest.afterTest();
    }

    protected void equalB(double f, BigDecimal b, int afterdot) {
        BigDecimal c = new BigDecimal(f).setScale(afterdot,
                BigDecimal.ROUND_HALF_UP);
        assertEquals(c, b);
    }

    protected DialogFormat findDialog(String dialogName) {
        DialogInfo d = iServer.findDialog(null, dialogName);
        if (d == null)
            return null;
        return d.getDialog();
    }

}