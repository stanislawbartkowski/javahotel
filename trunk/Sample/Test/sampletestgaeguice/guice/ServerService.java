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
package guice;

import com.google.inject.Singleton;
import com.gwtmodel.commoncache.CommonCacheFactory;
import com.gwtmodel.mapcache.ICommonCacheFactory;
import com.gwtmodel.table.common.dateutil.ISetTestToday;
import com.gwtmodel.table.common.dateutil.SetTestTodayProvider;
import com.gwtmodel.testenhancer.ITestEnhancer;
import com.jython.ui.ServerProperties;
import com.jython.ui.TestHelper;
import com.jython.ui.server.datastore.IDateLineOp;
import com.jython.ui.server.datastore.IDateRecordOp;
import com.jython.ui.server.datastore.IPersonOp;
import com.jython.ui.server.datastore.gae.DateLineOp;
import com.jython.ui.server.datastore.gae.DateRecordOp;
import com.jython.ui.server.datastore.gae.PersonOp;
import com.jython.ui.server.gaestoragekey.BlobStorage;
import com.jython.ui.server.gaestoragekey.GaeStorageRegistry;
import com.jythonui.server.IGetConnection;
import com.jythonui.server.IJythonRPCNotifier;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.defa.EmptyConnectionProvider;
import com.jythonui.server.defa.EmptyRPCNotifier;
import com.jythonui.server.defa.SecurityNullConvert;
import com.jythonui.server.guice.JythonServerService.JythonServiceModule;
import com.jythonui.server.security.ISecurityConvert;
import com.jythonui.server.semaphore.ISemaphore;
import com.jythonui.server.semaphore.impl.SemaphoreRegistry;
import com.jythonui.server.storage.blob.IBlobHandler;
import com.jythonui.server.storage.registry.IStorageRealmRegistry;
import com.table.testenhancer.gae.LocalDataStoreTestEnvironment;

/**
 * @author hotel
 * 
 */
public class ServerService {

    public static class ServiceModule extends JythonServiceModule {
        @Override
        protected void configure() {
            configureJythonUi();
            bind(IPersonOp.class).to(PersonOp.class).in(Singleton.class);
            bind(IDateLineOp.class).to(DateLineOp.class).in(Singleton.class);
            bind(IJythonUIServerProperties.class).to(ServerProperties.class)
                    .in(Singleton.class);
            bind(ITestEnhancer.class).to(LocalDataStoreTestEnvironment.class);
            bind(ICommonCacheFactory.class).to(CommonCacheFactory.class).in(
                    Singleton.class);
            bind(ISecurityConvert.class).to(SecurityNullConvert.class).in(
                    Singleton.class);
            // common
            // bind(IStorageRegistryFactory.class).to(
            // StorageRealmRegistryFactory.class).in(Singleton.class);
            bind(IBlobHandler.class).to(BlobStorage.class).in(Singleton.class);
            bind(IStorageRealmRegistry.class).to(GaeStorageRegistry.class).in(
                    Singleton.class);
            bind(IDateRecordOp.class).to(DateRecordOp.class)
                    .in(Singleton.class);
            bind(ISemaphore.class).to(SemaphoreRegistry.class).in(
                    Singleton.class);
            bind(IGetConnection.class)
                    .toProvider(EmptyConnectionProvider.class).in(
                            Singleton.class);
            bind(ISetTestToday.class).toProvider(SetTestTodayProvider.class)
                    .in(Singleton.class);
            bind(IJythonRPCNotifier.class).to(EmptyRPCNotifier.class).in(
                    Singleton.class);
            requestStatic();
            requestStaticInjection(TestHelper.class);
        }

    }

}
