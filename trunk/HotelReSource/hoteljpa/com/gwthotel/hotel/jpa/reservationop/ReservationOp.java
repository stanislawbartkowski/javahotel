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
import com.gwthotel.hotel.HUtils;
import com.gwthotel.hotel.jpa.JUtils;
import com.gwthotel.hotel.jpa.entities.EHotelAddPayment;
import com.gwthotel.hotel.jpa.entities.EHotelCustomer;
import com.gwthotel.hotel.jpa.entities.EHotelGuest;
import com.gwthotel.hotel.jpa.entities.EHotelReservation;
import com.gwthotel.hotel.jpa.entities.EHotelReservationDetail;
import com.gwthotel.hotel.jpa.entities.EHotelRoom;
import com.gwthotel.hotel.jpa.entities.EHotelRoomCustomer;
import com.gwthotel.hotel.jpa.entities.EHotelServices;
import com.gwthotel.hotel.reservation.ResStatus;
import com.gwthotel.hotel.reservationop.IReservationOp;
import com.gwthotel.hotel.reservationop.ResData;
import com.gwthotel.hotel.reservationop.ResQuery;
import com.gwthotel.hotel.stay.AbstractResHotelGuest;
import com.gwthotel.hotel.stay.ResAddPayment;
import com.gwthotel.hotel.stay.ResGuest;
import com.gwtmodel.table.common.CUtil;
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

    private abstract class doResTransaction extends JpaTransaction {

        protected final HotelId hotel;
        protected final String resName;

        doResTransaction(HotelId hotel, String resName) {
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
    public List<ResData> queryReservation(HotelId hotel, List<ResQuery> rQuery) {
        QueryCommand q = new QueryCommand(hotel, rQuery);
        q.executeTran();
        return q.resList;
    }

    private class ChangeStatus extends doResTransaction {

        private final ResStatus newStatus;

        ChangeStatus(HotelId hotel, String resName, ResStatus newStatus) {
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
    public void changeStatus(HotelId hotel, String resName, ResStatus newStatus) {
        ChangeStatus comma = new ChangeStatus(hotel, resName, newStatus);
        comma.executeTran();
    }

    private class SetReservGuest extends doResTransaction {

        private final List<ResGuest> gList;

        SetReservGuest(HotelId hotel, String resName, List<ResGuest> gList) {
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
    public void setResGuestList(HotelId hotel, String resName,
            List<ResGuest> gList) {
        SetReservGuest comma = new SetReservGuest(hotel, resName, gList);
        comma.executeTran();
    }

    private class GetResGuest extends doResTransaction {

        private final List<ResGuest> outList = new ArrayList<ResGuest>();

        GetResGuest(HotelId hotel, String resName) {
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
    public List<ResGuest> getResGuestList(HotelId hotel, String resName) {
        GetResGuest comma = new GetResGuest(hotel, resName);
        comma.executeTran();
        return comma.outList;
    }

    private class AddResPayment extends doResTransaction {

        private final ResAddPayment addP;

        AddResPayment(HotelId hotel, String resName, ResAddPayment add) {
            super(hotel, resName);
            this.addP = add;
        }

        @Override
        protected void dosth(EntityManager em) {
            EHotelReservation r = getRes(em);
            EHotelAddPayment eAdd = new EHotelAddPayment();
            toE(em, eAdd, addP);
            eAdd.setDescription(addP.getDescription());
            // TODO: very strange, rounding is necessary after bean
            // it seems that is looses (or set) round more then 31
            eAdd.setListPrice(HUtils.roundB(addP.getPriceList()));
            eAdd.setPrice(HUtils.roundB(addP.getPrice()));
            eAdd.setQuantity(addP.getQuantity());
            eAdd.setTotal(HUtils.roundB(addP.getPriceTotal()));
            eAdd.setReservation(r);
            eAdd.setServDate(addP.getServDate());
            eAdd.setServicevat(addP.getServiceVat());
            String servName = addP.getServiceName();
            if (!CUtil.EmptyS(servName)) {
                EHotelServices serv = JUtils.findService(em, hotel, servName);
                eAdd.setService(serv);
            }
            em.persist(eAdd);
        }

    }

    @Override
    public void addResAddPayment(HotelId hotel, String resName,
            ResAddPayment add) {
        AddResPayment comma = new AddResPayment(hotel, resName, add);
        comma.executeTran();
    }

    private class AddResPaymentCommand extends doResTransaction {

        private List<ResAddPayment> pList = new ArrayList<ResAddPayment>();

        AddResPaymentCommand(HotelId hotel, String resName) {
            super(hotel, resName);
        }

        @Override
        protected void dosth(EntityManager em) {
            EHotelReservation e = getRes(em);
            Query q = em.createNamedQuery("findAddPaymentForReservation");
            q.setParameter(1, e);
            @SuppressWarnings("unchecked")
            List<EHotelAddPayment> resList = q.getResultList();
            for (EHotelAddPayment a : resList) {
                ResAddPayment add = new ResAddPayment();
                toT(add, a);
                add.setDescription(a.getDescription());
                add.setPrice(a.getPrice());
                add.setPriceList(a.getListPrice());
                add.setPriceTotal(a.getTotal());
                add.setQuantity(a.getQuantity());
                add.setServDate(a.getServDate());
                add.setServiceVat(a.getServicevat());
                if (a.getService() != null)
                    add.setServiceName(a.getService().getName());
                pList.add(add);
            }
        }

    }

    @Override
    public List<ResAddPayment> getResAddPaymentList(HotelId hotel,
            String resName) {
        AddResPaymentCommand comma = new AddResPaymentCommand(hotel, resName);
        comma.executeTran();
        return comma.pList;
    }

}
