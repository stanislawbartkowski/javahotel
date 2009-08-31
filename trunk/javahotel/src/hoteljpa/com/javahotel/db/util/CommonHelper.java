/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.db.util;

import java.util.ArrayList;
import java.util.List;

import com.javahotel.db.context.ICommandContext;
import com.javahotel.db.hotelbase.jpa.OfferPrice;
import com.javahotel.db.hotelbase.jpa.RHotel;
import com.javahotel.db.hotelbase.queries.GetQueries;
import com.javahotel.db.hotelbase.types.IHotelDictionary;
import com.javahotel.db.hotelbase.types.IPureDictionary;
import com.javahotel.dbjpa.ejb3.JpaDataId;
import com.javahotel.dbjpa.ejb3.JpaEntity;
import com.javahotel.dbres.log.HLog;
import com.javahotel.dbres.resources.GetProp;
import com.javahotel.remoteinterfaces.HotelT;

public class CommonHelper {

    public static JpaEntity getAutJPA() {

        JpaEntity jpa = new JpaEntity(new JpaDataId(GetProp.getSeID()), HLog.getL());
        return jpa;
    }

    synchronized public static RHotel getH(final ICommandContext iC,
            final HotelT hotel) {
        RHotel o = iC.getJpa().getOneWhereRecord(RHotel.class, "name",
                hotel.getName());
        if (o == null) {
            o = new RHotel();
            o.setName(hotel.getName());
            addTraObject(iC, o);
        }
        return o;
    }

    private static <T> void addCol(final ICommandContext iC,
            final List<T> col) {
        for (T o : col) {
            iC.getJpa().addRecord(o);
        }
    }

    public static <T> void addTraList(final ICommandContext iC,
            final List<T> col) {

        if (iC.getJpa().tranIsActive()) {
            addCol(iC, col);
        } else {
            iC.getJpa().startTransaction(true);
            addCol(iC, col);
            iC.getJpa().endTransaction(true);
        }
    }

    static <T> void addTraObject(final ICommandContext iC, final T o) {
        List<T> col = new ArrayList<T>();
        col.add(o);
        addTraList(iC, col);

    }

    public static <T> T getA(final ICommandContext iC, final Class<?> cla,
            final String name) {
        IHotelDictionary o = GetQueries.getD(iC, cla, iC.getRHotel(), name);
        return (T) o;
    }

    public static OfferPrice getOneOffer(final ICommandContext iC,
            final String seasonName, final String offerName) {
        OfferPrice o = GetQueries.getOnePriceList(iC, seasonName, offerName);
        return o;
    }

    public static <T extends IPureDictionary> T getName(
            final List<? extends IPureDictionary> col, final String name) {
        for (IPureDictionary t : col) {
            if (t.getName().equals(name)) {
                return (T) t;
            }
        }
        return null;
    }
}
