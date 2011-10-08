/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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

import com.javahotel.common.command.BookingEnumTypes;
import com.javahotel.common.command.BookingStateType;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.HotelOpType;
import com.javahotel.common.command.PaymentMethod;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.PaymentP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.common.util.GetMaxUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.toobject.PaymentRowP;
import com.javahotel.common.toobject.ResObjectP;


/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite22 extends TestHelper {

    @Test
    public void Test1() {
        loginuser();
        BookingP bok = createB();
        bok.setHotel(HOTEL1);
        ReturnPersist ret = hot.persistDicReturn(se, DictType.BookingList, bok);
        System.out.println(ret.getIdName());

        CommandParam pa = new CommandParam();
        pa.setReservName(ret.getIdName());
        pa.setHotel(HOTEL1);
        ReturnPersist ret1 = hotop.hotelOpRet(se, HotelOpType.ChangeBookingToStay, pa);
        System.out.println(ret1.getIdName());

        bok = getOneNameN(DictType.BookingList, ret.getIdName());
        BookingStateP sta = GetMaxUtil.getLastStateRecord(bok);
        assertEquals(BookingStateType.ChangedToCheckin, sta.getBState());

        bok = getOneNameN(DictType.BookingList, ret1.getIdName());
        assertEquals(ret.getIdName(), bok.getResName());
        assertEquals(BookingEnumTypes.Stay, bok.getBookingType());
        sta = GetMaxUtil.getLastStateRecord(bok);
        assertEquals(BookingStateType.Stay, sta.getBState());
    }

    @Test
    public void Test2() {
        loginuser();
        BookingP bok = createB();
        bok.setHotel(HOTEL1);
        PaymentP pay = new PaymentP();
        pay.setAmount(new BigDecimal(90));
        pay.setSumOp(true);
        pay.setDatePayment(D("2008/02/02"));
        pay.setPayMethod(PaymentMethod.Cache);
        pay.setLp(new Integer(1));
        List<PaymentP> colP = new ArrayList<PaymentP>();
        colP.add(pay);
        setPay(bok, colP);

        ReturnPersist ret = hot.persistDicReturn(se, DictType.BookingList, bok);
        System.out.println(ret.getIdName());

        CommandParam pa = new CommandParam();
        pa.setReservName(ret.getIdName());
        pa.setHotel(HOTEL1);
        ReturnPersist ret1 = hotop.hotelOpRet(se, HotelOpType.ChangeBookingToStay, pa);
        System.out.println(ret1.getIdName());

        bok = getOneNameN(DictType.BookingList, ret.getIdName());
        colP = getPay(bok);
        assertEquals(2, colP.size());
        int i = 0;
        for (PaymentP pe : colP) {
            i++;
            System.out.println(pe.getAmount());
            eqBig(new BigDecimal(90), pe.getAmount());
            switch (i) {
                case 1:
                    assertTrue(pe.isSumOp());
                    break;
                case 2:
                    assertFalse(pe.isSumOp());
                    break;
                default:
                    fail();
            }
        }

        bok = getOneNameN(DictType.BookingList, ret1.getIdName());
        colP = getFPay(bok);
        assertEquals(1, colP.size());
    }

    @Test
    public void Test3() {
        loginuser();
        BookingP bok = createB();
        bok.setHotel(HOTEL1);
        bok.setName("BOKRYBKA");
        List<BookRecordP> col = new ArrayList<BookRecordP>();
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
        be.setService("LUX");

        be.setCheckIn(DateFormatUtil.toD("2008/02/07"));
        be.setCheckOut(DateFormatUtil.toD("2008/02/08"));
        List<BookElemP> colE = new ArrayList<BookElemP>();
        colE.add(be);
        p.setBooklist(colE);
        PaymentRowP rP = new PaymentRowP();
        rP.setCustomerPrice(new BigDecimal(100));
        rP.setOfferPrice(new  BigDecimal(100));
        rP.setRowFrom(be.getCheckIn());
        rP.setRowTo(be.getCheckOut());
        List<PaymentRowP> rCol = new ArrayList<PaymentRowP>();
        rCol.add(rP);
        be.setPaymentrows(rCol);
        
        ReturnPersist ret = hot.persistDicReturn(se, DictType.BookingList, bok);

        CommandParam pa = new CommandParam();
        pa.setReservName(ret.getIdName());
        pa.setHotel(HOTEL1);
        ReturnPersist ret1 = hotop.hotelOpRet(se, HotelOpType.ChangeBookingToStay, pa);
        System.out.println(ret1.getIdName());
        
        bok = getOneNameN(DictType.BookingList, ret1.getIdName());
        col = bok.getBookrecords();
        assertEquals(1,col.size());
        for (BookRecordP r : col) {
            assertEquals(1,r.getBooklist().size());
            for (BookElemP el : r.getBooklist()) {
                assertEquals(1,el.getPaymentrows().size());
            }
        }

    }
}
