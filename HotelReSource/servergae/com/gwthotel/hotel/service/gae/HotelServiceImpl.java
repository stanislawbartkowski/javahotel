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

import javax.inject.Inject;
import javax.inject.Named;

import com.googlecode.objectify.ObjectifyService;
import com.gwthotel.admin.gae.DictUtil;
import com.gwthotel.admin.gae.entities.EHotel;
import com.gwthotel.hotel.HotelObjects;
import com.gwthotel.hotel.IHotelObjectGenSym;
import com.gwthotel.hotel.service.gae.crud.CrudGaeAbstract;
import com.gwthotel.hotel.service.gae.entities.EHotelPriceElem;
import com.gwthotel.hotel.service.gae.entities.EHotelRoomServices;
import com.gwthotel.hotel.service.gae.entities.EHotelServices;
import com.gwthotel.hotel.services.HotelServices;
import com.gwthotel.hotel.services.IHotelServices;
import com.gwthotel.shared.IHotelConsts;
import com.jythonui.server.getmess.IGetLogMess;

public class HotelServiceImpl extends
        CrudGaeAbstract<HotelServices, EHotelServices> implements
        IHotelServices {

    static {
        ObjectifyService.register(EHotelServices.class);
    }

    @Inject
    public HotelServiceImpl(@Named(IHotelConsts.MESSNAMED) IGetLogMess lMess,
            IHotelObjectGenSym iGen) {
        super(lMess, EHotelServices.class, HotelObjects.SERVICE, iGen);
    }

    @Override
    protected HotelServices constructProp(EHotel ho, EHotelServices e) {
        return DictUtil.toS(e);
    }

    @Override
    protected EHotelServices constructE() {
        return new EHotelServices();
    }

    @Override
    protected void toE(EHotel ho, EHotelServices e, HotelServices t) {
        e.setVat(t.getAttr(IHotelConsts.VATPROP));
        e.setNoperson(t.getNoPersons());
        e.setServiceType(t.getServiceType());

    }

    @Override
    protected void beforeDelete(DeleteItem i, EHotel ho, EHotelServices elem) {
        if (elem != null) {
            i.pList = ofy().load().type(EHotelPriceElem.class).ancestor(ho)
                    .filter("serviceName == ", elem.getName()).list();
            i.sList = ofy().load().type(EHotelRoomServices.class).ancestor(ho)
                    .filter("serviceName == ", elem.getName()).list();
        } else {
            i.readAllPriceElems(ho);
            i.readAllRoomServices(ho);
        }

    }

}
