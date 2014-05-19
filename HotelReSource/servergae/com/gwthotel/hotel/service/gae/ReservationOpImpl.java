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
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.gwthotel.admin.gae.DictUtil;
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
import com.gwthotel.hotel.service.gae.entities.EHotelRoom;
import com.gwthotel.hotel.service.gae.entities.EResDetails;
import com.gwthotel.hotel.stay.ResGuest;
import com.gwthotel.shared.IHotelConsts;
import com.jython.serversecurity.OObjectId;
import com.jython.ui.server.gae.security.entities.EObject;
import com.jython.ui.server.gae.security.impl.EntUtil;
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
    public List<ResData> queryReservation(OObjectId hotel, List<ResQuery> rQuery) {
        List<ResData> resList = new ArrayList<ResData>();
        EObject eh = DictUtil.findEHotel(lMess, hotel);
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
    public void changeStatus(OObjectId hotel, String resName,
            ResStatus newStatus) {
        EObject eh = DictUtil.findEHotel(lMess, hotel);
        final EHotelReservation e = DictUtil.findReservation(eh, resName);
        e.setStatus(newStatus);
        ofy().save().entity(e).now();
    }

    private List<EHotelGuest> findGuestsForRes(EObject ho, String resName) {
        final List<EHotelGuest> li = ofy().load().type(EHotelGuest.class)
                .ancestor(ho).filter("resName == ", resName).list();
        return li;
    }

    @Override
    public void setResGuestList(OObjectId hotel, String resName,
            List<ResGuest> gList) {
        EObject eh = DictUtil.findEHotel(lMess, hotel);
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
    public List<ResGuest> getResGuestList(OObjectId hotel, String resName) {
        EObject eh = DictUtil.findEHotel(lMess, hotel);
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
    public void addResAddPayment(OObjectId hotel, String resName,
            ReservationPaymentDetail add) {
        EObject eh = DictUtil.findEHotel(lMess, hotel);
        EHotelReservation re = DictUtil.findReservation(eh, resName);
        add.setServiceType(ServiceType.OTHER);
        EResDetails eRes = DictUtil.toEResDetail(eh, re, add);
        ofy().save().entity(eRes).now();

    }

    @Override
    public List<ReservationPaymentDetail> getResAddPaymentList(OObjectId hotel,
            String resName) {
        EObject eh = DictUtil.findEHotel(lMess, hotel);
        List<EResDetails> li = DictUtil.findResDetailsForRes(eh, resName,
                ServiceType.OTHER);
        List<ReservationPaymentDetail> rList = new ArrayList<ReservationPaymentDetail>();
        DictUtil.toRP(rList, li);
        return rList;
    }

    @Override
    public List<CustomerBill> findBillsForReservation(OObjectId hotel,
            String resName) {
        EObject eh = DictUtil.findEHotel(lMess, hotel);
        List<ECustomerBill> re = DictUtil.findBillsForRese(eh, resName);
        List<CustomerBill> bList = new ArrayList<CustomerBill>();
        for (ECustomerBill b : re) {
            CustomerBill bi = DictUtil.toCustomerBill(b);
            EntUtil.toProp(bi, b);
            bList.add(bi);
        }
        return bList;
    }

    @Override
    public List<ResData> searchReservation(OObjectId hotel, ResQuery rQuery) {
        Date dFrom = rQuery.getFromRes();
        Date dTo = rQuery.getToRes();
        EObject eh = DictUtil.findEHotel(lMess, hotel);
        List<EHotelRoom> li = ofy().load().type(EHotelRoom.class).ancestor(eh)
                .list();
        List<ResQuery> ql = new ArrayList<ResQuery>();
        for (EHotelRoom r : li) {
            ResQuery q = new ResQuery();
            q.setFromRes(dFrom);
            q.setToRes(dTo);
            q.setRoomName(r.getName());
            ql.add(q);
        }
        List<ResData> dRes = queryReservation(hotel, ql);
        Set<String> rMap = new HashSet<String>();
        for (ResData r : dRes)
            rMap.add(r.getRoomName());

        List<ResData> outres = new ArrayList<ResData>();
        for (EHotelRoom r : li)
            if (!rMap.contains(r.getName())) {
                ResData rd = new ResData();
                rd.setRoomName(r.getName());
                outres.add(rd);
            }
        return outres;
    }

    private class DistinctResName {
        private Set<String> rName = new HashSet<String>();

        void addRes(String resName) {
            rName.add(resName);
        }

        List<String> getResName() {
            List<String> rList = new ArrayList<String>();
            Iterator<String> r = rName.iterator();
            while (r.hasNext())
                rList.add(r.next());
            return rList;
        }

    }

    private List<String> getReseForInfo(EObject eh, ResInfoType iType,
            String iName) {
        DistinctResName re = new DistinctResName();
        List<EResDetails> li = ofy().load().type(EResDetails.class)
                .ancestor(eh).list();
        for (EResDetails e : li) {
            switch (iType) {
            case FORSERVICE:
                if (!e.getService().getName().equals(iName))
                    continue;
                break;
            case FORROOM:
                if (!e.getRoom().getName().equals(iName))
                    continue;
                break;
            case FORPRICELIST:
                if (e.getPricelistName() == null)
                    continue;
                if (!e.getPricelistName().equals(iName))
                    continue;
                break;
            case FORGUEST:
                if (e.getGuest() == null)
                    continue;
                if (!e.getGuest().getName().equals(iName))
                    continue;
                break;
            default:
                break;
            }
            re.addRes(e.getReservation().getName());
        }

        return re.getResName();
    }

    private List<String> getReseForCustomer(EObject eh, String iName) {
        List<String> rList = new ArrayList<String>();
        List<EHotelReservation> li = ofy().load().type(EHotelReservation.class)
                .ancestor(eh).list();
        for (EHotelReservation r : li)
            if (r.getCustomer().getName().equals(iName))
                rList.add(r.getName());
        return rList;
    }

    private List<String> getReseForPayer(EObject eh, String iName) {
        DistinctResName re = new DistinctResName();
        List<ECustomerBill> rel = ofy().load().type(ECustomerBill.class)
                .ancestor(eh).filter("payerName ==", iName).list();
        for (ECustomerBill b : rel)
            re.addRes(b.getReservation().getName());
        return re.getResName();
    }

    private List<String> getReseForGuest(EObject eh, String iName) {
        DistinctResName re = new DistinctResName();
        List<EHotelGuest> rel = ofy().load().type(EHotelGuest.class)
                .ancestor(eh).filter("guestName ==", iName).list();
        for (EHotelGuest g : rel)
            re.addRes(g.getReservation().getName());
        return re.getResName();
    }

    @Override
    public List<String> getReseForInfoType(OObjectId hotel, ResInfoType iType,
            String iName) {
        EObject eh = DictUtil.findEHotel(lMess, hotel);
        switch (iType) {
        case FORSERVICE:
        case FORROOM:
        case FORPRICELIST:
            return getReseForInfo(eh, iType, iName);
        case FORGUEST:
            return getReseForGuest(eh, iName);
        case FORCUSTOMER:
            return getReseForCustomer(eh, iName);
        case FORPAYER:
            return getReseForPayer(eh, iName);
        default:
            break;
        }
        return null;

    }

}
