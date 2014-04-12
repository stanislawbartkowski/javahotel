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
package com.gwthotel.admin.ejblocator;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.google.inject.Inject;
import com.gwthotel.admin.IAppInstanceHotel;
import com.gwthotel.admin.IHotelAdmin;
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
import com.jythonui.server.IJythonUIServerProperties;
import com.jythonui.server.storage.blob.IBlobHandler;
import com.jythonui.server.storage.registry.IStorageRealmRegistry;
import com.jythonui.server.storage.seq.ISequenceRealmGen;
import com.jythonui.shared.JythonUIFatal;

public class AdminEjbLocator {

    private AdminEjbLocator() {

    }

    static final private Logger log = Logger.getLogger(AdminEjbLocator.class
            .getName());

    static private void error(String mess, Throwable e) {
        log.log(Level.SEVERE, mess);
        throw new JythonUIFatal(mess, e);
    }

    @Inject
    private static IJythonUIServerProperties iServer;

    private static <T> T construct(String name) {
        try {
            Properties props = null;
            if (iServer.getEJBHost() != null) {
                props = new Properties();
                props.setProperty("java.naming.factory.initial",
                        "com.sun.enterprise.naming.SerialInitContextFactory");
                props.setProperty("java.naming.factory.url.pkgs",
                        "com.sun.enterprise.naming");
                props.setProperty("java.naming.factory.state",
                        "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
                // props.setProperty("org.omg.CORBA.ORBInitialHost", "think");
                props.setProperty("org.omg.CORBA.ORBInitialHost",
                        iServer.getEJBHost());
                // props.setProperty("org.omg.CORBA.ORBInitialHost",
                // props.setProperty("org.omg.CORBA.ORBInitialPort", "3700");
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
            error("Cannot load service " + name, e);
        }
        return null;
    }

    static IHotelAdmin getHotelAdmin() {
        return construct(IHotelConsts.HOTELADMINEJBJNDI);
    }

    static IStorageRealmRegistry getStorageRealm() {
        return construct(ISharedConsts.COMMONREGISTRYBEANJNDI);
    }

    static public IBlobHandler getBlobHandler() {
        return construct(ISharedConsts.COMMONBEANBLOBJNDI);
    }

    static IHotelServices getHotelServices() {
        return construct(IHotelConsts.HOTELSERVICESJNDI);
    }

    static IHotelPriceList getHotelPriceList() {
        return construct(IHotelConsts.HOTELPRICELISTJNDI);
    }

    static IHotelPriceElem getHotelPriceElem() {
        return construct(IHotelConsts.HOTELPRICEELEMJNDI);
    }

    static IHotelRooms getHotelRooms() {
        return construct(IHotelConsts.HOTELROOMSJNDI);
    }

    static IHotelCustomers getHotelCustomers() {
        return construct(IHotelConsts.HOTELCUSTOMERSJNDI);
    }

    static IAppInstanceHotel getAppInstanceHotel() {
        return construct(IHotelConsts.HOTELADMININSTANCEEJBJNDI);
    }

    static ISequenceRealmGen getSequenceRealmGen() {
        return construct(ISharedConsts.COMMONSEQGENJNDI);
    }

    public static IReservationForm getReservationForm() {
        return construct(IHotelConsts.HOTELRESERVATIONJNDI);
    }

    public static IReservationOp getReservationOp() {
        return construct(IHotelConsts.HOTELRESERVATIONOPJNDI);
    }

    public static IClearHotel getClearHotel() {
        return construct(IHotelConsts.HOTELCLEAROPJNDI);
    }

    public static ICustomerBills getCustomerBills() {
        return construct(IHotelConsts.HOTELBILLJNDI);
    }

    public static IPaymentBillOp getBillPaymentOp() {
        return construct(IHotelConsts.HOTELPAYMENTOPJNDI);
    }

}
