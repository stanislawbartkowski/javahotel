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

import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RRoom;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.RoomStandardP;

import com.javahotel.remoteinterfaces.HotelT;
import com.javahotel.remoteinterfaces.IHotelTest;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite5 extends TestHelper {

    /**
     * Negative test : try persist object in nonexistring hotel
     * 1. Set object for non-existring hotel
     * 2. Persist object
     * Expected result: exception
     */
    @Test
    public void Test1() throws Exception {
        System.out.println("Negative test");
        loginuser();
        try {
            // 1.
            DictionaryP a = new DictionaryP();
            a.setName("internet");
            a.setHotel("nonhotel");
            // 2.
            hot.persistDic(se, DictType.RoomFacility, a);
            // failure
            fail();
        } catch (Exception e) {
            // expected result
        }
    }

    private void test1(DictType t) throws Exception {

        String s = t.name();
        System.out.println(s);
        List<DictionaryP> col = getDicList(se, t, new HotelT("hotel1"));
        assertEquals(0, col.size());
        final String hotel1 = "hotel1";
        DictionaryP a = getDict(t, hotel1);
        int rType = -1;
        switch (t) {
            case PriceListDict:
                rType = IHotelTest.OFFERSERVICEPRICE;
                break;

            case CustomerList:
                rType = IHotelTest.CUSTOMERREMARKS;
                break;

            case BookingList:
                rType = IHotelTest.BOOKINGPAYMENTREGISTER;
                break;
        }
        a.setName("internet");
        a.setHotel("hotel1");
        hot.persistDic(seu, t, a);
        col = getDicList(se, t, new HotelT("hotel1"));
        assertEquals(1, col.size());
        for (DictionaryP aa : col) {
            assertEquals("internet", aa.getName());
        }
        hot.removeDic(seu, t, a);
        if (rType != -1) {
            int no = itest.getNumberOfRecord(se, rType, new HotelT("hotel1"));
            assertEquals(0, no);
        }
        col = getDicList(se, t, new HotelT("hotel1"));
        assertEquals(0, col.size());
//        a = getDict(t, hotel1);
        a.setDescription("desc1");
        hot.persistDic(seu, t, a);
        a.setDescription("rybka");
        hot.persistDic(seu, t, a);
        col = getDicList(se, t, new HotelT("hotel1"));
        assertEquals(1, col.size());
        for (DictionaryP aa : col) {
            assertEquals("rybka", aa.getDescription());
        }
    }


    /**
     * Simple test: persist object in existing hotel
     * 1. Set object
     * 2. Persist object
     * Expected result: no exception
     */
    @Test
    public void Test2() throws Exception {
        System.out.println("Persist element");
        loginuser();
        // 1.
        DictionaryP a = new DictionaryP();
        a.setName("internet");
        a.setHotel(HOTEL1);
        // 2.
        hot.persistDic(se, DictType.RoomFacility, a);
        // Expected result
    }

    @Test
    public void Test3() throws Exception {
        loginuser();
        test1(DictType.RoomFacility);
        test1(DictType.RoomStandard);
        test1(DictType.RoomObjects);
        test1(DictType.ServiceDict);
        test1(DictType.OffSeasonDict);
        test1(DictType.PriceListDict);
        test1(DictType.CustomerList);
        test1(DictType.BookingList);
        test1(DictType.IssuerInvoiceList);
    }

    @Test
    public void Test4() throws Exception {
        System.out.println("Test facilities");
        loginuser();

        DictionaryP a = new DictionaryP();
        a.setName("internet");
        a.setHotel(HOTEL1);
        hot.persistDic(se, DictType.RoomFacility, a);
        a.setName("telefon");
        hot.persistDic(se, DictType.RoomFacility, a);

        ResObjectP r = new ResObjectP();
        r.setHotel(HOTEL1);
        r.setName("jedynka");
        RoomStandardP st = new RoomStandardP();
        st.setName("1p");
        r.setRStandard(st);

        List<DictionaryP> fac = new ArrayList<DictionaryP>();
        DictionaryP f1 = new DictionaryP();
        DictionaryP f2 = new DictionaryP();
        f1.setHotel(HOTEL1);
        f1.setName("internet");
        fac.add(f1);
        r.setFacilities(fac);
        r.setRType(RRoom.Room);
        hot.persistDic(se, DictType.RoomObjects, r);

        List<DictionaryP> ro = getDicList(se, DictType.RoomObjects, new HotelT(HOTEL1));
        assertEquals(1, ro.size());
        for (DictionaryP p : ro) {
            ResObjectP re = (ResObjectP) p;
            assertEquals(1, re.getFacilities().size());
            for (DictionaryP d : re.getFacilities()) {
                String na = d.getName();
                System.out.println(na);
                assertEquals("internet", na);
            }
        }

        r.setHotel(HOTEL1);
        r.setName("dwojka");
        fac = new ArrayList<DictionaryP>();
        f1.setHotel(HOTEL1);
        f1.setName("internet");
        fac.add(f1);
        f2.setHotel(HOTEL1);
        f2.setName("telefon");
        fac.add(f2);
        r.setFacilities(fac);
        r.setRType(RRoom.Room);
        hot.persistDic(se, DictType.RoomObjects, r);

        ro = getDicList(se, DictType.RoomObjects, new HotelT(HOTEL1));
        assertEquals(2, ro.size());
        for (DictionaryP p : ro) {
            ResObjectP re = (ResObjectP) p;
            String hna = re.getName();
            if (hna.equals("jedynka")) {
                assertEquals(1, re.getFacilities().size());
            } else {
                assertEquals(2, re.getFacilities().size());
            }
            for (DictionaryP d : re.getFacilities()) {
                String na = d.getName();
                System.out.println(na);
//                assertEquals("internet",na);
            }
        }

    }

    private void ustawHotel(final String ho) {
        ResObjectP r = new ResObjectP();
        r.setHotel(ho);
        r.setName("jedynka");
        List<DictionaryP> fac = new ArrayList<DictionaryP>();
        DictionaryP f1 = new DictionaryP();
        DictionaryP f2 = new DictionaryP();
        f1.setHotel(ho);
        f1.setName("internet");
        fac.add(f1);
        f2.setHotel(ho);
        f2.setName("telefon");
        fac.add(f2);
        r.setFacilities(fac);
        r.setRType(RRoom.Room);
        DictionaryP sta = new DictionaryP();
        sta.setHotel(ho);
        sta.setName("1p");
        sta.setDescription("Jedynak ze sniadaniem");
        hot.persistDic( se, DictType.RoomStandard, sta);
        r.setRStandard(sta);
        hot.persistDic(se, DictType.RoomObjects, r);
    }

    @Test
    public void Test5() throws Exception {
        System.out.println("Test standard");
        loginuser();
        ustawHotel(HOTEL1);

        List<DictionaryP> col;

        col = getDicList(se, DictType.RoomFacility, new HotelT(HOTEL1));
        assertEquals(2, col.size());
        col = getDicList(se, DictType.RoomStandard, new HotelT(HOTEL1));
        assertEquals(1, col.size());

        List<DictionaryP> ro = getDicList(se, DictType.RoomObjects, new HotelT(HOTEL1));
        assertEquals(1, ro.size());
        for (DictionaryP p : ro) {
            ResObjectP re = (ResObjectP) p;
            DictionaryP sta = re.getRStandard();
            assertEquals("1p", sta.getName());
        }
    }

    @Test
    public void Test6() throws Exception {
        System.out.println("Test two hotels");
        loginuser();
        ustawHotel(HOTEL1);
        ustawHotel(HOTEL2);
        List<DictionaryP> col;

        col = getDicList(se, DictType.RoomFacility, new HotelT(HOTEL1));
        assertEquals(2, col.size());

        col = getDicList(se, DictType.RoomFacility, new HotelT(HOTEL2));
        assertEquals(2, col.size());

        col = getDicList(se, DictType.RoomStandard, new HotelT(HOTEL1));
        assertEquals(1, col.size());

        col = getDicList(se, DictType.RoomStandard, new HotelT(HOTEL2));
        assertEquals(1, col.size());
    }

    @Test
    public void Test7() throws Exception {
        System.out.println("Test two hotels");
        loginuser();
        ustawHotel(HOTEL1);
        ustawHotel(HOTEL2);
        List<DictionaryP> col;

        col = getDicList(se, DictType.RoomFacility, new HotelT(HOTEL1));
        assertEquals(2, col.size());
        for (DictionaryP p : col) {
            p.setDescription("opis hotel1");
            hot.persistDic(se, DictType.RoomFacility, p);
        }

        col = getDicList(se, DictType.RoomFacility, new HotelT(HOTEL2));
        assertEquals(2, col.size());
        for (DictionaryP p : col) {
            System.out.println(p.getDescription());
            assertNull(p.getDescription());
        }

        col = getDicList(se, DictType.RoomFacility, new HotelT(HOTEL1));
        assertEquals(2, col.size());
        for (DictionaryP p : col) {
            System.out.println(p.getDescription());
            assertEquals("opis hotel1", p.getDescription());
        }

    }
}