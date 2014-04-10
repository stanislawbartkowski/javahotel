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
package com.jythonui.server.holder;

import javax.inject.Inject;
import javax.inject.Named;

import com.jythonui.server.IConsts;
import com.jythonui.server.IDefaultData;
import com.jythonui.server.IGetConnection;
import com.jythonui.server.IJythonClientRes;
import com.jythonui.server.IJythonUIServer;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.IXMLToMap;
import com.jythonui.server.dict.IGetLocalizedDict;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.registry.IStorageRegistryFactory;
import com.jythonui.server.resbundle.IAppMess;
import com.jythonui.server.security.ISecurity;
import com.jythonui.server.security.ISecurityConvert;
import com.jythonui.server.xml.IXMLTransformer;
import com.jythonui.shared.RequestContext;

public class Holder {

    @Inject
    private static IJythonUIServer iServer;

    @Inject
    private static IJythonClientRes iClient;

    @Inject
    private static ISecurity iSec;

    @Inject
    private static IXMLTransformer iXml;

    @Inject
    private static IGetConnection getConnection;

    @Inject
    private static IJythonUIServerProperties iJython;

    @Inject
    private static IStorageRegistryFactory regFactory;

    private static boolean auth = false;

    @Inject
    @Named(IConsts.APPMESS)
    private static IAppMess appMess;

    @Inject
    private static ISecurityConvert iConvert;

    @Inject
    private static IXMLToMap xmlMap;

    @Inject
    @Named(IConsts.COUNTRIESDICT)
    private static IGetLocalizedDict iListC;

    @Inject
    @Named(IConsts.TITLESDICT)
    private static IGetLocalizedDict iListT;

    @Inject
    @Named(IConsts.IDTYPEDICT)
    private static IGetLocalizedDict iListI;

    @Inject
    @Named(IConsts.PAYMENTDICT)
    private static IGetLocalizedDict iListP;

    @Inject
    private static IDefaultData dData;

    private static final ThreadLocal<RequestContext> locale = new ThreadLocal<RequestContext>();

    public static boolean isAuth() {
        return auth;
    }

    public static void setAuth(boolean pauth) {
        auth = pauth;
    }

    public static IJythonUIServer getiServer() {
        return iServer;
    }

    public static IJythonClientRes getiClient() {
        return iClient;
    }

    public static ISecurity getiSec() {
        return iSec;
    }

    public static void setContext(RequestContext req) {
        locale.set(req);
    }

    public static RequestContext getRequest() {
        return locale.get();
    }

    public static IAppMess getAppMess() {
        return appMess;
    }

    public static ISecurityConvert getSecurityConvert() {
        return iConvert;
    }

    public static IGetLogMess getM() {
        return SHolder.getM();
    }

    public static IXMLTransformer getXMLTransformer() {
        return iXml;
    }

    public static IGetConnection getJDBCConnection() {
        return getConnection;
    }

    public static IXMLToMap getMapXML() {
        return xmlMap;
    }

    public static IGetLocalizedDict getListOfCountries() {
        return iListC;
    }

    public static IGetLocalizedDict getListOfTitles() {
        return iListT;
    }

    public static IGetLocalizedDict getListOfIdTypes() {
        return iListI;
    }

    public static IGetLocalizedDict getListOfPayment() {
        return iListP;
    }

    public static IDefaultData getDefaultData() {
        return dData;
    }

    public static IJythonUIServerProperties getIJython() {
        return iJython;
    }

    public static IStorageRegistryFactory getRegFactory() {
        return regFactory;
    }

}
