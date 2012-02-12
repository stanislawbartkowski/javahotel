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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.command.ServiceType;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.PaymentRowP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.ServiceDictionaryP;

/*
 * @author hotel
 * Test if reservation ignores all BookElem not related to reservation
 * Step1 : make reservation containing only 'not reserve' service
 * Step2 : get list of reservation
 * Expected result: should be 0
 */
public class TestSuite34 extends TestHelper {

    @Test
    public void Test1() {
        loginuser();
        // Step 1
        BookingP bok = createB();
        bok.setHotel(HOTEL1);
        bok.setName("BOKRYBKA");
        OfferPriceP oPrice = getOfferPrice(bok.getSeason(), "Norm");
        bok.setOPrice("Norm");
        bok.setOPrice(oPrice.getName());
        BookElemP be = new BookElemP();
        ResObjectP rO = getResObject("1p");
        be.setResObject("1p");
        ServiceDictionaryP servi = (ServiceDictionaryP) getDict(
                DictType.ServiceDict, HOTEL1);
        servi.setServType(ServiceType.INNE);
        servi = getpersistName(DictType.ServiceDict, servi, "LUX");
        be.setService("LUX");

        be.setCheckIn(DateFormatUtil.toD("2008/02/07"));
        be.setCheckOut(DateFormatUtil.toD("2008/02/08"));
        List<BookElemP> colE = new ArrayList<BookElemP>();
        colE.add(be);
        bok.setBooklist(colE);
        PaymentRowP rP = new PaymentRowP();
        rP.setCustomerRate(new BigDecimal(100));
        rP.setOfferRate(new BigDecimal(100));
        rP.setRowFrom(be.getCheckIn());
        rP.setRowTo(be.getCheckOut());
        List<PaymentRowP> rCol = new ArrayList<PaymentRowP>();
        rCol.add(rP);
        be.setPaymentrows(rCol);

        ReturnPersist ret = hot.persistDic(se, DictType.BookingList, bok);
        System.out.println(ret.getIdName());

        bok = getOneNameN(DictType.BookingList, ret.getIdName());

        servi = (ServiceDictionaryP) getDict(DictType.ServiceDict, HOTEL1);
        servi.setServType(ServiceType.INNE);
        servi = getpersistName(DictType.ServiceDict, servi, "BREAKFEST");

        be = new BookElemP();
        rO = getResObject("1p");
        be.setResObject("1p");
        be.setCheckIn(DateFormatUtil.toD("2008/02/07"));
        be.setCheckOut(DateFormatUtil.toD("2008/02/08"));
        be.setService("BREAKFEST");
        bok.getBooklist().add(be);
        rP = new PaymentRowP();
        rP.setCustomerRate(new BigDecimal(100));
        rP.setOfferRate(new BigDecimal(100));
        rP.setRowFrom(be.getCheckIn());
        rP.setRowTo(be.getCheckOut());
        rCol = new ArrayList<PaymentRowP>();
        rCol.add(rP);
        be.setPaymentrows(rCol);

        ret = hot.persistDic(se, DictType.BookingList, bok);
        System.out.println(ret.getErrorMessage());
        System.out.println(ret.getIdName());

        // Step 2
        CommandParam par = new CommandParam();
        par.setHotel(HOTEL1);
        par.setDateFrom(DateFormatUtil.toD("2008/02/01"));
        par.setDateTo(DateFormatUtil.toD("2008/03/09"));
        par.setResListNo(0, "1p");
        // Expected result
        List<AbstractTo> res = list.getList(se, RType.ResObjectState, par);
        assertEquals(0, res.size());

    }

}
