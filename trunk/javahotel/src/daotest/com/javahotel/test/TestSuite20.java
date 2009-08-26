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
package com.javahotel.test;

import com.javahotel.common.command.DictType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.dateutil.DateUtil;
import com.javahotel.common.dateutil.PeriodT;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.PaymentRowP;
import com.javahotel.remoteinterfaces.HotelT;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite20 extends TestHelper {

    private void runB(String name, PeriodT[] per) {
        BookingP bok;
        System.out.println("Start " + name);
        for (int i = 0; i < per.length; i++) {
            Date dpocz = per[i].getFrom();
            Date dkon = per[i].getTo();
            bok = createB();
            bok.setHotel(HOTEL1);
            System.out.println("Persist " + name);
            BookElemP be = new BookElemP();
            be.setResObject("1p");
            be.setService("LUX");
            be.setCheckIn(dpocz);
            be.setCheckOut(dkon);
            Collection<BookElemP> colE = new ArrayList<BookElemP>();
            colE.add(be);

            BookRecordP p = new BookRecordP();
            p.setCustomerPrice(new BigDecimal(999));
            p.setDataFrom(dpocz);
            p.setLp(new Integer(1));
            p.setOPrice("Norm");
            Collection<BookRecordP> col = new ArrayList<BookRecordP>();
            col.add(p);
            bok.setBookrecords(col);
            p.setBooklist(colE);

            PaymentRowP pR = new PaymentRowP();
            Collection<PaymentRowP> colP = new ArrayList<PaymentRowP>();
            pR.setCustomerPrice(new BigDecimal(123));
            pR.setOfferPrice(new BigDecimal(333));
            pR.setRowFrom(dpocz);
            pR.setRowTo(dpocz);
            colP.add(pR);
            pR = new PaymentRowP();
            pR.setCustomerPrice(new BigDecimal(123));
            pR.setOfferPrice(new BigDecimal(333));
            pR.setRowFrom(dkon);
            pR.setRowTo(dkon);
            colP.add(pR);

            be.setPaymentrows(colP);
            ReturnPersist ret = hot.persistResBookingReturn(se, bok);

            System.out.println(DateFormatUtil.toS(dpocz) + " - " + DateFormatUtil.toS(dkon));
            System.out.println(name + " " + ret.getIdName());
        }
    }

    private class AddB extends Thread {

        private final String name;
        private final PeriodT[] per;

        AddB(String name, PeriodT[] per) {
            this.name = name;
            this.per = per;
        }

        @Override
        public void run() {
            runB(name, per);
        }
    }

    @Test
    public void Test1() throws InterruptedException {
        loginuser();
        itest.setTodayDate(D("2008/10/08"));
        BookingP bok = createB();
        Collection<BookRecordP> col = new ArrayList<BookRecordP>();
        BookRecordP p = new BookRecordP();
        OfferPriceP oPrice = getOfferPrice(bok.getSeason(), "Norm");
        p.setCustomerPrice(new BigDecimal(999));
        p.setDataFrom(DateFormatUtil.toD("2008/02/07"));
        p.setLp(new Integer(1));
        p.setOPrice("Norm");
        p.setOPrice(oPrice.getName());
        col.add(p);
        bok.setBookrecords(col);
        BookElemP be = new BookElemP();
        ResObjectP rO = getResObject("1p");
        be.setResObject("1p");
        ServiceDictionaryP servi = (ServiceDictionaryP) getDict(DictType.ServiceDict, "LUX");
        servi = getpersistName(DictType.ServiceDict, servi, "LUX");

//        runB("Thread 1", 50);

        PeriodT per[] = new PeriodT[50];
        Date dpocz = D("2008/10/01");
        for (int i = 0; i < per.length; i++) {
            Date dkon = DateUtil.copyDate(dpocz);
            DateUtil.NextDay(dkon);
            per[i] = new PeriodT(dpocz, dkon);
            dpocz = DateUtil.copyDate(dkon);
            DateUtil.NextDay(dpocz);
        }

        AddB ta[] = new AddB[10];
        for (int i = 0; i < ta.length; i++) {
            AddB t = new AddB("T" + i, per);
            ta[i] = t;
            t.start();
        }
        for (int i = 0; i < ta.length; i++) {
            ta[i].join();
        }

        Collection<DictionaryP> res = getDicList(se, DictType.BookingList, new HotelT(HOTEL1));
        assertEquals(50, res.size());
        int no100 = 0;
        for (DictionaryP pe : res) {
            if (pe.getName().equals("50/10/2008")) {
                no100++;
            }
        }
        assertEquals(1, no100);
    }
}
