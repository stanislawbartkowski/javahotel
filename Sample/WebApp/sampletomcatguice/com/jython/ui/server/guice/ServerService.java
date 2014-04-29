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
package com.jython.ui.server.guice;

import javax.persistence.EntityManagerFactory;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.gwtmodel.mapcache.ICommonCacheFactory;
import com.gwtmodel.mapcache.SimpleMapCacheFactory;
import com.jython.serversecurity.IOObjectAdmin;
import com.jython.serversecurity.instance.IAppInstanceOObject;
import com.jython.serversecurity.jpa.OObjectAdminInstance;
import com.jython.serversecurity.jpa.OObjectAdminJpa;
import com.jython.ui.server.Cached;
import com.jython.ui.server.datastore.IDateLineOp;
import com.jython.ui.server.datastore.IDateRecordOp;
import com.jython.ui.server.datastore.IPersonOp;
import com.jython.ui.server.jpastoragekey.BlobEntryJpaHandler;
import com.jython.ui.server.jpastoragekey.IStorageJpaRegistryFactory;
import com.jython.ui.server.jpastoragekey.StorageJpaRegistryFactory;
import com.jython.ui.server.jpatrans.ITransactionContext;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;
import com.jython.ui.server.jpatrans.JpaTransactionContext;
import com.jythonui.datastore.DateLineOp;
import com.jythonui.datastore.DateRecordOp;
import com.jythonui.datastore.EntityManagerFactoryProvider;
import com.jythonui.datastore.PersonOp;
import com.jythonui.server.IGetConnection;
import com.jythonui.server.IJythonRPCNotifier;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.defa.EmptyConnectionProvider;
import com.jythonui.server.defa.EmptyRPCNotifier;
import com.jythonui.server.defa.IGetResourceJNDI;
import com.jythonui.server.defa.IsCached;
import com.jythonui.server.defa.SecurityNullConvert;
import com.jythonui.server.defa.ServerPropertiesEnv;
import com.jythonui.server.defa.StorageRealmRegistryFactory;
import com.jythonui.server.guice.JythonServerService;
import com.jythonui.server.registry.IStorageRegistryFactory;
import com.jythonui.server.security.ISecurityConvert;
import com.jythonui.server.semaphore.ISemaphore;
import com.jythonui.server.semaphore.impl.SemaphoreSynch;
import com.jythonui.server.storage.blob.IBlobHandler;
import com.jythonui.server.storage.registry.IStorageRealmRegistry;

/**
 * @author hotel
 * 
 */
public class ServerService {

    public static class ServiceModule extends
            JythonServerService.JythonServiceModule {
        @Override
        protected void configure() {
            configureJythonUi();
            bind(IsCached.class).to(Cached.class).in(Singleton.class);
            bind(IPersonOp.class).to(PersonOp.class).in(Singleton.class);
            bind(IDateLineOp.class).to(DateLineOp.class).in(Singleton.class);
            bind(IDateRecordOp.class).to(DateRecordOp.class)
                    .in(Singleton.class);

            bind(IJythonUIServerProperties.class).to(ServerPropertiesEnv.class)
                    .in(Singleton.class);
            bind(ICommonCacheFactory.class).to(SimpleMapCacheFactory.class).in(
                    Singleton.class);
            bind(EntityManagerFactory.class).toProvider(
                    EntityManagerFactoryProvider.class).in(Singleton.class);
            bind(ISecurityConvert.class).to(SecurityNullConvert.class).in(
                    Singleton.class);
            bind(IGetResourceJNDI.class).to(GetResourceJNDI.class).in(
                    Singleton.class);
            bind(ISemaphore.class).to(SemaphoreSynch.class).in(Singleton.class);

            // common
            bind(IStorageJpaRegistryFactory.class).to(
                    StorageJpaRegistryFactory.class).in(Singleton.class);
            // bind(IStorageRegistryFactory.class).to(
            // StorageRealmRegistryFactory.class).in(Singleton.class);
            bind(IGetConnection.class)
                    .toProvider(EmptyConnectionProvider.class).in(
                            Singleton.class);
            bind(IBlobHandler.class).to(BlobEntryJpaHandler.class).in(
                    Singleton.class);
            bind(IJythonRPCNotifier.class).to(EmptyRPCNotifier.class).in(
                    Singleton.class);
            bind(IAppInstanceOObject.class).to(OObjectAdminInstance.class).in(
                    Singleton.class);
            bind(IOObjectAdmin.class).to(OObjectAdminJpa.class).in(
                    Singleton.class);
            // -----

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

    }

}
