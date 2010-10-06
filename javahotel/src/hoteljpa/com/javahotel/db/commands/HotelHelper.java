/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.javahotel.db.commands;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.util.AbstractObjectFactory;
import com.javahotel.common.util.BillUtil;
import com.javahotel.db.context.ICommandContext;
import com.javahotel.db.copy.CommonCopyBean;
import com.javahotel.db.hotelbase.jpa.Bill;
import com.javahotel.db.hotelbase.jpa.Booking;
import com.javahotel.db.hotelbase.jpa.Payment;
import com.javahotel.db.hotelbase.jpa.RHotel;
import com.javahotel.db.hotelbase.queries.GetQueries;
import com.javahotel.db.hotelbase.types.IPureDictionary;
import com.javahotel.db.hoteldb.HotelStore;
import com.javahotel.dbjpa.ejb3.JpaDataId;
import com.javahotel.dbjpa.ejb3.JpaEntity;
import com.javahotel.dbres.log.HLog;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.SessionT;
import java.util.List;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class HotelHelper {

    private HotelHelper() {
    }

    static void removeAllDic(final ICommandContext iC, final DictType d,
            final HotelT ho) {
        List<?> c = GetQueries.getDList(iC, ObjectFactory.getC(d), d,
                true);
        iC.getJpa().removeList(c);
    }

    static void removeHo(final ICommandContext iC, final HotelT ho) {
        // re-read to make attached
        RHotel ro = iC.getJpa().getRecord(RHotel.class, iC.getRHotel().getId());
        iC.getJpa().removeObject(ro);
    }

    static JpaEntity getJPA(final SessionT se, final HotelT ho) {

        String databasename = HotelStore.getDatabase(se, ho);

        JpaEntity jpa = new JpaEntity(new JpaDataId(databasename), HLog.getL());
        return jpa;
    }

    static List<AbstractTo> toA(final ICommandContext iC,
            final List<?> sou, final DictType d) {
        List<AbstractTo> dest = new ArrayList<AbstractTo>();
        for (Object o : sou) {
            AbstractTo a = AbstractObjectFactory.getAbstract(d);
            CommonCopyBean.copyB(iC, o, a);
            dest.add(a);
        }
        return dest;
    }

    static BigDecimal sumPayment(List<Payment> col) {
        BigDecimal sum = new BigDecimal(0);
        for (Payment pa : col) {
            if (pa.isSumOp()) {
                sum = sum.add(pa.getAmount());
            } else {
                sum = sum.subtract(pa.getAmount());
            }
        }
        return sum;
    }

    static BigDecimal sumPayment(final Booking p) {
        List<Payment> col = getBill(p).getPayments();
        return sumPayment(col);
    }

    private static <T extends IPureDictionary> T getName(
            final List<? extends IPureDictionary> col, final String name) {
        for (IPureDictionary t : col) {
            if (t.getName().equals(name)) {
                return (T) t;
            }
        }
        return null;
    }

    public static Bill getBill(Booking p) {
        Bill b = getName(p.getBill(), BillUtil.DOWNSYMBOL);
        return b;
    }
}
