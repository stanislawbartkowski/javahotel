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
package com.gwthotel.adminejb.guice;

import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.gwthotel.admin.IHotelAdmin;
import com.gwthotel.admin.jpa.HotelAdminProvider;
import com.gwthotel.admin.jpa.TestHotelAdminProvider;
import com.gwthotel.mess.HotelMessProvider;
import com.gwthotel.shared.IHotelConsts;
import com.jython.ui.server.jpastoragekey.StorageJpaRegistryProvider;
import com.jython.ui.server.jpastoragekey.TestStorageJpaRegistryProvider;
import com.jython.ui.shared.ISharedConsts;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.storage.registry.IStorageRealmRegistry;

public class GuiceService {

    public static class ServiceModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(EntityManagerFactory.class)
                    .annotatedWith(
                            Names.named(ISharedConsts.STORAGEREGISTRYENTITYMANAGERFACTORY))
                    .toProvider(EntityManagerFactoryProvider.class)
                    .in(Singleton.class);
            bind(EntityManagerFactory.class)
                    .annotatedWith(
                            Names.named(ISharedConsts.TESTSTORAGEREGISTRYENTITYMANAGERFACTORY))
                    .toProvider(TestEntityManagerFactoryProvider.class)
                    .in(Singleton.class);
            bind(IStorageRealmRegistry.class).toProvider(
                    StorageJpaRegistryProvider.class).in(Singleton.class);
            bind(IStorageRealmRegistry.class)
                    .annotatedWith(
                            Names.named(ISharedConsts.TESTSTORAGEREGISTRY))
                    .toProvider(TestStorageJpaRegistryProvider.class)
                    .in(Singleton.class);

            bind(EntityManagerFactory.class).toProvider(
                    EntityManagerFactoryProvider.class).in(Singleton.class);
            bind(IHotelAdmin.class).toProvider(HotelAdminProvider.class).in(
                    Singleton.class);
            bind(IHotelAdmin.class)
                    .annotatedWith(Names.named(IHotelConsts.TESTHOTELADMIN))
                    .toProvider(TestHotelAdminProvider.class)
                    .in(Singleton.class);
            bind(EntityManagerFactory.class)
                    .annotatedWith(Names.named(IHotelConsts.TESTFACTORYMANAGER))
                    .toProvider(TestEntityManagerFactoryProvider.class)
                    .in(Singleton.class);

            bind(IGetLogMess.class)
                    .annotatedWith(Names.named(IHotelConsts.MESSNAMED))
                    .toProvider(HotelMessProvider.class).in(Singleton.class);
        }
    }
}
