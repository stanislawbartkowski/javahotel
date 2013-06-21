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
package com.gwthotel.hotel.jpa;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.gwthotel.admin.HotelId;
import com.gwthotel.admin.holder.HHolder;
import com.gwthotel.admin.jpa.PropUtils;
import com.gwthotel.hotel.jpa.entities.EHotelDict;
import com.gwthotel.hotel.jpa.entities.EHotelRoom;
import com.gwthotel.hotel.jpa.entities.EHotelServices;
import com.gwthotel.hotel.rooms.HotelRoom;
import com.gwthotel.hotel.services.HotelServices;
import com.gwthotel.mess.IHError;
import com.gwthotel.mess.IHMess;
import com.gwthotel.shared.IHotelConsts;
import com.gwthotel.shared.PropDescription;
import com.jythonui.shared.JythonUIFatal;

public class JUtils {

    private JUtils() {
    }

    static final private Logger log = Logger.getLogger(JUtils.class.getName());

    public static HotelServices toT(EHotelServices sou) {
        HotelServices ho = new HotelServices();
        PropUtils.copyToProp(ho, sou);
        ho.setNoPersons(sou.getNoPersons());
        ho.setAttr(IHotelConsts.VATPROP, sou.getVat());
        return ho;
    }

    public static void copyToEDict(HotelId hotel, EHotelDict pers,
            PropDescription elem) {
        PropUtils.copyToEDict(pers, elem);
        pers.setHotel(hotel.getId());
    }

    public static void copyE(EHotelRoom dest, HotelRoom sou) {
        dest.setNoPersons(sou.getNoPersons());
    }

    public static <E extends EHotelDict> E getElem(EntityManager em,
            HotelId hotel, String qName, String name) {
        Query q = em.createNamedQuery(qName);
        q.setParameter(1, hotel.getId());
        q.setParameter(2, name);
        try {
            E pers = (E) q.getSingleResult();
            return pers;
        } catch (NoResultException e) {
            return null;
        }
    }

    public static <E extends EHotelDict> E getElemE(EntityManager em,
            HotelId hotel, String qName, String name) {
        E e = getElem(em, hotel, qName, name);
        if (e != null)
            return e;
        String mess = HHolder.getHM().getMess(IHError.HERROR021,
                IHMess.OBJECTBYNAMECANNOTBEFOUND, name, hotel.getHotel());
        log.log(Level.SEVERE, mess, e);
        throw new JythonUIFatal(mess);
    }

}
