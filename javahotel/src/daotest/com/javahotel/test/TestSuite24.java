/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javahotel.test;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.javahotel.common.command.BookingStateType;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RType;
import com.javahotel.common.command.ReturnPersist;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.BookElemP;
import com.javahotel.common.toobject.BookRecordP;
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
 * @author hotel
 */
public class TestSuite24 extends TestHelper {

    @Test
    public void Test1() {
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
        rP.setOfferPrice(new BigDecimal(100));
        rP.setRowFrom(be.getCheckIn());
        rP.setRowTo(be.getCheckOut());
        List<PaymentRowP> rCol = new ArrayList<PaymentRowP>();
        rCol.add(rP);
        be.setPaymentrows(rCol);

        ReturnPersist ret = hot.persistDicReturn(se, DictType.BookingList, bok);

        CommandParam par = new CommandParam();
        par.setHotel(HOTEL1);
        par.setDateFrom(DateFormatUtil.toD("2008/02/01"));
        par.setDateTo(DateFormatUtil.toD("2008/03/09"));
        par.setResListNo(0, "1p");
        List<AbstractTo> res = list.getList(se, RType.ResObjectState, par);
        assertEquals(2, res.size());


        bok = getOneNameN(DictType.BookingList, ret.getIdName());

        BookRecordP p1 = new BookRecordP();
        p1.setCustomerPrice(new BigDecimal(999));
        p1.setDataFrom(DateFormatUtil.toD("2008/02/07"));
        p1.setLp(new Integer(2));
        p1.setOPrice("Norm");
        p1.setOPrice(oPrice.getName());

        BookElemP be1 = new BookElemP();
        be1.setResObject("1p");
        be1.setService("LUX");
        be1.setCheckIn(DateFormatUtil.toD("2008/02/07"));
        be1.setCheckOut(DateFormatUtil.toD("2008/02/09"));
        List<BookElemP> colE1 = new ArrayList<BookElemP>();
        colE1.add(be1);
        p1.setBooklist(colE1);
        PaymentRowP rP1 = new PaymentRowP();
        rP1.setCustomerPrice(new BigDecimal(200));
        rP1.setOfferPrice(new BigDecimal(200));
        rP1.setRowFrom(be1.getCheckIn());
        rP1.setRowTo(be1.getCheckOut());
        List<PaymentRowP> rCol1 = new ArrayList<PaymentRowP>();
        rCol1.add(rP1);
        be1.setPaymentrows(rCol1);

        bok.getBookrecords().add(p1);

        hot.persistDic(se, DictType.BookingList, bok);
        res = list.getList(se, RType.ResObjectState, par);
        assertEquals(3, res.size());
        bok = getOneNameN(DictType.BookingList, ret.getIdName());
        for (AbstractTo a : res) {
            ResDayObjectStateP re = (ResDayObjectStateP) a;
            System.out.println(re.getBookName());
            System.out.println(re.getLState().getBState());
            System.out.println(re.getD());
            LId id = re.getPaymentRowId();
            BigDecimal bi = null;
            for (BookRecordP br : bok.getBookrecords()) {
                for (BookElemP el : br.getBooklist()) {
                    for (PaymentRowP pr : el.getPaymentrows()) {
                        if (pr.getId().equals(id)) {
                            if (bi == null) {
                                bi = pr.getCustomerPrice();
                            }
                        }
                    }
                }
            }
            eqBig(new BigDecimal(200), bi);
        }

        int lp = 3;
        int amou = 200;
        for (int i = 0; i < 20; i++) {
            BookRecordP p2 = new BookRecordP();
            p2.setCustomerPrice(new BigDecimal(999));
            p2.setDataFrom(DateFormatUtil.toD("2008/02/07"));
            p2.setLp(new Integer(lp));
            lp++;
            p2.setOPrice("Norm");
            p2.setOPrice(oPrice.getName());

            BookElemP be2 = new BookElemP();
            be2.setResObject("1p");
            be2.setService("LUX");
            be2.setCheckIn(DateFormatUtil.toD("2008/02/07"));
            be2.setCheckOut(DateFormatUtil.toD("2008/02/10"));
            List<BookElemP> colE2 = new ArrayList<BookElemP>();
            colE2.add(be2);
            p2.setBooklist(colE2);
            PaymentRowP rP2 = new PaymentRowP();
            amou += 100;
            rP2.setCustomerPrice(new BigDecimal(amou));
            rP2.setOfferPrice(new BigDecimal(amou));
            rP2.setRowFrom(be2.getCheckIn());
            rP2.setRowTo(be2.getCheckOut());
            List<PaymentRowP> rCol2 = new ArrayList<PaymentRowP>();
            rCol2.add(rP2);
            be2.setPaymentrows(rCol2);
            bok.getBookrecords().add(p2);
        }

        hot.persistDic(se, DictType.BookingList, bok);
        res = list.getList(se, RType.ResObjectState, par);
        assertEquals(4, res.size());

        bok = getOneNameN(DictType.BookingList, ret.getIdName());
        for (AbstractTo a : res) {
            ResDayObjectStateP re = (ResDayObjectStateP) a;
            System.out.println(re.getBookName());
            System.out.println(re.getLState().getBState());
            System.out.println(re.getD());
            LId id = re.getPaymentRowId();
            BigDecimal bi = null;
            for (BookRecordP br : bok.getBookrecords()) {
                for (BookElemP el : br.getBooklist()) {
                    for (PaymentRowP pr : el.getPaymentrows()) {
                        if (pr.getId().equals(id)) {
                            if (bi == null) {
                                bi = pr.getCustomerPrice();
                            }
                        }
                    }
                }
            }
            eqBig(new BigDecimal(amou), bi);
        }


        System.out.println("Now cancel");
        BookingStateP bState = new BookingStateP();
        bState.setBState(BookingStateType.Canceled);
        bState.setLp(new Integer(2));
        bok.getState().add(bState);
        hot.persistDic(se, DictType.BookingList, bok);
        res = list.getList(se, RType.ResObjectState, par);
        assertEquals(0, res.size());
//        for (AbstractTo a : res) {
//            ResDayObjectStateP re = (ResDayObjectStateP) a;
//            System.out.println(re.getBookName());
//            System.out.println(re.getLState().getBState());
//            System.out.println(re.getD());
//
//        }
        System.out.println("Add new reserv");

        bok = createB();
        bok.setHotel(HOTEL1);
        col = new ArrayList<BookRecordP>();
        p = new BookRecordP();
        p.setCustomerPrice(new BigDecimal(999));
        p.setDataFrom(DateFormatUtil.toD("2008/02/07"));
        p.setLp(new Integer(1));
        p.setOPrice("Norm");
        p.setOPrice(oPrice.getName());
        col.add(p);
        bok.setBookrecords(col);
        be = new BookElemP();
        rO = getResObject("1p");
        be.setResObject("1p");
        be.setService("LUX");

        be.setCheckIn(DateFormatUtil.toD("2008/02/07"));
        be.setCheckOut(DateFormatUtil.toD("2008/02/08"));
        colE = new ArrayList<BookElemP>();
        colE.add(be);
        p.setBooklist(colE);
        rP = new PaymentRowP();
        rP.setCustomerPrice(new BigDecimal(500));
        rP.setOfferPrice(new BigDecimal(500));
        rP.setRowFrom(be.getCheckIn());
        rP.setRowTo(be.getCheckOut());
        rCol = new ArrayList<PaymentRowP>();
        rCol.add(rP);
        be.setPaymentrows(rCol);

        ret = hot.persistDicReturn(se, DictType.BookingList, bok);
        System.out.println(ret.getIdName());

        res = list.getList(se, RType.ResObjectState, par);
        assertEquals(2, res.size());
        bok = getOneNameN(DictType.BookingList, ret.getIdName());
        for (AbstractTo a : res) {
            ResDayObjectStateP re = (ResDayObjectStateP) a;
            System.out.println(re.getBookName());
            System.out.println(re.getLState().getBState());
            System.out.println(re.getD());
            LId id = re.getPaymentRowId();
            BigDecimal bi = null;
            for (BookRecordP br : bok.getBookrecords()) {
                for (BookElemP el : br.getBooklist()) {
                    for (PaymentRowP pr : el.getPaymentrows()) {
                        if (pr.getId().equals(id)) {
                            if (bi == null) {
                                bi = pr.getCustomerPrice();
                            }
                        }
                    }
                }
            }
            eqBig(new BigDecimal(500), bi);
        }
    }
}

