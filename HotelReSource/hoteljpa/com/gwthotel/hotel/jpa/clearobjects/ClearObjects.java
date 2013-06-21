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
package com.gwthotel.hotel.jpa.clearobjects;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.gwthotel.admin.HotelId;
import com.gwthotel.hotel.IClearHotel;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;
import com.jython.ui.server.jpatrans.JpaTransaction;

public class ClearObjects implements IClearHotel {

    private final ITransactionContextFactory eFactory;

    @Inject
    public ClearObjects(ITransactionContextFactory eFactory) {
        this.eFactory = eFactory;
    }

    private class RemoveObject extends JpaTransaction {

        private final HotelId hotel;

        RemoveObject(HotelId hotel) {
            super(eFactory);
            this.hotel = hotel;
        }

        @Override
        protected void dosth(EntityManager em) {
            String[] remQuery = {"deleteAllReservationsDetails","deleteAllReservations",
                    "deleteAllCustomers", "deleteAllRoomServices",
                    "deletePricesForHotel", "deleteAllRooms",
                    "deleteAllPriceLists", "deleteAllServices" };

            for (String r : remQuery) {
                Query q = em.createNamedQuery(r);
                q.setParameter(1, hotel.getId());
                q.executeUpdate();
            }
 
        }

    }

    @Override
    public void clearObjects(HotelId hotel) {
        RemoveObject comma = new RemoveObject(hotel);
        comma.executeTran();
    }

}
