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
package com.gwthotel.hotel.server.guice;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.gwthotel.admin.IAppInstanceHotel;
import com.gwthotel.admin.IHotelAdmin;
import com.gwthotel.admin.ejblocator.AdminEjbLocator;
import com.gwthotel.admin.ejblocator.HotelAdminProvider;
import com.gwthotel.admin.ejblocator.HotelAppInstanceProvider;
import com.gwthotel.admin.ejblocator.HotelCustomersProvider;
import com.gwthotel.admin.ejblocator.HotelPriceElemProvider;
import com.gwthotel.admin.ejblocator.HotelPriceListProvider;
import com.gwthotel.admin.ejblocator.HotelRoomsProvider;
import com.gwthotel.admin.ejblocator.HotelServicesProvider;
import com.gwthotel.admin.ejblocator.StorageRealmProvider;
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
import com.gwthotel.hotel.server.autompatt.GetAutomPatterns;
import com.gwthotel.hotel.server.service.H;
import com.gwthotel.hotel.services.IHotelServices;
import com.gwthotel.resource.GetResourceJNDI;
import com.gwtmodel.mapcache.ICommonCacheFactory;
import com.gwtmodel.mapcache.SimpleMapCacheFactory;
import com.jythonui.server.IGetConnection;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.defa.EmptyConnectionProvider;
import com.jythonui.server.defa.IGetResourceJNDI;
import com.jythonui.server.defa.ServerPropertiesEnv;
import com.jythonui.server.defa.StorageRealmRegistryFactory;
import com.jythonui.server.registry.IStorageRegistryFactory;
import com.jythonui.server.resbundle.Mess;
import com.jythonui.server.semaphore.ISemaphore;
import com.jythonui.server.semaphore.impl.SemaphoreRegistry;
import com.jythonui.server.storage.registry.IStorageRealmRegistry;

/**
 * @author hotel
 * 
 */
public class ServerService {

    public static class ServiceModule extends HotelServiceModule {
        @Override
        protected void configure() {
            configureHotel();
            bind(IJythonUIServerProperties.class).to(ServerPropertiesEnv.class)
                    .in(Singleton.class);
            bind(ICommonCacheFactory.class).to(SimpleMapCacheFactory.class).in(
                    Singleton.class);
            bind(IStorageRegistryFactory.class).to(
                    StorageRealmRegistryFactory.class).in(Singleton.class);
            bind(IHotelAdmin.class).toProvider(HotelAdminProvider.class).in(
                    Singleton.class);
            bind(IHotelServices.class).toProvider(HotelServicesProvider.class)
                    .in(Singleton.class);
            bind(Mess.class).in(Singleton.class);
            bind(IGetResourceJNDI.class).to(GetResourceJNDI.class).in(
                    Singleton.class);
            bind(IStorageRealmRegistry.class).toProvider(
                    StorageRealmProvider.class).in(Singleton.class);
            bind(IHotelPriceList.class)
                    .toProvider(HotelPriceListProvider.class).in(
                            Singleton.class);
            bind(IHotelPriceElem.class)
                    .toProvider(HotelPriceElemProvider.class).in(
                            Singleton.class);
            bind(IHotelRooms.class).toProvider(HotelRoomsProvider.class).in(
                    Singleton.class);
            bind(IHotelCustomers.class)
                    .toProvider(HotelCustomersProvider.class).in(
                            Singleton.class);

            // common
            bind(IAppInstanceHotel.class).toProvider(
                    HotelAppInstanceProvider.class).in(Singleton.class);
            bind(IGetAutomPatterns.class).to(GetAutomPatterns.class).in(
                    Singleton.class);
            bind(ISemaphore.class).to(SemaphoreRegistry.class).in(
                    Singleton.class);
            bind(IGetConnection.class)
                    .toProvider(EmptyConnectionProvider.class).in(
                            Singleton.class);

            // common
            requestStatic();
            requestStaticInjection(H.class);
        }

        @Provides
        IReservationForm getReservationForm() {
            return AdminEjbLocator.getReservationForm();
        }

        @Provides
        IReservationOp getReservationOp() {
            return AdminEjbLocator.getReservationOp();
        }

        @Provides
        IClearHotel getClearHotel() {
            return AdminEjbLocator.getClearHotel();
        }

        @Provides
        ICustomerBills getCustomerBills() {
            return AdminEjbLocator.getCustomerBills();
        }

        @Provides
        IPaymentBillOp getPaymentBillOp() {
            return AdminEjbLocator.getBillPaymentOp();
        }

    }

}
