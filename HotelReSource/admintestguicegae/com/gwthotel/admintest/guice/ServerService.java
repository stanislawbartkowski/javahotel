/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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

import javax.mail.Session;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.gwthotel.admintest.suite.TestHelper;
import com.gwthotel.hotel.IClearHotel;
import com.gwthotel.hotel.IGetAutomPatterns;
import com.gwthotel.hotel.bill.ICustomerBills;
import com.gwthotel.hotel.customer.IHotelCustomers;
import com.gwthotel.hotel.guice.HotelCommonGuice.HotelServiceModule;
import com.gwthotel.hotel.mailing.IHotelMailList;
import com.gwthotel.hotel.payment.IPaymentBillOp;
import com.gwthotel.hotel.pricelist.IHotelPriceList;
import com.gwthotel.hotel.prices.IHotelPriceElem;
import com.gwthotel.hotel.reservation.IReservationForm;
import com.gwthotel.hotel.reservationop.IReservationOp;
import com.gwthotel.hotel.rooms.IHotelRooms;
import com.gwthotel.hotel.service.gae.ClearHotelImpl;
import com.gwthotel.hotel.service.gae.HotelCustomerBillsImpl;
import com.gwthotel.hotel.service.gae.HotelCustomersImpl;
import com.gwthotel.hotel.service.gae.HotelMailImpl;
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
import com.gwtmodel.table.common.dateutil.ISetTestToday;
import com.gwtmodel.table.common.dateutil.SetTestTodayProvider;
import com.gwtmodel.testenhancer.ITestEnhancer;
import com.jython.serversecurity.IOObjectAdmin;
import com.jython.serversecurity.instance.IAppInstanceOObject;
import com.jython.ui.server.gae.noteimpl.NoteStoreImpl;
import com.jython.ui.server.gae.security.impl.ObjectAdminGae;
import com.jython.ui.server.gae.security.impl.ObjectInstanceImpl;
import com.jython.ui.server.gaestoragekey.BlobStorage;
import com.jython.ui.server.gaestoragekey.GaeStorageRegistry;
import com.jythonui.server.IConsts;
import com.jythonui.server.IConvertJythonTimestamp;
import com.jythonui.server.IGetConnection;
import com.jythonui.server.IGetEnvDefaultData;
import com.jythonui.server.IJythonRPCNotifier;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.crud.ICrudObjectGenSym;
import com.jythonui.server.defa.EmptyConnectionProvider;
import com.jythonui.server.defa.EmptyGetEnvDefaultData;
import com.jythonui.server.defa.EmptyRPCNotifier;
import com.jythonui.server.defa.JavaGetMailSessionProvider;
import com.jythonui.server.defa.JavaMailSessionProvider;
import com.jythonui.server.defa.StorageRealmRegistryFactory;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.jython.GAEConvert;
import com.jythonui.server.mail.INoteStorage;
import com.jythonui.server.objectgensymimpl.CrudObjectGenSym;
import com.jythonui.server.registry.IStorageRegistryFactory;
import com.jythonui.server.semaphore.ISemaphore;
import com.jythonui.server.semaphore.impl.SemaphoreRegistry;
import com.jythonui.server.storage.blob.IBlobHandler;
import com.jythonui.server.storage.gensym.ISymGenerator;
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
            bind(IOObjectAdmin.class).to(ObjectAdminGae.class).in(
                    Singleton.class);
            bind(IAppInstanceOObject.class).to(ObjectInstanceImpl.class).in(
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
            bind(IHotelMailList.class).to(HotelMailImpl.class).in(
                    Singleton.class);

            // common
            bind(IGetAutomPatterns.class).to(GetTestPatterns.class).in(
                    Singleton.class);
            bind(IStorageRegistryFactory.class).to(
                    StorageRealmRegistryFactory.class).in(Singleton.class);
            bind(ISemaphore.class).to(SemaphoreRegistry.class).in(
                    Singleton.class);
            bind(IGetConnection.class)
                    .toProvider(EmptyConnectionProvider.class).in(
                            Singleton.class);
            bind(IJythonRPCNotifier.class).to(EmptyRPCNotifier.class).in(
                    Singleton.class);
            bind(IBlobHandler.class).to(BlobStorage.class).in(Singleton.class);
            bind(IStorageRealmRegistry.class).to(GaeStorageRegistry.class).in(
                    Singleton.class);
            bind(ISetTestToday.class).toProvider(SetTestTodayProvider.class)
                    .in(Singleton.class);
            bind(Session.class).annotatedWith(Names.named(IConsts.SENDMAIL))
                    .toProvider(JavaMailSessionProvider.class)
                    .in(Singleton.class);
            bind(Session.class).annotatedWith(Names.named(IConsts.GETMAIL))
                    .toProvider(JavaGetMailSessionProvider.class)
                    .in(Singleton.class);
            bind(INoteStorage.class).to(NoteStoreImpl.class)
                    .in(Singleton.class);
            bind(IGetEnvDefaultData.class).to(EmptyGetEnvDefaultData.class).in(
                    Singleton.class);
			bind(IConvertJythonTimestamp.class).to(GAEConvert.class).in(
					Singleton.class);
            // --
            requestStaticInjection(TestHelper.class);
            requestStatic();
        }

        @Provides
        @Singleton
        ICrudObjectGenSym getCrudObjectGenSym(ISymGenerator iGen,
                @Named(ISharedConsts.JYTHONMESSSERVER) IGetLogMess lMess) {
            return new CrudObjectGenSym(iGen, lMess);
        }

    }

}
