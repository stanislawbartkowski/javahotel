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
package com.jython.ui;

import static org.junit.Assert.assertEquals;
import guice.ServiceInjector;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.After;
import org.junit.Before;

import com.gwtmodel.table.common.dateutil.DateFormatUtil;
import com.gwtmodel.testenhancer.ITestEnhancer;
import com.jython.ui.server.datastore.IDateLineOp;
import com.jython.ui.server.datastore.IDateRecordOp;
import com.jython.ui.server.datastore.IPersonOp;
import com.jythonui.server.IDefaultData;
import com.jythonui.server.IJythonUIServer;
import com.jythonui.server.dict.IGetLocalizedDict;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.holder.SHolder;
import com.jythonui.server.registry.IStorageRegistryFactory;
import com.jythonui.server.resbundle.IAppMess;
import com.jythonui.server.security.ISecurity;
import com.jythonui.server.semaphore.ISemaphore;
import com.jythonui.server.storage.gensym.ISymGenerator;
import com.jythonui.server.storage.seq.ISequenceRealmGen;
import com.jythonui.server.xml.IXMLTransformer;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogInfo;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.RequestContext;
import com.jythonui.server.storage.blob.IBlobHandler;

/**
 * @author hotel
 * 
 */
public class TestHelper {

    protected final IJythonUIServer iServer;
    protected final ITestEnhancer iTest;
    protected final IPersonOp po;
    protected final ISecurity iSec;
    protected final IAppMess appMess;
    protected final ISequenceRealmGen iSeq;
    protected final ISymGenerator iSym;
    protected final IDateLineOp iOp;
    protected final IDateRecordOp dOp;
    protected final IStorageRegistryFactory iReg;
    protected final IXMLTransformer iXml;
    protected final ISemaphore iSem;
    protected final IGetLocalizedDict iListC;
    protected final IGetLocalizedDict iListT;
    protected final IGetLocalizedDict iListI;
    protected final IGetLocalizedDict iListP;
    protected final IDefaultData dData;
    protected final IBlobHandler iBlob;

    protected final static String realmIni = "classpath:resources/shiro/shiro.ini";
    protected final static String derbyIni = "classpath:resources/shiro/shiroderby.ini";

    public TestHelper() {
        iServer = ServiceInjector.contructJythonUiServer();
        iTest = ServiceInjector.constructITestEnhancer();
        po = ServiceInjector.constructPersonOp();
        iSec = ServiceInjector.constructSecurity();
        appMess = Holder.getAppMess();
        iSeq = SHolder.getSequenceRealmGen();
        iSym = ServiceInjector.getSymGenerator();
        iOp = ServiceInjector.constructDateLineElem();
        dOp = ServiceInjector.getDateRecordOp();
        iReg = ServiceInjector.getStorageRegistryFactory();
        iXml = Holder.getXMLTransformer();
        iSem = SHolder.getSem();
        iListC = Holder.getListOfCountries();
        iListT = Holder.getListOfTitles();
        iListI = Holder.getListOfIdTypes();
        iListP = Holder.getListOfPayment();
        dData = Holder.getDefaultData();
        iBlob = SHolder.getBlobHandler();
    }

    protected void putLocale(String lang) {
        RequestContext req = new RequestContext();
        req.setLocale(lang);
        Holder.setContext(req);

    }

    @Before
    public void setUp() {
        iTest.beforeTest();
        Holder.setAuth(false);
        putLocale("pl");
        dData.clear();
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
        DialogInfo d = iServer.findDialog(new RequestContext(), dialogName);
        if (d == null)
            return null;
        return d.getDialog();
    }

    protected DialogInfo findDialog(String token, String dialogName) {
        RequestContext req = new RequestContext();
        req.setToken(token);
        return iServer.findDialog(req, dialogName);
    }

    protected void runAction(DialogVariables v, String dialogName,
            String actionId) {
        iServer.runAction(new RequestContext(), v, dialogName, actionId);
    }

    protected void runAction(String token, DialogVariables v,
            String dialogName, String actionId) {
        RequestContext req = new RequestContext();
        req.setToken(token);
        iServer.runAction(req, v, dialogName, actionId);
    }

    protected String authenticateToken(String realm, String user,
            String password) {
        String t = iSec.authenticateToken(realm, user, password, null);
        return t;
    }

    protected Date getD(int year, int m, int d) {
        return DateFormatUtil.toD(year, m, d);
    }

    protected boolean eqD(int year, int m, int d, Date da) {
        int dd = DateFormatUtil.getY(da);
        if (year != dd)
            return false;
        int mm = DateFormatUtil.getM(da);
        if (m != mm)
            return false;
        if (d != da.getDay())
            return false;
        return true;
    }

}
