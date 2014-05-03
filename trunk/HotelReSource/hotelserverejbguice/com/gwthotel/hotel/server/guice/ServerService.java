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
package com.gwthotel.hotel.server.guice;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.gwthotel.admin.ejblocator.IBeanLocator;
import com.gwthotel.admin.ejblocator.impl.EjbLocatorWildFly;
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
import com.gwthotel.hotel.services.IHotelServices;
import com.gwthotel.resource.GetResourceJNDI;
import com.gwtmodel.mapcache.ICommonCacheFactory;
import com.gwtmodel.mapcache.SimpleMapCacheFactory;
import com.jython.serversecurity.IOObjectAdmin;
import com.jython.serversecurity.instance.IAppInstanceOObject;
import com.jythonui.server.IGetConnection;
import com.jythonui.server.IJythonRPCNotifier;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.defa.EmptyConnectionProvider;
import com.jythonui.server.defa.IGetResourceJNDI;
import com.jythonui.server.defa.ServerPropertiesEnv;
import com.jythonui.server.defa.StorageRealmRegistryFactory;
import com.jythonui.server.registry.IStorageRegistryFactory;
import com.jythonui.server.resbundle.Mess;
import com.jythonui.server.semaphore.ISemaphore;
import com.jythonui.server.semaphore.impl.SemaphoreRegistry;
import com.jythonui.server.storage.blob.IBlobHandler;
import com.jythonui.server.storage.registry.IStorageRealmRegistry;

/**
 * @author hotel
 * 
 */
public class ServerService {

    public static class ServiceModule extends HotelServiceModule {
        @Override
        protected void configure() {
            // before configure hotel
            configureHotel();
            bind(IJythonUIServerProperties.class).to(ServerPropertiesEnv.class)
                    .in(Singleton.class);
            bind(ICommonCacheFactory.class).to(SimpleMapCacheFactory.class).in(
                    Singleton.class);
            bind(IStorageRegistryFactory.class).to(
                    StorageRealmRegistryFactory.class).in(Singleton.class);
            bind(Mess.class).in(Singleton.class);
            bind(IGetResourceJNDI.class).to(GetResourceJNDI.class).in(
                    Singleton.class);

            // common
            bind(IGetAutomPatterns.class).to(GetAutomPatterns.class).in(
                    Singleton.class);
            bind(ISemaphore.class).to(SemaphoreRegistry.class).in(
                    Singleton.class);
            bind(IGetConnection.class)
                    .toProvider(EmptyConnectionProvider.class).in(
                            Singleton.class);
            // bind(IBeanLocator.class).to(EjbLocatorGlassfish.class).in(
            // Singleton.class);

            bind(IBeanLocator.class).to(EjbLocatorWildFly.class).in(
                    Singleton.class);

            // common
            requestStatic();
        }

        @Provides
        @Singleton
        IReservationForm getReservationForm(IBeanLocator iBean) {
            return iBean.getReservationForm();
        }

        @Provides
        @Singleton
        IReservationOp getReservationOp(IBeanLocator iBean) {
            return iBean.getReservationOp();
        }

        @Provides
        @Singleton
        IClearHotel getClearHotel(IBeanLocator iBean) {
            return iBean.getClearHotel();
        }

        @Provides
        @Singleton
        ICustomerBills getCustomerBills(IBeanLocator iBean) {
            return iBean.getCustomerBills();
        }

        @Provides
        @Singleton
        IPaymentBillOp getPaymentBillOp(IBeanLocator iBean) {
            return iBean.getBillPaymentOp();
        }

        @Provides
        @Singleton
        IBlobHandler getBlobHandler(IBeanLocator iBean) {
            return iBean.getBlobHandler();
        }

        @Provides
        @Singleton
        IAppInstanceOObject getAppHotel(IBeanLocator iBean) {
            return iBean.getAppInstanceObject();
        }

        @Provides
        @Singleton
        IOObjectAdmin getHotelAdmin(IBeanLocator iBean) {
            return iBean.getObjectAdmin();
        }

        @Provides
        @Singleton
        IStorageRealmRegistry getRealmRegistry(IBeanLocator iBean) {
            return iBean.getStorageRealm();
        }

        @Provides
        @Singleton
        IHotelServices getHotelServices(IBeanLocator iBean) {
            return iBean.getHotelServices();
        }

        @Provides
        @Singleton
        IHotelPriceList getHotelPriceList(IBeanLocator iBean) {
            return iBean.getHotelPriceList();
        }

        @Provides
        @Singleton
        IHotelPriceElem getPriceElem(IBeanLocator iBean) {
            return iBean.getHotelPriceElem();
        }

        @Provides
        @Singleton
        IHotelRooms getHotelRooms(IBeanLocator iBean) {
            return iBean.getHotelRooms();
        }

        @Provides
        @Singleton
        IHotelCustomers getHotelCustomers(IBeanLocator iBean) {
            return iBean.getHotelCustomers();
        }

        @Provides
        @Singleton
        IJythonRPCNotifier getNotifier(final IClearHotel iSet) {
            return new IJythonRPCNotifier() {

                @Override
                public void hello(int what) {
                    iSet.setTestDataToday(null);
                }

            };

        }

    }

}
