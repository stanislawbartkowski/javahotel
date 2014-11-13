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

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.gwthotel.admin.gae.DictUtil;
import com.gwthotel.hotel.ServiceType;
import com.gwthotel.hotel.reservation.IReservationForm;
import com.gwthotel.hotel.reservation.ReservationForm;
import com.gwthotel.hotel.service.gae.entities.EBillPayment;
import com.gwthotel.hotel.service.gae.entities.ECustomerBill;
import com.gwthotel.hotel.service.gae.entities.EHotelReservation;
import com.gwthotel.hotel.service.gae.entities.EResDetails;
import com.jython.serversecurity.cache.OObjectId;
import com.jython.ui.server.gae.security.entities.EObject;
import com.jython.ui.server.gae.security.impl.EntUtil;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.crud.ICrudObjectGenSym;

public class HotelReservationImpl implements IReservationForm {

    private final ICrudObjectGenSym iGen;

    @Inject
    public HotelReservationImpl(ICrudObjectGenSym iGen) {
        this.iGen = iGen;
    }

    static {
        ObjectifyService.register(EHotelReservation.class);
        ObjectifyService.register(EResDetails.class);
    }

    private ReservationForm constructProp(EObject ho, EHotelReservation e) {
        ReservationForm r = new ReservationForm();
        EntUtil.toProp(r, e);
        r.setAttr(ISharedConsts.OBJECTPROP, ho.getName());
        r.setId(e.getId());
        r.setStatus(e.getStatus());
        r.setCustomerName(e.getCustomer().getName());
        r.setAdvanceDeposit(e.getAdvanceDeposit());
        r.setAdvancePayment(e.getAdvancePayment());
        r.setDateofadvancePayment(e.getDateofadvancePayment());
        r.setTermOfAdvanceDeposit(e.getTermOfAdvanceDeposit());
        List<EResDetails> li = DictUtil.findResDetailsForRes(ho, e.getName(),
                ServiceType.HOTEL);
        DictUtil.toRP(r.getResDetail(), li);
        return r;
    }

    @Override
    public List<ReservationForm> getList(OObjectId hotel) {
        EObject eh = EntUtil.findEOObject(hotel);
        List<EHotelReservation> li = ofy().load().type(EHotelReservation.class)
                .ancestor(eh).list();
        List<ReservationForm> outList = new ArrayList<ReservationForm>();
        for (EHotelReservation e : li) {
            outList.add(constructProp(eh, e));
        }
        return outList;
    }

    @Override
    public ReservationForm addElem(OObjectId hotel, final ReservationForm elem) {
        ReservationAdd add = new ReservationAdd(hotel, elem, false, iGen);
        add.beforeAdd();
        add.addTran();
        return findElem(hotel, elem.getName());
    }

    @Override
    public void changeElem(OObjectId hotel, ReservationForm elem) {
        ReservationAdd add = new ReservationAdd(hotel, elem, true, iGen);
        add.beforeAdd();
        add.addTran();
    }

    @Override
    public void deleteElem(OObjectId hotel, ReservationForm elem) {
        EObject eh = EntUtil.findEOObject(hotel);
        final List<EBillPayment> liP = new ArrayList<EBillPayment>();
        final List<ECustomerBill> re = DictUtil.findBillsForRese(eh,
                elem.getName());
        for (ECustomerBill b : re)
            liP.addAll(DictUtil.findPaymentsForBill(eh, b.getName()));

        final List<EResDetails> li = DictUtil.findResDetailsForRes(eh,
                elem.getName(), ServiceType.HOTEL);
        final EHotelReservation rese = DictUtil.findReservation(eh,
                elem.getName());

        ofy().transact(new VoidWork() {
            public void vrun() {
                ofy().delete().entities(liP);
                ofy().delete().entities(re);
                ofy().delete().entities(li);
                ofy().delete().entity(rese);
            }
        });
    }

    @Override
    public ReservationForm findElem(OObjectId hotel, String name) {
        EObject eh = EntUtil.findEOObject(hotel);
        EHotelReservation rese = DictUtil.findReservation(eh, name);
        return constructProp(eh, rese);
    }

    @Override
    public ReservationForm findElemById(OObjectId hotel, Long id) {
        EObject eh = EntUtil.findEOObject(hotel);
        EHotelReservation e = ofy().load().type(EHotelReservation.class).id(id)
                .now();
        return constructProp(eh, e);
    }

}
