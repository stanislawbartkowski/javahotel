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
package com.javahotel.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.gwtmodel.table.common.PeriodT;
import com.gwtmodel.table.common.dateutil.DateFormatUtil;
import com.gwtmodel.table.common.dateutil.DateUtil;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.PaymentRowP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.remoteinterfaces.HotelT;

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
            modifPaymentRow(be);
            List<BookElemP> colE = new ArrayList<BookElemP>();
            colE.add(be);

            bok.setOPrice("Norm");
            bok.setBooklist(colE);

            PaymentRowP pR = new PaymentRowP();
            List<PaymentRowP> colP = new ArrayList<PaymentRowP>();
            pR.setCustomerRate(new BigDecimal(123));
            pR.setOfferRate(new BigDecimal(333));
            pR.setRowFrom(dpocz);
            pR.setRowTo(dpocz);
            colP.add(pR);
            pR = new PaymentRowP();
            pR.setCustomerRate(new BigDecimal(123));
            pR.setOfferRate(new BigDecimal(333));
            pR.setRowFrom(dkon);
            pR.setRowTo(dkon);
            colP.add(pR);

            be.setPaymentrows(colP);
            ReturnPersist ret = hot.persistResBookingReturn(se, bok);

            System.out.println(DateFormatUtil.toS(dpocz) + " - "
                    + DateFormatUtil.toS(dkon));
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
        OfferPriceP oPrice = getOfferPrice(bok.getSeason(), "Norm");
        bok.setOPrice("Norm");
        bok.setOPrice(oPrice.getName());
        BookElemP be = new BookElemP();
        ResObjectP rO = getResObject("1p");
        be.setResObject("1p");
        modifPaymentRow(be);
        ServiceDictionaryP servi = (ServiceDictionaryP) getDict(
                DictType.ServiceDict, HOTEL1);
        servi = getpersistName(DictType.ServiceDict, servi, HOTEL1);

        // runB("Thread 1", 50);

        PeriodT per[] = new PeriodT[50];
        Date dpocz = D("2008/10/01");
        for (int i = 0; i < per.length; i++) {
            // Date dkon = DateUtil.copyDate(dpocz);
            Date dkon = DateUtil.NextDayD(dpocz);
            per[i] = new PeriodT(dpocz, dkon);
            // dpocz = DateUtil.copyDate(dkon);
            dpocz = DateUtil.NextDayD(dkon);
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

        List<DictionaryP> res = getDicList(se, DictType.BookingList,
                new HotelT(HOTEL1));
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
