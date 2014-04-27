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
package com.gwthotel.adminejb.guice;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.EntityManager;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.gwthotel.hotel.IClearHotel;
import com.gwthotel.hotel.IHotelObjectGenSym;
import com.gwthotel.hotel.bill.ICustomerBills;
import com.gwthotel.hotel.customer.IHotelCustomers;
import com.gwthotel.hotel.jpa.IHotelObjectGenSymFactory;
import com.gwthotel.hotel.jpa.bill.CustomerBillProvider;
import com.gwthotel.hotel.jpa.clearobjects.ClearObjects;
import com.gwthotel.hotel.jpa.customers.HotelCustomersProvider;
import com.gwthotel.hotel.jpa.payment.PaymentOpProvider;
import com.gwthotel.hotel.jpa.pricelist.HotelPriceListProvider;
import com.gwthotel.hotel.jpa.prices.HotelPriceElemProvider;
import com.gwthotel.hotel.jpa.reservation.HotelReservationProvider;
import com.gwthotel.hotel.jpa.reservationop.ReservationOpProvider;
import com.gwthotel.hotel.jpa.rooms.HotelRoomsProvider;
import com.gwthotel.hotel.jpa.services.HotelServicesProvider;
import com.gwthotel.hotel.objectgensymimpl.HotelObjectGenSym;
import com.gwthotel.hotel.payment.IPaymentBillOp;
import com.gwthotel.hotel.pricelist.IHotelPriceList;
import com.gwthotel.hotel.prices.IHotelPriceElem;
import com.gwthotel.hotel.reservation.IReservationForm;
import com.gwthotel.hotel.reservationop.IReservationOp;
import com.gwthotel.hotel.rooms.IHotelRooms;
import com.gwthotel.hotel.services.IHotelServices;
import com.gwthotel.mess.HotelMessProvider;
import com.gwthotel.shared.IHotelConsts;
import com.gwtmodel.table.common.dateutil.ISetTestToday;
import com.gwtmodel.table.common.dateutil.SetTestTodayProvider;
import com.jython.serversecurity.IOObjectAdmin;
import com.jython.serversecurity.instance.IAppInstanceOObject;
import com.jython.serversecurity.jpa.OObjectAdminInstance;
import com.jython.serversecurity.jpa.OObjectAdminJpa;
import com.jython.ui.server.jpastoragekey.BlobEntryJpaHandler;
import com.jython.ui.server.jpastoragekey.IStorageJpaRegistryFactory;
import com.jython.ui.server.jpastoragekey.StorageJpaRegistryFactory;
import com.jython.ui.server.jpatrans.ITransactionContext;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;
import com.jython.ui.server.jpatrans.JpaEmTransactionContext;
import com.jython.ui.server.jpatrans.JpaNonTransactionContext;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.logmess.MessProvider;
import com.jythonui.server.resource.IReadResourceFactory;
import com.jythonui.server.resource.ReadResourceFactory;
import com.jythonui.server.semaphore.ISemaphore;
import com.jythonui.server.semaphore.impl.SemaphoreRegistry;
import com.jythonui.server.storage.blob.IBlobHandler;
import com.jythonui.server.storage.gensym.ISymGenerator;
import com.jythonui.server.storage.gensym.ISymGeneratorFactory;
import com.jythonui.server.storage.gensymimpl.SymGeneratorFactory;
import com.jythonui.server.storage.registry.IStorageRealmRegistry;
import com.jythonui.server.storage.seq.ISequenceRealmGen;
import com.jythonui.server.storage.seq.ISequenceRealmGenFactory;
import com.jythonui.server.storage.seqimpl.SequenceRealmGenFactory;

public class GuiceService {

    public static class ServiceModule extends AbstractModule {
        @Override
        protected void configure() {

//            bind(IHotelAdmin.class).toProvider(HotelAdminProvider.class).in(
//                    Singleton.class);

//            bind(IAppInstanceHotel.class).toProvider(
//                    HotelAppInstanceProvider.class).in(Singleton.class);

            bind(IHotelServices.class).toProvider(HotelServicesProvider.class)
                    .in(Singleton.class);

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

            bind(IGetLogMess.class)
                    .annotatedWith(Names.named(IHotelConsts.MESSNAMED))
                    .toProvider(HotelMessProvider.class).in(Singleton.class);

            bind(IGetLogMess.class)
                    .annotatedWith(Names.named(ISharedConsts.JYTHONMESSSERVER))
                    .toProvider(MessProvider.class).in(Singleton.class);

            bind(IReservationForm.class).toProvider(
                    HotelReservationProvider.class).in(Singleton.class);

            bind(IReservationOp.class).toProvider(ReservationOpProvider.class)
                    .in(Singleton.class);
            bind(IClearHotel.class).to(ClearObjects.class).in(Singleton.class);
            bind(ICustomerBills.class).toProvider(CustomerBillProvider.class)
                    .in(Singleton.class);
            bind(IPaymentBillOp.class).toProvider(PaymentOpProvider.class).in(
                    Singleton.class);
            // common
            bind(IStorageJpaRegistryFactory.class).to(
                    StorageJpaRegistryFactory.class).in(Singleton.class);
            bind(ISequenceRealmGenFactory.class).to(
                    SequenceRealmGenFactory.class).in(Singleton.class);
            bind(IBlobHandler.class).to(BlobEntryJpaHandler.class).in(
                    Singleton.class);

            bind(ISymGeneratorFactory.class).to(SymGeneratorFactory.class).in(
                    Singleton.class);
            bind(ISemaphore.class).to(SemaphoreRegistry.class).in(
                    Singleton.class);
            bind(ISetTestToday.class).toProvider(SetTestTodayProvider.class)
                    .in(Singleton.class);
            bind(IReadResourceFactory.class).to(ReadResourceFactory.class).in(
                    Singleton.class);
            bind(IAppInstanceOObject.class).to(OObjectAdminInstance.class).in(
                    Singleton.class);
            bind(IOObjectAdmin.class).to(OObjectAdminJpa.class).in(
                    Singleton.class);
            // -----

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
        ITransactionContextFactory getTransactionContextFactory() {
            return new ITransactionContextFactory() {
                @Override
                public ITransactionContext construct() {
                    return new JpaNonTransactionContext(EMHolder.getEm());
                }
            };
        }

        // -----

        @Provides
        @Singleton
        IHotelObjectGenSymFactory getHotelObjectGenSymFactory(
                final ISequenceRealmGenFactory seqFactory,
                final ISymGeneratorFactory symFactory,
                final IStorageJpaRegistryFactory regFactory,
                final ISemaphore iSem,
                final @Named(IHotelConsts.MESSNAMED) IGetLogMess lMess) {
            return new IHotelObjectGenSymFactory() {

                @Override
                public IHotelObjectGenSym construct(final EntityManager em) {
                    ITransactionContextFactory tFactory = new ITransactionContextFactory() {

                        @Override
                        public ITransactionContext construct() {
                            return new JpaEmTransactionContext(em);
                        }
                    };
                    IStorageRealmRegistry iReg = regFactory.construct(tFactory);
                    ISequenceRealmGen iSeq = seqFactory.construct(iReg, iSem);
                    ISymGenerator iSym = symFactory.construct(iSeq);
                    return new HotelObjectGenSym(iSym, lMess);
                }

            };
        }

    }
}
