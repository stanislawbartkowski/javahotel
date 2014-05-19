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
package com.gwthotel.admin.gae;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.LoadResult;
import com.gwthotel.admin.holder.HHolder;
import com.gwthotel.hotel.ServiceType;
import com.gwthotel.hotel.bill.CustomerBill;
import com.gwthotel.hotel.reservation.ReservationPaymentDetail;
import com.gwthotel.hotel.service.gae.entities.EBillPayment;
import com.gwthotel.hotel.service.gae.entities.ECustomerBill;
import com.gwthotel.hotel.service.gae.entities.EHotelCustomer;
import com.gwthotel.hotel.service.gae.entities.EHotelReservation;
import com.gwthotel.hotel.service.gae.entities.EHotelRoom;
import com.gwthotel.hotel.service.gae.entities.EHotelServices;
import com.gwthotel.hotel.service.gae.entities.EResDetails;
import com.gwthotel.hotel.services.HotelServices;
import com.gwthotel.mess.IHError;
import com.gwthotel.mess.IHMess;
import com.gwthotel.shared.IHotelConsts;
import com.gwtmodel.table.common.CUtil;
import com.jython.serversecurity.OObjectId;
import com.jython.ui.server.gae.security.entities.EInstance;
import com.jython.ui.server.gae.security.entities.EObject;
import com.jython.ui.server.gae.security.impl.EntUtil;
import com.jythonui.server.RUtils;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.getmess.IGetLogMess;

public class DictUtil extends UtilHelper {

    private DictUtil() {

    }

    public static EObject findEHotel(IGetLogMess lMess, OObjectId hotel) {
        EInstance eI = EntUtil.findI(lMess, hotel.getInstanceId());
        LoadResult<EObject> p = ofy().load().type(EObject.class).parent(eI)
                .id(hotel.getId());
        // System.out.println("Hotel " + hotel.getId() + " instance :" +
        // hotel.getInstanceId().getId() );
        if (p.now() == null) {
            String mess = lMess.getMess(IHError.HERROR005,
                    IHMess.HOTELBYIDNOTFOUND, hotel.getId().toString(), eI
                            .getId().toString());
            errorLog(mess);
        }
        return p.now();
    }

    public static HotelServices toS(EHotelServices e) {
        HotelServices h = new HotelServices();
        h.setAttr(IHotelConsts.VATPROP, e.getVat());
        h.setNoPersons(e.getNoperson());
        h.setNoChildren(e.getNoChildren());
        h.setNoExtraBeds(e.getNoExtraBeds());
        h.setServiceType(e.getServiceType());
        h.setPerperson(e.isPerperson());
        EntUtil.toProp(h, e);
        return h;
    }

    public static <E> E findE(EObject eh, String name, Class<E> cl) {
        LoadResult<E> p = ofy().load().type(cl).ancestor(eh)
                .filter("name == ", name).first();
        if (p == null) {
            return null;
        }
        return p.now();
    }

    public static <E> E findEE(EObject eh, String name, Class<E> cl) {
        LoadResult<E> p = ofy().load().type(cl).ancestor(eh)
                .filter("name == ", name).first();
        E e = p.now();
        if (e != null)
            return e;
        String mess = HHolder.getHM().getMess(IHError.HERROR021,
                IHMess.OBJECTBYNAMECANNOTBEFOUND, name, eh.getName());
        errorLog(mess);
        return null;
    }

    public static EHotelCustomer findCustomer(EObject eh, String name) {
        return findEE(eh, name, EHotelCustomer.class);
    }

    public static EHotelRoom findRoom(EObject eh, String name) {
        return findEE(eh, name, EHotelRoom.class);
    }

    public static EHotelReservation findReservation(EObject eh, String name) {
        return findEE(eh, name, EHotelReservation.class);
    }

    public static ECustomerBill findCustomerBill(EObject eh, String name) {
        return findEE(eh, name, ECustomerBill.class);
    }

    public static EHotelServices findService(EObject eh, String name) {
        return findEE(eh, name, EHotelServices.class);
    }

    public static List<EResDetails> findResDetailsForRes(EObject ho,
            String resName, ServiceType serviceType) {
        List<EResDetails> li = ofy().load().type(EResDetails.class)
                .ancestor(ho).filter("resName == ", resName).list();
        List<EResDetails> rLi = new ArrayList<EResDetails>();
        for (EResDetails r : li)
            if (r.getServiceType() == serviceType)
                rLi.add(r);
        return rLi;
    }

    public static BigDecimal toBD(Double b) {
        if (b == null)
            return null;
        return new BigDecimal(b);
    }

    public static void toRP(List<ReservationPaymentDetail> resList,
            List<EResDetails> rList) {
        for (EResDetails d : rList) {
            ReservationPaymentDetail dd = new ReservationPaymentDetail();
            if (d.getGuest() != null)
                dd.setGuestName(d.getGuest().getName());
            dd.setRoomName(d.getRoom().getName());
            dd.setNoP(d.getNoP());
            dd.setPrice(toBD(d.getPrice()));
            dd.setPriceList(toBD(d.getPriceList()));
            dd.setNoChildren(d.getNoChildren());
            dd.setPriceChildren(toBD(d.getPriceChildren()));
            dd.setPriceListChildren(toBD(d.getPriceListChildren()));
            dd.setNoExtraBeds(d.getNoExtraBeds());
            dd.setPriceExtraBeds(toBD(d.getPriceExtraBeds()));
            dd.setPriceListExtraBeds(toBD(d.getPriceListExtraBeds()));
            dd.setPriceTotal(toBD(d.getPriceTotal()));
            dd.setQuantity(d.getNoP());
            dd.setServDate(d.getResDate());
            dd.setPerperson(d.isPerperson());
            dd.setResDate(d.getResDate());
            if (d.getService() != null)
                dd.setService(d.getService().getName());
            dd.setServiceType(d.getServiceType());
            dd.setDescription(d.getDescription());
            dd.setVat(d.getVatTax());
            dd.setId(d.getId());
            dd.setPriceListName(d.getPricelistName());
            resList.add(dd);
        }
    }

    public static Double toDouble(BigDecimal b, boolean allownull) {
        if (allownull && b == null)
            return null;
        return b.doubleValue();
    }

    public static EResDetails toEResDetail(EObject ho, EHotelReservation e,
            ReservationPaymentDetail r) {
        EResDetails er = new EResDetails();
        if (!CUtil.EmptyS(r.getGuestName()))
            er.setGuest(DictUtil.findCustomer(ho, r.getGuestName()));
        er.setNoP(r.getNoP());
        er.setPrice(toDouble(r.getPrice(), false));
        er.setPriceList(toDouble(r.getPriceList(), true));
        er.setPriceTotal(toDouble(r.getPriceTotal(), true));
        er.setNoChildren(r.getNoChildren());
        er.setPriceChildren(toDouble(r.getPriceChildren(), true));
        er.setPriceListChildren(toDouble(r.getPriceListChildren(), true));
        er.setNoExtraBeds(r.getNoExtraBeds());
        er.setPriceExtraBeds(toDouble(r.getPriceExtraBeds(), true));
        er.setPriceListExtraBeds(toDouble(r.getPriceListExtraBeds(), true));
        er.setPerperson(r.isPerperson());
        er.setResDate(r.getResDate());
        if (!CUtil.EmptyS(r.getRoomName()))
            er.setRoom(DictUtil.findRoom(ho, r.getRoomName()));
        er.setServiceType(r.getServiceType());
        er.setReservation(e);
        if (!CUtil.EmptyS(r.getService()))
            er.setService(findService(ho, r.getService()));
        er.setHotel(ho);
        er.setDescription(r.getDescription());
        er.setVatTax(r.getVat());
        er.setPricelistName(r.getPriceListName());
        return er;

    }

    public static List<EResDetails> toED(EObject ho, EHotelReservation e,
            List<ReservationPaymentDetail> rList) {
        List<EResDetails> resList = new ArrayList<EResDetails>();
        for (ReservationPaymentDetail r : rList)
            resList.add(toEResDetail(ho, e, r));
        return resList;
    }

    public static List<ECustomerBill> findBillsForRese(EObject ho,
            String resName) {
        List<ECustomerBill> re = ofy().load().type(ECustomerBill.class)
                .ancestor(ho).filter("resName ==", resName).list();
        return re;
    }

    public static List<EBillPayment> findPaymentsForBill(EObject ho,
            String billName) {
        List<EBillPayment> li = ofy().load().type(EBillPayment.class)
                .ancestor(ho).filter("billName == ", billName).list();
        return li;
    }

    public static CustomerBill toCustomerBill(ECustomerBill e) {
        CustomerBill bi = new CustomerBill();
        bi.setDateOfPayment(e.getDateOfPayment());
        bi.setIssueDate(e.getIssueDate());
        bi.setPayer(e.getPayer().getName());
        bi.setReseName(e.getReservation().getName());
        bi.getPayList().addAll(e.getResDetails());
        bi.setId(e.getId());
        RUtils.retrieveCreateModif(bi, e);
        return bi;
    }
}
