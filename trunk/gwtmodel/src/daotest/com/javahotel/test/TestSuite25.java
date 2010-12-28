/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javahotel.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.javahotel.common.command.DictType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.GuestP;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.PaymentRowP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.common.util.GetMaxUtil;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.IHotelTest;
import com.javahotel.types.LId;

/**
 *
 * @author hotel
 */
public class TestSuite25 extends TestHelper {

    @Test
    public void Test1() {
        System.out.println("Test empty string");
        loginuser();
        itest.setTodayDate(D("2008/12/08"));
        BookingP bok = createB();
        bok.setHotel(HOTEL1);
        bok.setName("");
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
        rP.setOfferPrice(new BigDecimal(100));
        rP.setRowFrom(be.getCheckIn());
        rP.setRowTo(be.getCheckOut());
        List<PaymentRowP> rCol = new ArrayList<PaymentRowP>();
        rCol.add(rP);
        be.setPaymentrows(rCol);

        ReturnPersist ret = hot.persistDicReturn(se, DictType.BookingList, bok);
        System.out.println(ret.getIdName());
        assertEquals("1/12/2008", ret.getIdName());
        System.out.println("Now remove ..");
        bok = getOneNameN(DictType.BookingList, ret.getIdName());
        hot.removeDic(se, DictType.BookingList, bok);
        int no = itest.getNumberOfRecord(se, IHotelTest.BOOKINGPAYMENTREGISTER, new HotelT(HOTEL1));
        assertEquals(0, no);
    }

    @Test
    public void Test2() {
        System.out.println("Test set/get guests");
        loginuser();
        itest.setTodayDate(D("2008/12/08"));
        BookingP bok = createB();
        bok.setHotel(HOTEL1);
        bok.setName("");
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

        be.setCheckIn(D("2008/02/07"));
        be.setCheckOut(D("2008/02/08"));

        CustomerP cust = (CustomerP) getDict(DictType.CustomerList, HOTEL1);
        cust = getpersistName(DictType.CustomerList, cust, "CUST001");
        GuestP ge = new GuestP();
        ge.setCustomer(cust.getId());
        ge.setCheckIn(D("2009/02/07"));
        ge.setCheckOut(D("2009/02/08"));
        List<GuestP> gList = new ArrayList<GuestP>();
        gList.add(ge);
        be.setGuests(gList);

        List<BookElemP> colE = new ArrayList<BookElemP>();
        colE.add(be);
        p.setBooklist(colE);

//        PaymentRowP rP = new PaymentRowP();
//        rP.setCustomerPrice(new BigDecimal(100));
//        rP.setOfferPrice(new BigDecimal(100));
//        rP.setRowFrom(be.getCheckIn());
//        rP.setRowTo(be.getCheckOut());
//        List<PaymentRowP> rCol = new ArrayList<PaymentRowP>();
//        rCol.add(rP);
//        be.setPaymentrows(rCol);

        ReturnPersist ret = hot.persistDicReturn(se, DictType.BookingList, bok);
        System.out.println(ret.getIdName());

        bok = getOneNameN(DictType.BookingList, ret.getIdName());
        p = GetMaxUtil.getLastBookRecord(bok);
        be = null;
        for (BookElemP b : p.getBooklist()) {
            be = b;
        }
        gList = be.getGuests();
        assertNotNull(gList);
        assertEquals(1, gList.size());
        for (GuestP g : gList) {
            System.out.println(g.getCheckIn());
            System.out.println(g.getCheckOut());
            eqD("2009/02/07",g.getCheckIn());
            eqD("2009/02/08",g.getCheckOut());
        }

        cust = (CustomerP) getDict(DictType.CustomerList, HOTEL1);
        cust = getpersistName(DictType.CustomerList, cust, "CUST002");
        ge = new GuestP();
        ge.setCustomer(cust.getId());
        gList.add(ge);

        cust = (CustomerP) getDict(DictType.CustomerList, HOTEL1);
        cust = getpersistName(DictType.CustomerList, cust, "CUST003");
        ge = new GuestP();
        ge.setCustomer(cust.getId());
        gList.add(ge);

        ret = hot.persistDicReturn(se, DictType.BookingList, bok);
        System.out.println(ret.getIdName());

        bok = getOneNameN(DictType.BookingList, ret.getIdName());
        p = GetMaxUtil.getLastBookRecord(bok);
        be = null;
        for (BookElemP b : p.getBooklist()) {
            be = b;
        }
        gList = be.getGuests();
        assertNotNull(gList);
        assertEquals(3, gList.size());
        String[] li = new String[]{"CUST001", "CUST002", "CUST003"};
        for (GuestP g : gList) {
            LId id = g.getCustomer();
            CustomerP cu = getById(DictType.CustomerList, id);
            System.out.println(cu.getName());
            boolean found = false;
            for (int i = 0; i < li.length; i++) {
                if (li[i] == null) {
                    continue;
                }
                if (li[i].equals(cu.getName())) {
                    found = true;
                    li[i] = null;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    public void Test3() {
        System.out.println("Test null customer");
        loginuser();
        itest.setTodayDate(D("2008/12/08"));
        CustomerP cust = (CustomerP) getDict(DictType.CustomerList, HOTEL1);
        ReturnPersist ret = hot.persistDicReturn(se, DictType.CustomerList, cust);
        System.out.println(ret.getIdName());
    }
}
