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
package com.gwthotel.hotel.jpa.clearobjects;

import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.IClearHotel;
import com.gwthotel.hotel.jpa.JUtils;
import com.gwthotel.hotel.jpa.entities.EBillPayment;
import com.gwthotel.hotel.jpa.entities.ECustomerBill;
import com.gwthotel.hotel.jpa.entities.EHotelCustomer;
import com.gwthotel.hotel.jpa.entities.EHotelMail;
import com.gwthotel.hotel.jpa.entities.EHotelPriceList;
import com.gwthotel.hotel.jpa.entities.EHotelReservation;
import com.gwthotel.hotel.jpa.entities.EHotelRoom;
import com.gwthotel.hotel.jpa.entities.EHotelServices;
import com.gwtmodel.table.common.dateutil.ISetTestToday;
import com.jython.jpautil.JpaUtils;
import com.jython.serversecurity.cache.OObjectId;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;
import com.jython.ui.server.jpatrans.JpaTransaction;

public class ClearObjects implements IClearHotel {

    private final ITransactionContextFactory eFactory;
    private final ISetTestToday iSetToday;

    @Inject
    public ClearObjects(ITransactionContextFactory eFactory,
            ISetTestToday iSetToday) {
        this.eFactory = eFactory;
        this.iSetToday = iSetToday;

    }

    private class RemoveObject extends JpaTransaction {

        private final OObjectId hotel;

        RemoveObject(OObjectId hotel) {
            super(eFactory);
            this.hotel = hotel;
        }

        @Override
        protected void dosth(EntityManager em) {
            JUtils.runQueryForHotels(em, hotel, "removeAllPayments");
            // Important:
            // The removal should be performed through list (not single DELETE
            // statement)
            // Hibernate does not remove embedded list automatically
            JUtils.removeList(em, hotel.getId(), "findAllBills");
            String[] remQuery = { "deleteAllHotelmails",
                    "deleteAllGuestsReservationFromHotel",
                    "deleteAllReservationDetails", "deleteAllReservations",
                    "deleteAllCustomers", "deleteAllRoomServices",
                    "deletePricesForHotel", "deleteAllRooms",
                    "deleteAllPriceLists", "deleteAllServices" };
            JUtils.runQueryForHotels(em, hotel, remQuery);
        }

    }

    @Override
    public void clearObjects(OObjectId hotel) {
        RemoveObject comma = new RemoveObject(hotel);
        comma.executeTran();
    }

    @Override
    public void setTestDataToday(Date d) {
        iSetToday.setToday(d);
    }

    private class CountHotelObject extends JpaTransaction {

        private final OObjectId hotel;
        private final Class cl;
        Long l;

        CountHotelObject(OObjectId hotel, Class cl) {
            super(eFactory);
            this.hotel = hotel;
            this.cl = cl;
        }

        @Override
        protected void dosth(EntityManager em) {
            String sql = "SELECT COUNT(x) FROM " + cl.getSimpleName()
                    + " x WHERE x.hotel = " + hotel.getId();
            l = (Long) em.createQuery(sql).getSingleResult();
        }
    }

    private class CountHotelObject1 extends JpaTransaction {

        private final OObjectId hotel;
        private final String st;
        Long l;

        CountHotelObject1(OObjectId hotel, String st) {
            super(eFactory);
            this.hotel = hotel;
            this.st = st;
        }

        @Override
        protected void dosth(EntityManager em) {
            Query q = JpaUtils.createObjectIdQuery(em, hotel, st);
            l = (Long) q.getSingleResult();
        }
    }

    @Override
    public long numberOf(OObjectId hotel, HotelObjects o) {
        Class cl = JUtils.getClass(o);
        if (o == HotelObjects.RESERVATION || o == HotelObjects.CUSTOMER
                || o == HotelObjects.PRICELIST || o == HotelObjects.ROOM
                || o == HotelObjects.SERVICE || o == HotelObjects.BILL) {
            CountHotelObject comm = new CountHotelObject(hotel, cl);
            comm.executeTran();
            return comm.l;
        }
        String st;
        if (o == HotelObjects.PAYMENTS)
            st = "countAllPayments";
        else if (o == HotelObjects.GUESTS)
            st = "countAllGuests";
        else
            st = "countAllReservationDetails";
        // payments
        CountHotelObject1 comm = new CountHotelObject1(hotel, st);
        comm.executeTran();
        return comm.l;
    }

}
