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
package com.gwthotel.hotel.service.gae;

import static com.googlecode.objectify.ObjectifyService.ofy;

import javax.inject.Inject;
import javax.inject.Named;

import com.googlecode.objectify.ObjectifyService;
import com.gwthotel.admin.gae.entities.EHotel;
import com.gwthotel.hotel.pricelist.HotelPriceList;
import com.gwthotel.hotel.pricelist.IHotelPriceList;
import com.gwthotel.hotel.service.gae.crud.CrudGaeAbstract;
import com.gwthotel.hotel.service.gae.entities.EHotelPriceElem;
import com.gwthotel.hotel.service.gae.entities.EHotelPriceList;
import com.gwthotel.shared.IHotelConsts;
import com.jython.ui.shared.MUtil;
import com.jythonui.server.getmess.IGetLogMess;

public class HotelPriceListImpl extends
        CrudGaeAbstract<HotelPriceList, EHotelPriceList> implements
        IHotelPriceList {

    static {
        ObjectifyService.register(EHotelPriceList.class);
    }

    @Inject
    public HotelPriceListImpl(@Named(IHotelConsts.MESSNAMED) IGetLogMess lMess) {
        super(lMess, EHotelPriceList.class);
    }

    @Override
    protected HotelPriceList constructProp(EHotelPriceList e) {
        HotelPriceList pr = new HotelPriceList();
        pr.setFromDate(e.getPriceFrom());
        pr.setToDate(e.getPriceTo());
        return pr;
    }

    @Override
    protected EHotelPriceList constructE() {
        return new EHotelPriceList();
    }

    @Override
    protected void toE(EHotelPriceList e, HotelPriceList t) {
        e.setPriceFrom(t.getFromDate());
        e.setPriceTo(t.getToDate());
    }

    @Override
    protected void beforeDelete(DeleteItem i, EHotel ho, EHotelPriceList elem) {
        if (elem != null) {
            i.pList = ofy().load().type(EHotelPriceElem.class).ancestor(ho)
                    .filter("pricelistName == ", elem.getName()).list();
        } else {
            i.readAllPriceElems(ho);
        }

    }

}
