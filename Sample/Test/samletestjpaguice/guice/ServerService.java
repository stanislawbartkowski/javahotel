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
package guice;

import javax.persistence.EntityManagerFactory;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.gwtmodel.mapcache.ICommonCacheFactory;
import com.gwtmodel.mapcache.SimpleMapCacheFactory;
import com.gwtmodel.testenhancer.ITestEnhancer;
import com.gwtmodel.testenhancer.notgae.TestEnhancer;
import com.jython.ui.ServerProperties;
import com.jython.ui.server.datastore.IDateLineOp;
import com.jython.ui.server.datastore.IDateRecordOp;
import com.jython.ui.server.datastore.IPersonOp;
import com.jython.ui.server.jpastoragekey.IStorageJpaRegistryFactory;
import com.jython.ui.server.jpastoragekey.StorageJpaRegistryFactory;
import com.jython.ui.server.jpatrans.ITransactionContext;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;
import com.jython.ui.server.jpatrans.JpaTransactionContext;
import com.jythonui.datastore.DateLineOp;
import com.jythonui.datastore.DateRecordOp;
import com.jythonui.datastore.EntityManagerFactoryProvider;
import com.jythonui.datastore.PersonOp;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.defa.SecurityNullConvert;
import com.jythonui.server.defa.StorageRealmRegistryFactory;
import com.jythonui.server.guice.JythonServerService.JythonServiceModule;
import com.jythonui.server.registry.IStorageRegistryFactory;
import com.jythonui.server.security.ISecurityConvert;
import com.jythonui.server.semaphore.ISemaphore;
import com.jythonui.server.semaphore.impl.SemaphoreSynch;
import com.jythonui.server.storage.registry.IStorageRealmRegistry;

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
            bind(IDateRecordOp.class).to(DateRecordOp.class)
                    .in(Singleton.class);
            bind(IJythonUIServerProperties.class).to(ServerProperties.class)
                    .in(Singleton.class);
            bind(ITestEnhancer.class).to(TestEnhancer.class);
            bind(EntityManagerFactory.class).toProvider(
                    EntityManagerFactoryProvider.class).in(Singleton.class);
            bind(ICommonCacheFactory.class).to(SimpleMapCacheFactory.class).in(
                    Singleton.class);
            bind(ISecurityConvert.class).to(SecurityNullConvert.class).in(
                    Singleton.class);

            // common
            bind(IStorageJpaRegistryFactory.class).to(
                    StorageJpaRegistryFactory.class).in(Singleton.class);
            bind(IStorageRegistryFactory.class).to(
                    StorageRealmRegistryFactory.class).in(Singleton.class);
//            bind(ISemaphore.class).to(JpaSemaphore.class).in(Singleton.class);
//            bind(ISemaphore.class).to(SemaphoreRegistry.class).in(Singleton.class);
            bind(ISemaphore.class).to(SemaphoreSynch.class).in(Singleton.class);            

            requestStatic();
        }

        // common
        @Provides
        @Singleton
        IStorageRealmRegistry getStorageRealmRegistry(
                IStorageJpaRegistryFactory rFactory,
                ITransactionContextFactory iC) {
            return rFactory.construct(iC);
        }

        @Provides
        @Singleton
        ITransactionContextFactory getTransactionContextFactory(
                final EntityManagerFactory eFactory) {
            return new ITransactionContextFactory() {
                @Override
                public ITransactionContext construct() {
                    return new JpaTransactionContext(eFactory);
                }
            };
        }
        // -----

    }

}
