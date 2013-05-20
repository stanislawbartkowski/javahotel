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
package com.gwthotel.admintest.guice;

import javax.persistence.EntityManagerFactory;

import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.gwthotel.admin.IGetHotelRoles;
import com.gwthotel.admin.IHotelAdmin;
import com.gwthotel.admin.jpa.HotelAdminProvider;
import com.gwthotel.admin.roles.GetHotelRoles;
import com.gwthotel.auth.SecurityConverter;
import com.gwthotel.hotel.jpa.HotelJpaServices;
import com.gwthotel.hotel.services.IHotelServices;
import com.gwthotel.mess.HotelMessProvider;
import com.gwthotel.shared.IHotelConsts;
import com.gwtmodel.mapcache.ICommonCacheFactory;
import com.gwtmodel.mapcache.SimpleMapCacheFactory;
import com.gwtmodel.testenhancer.ITestEnhancer;
import com.gwtmodel.testenhancer.notgae.TestEnhancer;
import com.jython.ui.server.jpastoragekey.StorageJpaRegistryProvider;
import com.jython.ui.shared.ISharedConsts;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.defa.StorageRealmRegistryFactory;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.guice.JythonServerService.JythonServiceModule;
import com.jythonui.server.registry.IStorageRegistryFactory;
import com.jythonui.server.security.ISecurityConvert;
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
            bind(IJythonUIServerProperties.class).to(ServerProperties.class)
                    .in(Singleton.class);
            bind(ITestEnhancer.class).to(TestEnhancer.class);
            bind(EntityManagerFactory.class).toProvider(
                    EntityManagerFactoryProvider.class).in(Singleton.class);
            bind(ICommonCacheFactory.class).to(SimpleMapCacheFactory.class).in(
                    Singleton.class);
            bind(IHotelAdmin.class).toProvider(HotelAdminProvider.class).in(
                    Singleton.class);
            bind(EntityManagerFactory.class)
                    .annotatedWith(
                            Names.named(ISharedConsts.STORAGEREGISTRYENTITYMANAGERFACTORY))
                    .toProvider(EntityManagerFactoryProvider.class)
                    .in(Singleton.class);
            bind(IStorageRealmRegistry.class).toProvider(
                    StorageJpaRegistryProvider.class).in(Singleton.class);
            bind(IStorageRegistryFactory.class).to(
                    StorageRealmRegistryFactory.class).in(Singleton.class);
            bind(IGetHotelRoles.class).to(GetHotelRoles.class).in(
                    Singleton.class);
            bind(IGetLogMess.class)
                    .annotatedWith(Names.named(IHotelConsts.MESSNAMED))
                    .toProvider(HotelMessProvider.class).in(Singleton.class);
            bind(ISecurityConvert.class).to(SecurityConverter.class).in(
                    Singleton.class);
            bind(IHotelServices.class).to(HotelJpaServices.class).in(Singleton.class);
            requestStatic();
        }
    }

}
