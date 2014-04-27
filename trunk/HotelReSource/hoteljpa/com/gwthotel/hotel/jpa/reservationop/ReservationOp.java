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
package com.gwthotel.hotel.jpa.reservationop;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.gwthotel.hotel.ServiceType;
import com.gwthotel.hotel.bill.CustomerBill;
import com.gwthotel.hotel.jpa.JUtils;
import com.gwthotel.hotel.jpa.entities.ECustomerBill;
import com.gwthotel.hotel.jpa.entities.EHotelCustomer;
import com.gwthotel.hotel.jpa.entities.EHotelGuest;
import com.gwthotel.hotel.jpa.entities.EHotelReservation;
import com.gwthotel.hotel.jpa.entities.EHotelReservationDetail;
import com.gwthotel.hotel.jpa.entities.EHotelRoom;
import com.gwthotel.hotel.jpa.entities.EHotelRoomCustomer;
import com.gwthotel.hotel.reservation.AbstractResHotelGuest;
import com.gwthotel.hotel.reservation.ResStatus;
import com.gwthotel.hotel.reservation.ReservationPaymentDetail;
import com.gwthotel.hotel.reservationop.IReservationOp;
import com.gwthotel.hotel.reservationop.ResData;
import com.gwthotel.hotel.reservationop.ResQuery;
import com.gwthotel.hotel.stay.ResGuest;
import com.jython.serversecurity.OObjectId;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;
import com.jython.ui.server.jpatrans.JpaTransaction;

class ReservationOp implements IReservationOp {

    private final ITransactionContextFactory eFactory;

    ReservationOp(ITransactionContextFactory eFactory) {
        this.eFactory = eFactory;
    }

    private abstract class doTransaction extends JpaTransaction {

        protected final OObjectId hotel;

        doTransaction(OObjectId hotel) {
            super(eFactory);
            this.hotel = hotel;
        }
    }

    private abstract class doResTransaction extends JpaTransaction {

        protected final OObjectId hotel;
        protected final String resName;

        doResTransaction(OObjectId hotel, String resName) {
            super(eFactory);
            this.hotel = hotel;
            this.resName = resName;
        }

        protected EHotelReservation getRes(EntityManager em) {
            EHotelReservation r = JUtils.getElem(em, hotel,
                    "findOneReservation", resName);
            return r;
        }

        protected void toE(EntityManager em, EHotelRoomCustomer dest,
                AbstractResHotelGuest sou) {
            EHotelCustomer cu = JUtils.getElemE(em, hotel, "findOneCustomer",
                    sou.getGuestName());
            dest.setCustomer(cu);
            EHotelRoom room = JUtils.getElemE(em, hotel, "findOneRoom",
                    sou.getRoomName());
            dest.setRoom(room);
        }

        protected void toT(AbstractResHotelGuest dest, EHotelRoomCustomer sou) {
            dest.setGuestName(sou.getCustomer().getName());
            dest.setRoomName(sou.getRoom().getName());
        }
    }

    private class QueryCommand extends doTransaction {

        private final List<ResQuery> rQuery;
        private List<ResData> resList = new ArrayList<ResData>();

        QueryCommand(OObjectId hotel, List<ResQuery> rQuery) {
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
                @SuppressWarnings("unchecked")
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
    public List<ResData> queryReservation(OObjectId hotel, List<ResQuery> rQuery) {
        QueryCommand q = new QueryCommand(hotel, rQuery);
        q.executeTran();
        return q.resList;
    }

    private class ChangeStatus extends doResTransaction {

        private final ResStatus newStatus;

        ChangeStatus(OObjectId hotel, String resName, ResStatus newStatus) {
            super(hotel, resName);
            this.newStatus = newStatus;
        }

        @Override
        protected void dosth(EntityManager em) {
            EHotelReservation r = getRes(em);
            r.setStatus(newStatus);
            em.persist(r);
        }
    }

    @Override
    public void changeStatus(OObjectId hotel, String resName, ResStatus newStatus) {
        ChangeStatus comma = new ChangeStatus(hotel, resName, newStatus);
        comma.executeTran();
    }

    private class SetReservGuest extends doResTransaction {

        private final List<ResGuest> gList;

        SetReservGuest(OObjectId hotel, String resName, List<ResGuest> gList) {
            super(hotel, resName);
            this.gList = gList;
        }

        @Override
        protected void dosth(EntityManager em) {
            EHotelReservation e = getRes(em);
            Query q = em.createNamedQuery("deleteGuestsFromReservation");
            q.setParameter(1, e);
            q.executeUpdate();
            for (ResGuest r : gList) {
                EHotelGuest g = new EHotelGuest();
                toE(em, g, r);
                g.setReservation(e);
                em.persist(g);
            }
        }

    }

    @Override
    public void setResGuestList(OObjectId hotel, String resName,
            List<ResGuest> gList) {
        SetReservGuest comma = new SetReservGuest(hotel, resName, gList);
        comma.executeTran();
    }

    private class GetResGuest extends doResTransaction {

        private final List<ResGuest> outList = new ArrayList<ResGuest>();

        GetResGuest(OObjectId hotel, String resName) {
            super(hotel, resName);
        }

        @Override
        protected void dosth(EntityManager em) {
            EHotelReservation e = getRes(em);
            Query q = em.createNamedQuery("findGuestForReservation");
            q.setParameter(1, e);
            @SuppressWarnings("unchecked")
            List<EHotelGuest> resList = q.getResultList();
            for (EHotelGuest g : resList) {
                ResGuest r = new ResGuest();
                toT(r, g);
                outList.add(r);
            }
        }

    }

    @Override
    public List<ResGuest> getResGuestList(OObjectId hotel, String resName) {
        GetResGuest comma = new GetResGuest(hotel, resName);
        comma.executeTran();
        return comma.outList;
    }

    private class AddResPayment extends doResTransaction {

        private final ReservationPaymentDetail addP;

        AddResPayment(OObjectId hotel, String resName,
                ReservationPaymentDetail add) {
            super(hotel, resName);
            this.addP = add;
        }

        @Override
        protected void dosth(EntityManager em) {
            EHotelReservation r = getRes(em);
            EHotelReservationDetail eAdd = new EHotelReservationDetail();
            JUtils.ToEReservationDetails(em, hotel, eAdd, addP);
            eAdd.setServiceType(ServiceType.OTHER);
            eAdd.setReservation(r);
            em.persist(eAdd);
        }

    }

    @Override
    public void addResAddPayment(OObjectId hotel, String resName,
            ReservationPaymentDetail add) {
        AddResPayment comma = new AddResPayment(hotel, resName, add);
        comma.executeTran();
    }

    private class AddResPaymentCommand extends doResTransaction {

        private List<ReservationPaymentDetail> pList = new ArrayList<ReservationPaymentDetail>();

        AddResPaymentCommand(OObjectId hotel, String resName) {
            super(hotel, resName);
        }

        @Override
        protected void dosth(EntityManager em) {
            EHotelReservation e = getRes(em);
            Query q = em.createNamedQuery("findReservationForReservation");
            q.setParameter(1, e);
            q.setParameter(2, ServiceType.OTHER);
            @SuppressWarnings("unchecked")
            List<EHotelReservationDetail> resList = q.getResultList();
            for (EHotelReservationDetail a : resList) {
                ReservationPaymentDetail add = new ReservationPaymentDetail();
                JUtils.ToReservationDetails(add, a);
                pList.add(add);
            }
        }

    }

    @Override
    public List<ReservationPaymentDetail> getResAddPaymentList(OObjectId hotel,
            String resName) {
        AddResPaymentCommand comma = new AddResPaymentCommand(hotel, resName);
        comma.executeTran();
        return comma.pList;
    }

    private class FindBillsForReservation extends doResTransaction {

        private List<CustomerBill> bList = new ArrayList<CustomerBill>();

        FindBillsForReservation(OObjectId hotel, String resName) {
            super(hotel, resName);
        }

        @Override
        protected void dosth(EntityManager em) {
            EHotelReservation e = getRes(em);
            Query q = em.createNamedQuery("findBillsForReservation");
            q.setParameter(1, e);
            List<ECustomerBill> resList = q.getResultList();
            for (ECustomerBill eb : resList) {
                CustomerBill b = new CustomerBill();
                JUtils.toCustomerBill(em, hotel, b, eb);
                bList.add(b);
            }
        }

    }

    @Override
    public List<CustomerBill> findBillsForReservation(OObjectId hotel,
            String resName) {
        FindBillsForReservation comma = new FindBillsForReservation(hotel,
                resName);
        comma.executeTran();
        return comma.bList;
    }
    
}
