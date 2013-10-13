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
import com.gwthotel.admin.IAppInstanceHotel;
import com.gwthotel.admin.IHotelAdmin;
import com.gwthotel.admin.gae.HotelAdminGae;
import com.gwthotel.admin.gae.HotelInstanceImpl;
import com.gwthotel.hotel.IClearHotel;
import com.gwthotel.hotel.IGetAutomPatterns;
import com.gwthotel.hotel.bill.ICustomerBills;
import com.gwthotel.hotel.customer.IHotelCustomers;
import com.gwthotel.hotel.guice.HotelCommonGuice.HotelServiceModule;
import com.gwthotel.hotel.payment.IPaymentBillOp;
import com.gwthotel.hotel.pricelist.IHotelPriceList;
import com.gwthotel.hotel.prices.IHotelPriceElem;
import com.gwthotel.hotel.reservation.IReservationForm;
import com.gwthotel.hotel.reservationop.IReservationOp;
import com.gwthotel.hotel.rooms.IHotelRooms;
import com.gwthotel.hotel.service.gae.ClearHotelImpl;
import com.gwthotel.hotel.service.gae.HotelCustomerBillsImpl;
import com.gwthotel.hotel.service.gae.HotelCustomersImpl;
import com.gwthotel.hotel.service.gae.HotelPriceElemImpl;
import com.gwthotel.hotel.service.gae.HotelPriceListImpl;
import com.gwthotel.hotel.service.gae.HotelReservationImpl;
import com.gwthotel.hotel.service.gae.HotelRoomsImpl;
import com.gwthotel.hotel.service.gae.HotelServiceImpl;
import com.gwthotel.hotel.service.gae.PaymentBillImpl;
import com.gwthotel.hotel.service.gae.ReservationOpImpl;
import com.gwthotel.hotel.services.IHotelServices;
import com.gwtmodel.mapcache.ICommonCacheFactory;
import com.gwtmodel.mapcache.SimpleMapCacheFactory;
import com.gwtmodel.testenhancer.ITestEnhancer;
import com.jython.ui.server.gaestoragekey.StorageRegistryFactory;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.defa.StorageRealmRegistryFactory;
import com.jythonui.server.registry.IStorageRegistryFactory;
import com.jythonui.server.semaphore.ISemaphore;
import com.jythonui.server.semaphore.impl.SemaphoreRegistry;
import com.jythonui.server.storage.registry.IStorageRealmRegistry;
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
            bind(IAppInstanceHotel.class).to(HotelInstanceImpl.class).in(
                    Singleton.class);

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
            bind(ICustomerBills.class).to(HotelCustomerBillsImpl.class).in(
                    Singleton.class);
            bind(IPaymentBillOp.class).to(PaymentBillImpl.class).in(
                    Singleton.class);
            bind(IReservationForm.class).to(HotelReservationImpl.class).in(
                    Singleton.class);
            bind(IReservationOp.class).to(ReservationOpImpl.class).in(
                    Singleton.class);
            bind(IClearHotel.class).to(ClearHotelImpl.class)
                    .in(Singleton.class);

            // common
            bind(IGetAutomPatterns.class).to(GetTestPatterns.class).in(
                    Singleton.class);
            bind(IStorageRegistryFactory.class).to(
                    StorageRealmRegistryFactory.class).in(Singleton.class);
            bind(IStorageRealmRegistry.class).toProvider(
                    StorageRegistryFactory.class).in(Singleton.class);
            bind(ISemaphore.class).to(SemaphoreRegistry.class).in(
                    Singleton.class);
            // --

            requestStatic();
        }
    }

}
