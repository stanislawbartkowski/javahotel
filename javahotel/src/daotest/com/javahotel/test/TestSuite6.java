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

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
import com.javahotel.common.command.DictType;
import com.javahotel.common.command.RRoom;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.ResObjectP;
import com.javahotel.common.toobject.RoomStandardP;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.common.toobject.ServiceType;
import com.javahotel.common.toobject.VatDictionaryP;
import com.javahotel.remoteinterfaces.HotelT;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestSuite6 extends TestHelper {

    @Test
    public void Test1() throws Exception {
        System.out.println("Test change name standard");
        loginuser();
        // add standard
        DictionaryP a = new DictionaryP();
        a.setName("1p");
        a.setHotel(HOTEL1);
        a.setDescription("Pokoj jednosobowy");
        hot.persistDic(se, DictType.RoomStandard, a);
        // add room
        ResObjectP r = new ResObjectP();
        r.setHotel(HOTEL1);
        r.setName("jedynka");
        r.setRType(RRoom.Room);
        DictionaryP sta = new DictionaryP();
        sta.setName("1p");
        // pusty descr
        // pusty hotel
        r.setRStandard(sta);
        hot.persistDic(se, DictType.RoomObjects, r);
        // teraz sprawdz standard
        List<DictionaryP> col = getDicList(se, DictType.RoomStandard, new HotelT(HOTEL1));
        assertEquals(1, col.size());
        // czy zostala nazwa
        for (DictionaryP p : col) {
            assertEquals("Pokoj jednosobowy", p.getDescription());
        }
    }

    @Test
    public void Test2() throws Exception {
        System.out.println("Test change facilites");
        loginuser();
        // add standard
        DictionaryP a = new DictionaryP();
        a.setName("1p");
        a.setHotel(HOTEL1);
        a.setDescription("Pokoj jednosobowy");
        hot.persistDic(se, DictType.RoomStandard, a);
        // add room
        ResObjectP r = new ResObjectP();
        r.setHotel(HOTEL1);
        r.setName("jedynka");
        r.setRType(RRoom.Room);
        DictionaryP sta = new DictionaryP();
        sta.setName("1p");
        // pusty descr
        // pusty hotel
        r.setRStandard(sta);
        hot.persistDic(se, DictType.RoomObjects, r);

        // teraz dodaj wyposazenie
        List<DictionaryP> rC = getDicList(se, DictType.RoomObjects, new HotelT(HOTEL1));
        assertEquals(1, rC.size());
        ResObjectP rO = null;
        for (DictionaryP p : rC) {
            rO = (ResObjectP) p;
        }
        assertNotNull(rO);
        assertNotNull(rO.getHotel());
        List<DictionaryP> fDic = new ArrayList<DictionaryP>();
        DictionaryP fac;
        fac = new DictionaryP();
        fac.setName("internet");
        fac.setDescription("Dostep do internet");
        fDic.add(fac);
        rO.setFacilities(fDic);
        hot.persistDic(se, DictType.RoomObjects, rO);

        rC = getDicList(se, DictType.RoomObjects, new HotelT(HOTEL1));
        assertEquals(1, rC.size());
        for (DictionaryP p : rC) {
            rO = (ResObjectP) p;
        }
        assertEquals(1, rO.getFacilities().size());
    }

    @Test
    public void Test3() throws Exception {
        System.out.println("Vat Dict");
        loginuser();
        List<DictionaryP> col = getDicList(se, DictType.VatDict, new HotelT(HOTEL1));
        assertEquals(4, col.size());
        boolean found = false;
        for (DictionaryP o : col) {
            VatDictionaryP va = (VatDictionaryP) o;
            BigDecimal b = va.getVatPercent();
            if ((b != null) && (b.floatValue() == 7)) {
                assertTrue(va.isDefVat());
                found = true;
            }
        }
        assertTrue(found);
        VatDictionaryP v = new VatDictionaryP();
        v.setName("XXX");
        v.setVatPercent(new BigDecimal(999));
        v.setHotel(HOTEL1);
        hot.persistDic(se, DictType.VatDict, v);
        col = getDicList(se, DictType.VatDict, new HotelT(HOTEL1));
        assertEquals(5, col.size());
    }

    @Test
    public void Test4() throws Exception {
        System.out.println("Service dictionary");
        loginuser();
        ServiceDictionaryP ser = new ServiceDictionaryP();
        VatDictionaryP v = new VatDictionaryP();
        v.setName("zw");
        ser.setVat(v);
        ser.setServType(ServiceType.DOSTAWKA);
        ser.setName("1os2p");
        ser.setHotel(HOTEL1);
        hot.persistDic(se, DictType.ServiceDict, ser);
        List<DictionaryP> col;
        col = getDicList(se, DictType.ServiceDict, new HotelT(HOTEL1));
        assertEquals(1, col.size());
        ser = null;
        for (DictionaryP d : col) {
            ser = (ServiceDictionaryP) d;
            assertNotNull(ser.getVat());
        }
        getDicList(se, DictType.VatDict, new HotelT(HOTEL1));
        v = new VatDictionaryP();
        v.setName("zw");
        ser.setVat(v);
        hot.persistDic(se, DictType.ServiceDict, ser);
        col = getDicList(se, DictType.ServiceDict, new HotelT(HOTEL1));
        assertEquals(1, col.size());
        for (DictionaryP d : col) {
            ser = (ServiceDictionaryP) d;
            assertEquals("zw", ser.getVat().getName());
        }

        v = new VatDictionaryP();
        v.setName("7%");
        ser.setVat(v);
        hot.persistDic(se, DictType.ServiceDict, ser);
        col = getDicList(se, DictType.ServiceDict, new HotelT(HOTEL1));
        assertEquals(1, col.size());
        for (DictionaryP d : col) {
            ser = (ServiceDictionaryP) d;
            assertEquals("7%", ser.getVat().getName());
        }
    }

   @Test
    public void Test5() throws Exception {
        System.out.println("Service dictionary");
        loginuser();
        RoomStandardP r = new RoomStandardP();
        r.setName("1p");
        r.setHotel(HOTEL1);
        List<ServiceDictionaryP> col = new ArrayList<ServiceDictionaryP>();
        ServiceDictionaryP ser = new ServiceDictionaryP();
        VatDictionaryP va = new VatDictionaryP();
        va.setName("zw");
        ser.setVat(va);
        ser.setName("1p2");
        ser.setServType(ServiceType.NOCLEG);
        col.add(ser);
        r.setServices(col);
        hot.persistDic(seu, DictType.RoomStandard, r);

        List<DictionaryP> rcol;
        rcol = getDicList(se, DictType.RoomStandard, new HotelT(HOTEL1));
        for (DictionaryP dp : rcol) {
            r = (RoomStandardP) dp;
            assertEquals(1, r.getServices().size());
            for (DictionaryP rp : r.getServices()) {
                ser = (ServiceDictionaryP) rp;
                assertEquals("1p2", ser.getName());
                assertEquals("zw", ser.getVat().getName());
            }

        }
        assertEquals(1, rcol.size());
        rcol = getDicList(se, DictType.VatDict, new HotelT(HOTEL1));
        assertEquals(1, rcol.size());
        rcol = getDicList(se, DictType.ServiceDict, new HotelT(HOTEL1));
        assertEquals(1, rcol.size());

        col = new ArrayList<ServiceDictionaryP>();
        ser.setName("2p2");
        col.add(ser);
        r.setServices(col);
        hot.persistDic(seu, DictType.RoomStandard, r);
        rcol = getDicList(se, DictType.RoomStandard, new HotelT(HOTEL1));
        for (DictionaryP dp : rcol) {
            r = (RoomStandardP) dp;
            assertEquals(1, r.getServices().size());
            for (DictionaryP rp : r.getServices()) {
                ser = (ServiceDictionaryP) rp;
                assertEquals("2p2", ser.getName());
                assertEquals("zw", ser.getVat().getName());
            }
        }

    }
}
