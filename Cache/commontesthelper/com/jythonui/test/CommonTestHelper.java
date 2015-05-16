/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
package com.jythonui.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.gwtmodel.commoncache.ICommonCache;
import com.gwtmodel.table.common.dateutil.DateFormatUtil;
import com.gwtmodel.table.common.dateutil.ISetTestToday;
import com.gwtmodel.testenhancer.ITestEnhancer;
import com.jython.serversecurity.AppInstanceId;
import com.jython.serversecurity.IOObjectAdmin;
import com.jython.serversecurity.OObject;
import com.jython.serversecurity.OObjectRoles;
import com.jython.serversecurity.cache.IGetInstanceOObjectIdCache;
import com.jython.serversecurity.cache.OObjectId;
import com.jythonui.server.IConsts;
import com.jythonui.server.IDefaultData;
import com.jythonui.server.IGetMailFrom;
import com.jythonui.server.IGetResourceMap;
import com.jythonui.server.IJythonUIServer;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.IXMLToMap;
import com.jythonui.server.crud.ICrudObjectGenSym;
import com.jythonui.server.dict.IGetLocalizedDict;
import com.jythonui.server.holder.Holder;
import com.jythonui.server.mail.INoteStorage;
import com.jythonui.server.newblob.IAddNewBlob;
import com.jythonui.server.registry.IStorageRegistryFactory;
import com.jythonui.server.resbundle.IAppMess;
import com.jythonui.server.security.ISecurity;
import com.jythonui.server.security.token.ICustomSecurity;
import com.jythonui.server.semaphore.ISemaphore;
import com.jythonui.server.storage.blob.IBlobHandler;
import com.jythonui.server.storage.gensym.ISymGenerator;
import com.jythonui.server.storage.seq.ISequenceRealmGen;
import com.jythonui.server.xml.IXMLTransformer;
import com.jythonui.shared.ButtonItem;
import com.jythonui.shared.CustomSecurity;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogInfo;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.ListFormat;
import com.jythonui.shared.RequestContext;

abstract public class CommonTestHelper {

    @Inject
    protected static IJythonUIServer iServer;
    @Inject
    protected static IJythonUIServerProperties iResServer;
    // @Inject
    // This first one inject directly in the constructor
    protected static ITestEnhancer iTest;
    @Inject
    protected static ISecurity iSec;
    @Inject
    @Named(IConsts.APPMESS)
    protected static IAppMess appMess;
    @Inject
    protected static ISequenceRealmGen iSeq;
    @Inject
    protected static ISymGenerator iSym;
    @Inject
    protected static IStorageRegistryFactory iReg;
    @Inject
    protected static IXMLTransformer iXml;
    @Inject
    protected static ISemaphore iSem;
    @Inject
    protected static IAddNewBlob iAddBlob;
    // @Inject
    // protected static IMailSendGet iMail;
    protected final IGetLocalizedDict iListC;
    protected final IGetLocalizedDict iListT;
    protected final IGetLocalizedDict iListI;
    protected final IGetLocalizedDict iListP;
    protected final IGetLocalizedDict iListR;
    protected final IGetLocalizedDict iListV;
    @Inject
    protected static IDefaultData dData;
    @Inject
    protected static IBlobHandler iBlob;
    @Inject
    protected static ISetTestToday setTestToday;
    @Inject
    protected static IXMLToMap ixMap;
    @Inject
    protected static IOObjectAdmin iAdmin;
    @Inject
    protected static IGetInstanceOObjectIdCache iGetI;
    @Inject
    @Named(ISharedConsts.PERSONSONLYSECURITY)
    protected static IOObjectAdmin iPerson;
    @Inject
    protected static INoteStorage iNoteStorage;
    @Inject
    protected static ICrudObjectGenSym iGenSym;
    @Inject
    protected static IGetMailFrom iFrom;
    @Inject
    protected static IGetResourceMap iGet;
    @Inject
    protected static ICommonCache iCache;

    protected CommonTestHelper() {
        iListC = Holder.getListOfCountries();
        iListT = Holder.getListOfTitles();
        iListI = Holder.getListOfIdTypes();
        iListP = Holder.getListOfPayment();
        iListR = Holder.IGetListOfDefaultRoles();
        iListV = Holder.IGetListOfVat();
    }

    protected void createObjects() {
        iGetI.invalidateCache();
        iAdmin.clearAll(getI());
        String[] hNames = new String[] { HOTEL, HOTEL1 };
        for (String s : hNames) {
            OObject ho = new OObject();
            ho.setName(s);
            ho.setDescription("Pod Pieskiem");
            List<OObjectRoles> roles = new ArrayList<OObjectRoles>();
            iAdmin.addOrModifObject(getI(), ho, roles);
        }
    }

    protected void putLocale(String lang) {
        RequestContext req = new RequestContext();
        req.setLocale(lang);
        Holder.setContext(req);
    }

    protected void setTestToday(Date d) {
        setTestToday.setToday(d);
    }

    protected void assertEqB(double b1, BigDecimal b2) {
        assertEquals(b1, b2.doubleValue(), 2);
    }

    protected void equalB(double f, BigDecimal b, int afterdot) {
        BigDecimal c = new BigDecimal(f).setScale(afterdot,
                BigDecimal.ROUND_HALF_UP);
        assertEquals(c, b.setScale(afterdot, BigDecimal.ROUND_HALF_UP));
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
            String dialogName, String actionId, String locale) {
        RequestContext req = new RequestContext();
        req.setToken(token);
        req.setLocale(locale);
        iServer.runAction(req, v, dialogName, actionId);
    }

    protected void runAction(String token, DialogVariables v,
            String dialogName, String actionId) {
        runAction(token, v, dialogName, actionId, null);
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
        int day = DateFormatUtil.getD(da);
        if (d != day)
            return false;
        return true;
    }

    protected void assertOK(DialogVariables v) {
        assertNotNull(v.getValue("OK"));
        assertTrue(v.getValue("OK").getValueB());
    }

    protected Date incDay(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }

    protected boolean containsAttr(DialogInfo i, String field, String attr) {
        FieldItem ii = i.getDialog().findFieldItem(field);
        assertNotNull(ii);
        return ii.isAttr(attr);
    }

    protected boolean buttContainsAttr(DialogInfo i, String field, String attr) {
        ButtonItem ii = DialogFormat
                .findE(i.getDialog().getButtonList(), field);
        assertNotNull(ii);
        return ii.isAttr(attr);
    }

    protected boolean leftbuttContainsAttr(DialogInfo i, String field,
            String attr) {
        ButtonItem ii = DialogFormat.findE(i.getDialog().getLeftButtonList(),
                field);
        assertNotNull(ii);
        return ii.isAttr(attr);
    }

    protected boolean listContainsAttr(DialogInfo i, String list, String col,
            String attr) {
        ListFormat f = i.getDialog().findList(list);
        assertNotNull(f);
        FieldItem ii = f.getColumn(col);
        assertNotNull(ii);
        return ii.isAttr(attr);
    }

    // ------------
    protected static final String HOTEL = "hotel";
    protected static final String HOTEL1 = "hotel1";

    protected static final String TESTINSTANCE = ISharedConsts.INSTANCETEST;

    protected AppInstanceId getI() {
        return iGetI.getInstance(TESTINSTANCE, "user");
    }

    protected OObjectId getH(String objectName) {
        return iGetI.getOObject(TESTINSTANCE, objectName, "user");
    }

    protected OObjectId getH1(String objectName) {
        return iGetI.getOObject(TESTINSTANCE, objectName, "modifuser");
    }

    protected ICustomSecurity getPersonSec() {
        CustomSecurity cust = new CustomSecurity();
        cust.setAttr(IConsts.INSTANCEID, TESTINSTANCE);
        ICustomSecurity cu = Holder.getPersonSecurityConvert().construct(cust);
        return cu;
    }
    
    protected void printD(Timestamp d) {
    	System.out.println("year " + d.getYear());
    	System.out.println("month " + d.getMonth());
    	System.out.println("day " + d.getDate());
    	System.out.println("hour " + d.getHours());
    	System.out.println("minutes " + d.getMinutes());
    	System.out.println("seconds " + d.getSeconds());
    	
    }

}
