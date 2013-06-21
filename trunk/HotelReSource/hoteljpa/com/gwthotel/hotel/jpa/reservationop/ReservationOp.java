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
package com.gwthotel.hotel.jpa.reservationop;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.gwthotel.admin.HotelId;
import com.gwthotel.hotel.jpa.JUtils;
import com.gwthotel.hotel.jpa.entities.EHotelReservation;
import com.gwthotel.hotel.jpa.entities.EHotelReservationDetail;
import com.gwthotel.hotel.reservation.ResStatus;
import com.gwthotel.hotel.reservationop.IReservationOp;
import com.gwthotel.hotel.reservationop.ResData;
import com.gwthotel.hotel.reservationop.ResQuery;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;
import com.jython.ui.server.jpatrans.JpaTransaction;

class ReservationOp implements IReservationOp {

    private final ITransactionContextFactory eFactory;

    ReservationOp(ITransactionContextFactory eFactory) {
        this.eFactory = eFactory;
    }

    private abstract class doTransaction extends JpaTransaction {

        protected final HotelId hotel;

        doTransaction(HotelId hotel) {
            super(eFactory);
            this.hotel = hotel;
        }
    }

    private class QueryCommand extends doTransaction {

        private final List<ResQuery> rQuery;
        private List<ResData> resList = new ArrayList<ResData>();

        QueryCommand(HotelId hotel, List<ResQuery> rQuery) {
            super(hotel);
            this.rQuery = rQuery;
        }

        @Override
        protected void dosth(EntityManager em) {
            for (ResQuery r : rQuery) {
                Query q = em.createNamedQuery("findReservation");
                q.setParameter(1, hotel.getId());
                q.setParameter(2, r.getRoomName());
                q.setParameter(3, r.getFromRes());
                q.setParameter(4, r.getToRes());
                List<EHotelReservationDetail> list = q.getResultList();
                for (EHotelReservationDetail d : list) {
                    ResData rese = new ResData();
                    rese.setRoomName(d.getRoom().getName());
                    rese.setResDate(d.getResDate());
                    rese.setResId(d.getReservation().getName());
                    resList.add(rese);
                }
            }
        }

    }

    @Override
    public List<ResData> queryReservation(HotelId hotel, List<ResQuery> rQuery) {
        QueryCommand q = new QueryCommand(hotel, rQuery);
        q.executeTran();
        return q.resList;
    }

    private class ChangeStatus extends doTransaction {

        private final String resName;
        private final ResStatus newStatus;

        ChangeStatus(HotelId hotel, String resName, ResStatus newStatus) {
            super(hotel);
            this.resName = resName;
            this.newStatus = newStatus;
        }

        @Override
        protected void dosth(EntityManager em) {
            EHotelReservation r = JUtils.getElem(em, hotel,
                    "findOneReservation", resName);
            r.setStatus(newStatus);
            em.persist(r);
        }
    }

    @Override
    public void changeStatus(HotelId hotel, String resName, ResStatus newStatus) {
        ChangeStatus comma = new ChangeStatus(hotel, resName, newStatus);
        comma.executeTran();
    }

}
