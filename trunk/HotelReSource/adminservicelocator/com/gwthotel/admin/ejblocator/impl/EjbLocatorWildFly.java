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
package com.gwthotel.admin.ejblocator.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.google.inject.Inject;
import com.gwthotel.admin.ejblocator.IBeanLocator;
import com.gwthotel.hotel.IClearHotel;
import com.gwthotel.hotel.bill.ICustomerBills;
import com.gwthotel.hotel.customer.IHotelCustomers;
import com.gwthotel.hotel.mailing.IHotelMailList;
import com.gwthotel.hotel.payment.IPaymentBillOp;
import com.gwthotel.hotel.pricelist.IHotelPriceList;
import com.gwthotel.hotel.prices.IHotelPriceElem;
import com.gwthotel.hotel.reservation.IReservationForm;
import com.gwthotel.hotel.reservationop.IReservationOp;
import com.gwthotel.hotel.rooms.IHotelRooms;
import com.gwthotel.hotel.services.IHotelServices;
import com.gwthotel.shared.IHotelConsts;
import com.jython.serversecurity.IOObjectAdmin;
import com.jython.serversecurity.instance.IAppInstanceOObject;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.mail.INoteStorage;
import com.jythonui.server.storage.blob.IBlobHandler;
import com.jythonui.server.storage.registry.IStorageRealmRegistry;
import com.jythonui.server.storage.seq.ISequenceRealmGen;

public class EjbLocatorWildFly extends UtilHelper implements IBeanLocator {

    private final IJythonUIServerProperties iServer;

    private static final String APPNAME = "ejb:/HotelEJBWild";

    private static final Map<String, String> jndiM = new HashMap<String, String>();

    @Inject
    public EjbLocatorWildFly(IJythonUIServerProperties iServer) {
        this.iServer = iServer;
        jndiM.put(ISharedConsts.COMMONAPPINSTANCEJNDI,
                "AppInstanceOObjectEJB!com.jython.serversecurity.instance.IAppInstanceOObject");
        jndiM.put(ISharedConsts.COMMONOBJECTADMINJNDI,
                "OObjectAdminEJB!com.jython.serversecurity.IOObjectAdmin");
        jndiM.put(
                ISharedConsts.COMMONREGISTRYBEANJNDI,
                "StorageJpaRegistryEJB!com.jythonui.server.storage.registry.IStorageRealmRegistry");
        jndiM.put(ISharedConsts.COMMONBEANBLOBJNDI,
                "StorageBlobRegistryEJB!com.jythonui.server.storage.blob.IBlobHandler");
        jndiM.put(ISharedConsts.COMMONNOTESTORAGEJNDI,
                "NoteStorageEJB!com.jythonui.server.mail.INoteStorage");
        jndiM.put(IHotelConsts.HOTELSERVICESJNDI,
                "HotelServicesEJB!com.gwthotel.hotel.services.IHotelServices");
        jndiM.put(IHotelConsts.HOTELPRICELISTJNDI,
                "HotelPriceListEJB!com.gwthotel.hotel.pricelist.IHotelPriceList");
        jndiM.put(IHotelConsts.HOTELPRICEELEMJNDI,
                "HotelPriceElemEJB!com.gwthotel.hotel.prices.IHotelPriceElem");
        jndiM.put(IHotelConsts.HOTELROOMSJNDI,
                "HotelRoomsEJB!com.gwthotel.hotel.rooms.IHotelRooms");
        jndiM.put(IHotelConsts.HOTELCUSTOMERSJNDI,
                "HotelCustomersEJB!com.gwthotel.hotel.customer.IHotelCustomers");
        jndiM.put(ISharedConsts.COMMONSEQGENJNDI,
                "SequenceGenRealmEJB!com.jythonui.server.storage.seq.ISequenceRealmGen");
        jndiM.put(IHotelConsts.HOTELRESERVATIONJNDI,
                "HotelReservationsEJB!com.gwthotel.hotel.reservation.IReservationForm");
        jndiM.put(IHotelConsts.HOTELRESERVATIONOPJNDI,
                "HotelReservationOpEJB!com.gwthotel.hotel.reservationop.IReservationOp");
        jndiM.put(IHotelConsts.HOTELCLEAROPJNDI,
                "HotelClearOpEJB!com.gwthotel.hotel.IClearHotel");
        jndiM.put(IHotelConsts.HOTELBILLJNDI,
                "CustomerBillEJB!com.gwthotel.hotel.bill.ICustomerBills");
        jndiM.put(IHotelConsts.HOTELPAYMENTOPJNDI,
                "BillPaymentOpEJB!com.gwthotel.hotel.payment.IPaymentBillOp");
        jndiM.put(IHotelConsts.HOTELMAILJNDI,
                "HotelMailingEJB!com.gwthotel.hotel.mailing.IHotelMailList");
    }

    private <T> T construct(String bName) {
        String name = APPNAME + "/" + jndiM.get(bName);
        try {
            Properties props = null;
            traceLog("Search for EJB " + bName);
            String eHost = iServer.getEJBHost();
            // eHost = "think";
            if (eHost != null) {
                info("EJB on host " + eHost);
                props = new Properties();
                props.put(Context.INITIAL_CONTEXT_FACTORY,
                        "org.jboss.naming.remote.client.InitialContextFactory");
                props.put(Context.URL_PKG_PREFIXES,
                        "org.jboss.ejb.client.naming");
                props.put(Context.PROVIDER_URL, "XXXXXX");
            }
            InitialContext ic = new InitialContext(props);
            Object remoteObj = ic.lookup(name);
            T inter = (T) remoteObj;
            traceLog("OK " + bName + " found.");
            ic.close();
            return inter;
        } catch (NamingException e) {
            errorLog("Cannot load service " + name, e);
        }
        return null;
    }

    @Override
    public INoteStorage getNoteStorage() {
        return construct(ISharedConsts.COMMONNOTESTORAGEJNDI);
    }

    @Override
    public IOObjectAdmin getObjectAdmin() {
        return construct(ISharedConsts.COMMONOBJECTADMINJNDI);
    }

    @Override
    public IAppInstanceOObject getAppInstanceObject() {
        return construct(ISharedConsts.COMMONAPPINSTANCEJNDI);
    }

    @Override
    public IStorageRealmRegistry getStorageRealm() {
        return construct(ISharedConsts.COMMONREGISTRYBEANJNDI);
    }

    @Override
    public IBlobHandler getBlobHandler() {
        return construct(ISharedConsts.COMMONBEANBLOBJNDI);
    }

    @Override
    public IHotelServices getHotelServices() {
        return construct(IHotelConsts.HOTELSERVICESJNDI);
    }

    @Override
    public IHotelPriceList getHotelPriceList() {
        return construct(IHotelConsts.HOTELPRICELISTJNDI);
    }

    @Override
    public IHotelPriceElem getHotelPriceElem() {
        jndiM.put(IHotelConsts.HOTELBILLJNDI,
                "CustomerBillEJB!com.gwthotel.hotel.bill.ICustomerBills");

        return construct(IHotelConsts.HOTELPRICEELEMJNDI);
    }

    @Override
    public IHotelRooms getHotelRooms() {
        return construct(IHotelConsts.HOTELROOMSJNDI);
    }

    @Override
    public IHotelCustomers getHotelCustomers() {
        return construct(IHotelConsts.HOTELCUSTOMERSJNDI);
    }

    @Override
    public ISequenceRealmGen getSequenceRealmGen() {
        return construct(ISharedConsts.COMMONSEQGENJNDI);
    }

    @Override
    public IReservationForm getReservationForm() {
        return construct(IHotelConsts.HOTELRESERVATIONJNDI);
    }

    @Override
    public IReservationOp getReservationOp() {
        return construct(IHotelConsts.HOTELRESERVATIONOPJNDI);
    }

    @Override
    public IClearHotel getClearHotel() {
        return construct(IHotelConsts.HOTELCLEAROPJNDI);
    }

    @Override
    public ICustomerBills getCustomerBills() {
        return construct(IHotelConsts.HOTELBILLJNDI);
    }

    @Override
    public IPaymentBillOp getBillPaymentOp() {
        return construct(IHotelConsts.HOTELPAYMENTOPJNDI);
    }

    @Override
    public IHotelMailList getHotelMail() {
        return construct(IHotelConsts.HOTELMAILJNDI);
    }

}