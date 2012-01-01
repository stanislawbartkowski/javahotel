/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.javahotel.common.command.BookingStateType;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.PaymentRowP;
import com.javahotel.common.toobject.ResDayObjectStateP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.types.LId;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite11 extends TestHelper {

    @Test
    public void Test1() {
        loginuser();
        BookingP bok = createB();
        OfferPriceP oPrice = getOfferPrice(bok.getSeason(), "Norm");
        bok.setCustomerPrice(new BigDecimal(999));
        bok.setOPrice("Norm");
        bok.setOPrice(oPrice.getName());
        BookElemP be = new BookElemP();
        ResObjectP rO = getResObject("1p");
        be.setResObject("1p");
        ServiceDictionaryP servi = (ServiceDictionaryP) getDict(DictType.ServiceDict,HOTEL1);
        servi = getpersistName(DictType.ServiceDict, servi, "LUX");
        be.setService("LUX");
        List<PaymentRowP> pList = new ArrayList<PaymentRowP>();
        PaymentRowP pRow = new PaymentRowP();
        pList.add(pRow);
        be.setPaymentrows(pList);

        be.setCheckIn(DateFormatUtil.toD("2008/02/07"));
        be.setCheckOut(DateFormatUtil.toD("2008/03/07"));
        List<BookElemP> colE = new ArrayList<BookElemP>();
        colE.add(be);
        bok.setBooklist(colE);

        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        be = null;
        for (BookElemP bb : bok.getBooklist()) {
            be = bb;
        }

        CommandParam par = new CommandParam();
        par.setHotel(HOTEL1);
        par.setDateFrom(DateFormatUtil.toD("2008/03/07"));
        par.setDateTo(DateFormatUtil.toD("2008/03/09"));
        List<AbstractTo> res = list.getList(se, RType.ResObjectState, par);
        assertEquals(0, res.size());
    }

    @Test
    public void Test2() {
        Test1();
        BookingP bok = getOneName(DictType.BookingList, "BOK0001");
        BookElemP be = null;
        for (BookElemP bb : bok.getBooklist()) {
            be = bb;
        }
        PaymentRowP pR = new PaymentRowP();
        List<PaymentRowP> colp = new ArrayList<PaymentRowP>();
        pR.setCustomerPrice(new BigDecimal(123));
        pR.setOfferPrice(new BigDecimal(333));
        pR.setRowFrom(DateFormatUtil.toD("2008/02/07"));
        pR.setRowTo(DateFormatUtil.toD("2008/03/07"));
        colp.add(pR);
        pR = new PaymentRowP();
        pR.setCustomerPrice(new BigDecimal(1234));
        pR.setOfferPrice(new BigDecimal(3334));
        pR.setRowFrom(DateFormatUtil.toD("2008/06/07"));
        pR.setRowTo(DateFormatUtil.toD("2008/06/07"));
        colp.add(pR);
        be.setPaymentrows(colp);
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");

        CommandParam par = new CommandParam();
        par.setHotel(HOTEL1);
        par.setDateFrom(DateFormatUtil.toD("2008/03/09"));
        par.setDateTo(DateFormatUtil.toD("2008/03/09"));
        List<AbstractTo> res = list.getList(se, RType.ResObjectState, par);
        assertEquals(0, res.size());

        par = new CommandParam();
        par.setHotel(HOTEL1);
        par.setDateFrom(DateFormatUtil.toD("2008/03/09"));
        par.setDateTo(DateFormatUtil.toD("2008/03/09"));
        par.setResListNo(0, "1p");
        res = list.getList(se, RType.ResObjectState, par);
        assertEquals(0, res.size());

        par = new CommandParam();
        par.setHotel(HOTEL1);
        par.setDateFrom(DateFormatUtil.toD("2008/02/07"));
        par.setDateTo(DateFormatUtil.toD("2008/02/08"));
        par.setResListNo(0, "1p");
        res = list.getList(se, RType.ResObjectState, par);
        assertEquals(2, res.size());
        int no = 0;
        for (final AbstractTo a : res) {
//        	List<AbstractTo> ap = hot.getDicList(se, DictType.PaymentRowList, new HotelT(HOTEL1));
            ResDayObjectStateP pp = (ResDayObjectStateP) a;
            LId rId = pp.getPaymentRowId();
            CommandParam cP = new CommandParam();
            cP.setHotel(HOTEL1);
            cP.setRecId(rId);
            cP.setDict(DictType.PaymentRowList);
            PaymentRowP rP = (PaymentRowP) list.getOne(se, RType.ListDict, cP);
            assertEquals("2008/02/07", DateFormatUtil.toS(rP.getRowFrom()));
            assertEquals("2008/03/07", DateFormatUtil.toS(rP.getRowTo()));
            switch (no) {
                case 0:
                    assertEquals("2008/02/07", DateFormatUtil.toS(pp.getD()));
                    break;
                case 1:
                    assertEquals("2008/02/08", DateFormatUtil.toS(pp.getD()));
                    break;
                default:
                    fail();
                    break;
            }
            no++;
        }
    }

    @Test
    public void Test3() {
        Test2();
        BookingP bok = getOneName(DictType.BookingList, "BOK0001");
        List<BookingStateP> cC = new ArrayList<BookingStateP>();
        BookingStateP pe = new BookingStateP();
        pe.setBState(BookingStateType.Confirmed);
        pe.setDateOp(DateFormatUtil.toD("2008/03/09"));
        pe.setLp(new Integer(1));
        cC.add(pe);
        pe = new BookingStateP();
        pe.setBState(BookingStateType.WaitingForConfirmation);
        pe.setDateOp(DateFormatUtil.toD("2008/03/10"));
        pe.setLp(new Integer(2));
        cC.add(pe);
        bok.setState(cC);
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");

        CommandParam par = new CommandParam();
        par.setHotel(HOTEL1);
        par.setDateFrom(DateFormatUtil.toD("2008/02/07"));
        par.setDateTo(DateFormatUtil.toD("2008/02/08"));
        par.setResListNo(0, "1p");
        List<AbstractTo> res = list.getList(se, RType.ResObjectState, par);
        assertEquals(2, res.size());
        int no = 0;
        for (final AbstractTo a : res) {
            ResDayObjectStateP pp = (ResDayObjectStateP) a;
            LId rId = pp.getPaymentRowId();
            CommandParam cP = new CommandParam();
            cP.setHotel(HOTEL1);
            cP.setRecId(rId);
            cP.setDict(DictType.PaymentRowList);
            PaymentRowP rP = (PaymentRowP) list.getOne(se, RType.ListDict, cP);
            assertEquals("2008/02/07", DateFormatUtil.toS(rP.getRowFrom()));
            assertEquals("2008/03/07", DateFormatUtil.toS(rP.getRowTo()));
            assertEquals(new Integer(2),pp.getLState().getLp());
            assertEquals(BookingStateType.WaitingForConfirmation,pp.getLState().getBState());
            switch (no) {
                case 0:
                    assertEquals("2008/02/07", DateFormatUtil.toS(pp.getD()));
                    break;
                case 1:
                    assertEquals("2008/02/08", DateFormatUtil.toS(pp.getD()));
                    break;
                default:
                    fail();
                    break;
            }
            no++;
        }
    }
}