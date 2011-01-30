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

import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.OfferSeasonPeriodP;
import com.javahotel.common.toobject.SeasonPeriodT;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.remoteinterfaces.HotelT;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite7 extends TestHelper {

    @Test
    public void Test1() throws Exception {
        System.out.println("Test Season Period");
        loginuser();
        OfferSeasonP sep = new OfferSeasonP();
        sep.setHotel(HOTEL1);
        sep.setName("GL");
        sep.setStartP(DateFormatUtil.toD("2008/01/01"));
        sep.setEndP(DateFormatUtil.toD("2008/10/02"));
        hot.persistDic(seu, DictType.OffSeasonDict, sep);
        List<DictionaryP> col = getDicList(seu, DictType.OffSeasonDict, new HotelT(HOTEL1));
        assertEquals(1, col.size());
        for (DictionaryP d : col) {
            sep = (OfferSeasonP) d;
            String s = DateFormatUtil.toS(sep.getStartP());
            assertEquals("2008/01/01", s);
            s = DateFormatUtil.toS(sep.getEndP());
            assertEquals("2008/10/02", s);
        }

        List<OfferSeasonPeriodP> seL = new ArrayList<OfferSeasonPeriodP>();
        OfferSeasonPeriodP pe = new OfferSeasonPeriodP();
        pe.setStartP(DateFormatUtil.toD("2008/03/01"));
        pe.setEndP(DateFormatUtil.toD("2008/06/01"));
        pe.setPeriodT(SeasonPeriodT.LOW);
        pe.setPId(new Long(1));
        seL.add(pe);
        sep.setPeriods(seL);
        hot.persistDic(seu, DictType.OffSeasonDict, sep);
        col = getDicList(seu, DictType.OffSeasonDict, new HotelT(HOTEL1));
        assertEquals(1, col.size());
        for (DictionaryP d : col) {
            sep = (OfferSeasonP) d;
            seL = sep.getPeriods();
            assertEquals(1, seL.size());
            for (OfferSeasonPeriodP pep : seL) {
                String s = DateFormatUtil.toS(pep.getStartP());
                assertEquals("2008/03/01", s);
                s = DateFormatUtil.toS(pep.getEndP());
                assertEquals("2008/06/01", s);
            }
        }

        seL = new ArrayList<OfferSeasonPeriodP>();
        pe = new OfferSeasonPeriodP();
        pe.setStartP(DateFormatUtil.toD("2008/03/11"));
        pe.setEndP(DateFormatUtil.toD("2008/06/11"));
        pe.setPeriodT(SeasonPeriodT.LOW);
        pe.setPId(new Long(1));
        seL.add(pe);
        sep.setPeriods(seL);
        hot.persistDic(seu, DictType.OffSeasonDict, sep);
        col = getDicList(seu, DictType.OffSeasonDict, new HotelT(HOTEL1));
        assertEquals(1, col.size());
        for (DictionaryP d : col) {
            sep = (OfferSeasonP) d;
            seL = sep.getPeriods();
            assertEquals(1, seL.size());
            for (OfferSeasonPeriodP pep : seL) {
                String s = DateFormatUtil.toS(pep.getStartP());
                assertEquals("2008/03/11", s);
                s = DateFormatUtil.toS(pep.getEndP());
                assertEquals("2008/06/11", s);
                Long pId = pep.getPId();
                assertEquals(new Long(1), pId);
            }
        }

    }

    @Test
    public void Test2() throws Exception {
        System.out.println("Test Season Period");
        loginuser();
        OfferSeasonP sep = new OfferSeasonP();
        sep.setHotel(HOTEL1);
        sep.setName("GL");
        sep.setStartP(DateFormatUtil.toD("2008/01/01"));
        sep.setEndP(DateFormatUtil.toD("2008/10/02"));
        List<OfferSeasonPeriodP> seL = new ArrayList<OfferSeasonPeriodP>();

        OfferSeasonPeriodP pe = new OfferSeasonPeriodP();
        pe.setStartP(DateFormatUtil.toD("2008/03/01"));
        pe.setEndP(DateFormatUtil.toD("2008/06/01"));
        pe.setPeriodT(SeasonPeriodT.LOW);
        pe.setPId(new Long(1));
        seL.add(pe);

        pe = new OfferSeasonPeriodP();
        pe.setStartP(DateFormatUtil.toD("2008/04/01"));
        pe.setEndP(DateFormatUtil.toD("2008/04/02"));
        pe.setPeriodT(SeasonPeriodT.LOW);
        pe.setPId(new Long(2));
        seL.add(pe);

        pe = new OfferSeasonPeriodP();
        pe.setStartP(DateFormatUtil.toD("2008/05/01"));
        pe.setEndP(DateFormatUtil.toD("2008/05/02"));
        pe.setPeriodT(SeasonPeriodT.SPECIAL);
        pe.setPId(new Long(3));
        seL.add(pe);

        sep.setPeriods(seL);

        hot.persistDic(seu, DictType.OffSeasonDict, sep);

        List<DictionaryP> col = getDicList(seu, DictType.OffSeasonDict, new HotelT(HOTEL1));
        assertEquals(1, col.size());
        for (DictionaryP d : col) {
            sep = (OfferSeasonP) d;
            seL = sep.getPeriods();
            assertEquals(3, seL.size());
            OfferSeasonPeriodP pp = null;
            for (OfferSeasonPeriodP pep : seL) {
                if (pep.getPId().longValue() == 2) {
                    pp = pep;
                }
            }
            assertNotNull(pp);
            seL.remove(pp);
        }
        hot.persistDic(seu, DictType.OffSeasonDict, sep);

        col = getDicList(seu, DictType.OffSeasonDict, new HotelT(HOTEL1));
        assertEquals(1, col.size());
        for (DictionaryP d : col) {
            sep = (OfferSeasonP) d;
            seL = sep.getPeriods();
            assertEquals(2, seL.size());
            for (OfferSeasonPeriodP pep : seL) {
                if (pep.getPId().longValue() == 3) {
                    pep.setStartP(DateFormatUtil.toD("2008/05/13"));
                }
            }
            pe = new OfferSeasonPeriodP();
            pe.setStartP(DateFormatUtil.toD("2008/08/01"));
            pe.setEndP(DateFormatUtil.toD("2008/08/02"));
            pe.setPeriodT(SeasonPeriodT.SPECIAL);
            pe.setPId(new Long(4));
            seL.add(pe);

        }
        hot.persistDic(seu, DictType.OffSeasonDict, sep);
        col = getDicList(seu, DictType.OffSeasonDict, new HotelT(HOTEL1));
        assertEquals(1, col.size());
        for (DictionaryP d : col) {
            sep = (OfferSeasonP) d;
            seL = sep.getPeriods();
            assertEquals(3, seL.size());
            OfferSeasonPeriodP pp = null;
            for (OfferSeasonPeriodP pep : seL) {
                if (pep.getPId().longValue() == 3) {
                    pp = pep;
                   String s = DateFormatUtil.toS(pep.getStartP());
                   assertEquals("2008/05/13", s);                    
                }
            }
            assertNotNull(pp);
       }
    }
}

