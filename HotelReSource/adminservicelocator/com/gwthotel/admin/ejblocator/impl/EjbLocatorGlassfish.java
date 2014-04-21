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

import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.google.inject.Inject;
import com.gwthotel.admin.IAppInstanceHotel;
import com.gwthotel.admin.IHotelAdmin;
import com.gwthotel.admin.ejblocator.IBeanLocator;
import com.gwthotel.hotel.IClearHotel;
import com.gwthotel.hotel.bill.ICustomerBills;
import com.gwthotel.hotel.customer.IHotelCustomers;
import com.gwthotel.hotel.payment.IPaymentBillOp;
import com.gwthotel.hotel.pricelist.IHotelPriceList;
import com.gwthotel.hotel.prices.IHotelPriceElem;
import com.gwthotel.hotel.reservation.IReservationForm;
import com.gwthotel.hotel.reservationop.IReservationOp;
import com.gwthotel.hotel.rooms.IHotelRooms;
import com.gwthotel.hotel.services.IHotelServices;
import com.gwthotel.shared.IHotelConsts;
import com.jython.ui.shared.ISharedConsts;
import com.jython.ui.shared.UtilHelper;
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.storage.blob.IBlobHandler;
import com.jythonui.server.storage.registry.IStorageRealmRegistry;
import com.jythonui.server.storage.seq.ISequenceRealmGen;

public class EjbLocatorGlassfish extends UtilHelper implements IBeanLocator {

    private final IJythonUIServerProperties iServer;

    @Inject
    public EjbLocatorGlassfish(IJythonUIServerProperties iServer) {
        this.iServer = iServer;
    }

    private <T> T construct(String name) {
        try {
            Properties props = null;
            info("Search for EJB " + name);
            String eHost = iServer.getEJBHost();
            if (eHost != null) {
                // info(ILogMess.SEARCHEJBFORHOST, eHost);
                // WARNING: not IGetLogMess, not initialized
                info("EJB on host " + eHost);
                props = new Properties();
                props.setProperty("java.naming.factory.initial",
                        "com.sun.enterprise.naming.SerialInitContextFactory");
                props.setProperty("java.naming.factory.url.pkgs",
                        "com.sun.enterprise.naming");
                props.setProperty("java.naming.factory.state",
                        "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
                // props.setProperty("org.omg.CORBA.ORBInitialHost", "think");
                props.setProperty("org.omg.CORBA.ORBInitialHost", eHost);
                // props.setProperty("org.omg.CORBA.ORBInitialHost",
                props.setProperty("org.omg.CORBA.ORBInitialPort", "3700");
                // "localhost");
                if (iServer.getEJBPort() != null)
                    props.setProperty("org.omg.CORBA.ORBInitialPort",
                            iServer.getEJBPort());
            }
            InitialContext ic = new InitialContext(props);
            Object remoteObj = ic.lookup(name);
            T inter = (T) remoteObj;
            return inter;
        } catch (NamingException e) {
            errorLog("Cannot load service " + name, e);
        }
        return null;
    }

    @Override
    public IHotelAdmin getHotelAdmin() {
        return construct(IHotelConsts.HOTELADMINEJBJNDI);
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
    public IAppInstanceHotel getAppInstanceHotel() {
        return construct(IHotelConsts.HOTELADMININSTANCEEJBJNDI);
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

}
