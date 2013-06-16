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
package com.gwthotel.hotel.jpa.services;

import javax.persistence.EntityManager;

import com.gwthotel.admin.HotelId;
import com.gwthotel.hotel.jpa.AbstractJpaCrud;
import com.gwthotel.hotel.jpa.JUtils;
import com.gwthotel.hotel.jpa.entities.EHotelServices;
import com.gwthotel.hotel.services.HotelServices;
import com.gwthotel.hotel.services.IHotelServices;
import com.gwthotel.shared.IHotelConsts;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;
import com.jythonui.server.getmess.IGetLogMess;

class HotelJpaServices extends AbstractJpaCrud<HotelServices, EHotelServices>
        implements IHotelServices {

    HotelJpaServices(ITransactionContextFactory eFactory, IGetLogMess lMess) {
        super(new String[] { "findAllServices", "findOneService",
                "deleteAllServices" }, eFactory, lMess);
    }

    @Override
    protected HotelServices toT(EHotelServices sou, HotelId hotel) {
        return JUtils.toT(sou);
    }

    @Override
    protected EHotelServices constructE(HotelId hotel) {
        return new EHotelServices();
    }

    @Override
    protected void toE(EHotelServices dest, HotelServices sou, HotelId hotel) {
        dest.setNoPersons(sou.getNoPersons());
        dest.setVat(sou.getAttr(IHotelConsts.VATPROP));
    }

    @Override
    protected void beforedeleteAll(EntityManager em, HotelId hotel) {
        String qList[] = { "deletePricesForHotel", "deleteAllRoomServices" };
        executeHotelQuery(em, hotel, qList);
    }

    @Override
    protected void beforedeleteElem(EntityManager em, HotelId hotel,
            EHotelServices elem) {
        String qList[] = { "deletePricesForHotelAndService",
                "deleteForRoomServices" };
        executeElemQuery(em, hotel, qList, elem);
    }
}
