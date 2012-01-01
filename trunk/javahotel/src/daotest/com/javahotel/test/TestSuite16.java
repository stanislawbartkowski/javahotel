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

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.PaymentMethod;
import com.javahotel.common.command.RType;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.PaymentP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.common.util.SumUtil;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite16 extends TestHelper {

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
        ServiceDictionaryP servi = (ServiceDictionaryP) getDict(
                DictType.ServiceDict, "LUX");
        servi = getpersistName(DictType.ServiceDict, servi, "LUX");
        be.setService("LUX");

        be.setCheckIn(DateFormatUtil.toD("2008/02/07"));
        be.setCheckOut(DateFormatUtil.toD("2008/03/07"));
        List<BookElemP> colE = new ArrayList<BookElemP>();
        colE.add(be);

        bok.setValidationAmount(new BigDecimal(100));
        bok.setValidationDate(D("2008/03/08"));
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");

        CommandParam par = new CommandParam();
        par.setHotel(HOTEL1);
        par.setDateFrom(DateFormatUtil.toD("2008/03/07"));
        par.setDateTo(DateFormatUtil.toD("2008/03/09"));
        List<AbstractTo> res = list.getList(se, RType.DownPayments, par);
        assertEquals(1, res.size());
        for (AbstractTo a : res) {
            BookingP dp = (BookingP) a;
            eqBig(new BigDecimal(100), dp.getValidationAmount());
            BigDecimal sum = SumUtil.sumPayment(dp);
            eqBig(new BigDecimal(0), sum);
        }

        List<PaymentP> pCol = new ArrayList<PaymentP>();
        PaymentP pp = new PaymentP();
        pp.setAmount(new BigDecimal(80));
        pp.setLp(new Integer(1));
        pp.setPayMethod(PaymentMethod.Cache);
        pp.setDatePayment(D("2008/11/11"));
        pCol.add(pp);
        pp = new PaymentP();
        pp.setAmount(new BigDecimal(10));
        pp.setLp(new Integer(2));
        pp.setPayMethod(PaymentMethod.Cache);
        pp.setDatePayment(D("2008/11/11"));
        pCol.add(pp);
        // bok.setPayments(pCol);
        addPay(bok, pCol);
        bok = getpersistName(DictType.BookingList, bok, "BOK0001");

        res = list.getList(se, RType.DownPayments, par);
        assertEquals(1, res.size());
        for (AbstractTo a : res) {
            BookingP dp = (BookingP) a;
            eqBig(new BigDecimal(100), dp.getValidationAmount());
            BigDecimal sum = SumUtil.sumPayment(dp);
            eqBig(new BigDecimal(0), sum);
            eqBig(new BigDecimal(70), sum);
        }

    }

}
