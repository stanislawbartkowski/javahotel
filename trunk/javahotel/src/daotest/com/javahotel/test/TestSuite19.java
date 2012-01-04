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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.javahotel.common.command.DictType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.PaymentRowP;
import com.javahotel.common.toobject.ResDayObjectStateP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.ServiceDictionaryP;


/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite19 extends TestHelper {

    @Test
    public void Test1() {
        loginuser();
        BookingP bok = createB();
        OfferPriceP oPrice = getOfferPrice(bok.getSeason(), "Norm");
        bok.setOPrice("Norm");
        bok.setOPrice(oPrice.getName());
        BookElemP be = new BookElemP();
        ResObjectP rO = getResObject("1p");
        be.setResObject("1p");
        ServiceDictionaryP servi = (ServiceDictionaryP) getDict(DictType.ServiceDict, HOTEL1);
        servi = getpersistName(DictType.ServiceDict, servi, "LUX");
        be.setService("LUX");

        be.setCheckIn(DateFormatUtil.toD("2008/02/07"));
        be.setCheckOut(DateFormatUtil.toD("2008/02/08"));
        modifPaymentRow(be);
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
        
        bok = createB();
        bok.setHotel(HOTEL1);
        bok.setName("BOKRYBKA");
        oPrice = getOfferPrice(bok.getSeason(), "Norm");
        bok.setOPrice("Norm");
        bok.setOPrice(oPrice.getName());
        be = new BookElemP();
        rO = getResObject("1p");
        be.setResObject("1p");
        servi = (ServiceDictionaryP) getDict(DictType.ServiceDict, HOTEL1);
        servi = getpersistName(DictType.ServiceDict, servi, "LUX");
        be.setService("LUX");

        be.setCheckIn(DateFormatUtil.toD("2008/02/07"));
        be.setCheckOut(DateFormatUtil.toD("2008/02/08"));
        colE = new ArrayList<BookElemP>();
        colE.add(be);
        bok.setBooklist(colE);

        
        ReturnPersist ret = hot.persistResBookingReturn(se, bok);
        assertNull(ret.getIdName());
        assertEquals(2,ret.getResState().size());
        for (ResDayObjectStateP pe : ret.getResState()) {
            System.err.println(pe.getBookName());
            assertEquals("BOK0001",pe.getBookName());
            assertEquals("1p",pe.getResObject());
        }
        
        BookingP bok1 = getOneNameN(DictType.BookingList,bok.getName());
        assertNull(bok1);
        
        be.setCheckIn(DateFormatUtil.toD("2008/02/08"));
        be.setCheckOut(DateFormatUtil.toD("2008/02/12"));
        colE = new ArrayList<BookElemP>();
        colE.add(be);
        bok.setBooklist(colE);
        ret = hot.persistResBookingReturn(se, bok);
        assertNull(ret.getIdName());
        assertEquals(1,ret.getResState().size());
        bok1 = getOneNameN(DictType.BookingList,bok.getName());
        assertNull(bok1);

        be.setCheckIn(DateFormatUtil.toD("2008/02/09"));
        be.setCheckOut(DateFormatUtil.toD("2008/02/12"));
        modifPaymentRow(be);
        colE = new ArrayList<BookElemP>();
        colE.add(be);
        bok.setBooklist(colE);
        ret = hot.persistResBookingReturn(se, bok);
        assertEquals("BOKRYBKA",ret.getIdName());
        
        bok1 = getOneNameN(DictType.BookingList,bok.getName());
        assertNotNull(bok1);

    }
}
