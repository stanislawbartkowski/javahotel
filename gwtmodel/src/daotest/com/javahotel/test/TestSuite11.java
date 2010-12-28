/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite11 extends TestHelper {

    @Test
    public void Test1() {
        loginuser();
        BookingP bok = createB();
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
        ServiceDictionaryP servi = (ServiceDictionaryP) getDict(DictType.ServiceDict,"LUX");
        servi = getpersistName(DictType.ServiceDict, servi, "LUX");
        be.setService("LUX");

        be.setCheckIn(DateFormatUtil.toD("2008/02/07"));
        be.setCheckOut(DateFormatUtil.toD("2008/03/07"));
        List<BookElemP> colE = new ArrayList<BookElemP>();
        colE.add(be);
        p.setBooklist(colE);

        bok = getpersistName(DictType.BookingList, bok, "BOK0001");
        col = bok.getBookrecords();
        p = null;
        for (BookRecordP pp : bok.getBookrecords()) {
            p = pp;
        }
        be = null;
        for (BookElemP bb : p.getBooklist()) {
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
        List<BookRecordP> col = bok.getBookrecords();
        BookRecordP p = null;
        for (BookRecordP pp : bok.getBookrecords()) {
            p = pp;
        }
        BookElemP be = null;
        for (BookElemP bb : p.getBooklist()) {
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