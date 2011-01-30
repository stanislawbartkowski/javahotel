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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.javahotel.common.command.BillEnumTypes;
import com.javahotel.common.command.BookingEnumTypes;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.HotelOpType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.toobject.AddPaymentP;
import com.javahotel.common.toobject.BillP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.PaymentP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.common.util.BillUtil;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite27 extends TestHelper {

    @Test
    public void Test1() {
        System.out.println("Test1");
        loginuser();
        itest.setTodayDate(D("2008/12/08"));
        BookingP bok = createB();
        bok.setHotel(HOTEL1);
        bok.setName("");
        bok.setBookingType(BookingEnumTypes.Stay);
        setPay(bok, new ArrayList<PaymentP>());
        BillP bi = BillUtil.getBill(bok);
        bi.setBillType(BillEnumTypes.MainBill);
        List<AddPaymentP> add = new ArrayList<AddPaymentP>();
        AddPaymentP a = new AddPaymentP();
        a.setCustomerPrice(new BigDecimal(100));
        a.setLp(new Integer(1));
        a.setNoSe(new BigDecimal(1));
        a.setOfferPrice(new BigDecimal(200));
        a.setPayDate(D("2007/01/10"));
        a.setCustomerSum(new BigDecimal(200));
        ServiceDictionaryP servi = (ServiceDictionaryP) getDict(DictType.ServiceDict, "LUX");
        servi = getpersistName(DictType.ServiceDict, servi, "LUX");

        a.setRService(servi);
        add.add(a);
        bi.setAddpayments(add);

        ReturnPersist ret = hot.persistDicReturn(se, DictType.BookingList, bok);
        System.out.println(ret.getIdName());

        bok = getOneNameN(DictType.BookingList, ret.getIdName());

        bi = BillUtil.getBill(bok);

        add = bi.getAddpayments();
        assertEquals(1, add.size());
        a = null;
        for (AddPaymentP aa : add) {
            a = aa;
        }
        assertEquals("LUX", a.getRService().getName());
    }

    @Test
    public void Test2() {
        System.out.println("Test1");
        loginuser();
        itest.setTodayDate(D("2008/12/08"));
        BookingP bok = createB();
        bok.setHotel(HOTEL1);
        bok.setName("");
        bok.setBookingType(BookingEnumTypes.Stay);
        List<BillP> bil = bok.getBill();
        String bName = "rybka";
        BillP bi = new BillP();
        BillP bii = BillUtil.getBill(bok);
        bi.setCustomer(bii.getCustomer());
        bi.setName(bName);
        bil.add(bi);
        bi.setBillType(BillEnumTypes.MainBill);

        ReturnPersist ret = hot.persistDicReturn(se, DictType.BookingList, bok);
        System.out.println(ret.getIdName());

        ServiceDictionaryP servi = (ServiceDictionaryP) getDict(DictType.ServiceDict, "LUX");
        servi = getpersistName(DictType.ServiceDict, servi, "LUX");

        List<AddPaymentP> add = new ArrayList<AddPaymentP>();
        AddPaymentP a = new AddPaymentP();
        a.setCustomerPrice(new BigDecimal(100));
        a.setLp(new Integer(1));
        a.setNoSe(new BigDecimal(1));
        a.setOfferPrice(new BigDecimal(200));
        a.setCustomerPrice(new BigDecimal(200));
        a.setPayDate(D("2007/01/10"));
        a.setRService(servi);
        a.setSumOp(true);
        a.setCustomerSum(new BigDecimal(99));
        add.add(a);
        bi.setAddpayments(add);

        CommandParam pa = new CommandParam();
        BillP bili = new BillP();
        bili.setName(bName);
        bili.setBillType(BillEnumTypes.MainBill);
        
        pa.setBill(bili);
//        pa.setBillName(bName);
        pa.setReservName(ret.getIdName());
        pa.setHotel(HOTEL1);
        pa.setAddpayment(add);
        hotop.hotelOp(se, HotelOpType.PersistAddPayment, pa);

        bok = getOneNameN(DictType.BookingList, ret.getIdName());
        bi = BillUtil.getBill(bok, bName);
        add = bi.getAddpayments();
        assertEquals(1, add.size());
        a = null;
        for (AddPaymentP aa : add) {
            a = aa;
        }
        assertEquals("LUX", a.getRService().getName());

        for (int i = 0; i < 10; i++) {
            servi = (ServiceDictionaryP) getDict(DictType.ServiceDict, "LUX" + i);
            servi = getpersistName(DictType.ServiceDict, servi, "LUX" + i);
            a = new AddPaymentP();
            a.setCustomerPrice(new BigDecimal(100));
            a.setLp(new Integer(i + 1));
            a.setNoSe(new BigDecimal(1));
            a.setOfferPrice(new BigDecimal(200));
            a.setCustomerSum(new BigDecimal(200));
            a.setPayDate(D("2007/01/10"));
            a.setRService(servi);
            add.add(a);
        }

        pa.setAddpayment(add);
        hotop.hotelOp(se, HotelOpType.PersistAddPayment, pa);
        bi = BillUtil.getBill(bok, bName);
        System.out.println(bi.getId());
        assertNotNull(bi.getId());
        add = bi.getAddpayments();
        assertEquals(11, add.size());

    }

    @Test
    public void Test3() {
        System.out.println("Test1");
        loginuser();
        itest.setTodayDate(D("2008/12/08"));
        BookingP bok = createB();
        bok.setHotel(HOTEL1);
        bok.setName("");
        bok.setBookingType(BookingEnumTypes.Stay);
        ReturnPersist ret = hot.persistDicReturn(se, DictType.BookingList, bok);
        System.out.println(ret.getIdName());

        BillP bi = new BillP();
        BillP bii = BillUtil.getBill(bok);
        bi.setCustomer(bii.getCustomer());
        bi.setName("Rybka");
        bi.setBillType(BillEnumTypes.MainBill);

        ServiceDictionaryP servi = (ServiceDictionaryP) getDict(DictType.ServiceDict, "LUX");
        servi = getpersistName(DictType.ServiceDict, servi, "LUX");
        List<AddPaymentP> add = new ArrayList<AddPaymentP>();
        AddPaymentP a = new AddPaymentP();
        a.setCustomerPrice(new BigDecimal(100));
        a.setLp(new Integer(1));
        a.setNoSe(new BigDecimal(1));
        a.setOfferPrice(new BigDecimal(200));
        a.setCustomerPrice(new BigDecimal(200));
        a.setPayDate(D("2007/01/10"));
        a.setRService(servi);
        a.setSumOp(true);
        a.setCustomerSum(new BigDecimal(99));
        add.add(a);

        CommandParam pa = new CommandParam();
        pa.setReservName(ret.getIdName());
        pa.setHotel(HOTEL1);
        pa.setAddpayment(add);
        pa.setBill(bi);
        hotop.hotelOp(se, HotelOpType.PersistAddPayment, pa);

        bok = getOneNameN(DictType.BookingList, ret.getIdName());
        bi = BillUtil.getBill(bok, "Rybka");
        add = bi.getAddpayments();
        assertEquals(1,add.size());
        for (AddPaymentP aa : add) {
            System.out.println(aa.getLp());
        }

    }

   @Test
    public void Test4() {
        System.out.println("Test4");
        loginuser();
        itest.setTodayDate(D("2008/12/08"));
        BookingP bok = createB();
        bok.setHotel(HOTEL1);
        bok.setName("");
        bok.setBookingType(BookingEnumTypes.Stay);
        ReturnPersist ret = hot.persistDicReturn(se, DictType.BookingList, bok);
        System.out.println(ret.getIdName());

        BillP bi = new BillP();
        BillP bii = BillUtil.getBill(bok);
        bi.setBillType(BillEnumTypes.MainBill);
        bi.setCustomer(bii.getCustomer());
//        bi.setName("Rybka");

        ServiceDictionaryP servi = (ServiceDictionaryP) getDict(DictType.ServiceDict, "LUX");
        servi = getpersistName(DictType.ServiceDict, servi, "LUX");
        List<AddPaymentP> add = new ArrayList<AddPaymentP>();
        AddPaymentP a = new AddPaymentP();
        a.setCustomerPrice(new BigDecimal(100));
        a.setLp(new Integer(1));
        a.setNoSe(new BigDecimal(1));
        a.setOfferPrice(new BigDecimal(200));
        a.setCustomerSum(new BigDecimal(200));
        a.setPayDate(D("2007/01/10"));
        a.setRService(servi);
        a.setSumOp(true);
        a.setCustomerSum(new BigDecimal(99));
        add.add(a);

        CommandParam pa = new CommandParam();
        pa.setReservName(ret.getIdName());
        pa.setHotel(HOTEL1);
        pa.setAddpayment(add);
        pa.setBill(bi);
        ReturnPersist ret1 = hotop.hotelOpRet(se, HotelOpType.PersistAddPayment, pa);

        bok = getOneNameN(DictType.BookingList, ret.getIdName());
        for (BillP bp : bok.getBill()) {
            System.out.println(bp.getName());
            assertNotNull(bp.getName());
        }
        assertEquals(2,bok.getBill().size());
        bi = BillUtil.getBill(bok, ret1.getIdName());
        add = bi.getAddpayments();
        assertEquals(1,add.size());
        for (AddPaymentP aa : add) {
            System.out.println(aa.getLp());
            assertEquals(new Integer(1),aa.getLp());
        }
    }

}
