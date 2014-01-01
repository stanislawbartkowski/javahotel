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
package com.gwthotel.hotel.service.gae;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.gwthotel.admin.HotelId;
import com.gwthotel.admin.gae.DictUtil;
import com.gwthotel.admin.gae.entities.EHotel;
import com.gwthotel.hotel.ServiceType;
import com.gwthotel.hotel.bill.CustomerBill;
import com.gwthotel.hotel.reservation.ResStatus;
import com.gwthotel.hotel.reservation.ReservationPaymentDetail;
import com.gwthotel.hotel.reservationop.IReservationOp;
import com.gwthotel.hotel.reservationop.ResData;
import com.gwthotel.hotel.reservationop.ResQuery;
import com.gwthotel.hotel.service.gae.entities.ECustomerBill;
import com.gwthotel.hotel.service.gae.entities.EHotelGuest;
import com.gwthotel.hotel.service.gae.entities.EHotelReservation;
import com.gwthotel.hotel.service.gae.entities.EResDetails;
import com.gwthotel.hotel.stay.ResGuest;
import com.gwthotel.shared.IHotelConsts;
import com.jythonui.server.getmess.IGetLogMess;

public class ReservationOpImpl implements IReservationOp {

    private final IGetLogMess lMess;

    static {
        ObjectifyService.register(EHotelGuest.class);
    }

    @Inject
    public ReservationOpImpl(@Named(IHotelConsts.MESSNAMED) IGetLogMess lMess) {
        this.lMess = lMess;
    }

    @Override
    public List<ResData> queryReservation(HotelId hotel, List<ResQuery> rQuery) {
        List<ResData> resList = new ArrayList<ResData>();
        EHotel eh = DictUtil.findEHotel(lMess, hotel);
        for (ResQuery r : rQuery) {
            List<EResDetails> re = ofy().load().type(EResDetails.class)
                    .ancestor(eh).filter("resDate >=", r.getFromRes())
                    .filter("resDate <=", r.getToRes())
                    .filter("roomName ==", r.getRoomName()).list();
            for (EResDetails er : re) {
                if (er.getReservation().getStatus() == ResStatus.CANCEL)
                    continue;
                if (er.getServiceType() != ServiceType.HOTEL)
                    continue;
                ResData res = new ResData();
                res.setResDate(er.getResDate());
                res.setRoomName(er.getRoom().getName());
                res.setResId(er.getReservation().getName());
                resList.add(res);
            }
        }
        return resList;
    }

    @Override
    public void changeStatus(HotelId hotel, String resName, ResStatus newStatus) {
        EHotel eh = DictUtil.findEHotel(lMess, hotel);
        final EHotelReservation e = DictUtil.findReservation(eh, resName);
        e.setStatus(newStatus);
        ofy().save().entity(e).now();
    }

    private List<EHotelGuest> findGuestsForRes(EHotel ho, String resName) {
        final List<EHotelGuest> li = ofy().load().type(EHotelGuest.class)
                .ancestor(ho).filter("resName == ", resName).list();
        return li;
    }

    @Override
    public void setResGuestList(HotelId hotel, String resName,
            List<ResGuest> gList) {
        EHotel eh = DictUtil.findEHotel(lMess, hotel);
        final List<EHotelGuest> li = findGuestsForRes(eh, resName);
        final List<EHotelGuest> nLi = new ArrayList<EHotelGuest>();
        EHotelReservation re = DictUtil.findReservation(eh, resName);
        for (ResGuest r : gList) {
            EHotelGuest g = new EHotelGuest();
            g.setReservation(re);
            g.setHotel(eh);
            g.setRoom(DictUtil.findRoom(eh, r.getRoomName()));
            g.setGuest(DictUtil.findCustomer(eh, r.getGuestName()));
            nLi.add(g);
        }
        ofy().transact(new VoidWork() {
            public void vrun() {
                ofy().delete().entities(li);
                ofy().save().entities(nLi);
            }
        });
    }

    @Override
    public List<ResGuest> getResGuestList(HotelId hotel, String resName) {
        EHotel eh = DictUtil.findEHotel(lMess, hotel);
        List<ResGuest> resLi = new ArrayList<ResGuest>();
        List<EHotelGuest> li = findGuestsForRes(eh, resName);
        for (EHotelGuest g : li) {
            ResGuest r = new ResGuest();
            r.setRoomName(g.getRoom().getName());
            r.setGuestName(g.getGuest().getName());
            resLi.add(r);
        }
        return resLi;
    }

    @Override
    public void addResAddPayment(HotelId hotel, String resName,
            ReservationPaymentDetail add) {
        EHotel eh = DictUtil.findEHotel(lMess, hotel);
        EHotelReservation re = DictUtil.findReservation(eh, resName);
        add.setServiceType(ServiceType.OTHER);
        EResDetails eRes = DictUtil.toEResDetail(eh, re, add);
        ofy().save().entity(eRes).now();

    }

    @Override
    public List<ReservationPaymentDetail> getResAddPaymentList(HotelId hotel,
            String resName) {
        EHotel eh = DictUtil.findEHotel(lMess, hotel);
        List<EResDetails> li = DictUtil.findResDetailsForRes(eh, resName,
                ServiceType.OTHER);
        List<ReservationPaymentDetail> rList = new ArrayList<ReservationPaymentDetail>();
        DictUtil.toRP(rList, li);
        return rList;
    }

    @Override
    public List<CustomerBill> findBillsForReservation(HotelId hotel,
            String resName) {
        EHotel eh = DictUtil.findEHotel(lMess, hotel);
        List<ECustomerBill> re = ofy().load().type(ECustomerBill.class)
                .ancestor(eh).filter("resName ==", resName).list();
        List<CustomerBill> bList = new ArrayList<CustomerBill>();
        for (ECustomerBill b : re) {
            CustomerBill bi = DictUtil.toCustomerBill(b);
            DictUtil.toProp(bi, b);
            bList.add(bi);
        }
        return bList;
    }

}
