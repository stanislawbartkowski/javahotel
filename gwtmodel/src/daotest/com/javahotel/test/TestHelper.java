/*
 * Copyright 2009 stanislawbartkowski@gmail.com 
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
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;

import com.javahotel.common.command.BookingEnumTypes;
import com.javahotel.common.command.BookingStateType;
import com.javahotel.common.command.CommandParam;
import com.javahotel.common.command.CustomerType;
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.PaymentMethod;
import com.javahotel.common.command.RRoom;
import com.javahotel.common.command.RType;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.AdvancePaymentP;
import com.javahotel.common.toobject.BillP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.toobject.BookingStateP;
import com.javahotel.common.toobject.CustomerP;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.HotelP;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.OfferSeasonPeriodP;
import com.javahotel.common.toobject.OfferServicePriceP;
import com.javahotel.common.toobject.PaymentP;
import com.javahotel.common.toobject.RemarkP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.RoomStandardP;
import com.javahotel.common.toobject.SeasonPeriodT;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.common.toobject.ServiceType;
import com.javahotel.common.toobject.VatDictionaryP;
import com.javahotel.common.util.BillUtil;
import com.javahotel.dbutil.log.GetLogger;
//import com.javahotel.javatest.exttest.ExtTestHelper;
import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.IAuthentication;
import com.javahotel.remoteinterfaces.IHotelData;
import com.javahotel.remoteinterfaces.IHotelOp;
import com.javahotel.remoteinterfaces.IHotelTest;
import com.javahotel.remoteinterfaces.IList;
import com.javahotel.remoteinterfaces.ISecurity;
import com.javahotel.remoteinterfaces.PasswordT;
import com.javahotel.remoteinterfaces.SessionT;
import com.javahotel.types.LId;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
abstract public class TestHelper {

    protected ISecurity sec;
    protected SessionT se;
    protected SessionT seu;
    protected IList list;
    protected IAuthentication aut;
    protected IHotelTest itest;
    protected IHotelData hot;
    protected IHotelOp hotop;
    protected final String SESSION = "AAAAAA";
    protected final String SESSIONU = "AAAAAAU";
    protected final GetLogger log = new GetLogger("com.javahotel.javatest");
    protected static final String HOTEL1 = "hotel1";
    protected static final String HOTEL2 = "hotel2";
    
    private static ISetUpTestEnvironment iSetupx = null;
        
    public static void setiSetup(ISetUpTestEnvironment iSetup) {
        TestHelper.iSetupx = iSetup;
    }

    protected void setUpG() {
//        if (iSetup != null) {
//            iSetup.setUp();
//        }
//        beforeTest();
        sec = TestUtil.getSe();
        aut = TestUtil.getAu();
        list = TestUtil.getList();
        hot = TestUtil.getHotel();
        itest = TestUtil.getHotelTest();
        hotop = TestUtil.getHotelOp();
    }

    protected void tearDownG() {
        itest.setTodayDate(null);
//        if (iSetup != null) {
//            iSetup.tearDown();
//        }
//        afterTest();
    }

    @After
    public void tearDown() {
        tearDownG();
    }

    protected void login() {
        se = sec.loginadminSession(SESSION, "admin", new PasswordT("rybka"));
        TestUtil.testStart(se, sec);
    }

    protected void loginuser() {
        aut.persistPerson(se, "user1", new PasswordT("password1"));
        aut.persistHotel(se, new HotelT(HOTEL1), "super hotel 1", "hoteldb");
        aut.persistHotel(se, new HotelT(HOTEL2), "super hotel 2", "hoteldb");
        seu = sec.loginSession(SESSIONU, "user1", new PasswordT("password1"));
    }

    @Before
    public void setUp() throws Exception {
        setUpG();
        TestUtil.propStart(se, sec);
        login();
        TestUtil.testStart(se, sec);
    	clearAll(); // added because appengine does not support "clear database"
    }

    protected void clearAll() throws Exception {
        hot.clearHotelBase(se, new HotelT(HOTEL1));
        hot.clearHotelBase(se, new HotelT(HOTEL2));
        aut.clearAuthBase(se);
        List<HotelP> res = aut.getHotelList(se);
        assertEquals(0, res.size());
    }

    protected <T> T getOneName(DictType d, final String name) {
        List<AbstractTo> res = hot.getDicList(se, d, new HotelT(HOTEL1));
        assertNotNull(res);
//        assertEquals(1, res.size());
        T out = null;
        int no = 0;
        for (final AbstractTo aa : res) {
            DictionaryP o = (DictionaryP) aa;
            if (o.getName().equals(name)) {
                out = (T) o;
                no++;
            }
        }
        assertNotNull(out);
        assertEquals(1, no);
        return out;
    }

    protected <T extends AbstractTo> T getOneNameN(DictType d, final String name) {
        CommandParam pa = new CommandParam();
        pa.setDict(d);
        pa.setHotel(HOTEL1);
        pa.setRecName(name);
        T out = (T) list.getOne(se, RType.ListDict, pa);
        return out;
    }

    protected <T extends AbstractTo> T getById(DictType d, final LId id) {
        CommandParam pa = new CommandParam();
        pa.setDict(d);
        pa.setHotel(HOTEL1);
        pa.setRecId(id);
        T out = (T) list.getOne(se, RType.ListDict, pa);
        return out;
    }

    protected <T> T getpersistName(DictType d, DictionaryP cP, final String name) {
        cP.setName(name);
        cP.setHotel(HOTEL1);
        hot.persistDic(seu, d, cP);
        Object res = getOneName(d, name);
        return (T) res;
    }

    protected OfferSeasonP getOfferSeason(final String name) {
        OfferSeasonP sep = new OfferSeasonP();
        sep.setStartP(DateFormatUtil.toD("2008/01/01"));
        sep.setEndP(DateFormatUtil.toD("2008/10/02"));
        sep = getpersistName(DictType.OffSeasonDict, sep, name);
        return sep;
    }

    protected OfferPriceP getOfferPrice(final String sName, final String name) {
        OfferPriceP oP = new OfferPriceP();
        oP.setSeason(sName);
        oP.setHotel(HOTEL1);
        oP = getpersistName(DictType.PriceListDict, oP, name);
        return oP;
    }

    protected ResObjectP getResObject(final String name) {
        ResObjectP rp = new ResObjectP();
        rp.setRType(RRoom.Room);
        RoomStandardP st = new RoomStandardP();
        st.setName("1p");
        rp.setRStandard(st);
        rp = getpersistName(DictType.RoomObjects, rp, name);
        return rp;
    }

    protected BookingP createB() {
        CustomerP cP = new CustomerP();
        cP.setCType(CustomerType.Person);
        List<RemarkP> rCol = new ArrayList<RemarkP>();
        RemarkP r1 = new RemarkP();
        r1.setRemark("rybka");
        r1.setAddDate(DateFormatUtil.toT("2008/01/01"));
        r1.setLp(new Integer(1));
        rCol.add(r1);
        r1 = new RemarkP();
        r1.setRemark("rurka");
        r1.setLp(new Integer(2));
        r1.setAddDate(DateFormatUtil.toT("2008/02/01"));
        rCol.add(r1);
        cP.setRemarks(rCol);
        cP = getpersistName(DictType.CustomerList, cP, "K001");

        OfferSeasonP oP = new OfferSeasonP();
        oP.setStartP(DateFormatUtil.toD("2008/10/01"));
        oP.setEndP(DateFormatUtil.toD("2008/10/30"));
        oP = getpersistName(DictType.OffSeasonDict, oP, "CUR");

        BookingStateP bState = new BookingStateP();
        bState.setBState(BookingStateType.Confirmed);
        bState.setLp(new Integer(1));
        List<BookingStateP> staCol = new ArrayList<BookingStateP>();
        staCol.add(bState);


        BookingP bok = new BookingP();
        bok.setCheckIn(DateFormatUtil.toD("2008/02/02"));
        bok.setCheckOut(DateFormatUtil.toD("2008/05/05"));
        bok.setNoPersons(new Integer(2));
        bok.setCustomer(cP.getId());
        bok.setSeason(oP.getName());
        bok.setState(staCol);
        bok.setBookingType(BookingEnumTypes.Reservation);
        setPay(bok,new ArrayList<PaymentP>());
        return bok;
    }

    List<DictionaryP> getDicList(final SessionT sessionId, final DictType d, HotelT hotel) {
        List res = hot.getDicList(sessionId, d, hotel);
        return res;
    }

    DictionaryP getDict(DictType t, String hotel) {

        DictionaryP a;
        switch (t) {
            case RoomObjects:
                ResObjectP rp = new ResObjectP();
                rp.setRType(RRoom.Room);
                RoomStandardP st = new RoomStandardP();
                st.setName("1p");
                rp.setRStandard(st);
                a = rp;
                break;
            case ServiceDict:
                ServiceDictionaryP sP = new ServiceDictionaryP();
                sP.setServType(ServiceType.DOSTAWKA);
                VatDictionaryP va = new VatDictionaryP();
                va.setName("zw");
                va.setVatPercent(new BigDecimal("10"));
                sP.setVat(va);
                a = sP;
                break;
            case OffSeasonDict:
                OfferSeasonP oP = new OfferSeasonP();
                oP.setStartP(DateFormatUtil.toD("2008/10/01"));
                oP.setEndP(DateFormatUtil.toD("2008/10/30"));
                a = oP;
                break;
            case PriceListDict:
                OfferSeasonP sep = new OfferSeasonP();
                sep.setHotel(hotel);
                sep.setName("GL");
                sep.setStartP(DateFormatUtil.toD("2008/01/01"));
                sep.setEndP(DateFormatUtil.toD("2008/10/02"));

                OfferSeasonPeriodP pe = new OfferSeasonPeriodP();
                List<OfferSeasonPeriodP> seL = new ArrayList<OfferSeasonPeriodP>();
                pe.setStartP(DateFormatUtil.toD("2008/03/11"));
                pe.setEndP(DateFormatUtil.toD("2008/06/11"));
                pe.setPeriodT(SeasonPeriodT.LOW);
                pe.setPId(new Long(1));
                seL.add(pe);
                sep.setPeriods(seL);

                hot.persistDic(seu, DictType.OffSeasonDict, sep);


                VatDictionaryP va1 = new VatDictionaryP();
                va1.setHotel(hotel);
                va1.setName("zw");
                va1.setVatPercent(new BigDecimal("10"));
                hot.persistDic(seu, DictType.VatDict, va1);

                ServiceDictionaryP sP1 = new ServiceDictionaryP();
                sP1.setHotel(hotel);
                sP1.setServType(ServiceType.DOSTAWKA);
                sP1.setName("DOST");
                va1 = new VatDictionaryP();
                va1.setName("zw");
                va1.setVatPercent(new BigDecimal("10"));
                sP1.setVat(va1);
                hot.persistDic(seu, DictType.ServiceDict, sP1);

                OfferPriceP oP1 = new OfferPriceP();
                oP1.setSeason("GL");
//                oP1.setService("DOST");
                oP1.setHotel(sP1.getHotel());

                OfferServicePriceP osp;
                List<OfferServicePriceP> lP = new ArrayList<OfferServicePriceP>();
                osp = new OfferServicePriceP();
//                osp.setSpecialperiod(new Long(1));
//                osp.setPrice(new BigDecimal("123"));
                osp.setService("DOST");
                lP.add(osp);
                oP1.setServiceprice(lP);

                a = oP1;
                break;

            case CustomerList:
                CustomerP cP = new CustomerP();
                cP.setCType(CustomerType.Person);
                List<RemarkP> rCol = new ArrayList<RemarkP>();
                RemarkP r1 = new RemarkP();
                r1.setRemark("rybka");
                r1.setAddDate(DateFormatUtil.toT("2008/01/01"));
                r1.setLp(new Integer(1));
                rCol.add(r1);
                r1 = new RemarkP();
                r1.setRemark("rurka");
                r1.setLp(new Integer(2));
                r1.setAddDate(DateFormatUtil.toT("2008/02/01"));
                rCol.add(r1);
                cP.setRemarks(rCol);
                a = cP;
                break;
                
            case BookingList:
                CustomerP cP1 = new CustomerP();
                cP1.setCType(CustomerType.Person);
                List<RemarkP> rCol1 = new ArrayList<RemarkP>();
                RemarkP r2 = new RemarkP();
                r2.setRemark("rybka");
                r2.setAddDate(DateFormatUtil.toT("2008/01/01"));
                r2.setLp(new Integer(1));
                rCol1.add(r2);
                r1 = new RemarkP();
                r1.setRemark("rurka");
                r1.setLp(new Integer(2));
                r1.setAddDate(DateFormatUtil.toT("2008/02/01"));
                rCol1.add(r1);
                cP1.setRemarks(rCol1);
                cP = getpersistName(DictType.CustomerList, cP1, "K002");

                oP = new OfferSeasonP();
                oP.setStartP(DateFormatUtil.toD("2008/10/01"));
                oP.setEndP(DateFormatUtil.toD("2008/10/30"));
                oP = getpersistName(DictType.OffSeasonDict, oP, "CUR");

                BookingP bok = new BookingP();
                bok.setCheckIn(DateFormatUtil.toD("2008/02/02"));
                bok.setCheckOut(DateFormatUtil.toD("2008/05/05"));
                bok.setNoPersons(new Integer(2));
                bok.setCustomer(cP.getId());
                bok.setSeason(oP.getName());
                bok.setBookingType(BookingEnumTypes.Reservation);

                BookingStateP bState = new BookingStateP();
                bState.setBState(BookingStateType.Confirmed);
                bState.setLp(new Integer(1));
                List<BookingStateP> staCol = new ArrayList<BookingStateP>();
                staCol.add(bState);
                bok.setState(staCol);


                List<PaymentP> colP = new ArrayList<PaymentP>();
                PaymentP p = new PaymentP();
                p.setAmount(new BigDecimal(123));
                p.setDateOp(DateFormatUtil.toD("2008/02/02"));
                p.setPayMethod(PaymentMethod.Cache);
                p.setSumOp(true);
                p.setLp(new Integer(1));
                p.setDatePayment(D("2008/02/02"));
                colP.add(p);
                p = new PaymentP();
                p.setAmount(new BigDecimal(126));
                p.setDateOp(DateFormatUtil.toD("2008/02/03"));
                p.setPayMethod(PaymentMethod.Cache);
                p.setDatePayment(D("2008/02/03"));
                p.setSumOp(true);
                p.setLp(new Integer(6));
                colP.add(p);
//                BillP bill = BillUtil.createPaymentBill();
//                bill.setPayments(colP);
//                List<BillP> colB = new ArrayList<BillP>();
//                colB.add(bill);
//                bok.setBill(colB);
                setPay(bok,colP);
                a = bok;
                break;

            default:
                a = new DictionaryP();
                break;
        }
        a.setHotel(hotel);
        return a;

    }

    protected void setPay(BookingP bok, List<PaymentP> colP) {
        BillP bill = BillUtil.createPaymentBill();
        bill.setPayments(colP);
        bill.setCustomer(bok.getCustomer());
        List<BillP> colB = new ArrayList<BillP>();
        colB.add(bill);
        bok.setBill(colB);
    }
    
    protected void addPay(BookingP bok, List<PaymentP> colP) {
        List<BillP> colB =bok.getBill();
        for (BillP p : colB) {
            p.setPayments(colP);
        }
    }
    
    protected List<PaymentP> getPay(BookingP bok) {
        BillP bi = BillUtil.getBill(bok);
        return bi.getPayments();
    }
    
    protected List<PaymentP> getFPay(BookingP bok) {
        List<BillP> col = bok.getBill();
        for (BillP p : col) {
            return p.getPayments();
        }
        return null;
    }
    
    protected void setVal(BookingP bok, List<AdvancePaymentP> colP) {
        BillP bill = BillUtil.createPaymentBill();
        bill.setCustomer(bok.getCustomer());
        bill.setAdvancePay(colP);
        List<BillP> colB = new ArrayList<BillP>();
        colB.add(bill);
        bok.setBill(colB);
    }
    
    protected List<AdvancePaymentP> getVal(BookingP bok) {
        BillP bi = BillUtil.getBill(bok);
        return bi.getAdvancePay();
    }

    protected Date D(String s) {
        return DateFormatUtil.toD(s);
    }

    protected void eqBig(BigDecimal b1, BigDecimal b2) {
        BigDecimal bb1 = b1.stripTrailingZeros();
        BigDecimal bb2 = b2.stripTrailingZeros();
        bb1.setScale(2);
        bb2.setScale(2);
        assertEquals(bb1, bb2);
    }

    protected void eqD(String s, Date d) {
        assertEquals(s, DateFormatUtil.toS(d));
    }
    
    protected void logInfo(String s) {
    	log.getL().info(s);
    }
}