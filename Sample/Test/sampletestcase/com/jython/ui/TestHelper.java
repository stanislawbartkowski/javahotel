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
import static org.junit.Assert.assertTrue;
import guice.ServiceInjector;

import java.math.BigDecimal;
import java.util.Date;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;

import com.gwtmodel.table.common.dateutil.DateFormatUtil;
import com.gwtmodel.table.common.dateutil.ISetTestToday;
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
import com.jythonui.server.storage.blob.IBlobHandler;
import com.jythonui.server.storage.gensym.ISymGenerator;
import com.jythonui.server.storage.seq.ISequenceRealmGen;
import com.jythonui.server.xml.IXMLTransformer;
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

    protected final IPersonOp po;
    protected final IDateLineOp iOp;
    protected final IDateRecordOp dOp;

    protected final static String realmIni = "classpath:resources/shiro/shiro.ini";
    protected final static String derbyIni = "classpath:resources/shiro/shiroderby.ini";

    public TestHelper() {
        po = ServiceInjector.constructPersonOp();
        iOp = ServiceInjector.constructDateLineElem();
        dOp = ServiceInjector.getDateRecordOp();
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

}
