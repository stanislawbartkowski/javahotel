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

import com.google.inject.Singleton;
import com.gwthotel.admin.IHotelAdmin;
import com.gwthotel.admin.gae.HotelAdminGae;
import com.gwthotel.hotel.customer.IHotelCustomers;
import com.gwthotel.hotel.guice.HotelCommonGuice.HotelServiceModule;
import com.gwthotel.hotel.pricelist.IHotelPriceList;
import com.gwthotel.hotel.prices.IHotelPriceElem;
import com.gwthotel.hotel.rooms.IHotelRooms;
import com.gwthotel.hotel.service.gae.HotelCustomersImpl;
import com.gwthotel.hotel.service.gae.HotelPriceElemImpl;
import com.gwthotel.hotel.service.gae.HotelPriceListImpl;
import com.gwthotel.hotel.service.gae.HotelRoomsImpl;
import com.gwthotel.hotel.service.gae.HotelServiceImpl;
import com.gwthotel.hotel.services.IHotelServices;
import com.gwtmodel.mapcache.ICommonCacheFactory;
import com.gwtmodel.mapcache.SimpleMapCacheFactory;
import com.gwtmodel.testenhancer.ITestEnhancer;
import com.jython.ui.server.gaestoragekey.StorageRegistryFactory;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.registry.IStorageRegistryFactory;
import com.table.testenhancer.gae.LocalDataStoreTestEnvironment;

/**
 * @author hotel
 * 
 */
public class ServerService {

    public static class ServiceModule extends HotelServiceModule {
        @Override
        protected void configure() {
            configureHotel();
            bind(IJythonUIServerProperties.class).to(ServerProperties.class)
                    .in(Singleton.class);
            bind(ITestEnhancer.class).to(LocalDataStoreTestEnvironment.class);
            bind(ICommonCacheFactory.class).to(SimpleMapCacheFactory.class).in(
                    Singleton.class);
            bind(IHotelAdmin.class).to(HotelAdminGae.class).in(Singleton.class);
            bind(IStorageRegistryFactory.class)
                    .to(StorageRegistryFactory.class).in(Singleton.class);
            bind(IHotelServices.class).to(HotelServiceImpl.class).in(
                    Singleton.class);
            bind(IHotelPriceList.class).to(HotelPriceListImpl.class).in(
                    Singleton.class);
            bind(IHotelPriceElem.class).to(HotelPriceElemImpl.class).in(
                    Singleton.class);
            bind(IHotelCustomers.class).to(HotelCustomersImpl.class).in(
                    Singleton.class);
            bind(IHotelRooms.class).to(HotelRoomsImpl.class)
                    .in(Singleton.class);

            requestStatic();
        }
    }

}
