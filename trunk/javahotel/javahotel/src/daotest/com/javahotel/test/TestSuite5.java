/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import java.util.Collection;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite5 extends TestHelper {

//    @Test
    public void Test1() throws Exception {
        System.out.println("Negative test");
        loginuser();
        try {
            DictionaryP a = new DictionaryP();
            a.setName("internet");
            a.setHotel("nonhotel");
            hot.persistDic(se, DictType.RoomFacility, a);
            fail();
        } catch (Exception e) {
            // expected
        }
    }

    private void test1(DictType t) throws Exception {

        String s = t.name();
        System.out.println(s);
        Collection<DictionaryP> col = getDicList(se, t, new HotelT("hotel1"));
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

    @Test
    public void Test2() throws Exception {
        System.out.println("Persist element");
        loginuser();
        DictionaryP a = new DictionaryP();
        a.setName("internet");
        a.setHotel(HOTEL1);
        hot.persistDic(se, DictType.RoomFacility, a);
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

        Collection<DictionaryP> fac = new ArrayList<DictionaryP>();
        DictionaryP f1 = new DictionaryP();
        DictionaryP f2 = new DictionaryP();
        f1.setHotel(HOTEL1);
        f1.setName("internet");
        fac.add(f1);
        r.setFacilities(fac);
        r.setRType(RRoom.Room);
        hot.persistDic(se, DictType.RoomObjects, r);

        Collection<DictionaryP> ro = getDicList(se, DictType.RoomObjects, new HotelT(HOTEL1));
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
        Collection<DictionaryP> fac = new ArrayList<DictionaryP>();
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
        sta.setName("1p");
        sta.setDescription("Jedynak ze sniadaniem");
        r.setRStandard(sta);
        hot.persistDic(se, DictType.RoomObjects, r);
    }

    @Test
    public void Test5() throws Exception {
        System.out.println("Test standard");
        loginuser();
        ustawHotel(HOTEL1);

        Collection<DictionaryP> col;

        col = getDicList(se, DictType.RoomFacility, new HotelT(HOTEL1));
        assertEquals(2, col.size());
        col = getDicList(se, DictType.RoomStandard, new HotelT(HOTEL1));
        assertEquals(1, col.size());

        Collection<DictionaryP> ro = getDicList(se, DictType.RoomObjects, new HotelT(HOTEL1));
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
        Collection<DictionaryP> col;

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
        Collection<DictionaryP> col;

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
