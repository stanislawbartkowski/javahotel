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

import java.math.BigDecimal;
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
        ho.setServiceType(sou.getServiceType());
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
        Query q = createHotelQuery(em, hotel, qName);
        q.setParameter(2, name);
        try {
            @SuppressWarnings("unchecked")
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

    public static Query createHotelQuery(EntityManager em, HotelId hotel,
            String query) {
        Query q = em.createNamedQuery(query);
        q.setParameter(1, hotel.getId());
        return q;
    }

    public static EHotelServices findService(EntityManager em, HotelId hotel,
            String s) {
        return getElemE(em, hotel, "findOneService", s);
    }

    public static void runQueryForObject(EntityManager em, Object o,
            String[] remQuery) {
        for (String r : remQuery) {
            Query q = em.createNamedQuery(r);
            q.setParameter(1, o);
            q.executeUpdate();
        }
    }

    public static void runQueryForHotels(EntityManager em, HotelId hotel,
            String[] remQuery) {
        runQueryForObject(em, hotel.getId(), remQuery);
    }
}
