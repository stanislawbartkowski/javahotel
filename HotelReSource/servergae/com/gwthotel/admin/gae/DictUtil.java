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
package com.gwthotel.admin.gae;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.logging.Logger;

import com.googlecode.objectify.LoadResult;
import com.gwthotel.admin.AppInstanceId;
import com.gwthotel.admin.HotelId;
import com.gwthotel.admin.gae.entities.EDictionary;
import com.gwthotel.admin.gae.entities.EHotel;
import com.gwthotel.admin.gae.entities.EInstance;
import com.gwthotel.hotel.service.gae.entities.EHotelServices;
import com.gwthotel.hotel.services.HotelServices;
import com.gwthotel.mess.IHError;
import com.gwthotel.mess.IHMess;
import com.gwthotel.shared.IHotelConsts;
import com.gwthotel.shared.PropDescription;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.shared.JythonUIFatal;

public class DictUtil {

    private DictUtil() {

    }

    private static final Logger log = Logger
            .getLogger(DictUtil.class.getName());

    private static void setFailure(String mess) {
        log.severe(mess);
        throw new JythonUIFatal(mess);
    }

    public static void toProp(PropDescription dest, EDictionary e) {
        dest.setName(e.getName());
        dest.setDescription(e.getDescription());
    }

    public static void toEDict(EDictionary dest, PropDescription sou) {
        dest.setName(sou.getName());
        dest.setDescription(sou.getDescription());
    }

    public static EHotel findEHotel(IGetLogMess lMess, HotelId hotel) {
        EInstance eI = findI(lMess, hotel.getInstanceId());
        LoadResult<EHotel> p = ofy().load().type(EHotel.class).parent(eI)
                .id(hotel.getId());
        if (p.now() == null) {
            String mess = lMess.getMess(IHError.HERROR005,
                    IHMess.HOTELBYIDNOTFOUND, Long.toString(hotel.getId()));
            setFailure(mess);
        }
        return p.now();
    }

    public static HotelServices toS(EHotelServices e) {
        HotelServices h = new HotelServices();
        h.setAttr(IHotelConsts.VATPROP, e.getVat());
        h.setNoPersons(e.getNoperson());
        toProp(h, e);
        return h;
    }

    public static EInstance findI(IGetLogMess lMess, AppInstanceId i) {
        LoadResult<EInstance> p = ofy().load().type(EInstance.class)
                .id(i.getId());
        if (p == null) {
            String mess = lMess.getMess(IHError.HERROR014,
                    IHMess.INSTANCEBYIDCANNOTBEFOUND, Long.toString(i.getId()));
            setFailure(mess);
        }
        return p.now();

    }

}
