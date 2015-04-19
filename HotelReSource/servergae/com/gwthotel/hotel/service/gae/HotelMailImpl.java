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
package com.gwthotel.hotel.service.gae;

import javax.inject.Inject;

import com.googlecode.objectify.ObjectifyService;
import com.gwthotel.admin.gae.DictUtil;
import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.mailing.HotelMailElem;
import com.gwthotel.hotel.mailing.IHotelMailList;
import com.gwthotel.hotel.service.gae.crud.HotelCrudGaeAbstract;
import com.gwthotel.hotel.service.gae.entities.EHotelCustomer;
import com.gwthotel.hotel.service.gae.entities.EHotelMail;
import com.gwthotel.hotel.service.gae.entities.EHotelReservation;
import com.jython.ui.server.gae.security.entities.EObject;
import com.jythonui.server.crud.ICrudObjectGenSym;

public class HotelMailImpl extends
        HotelCrudGaeAbstract<HotelMailElem, EHotelMail> implements
        IHotelMailList {

    static {
        ObjectifyService.register(EHotelMail.class);
    }

    @Inject
    public HotelMailImpl(ICrudObjectGenSym iGen) {
        super(EHotelMail.class, HotelObjects.HOTELMAIL, iGen);
    }

    @Override
    protected void beforeDelete(
            HotelCrudGaeAbstract<HotelMailElem, EHotelMail>.DeleteItem i,
            EObject ho, EHotelMail elem) {
    }

    @Override
    protected HotelMailElem constructProp(EObject ho, EHotelMail sou) {
        HotelMailElem elem = new HotelMailElem();
        elem.setCustomerName(sou.getCustomer().getName());
        elem.setmType(sou.getmType());
        if (sou.getReservation() != null)
            elem.setReseName(sou.getReservation().getName());
        return elem;
    }

//    @Override
//    protected EHotelMail constructE() {
//        return new EHotelMail();
//    }

    @Override
    protected void toE(EObject ho, EHotelMail dest, HotelMailElem sou) {
        dest.setmType(sou.getmType());
        EHotelCustomer cust = DictUtil.findCustomer(ho, sou.getCustomerName());
        if (sou.isReseName()) {
            EHotelReservation rese = DictUtil.findReservation(ho,
                    sou.getReseName());
            dest.setReservation(rese);
        }
        dest.setCustomer(cust);

    }

}
