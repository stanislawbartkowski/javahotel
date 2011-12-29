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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.javahotel.common.command.BookingStateType;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.PaymentRowP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.ServiceDictionaryP;
/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite21 extends TestHelper {

    @Test
    public void Test1() {
        loginuser();
        BookingP bok = createB();
        OfferPriceP oPrice = getOfferPrice(bok.getSeason(), "Norm");
        bok.setCustomerPrice(new BigDecimal(999));
        bok.setOPrice("Norm");
        bok.setOPrice(oPrice.getName());
        BookElemP be = new BookElemP();
        modifPaymentRow(be);
        ResObjectP rO = getResObject("1p");
        be.setResObject("1p");
        ServiceDictionaryP servi = (ServiceDictionaryP) getDict(DictType.ServiceDict, HOTEL1);
        servi = getpersistName(DictType.ServiceDict, servi, "LUX");
        be.setService("LUX");

        be.setCheckIn(DateFormatUtil.toD("2008/02/07"));
        be.setCheckOut(DateFormatUtil.toD("2008/02/08"));
        List<BookElemP> colE = new ArrayList<BookElemP>();
        colE.add(be);
        bok.setBooklist(colE);

        PaymentRowP pR = new PaymentRowP();
        List<PaymentRowP> colP = new ArrayList<PaymentRowP>();
        pR.setCustomerPrice(new BigDecimal(123));
        pR.setOfferPrice(new BigDecimal(333));
        pR.setRowFrom(DateFormatUtil.toD("2008/02/07"));
        pR.setRowTo(DateFormatUtil.toD("2008/02/08"));
        colP.add(pR);
        be.setPaymentrows(colP);

        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        
        BookingP bok1 = createB();
        bok1.setHotel(HOTEL1);
        bok1.setName("BOKRYBKA");
        oPrice = getOfferPrice(bok.getSeason(), "Norm");
        bok1.setCustomerPrice(new BigDecimal(999));
        bok1.setOPrice("Norm");
        bok1.setOPrice(oPrice.getName());
        be = new BookElemP();
        rO = getResObject("1p");
        be.setResObject("1p");
        servi = (ServiceDictionaryP) getDict(DictType.ServiceDict, HOTEL1);
        servi = getpersistName(DictType.ServiceDict, servi, "LUX");
        be.setService("LUX");

        be.setCheckIn(DateFormatUtil.toD("2008/02/07"));
        be.setCheckOut(DateFormatUtil.toD("2008/02/08"));
        modifPaymentRow(be);
        colE = new ArrayList<BookElemP>();
        colE.add(be);
        bok1.setBooklist(colE);

        
        ReturnPersist ret = hot.persistResBookingReturn(se, bok1);
        assertNull(ret.getIdName());
        
        bok = getOneNameN(DictType.BookingList,"BOK0001");
        BookingStateP bState = new BookingStateP();
        bState.setBState(BookingStateType.Canceled);
        bState.setLp(new Integer(1));
        List<BookingStateP> staCol = bok.getState();
        staCol.add(bState);
        
        hot.persistDic(se, DictType.BookingList,bok);

        ret = hot.persistResBookingReturn(se, bok1);
        assertNotNull(ret.getIdName());

    }
}