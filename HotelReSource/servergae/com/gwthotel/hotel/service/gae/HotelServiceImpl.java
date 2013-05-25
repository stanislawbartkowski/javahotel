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

import javax.inject.Inject;
import javax.inject.Named;

import com.googlecode.objectify.ObjectifyService;
import com.gwthotel.hotel.service.gae.crud.CrudGaeAbstract;
import com.gwthotel.hotel.service.gae.entities.EHotelServices;
import com.gwthotel.hotel.services.HotelServices;
import com.gwthotel.hotel.services.IHotelServices;
import com.gwthotel.shared.IHotelConsts;
import com.jythonui.server.getmess.IGetLogMess;

public class HotelServiceImpl extends CrudGaeAbstract<HotelServices,EHotelServices> implements IHotelServices {

    static {
        ObjectifyService.register(EHotelServices.class);
    }

    @Inject
    public HotelServiceImpl(@Named(IHotelConsts.MESSNAMED) IGetLogMess lMess) {
        super(lMess,EHotelServices.class);
    }
    
    @Override
    protected HotelServices constructProp(EHotelServices e) {
        HotelServices h = new HotelServices();
        h.setAttr(IHotelConsts.VATPROP, e.getVat());
        h.setAttrInt(IHotelConsts.NOPERSONPROP, e.getNoperson());
        return h;
    }

    @Override
    protected EHotelServices constructE() {
        return new EHotelServices();
    }

    @Override
    protected void toE(EHotelServices e, HotelServices t) {
        e.setVat(t.getAttr(IHotelConsts.VATPROP));
        e.setNoperson(t.getAttrInt(IHotelConsts.NOPERSONPROP));
        
    }


}
