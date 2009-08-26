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

import com.javahotel.common.command.DictType;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.OfferSeasonPeriodP;
import com.javahotel.common.toobject.OfferServicePriceP;
import com.javahotel.common.toobject.OfferSpecialPriceP;
import com.javahotel.common.toobject.SeasonPeriodT;
import com.javahotel.common.toobject.ServiceDictionaryP;
import com.javahotel.common.toobject.ServiceType;
import com.javahotel.common.toobject.VatDictionaryP;

import com.javahotel.remoteinterfaces.HotelT;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite8 extends TestHelper {

    @Test
    public void Test1() {
        System.out.println("Price list");
        loginuser();

        OfferSeasonP sep = new OfferSeasonP();
        sep.setHotel(HOTEL1);
        sep.setName("GL");
        sep.setStartP(DateFormatUtil.toD("2008/01/01"));
        sep.setEndP(DateFormatUtil.toD("2008/10/02"));
        hot.persistDic(seu, DictType.OffSeasonDict, sep);

        VatDictionaryP va = new VatDictionaryP();
        va.setHotel(HOTEL1);
        va.setName("zw");
        va.setVatPercent(new BigDecimal("10"));
        hot.persistDic(seu, DictType.VatDict, va);

        ServiceDictionaryP sP = new ServiceDictionaryP();
        sP.setHotel(HOTEL1);
        sP.setServType(ServiceType.DOSTAWKA);
        sP.setName("DOST");
        va = new VatDictionaryP();
        va.setName("zw");
        va.setVatPercent(new BigDecimal("10"));
        sP.setVat(va);
        hot.persistDic(seu, DictType.ServiceDict, sP);

        OfferPriceP oP = new OfferPriceP();
        oP.setName("PODST");
        oP.setSeason("GL");
//        oP.setService("DOST");
        oP.setHotel(sP.getHotel());
        hot.persistDic(seu, DictType.PriceListDict, oP);

        Collection<DictionaryP> col = getDicList(seu, DictType.PriceListDict, new HotelT(HOTEL1));
        assertEquals(1, col.size());
        for (DictionaryP p : col) {
            OfferPriceP ooP = (OfferPriceP) p;
            assertEquals("PODST", p.getName());
//            assertEquals("DOST",ooP.getService());
            assertEquals("GL", ooP.getSeason());
        }
    }

    @Test
    public void Test2() {
        Test1();
        System.out.println("Price list podlist");
        Collection<DictionaryP> col = getDicList(seu, DictType.OffSeasonDict, new HotelT(HOTEL1));
        OfferSeasonP oP = null;
        for (DictionaryP p : col) {
            oP = (OfferSeasonP) p;
        }
        assertNotNull(oP);

        OfferSeasonPeriodP pe = new OfferSeasonPeriodP();
        Collection<OfferSeasonPeriodP> seL = new ArrayList<OfferSeasonPeriodP>();
        pe.setStartP(DateFormatUtil.toD("2008/03/11"));
        pe.setEndP(DateFormatUtil.toD("2008/06/11"));
        pe.setPeriodT(SeasonPeriodT.LOW);
        pe.setPId(new Long(1));
        seL.add(pe);
        pe = new OfferSeasonPeriodP();
        pe.setStartP(DateFormatUtil.toD("2008/04/11"));
        pe.setEndP(DateFormatUtil.toD("2008/08/11"));
        pe.setPeriodT(SeasonPeriodT.LOW);
        pe.setPId(new Long(2));
        seL.add(pe);

        oP.setPeriods(seL);
        hot.persistDic(seu, DictType.OffSeasonDict, oP);

        col = getDicList(seu, DictType.PriceListDict, new HotelT(HOTEL1));
        OfferPriceP opp = null;
        for (DictionaryP p : col) {
            opp = (OfferPriceP) p;
        }
        assertNotNull(opp);

        OfferSpecialPriceP osp;
        Collection<OfferSpecialPriceP> lP = new ArrayList<OfferSpecialPriceP>();
        osp = new OfferSpecialPriceP();
        osp.setSpecialperiod(new Long(1));
        osp.setPrice(new BigDecimal("123"));
        lP.add(osp);

        OfferServicePriceP ospp;
        Collection<OfferServicePriceP> lPp = new ArrayList<OfferServicePriceP>();
        ospp = new OfferServicePriceP();
        ospp.setService("DOST");
        ospp.setSpecialprice(lP);
        lPp.add(ospp);

        opp.setServiceprice(lPp);
        hot.persistDic(seu, DictType.PriceListDict, opp);

        col = getDicList(seu, DictType.PriceListDict, new HotelT(HOTEL1));
        opp = null;
        for (DictionaryP p : col) {
            opp = (OfferPriceP) p;
        }
        Collection<OfferServicePriceP> colP = opp.getServiceprice();
        assertNotNull(colP);
        assertEquals(1, colP.size());
        ospp = null;
        for (OfferServicePriceP o : colP) {
            ospp = o;
        }
       assertEquals("DOST",ospp.getService());
        for (OfferSpecialPriceP p : ospp.getSpecialprice()) {
            assertEquals(new Long(1), p.getSpecialperiod());
            eqBig(new BigDecimal("123"), p.getPrice());
        }

    }
}

