/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.statictest;

import com.javahotel.common.dateutil.GetPeriods.StartWeek;
import com.javahotel.common.tableprice.TableSeasonPrice;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.OfferSeasonPeriodP;
import com.javahotel.common.toobject.OfferServicePriceP;
import com.javahotel.common.toobject.OfferSpecialPriceP;
import com.javahotel.common.toobject.PaymentRowP;
import com.javahotel.common.toobject.SeasonPeriodT;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TestSuite7 {

    private static Date createD(int year, int mo, int da) {
        return TestDUtil.createD(year, mo, da);
    }

    private void eqD(final Date d, int year, int mo, int da) {
        Date dx = createD(year, mo, da);
        assertEquals(dx, d);
    }

    @Test
    public void Test1() throws Exception {
        System.out.println("Test create table day more");
        OfferSeasonP oP = new OfferSeasonP();
        List<OfferSeasonPeriodP> per = new ArrayList<OfferSeasonPeriodP>();
        OfferSeasonPeriodP spp = new OfferSeasonPeriodP();
        spp.setStartP(createD(2008, 10, 6));
        spp.setEndP(createD(2008, 10, 30));
        spp.setPeriodT(SeasonPeriodT.LOW);
        per.add(spp);
        spp = new OfferSeasonPeriodP();
        spp.setStartP(createD(2008, 9, 20));
        spp.setEndP(createD(2008, 9, 25));
        spp.setPeriodT(SeasonPeriodT.SPECIAL);
        spp.setPId(new Long(1001));
        per.add(spp);
        oP.setPeriods(per);
        oP.setStartP(createD(2008, 9, 1));
        oP.setEndP(createD(2008, 10, 30)); // day more
        OfferPriceP pri = new OfferPriceP();
        List<OfferServicePriceP> sp = new ArrayList<OfferServicePriceP>();
        OfferServicePriceP pp1 = new OfferServicePriceP();
        pp1.setHighseasonprice(new BigDecimal(100));
        pp1.setHighseasonweekendprice(new BigDecimal(200));
        pp1.setLowseasonprice(new BigDecimal(300));
        pp1.setLowseasonweekendprice(new BigDecimal(400));
        pp1.setService("1p");
        sp.add(pp1);
        OfferServicePriceP pp = new OfferServicePriceP();
        pp.setHighseasonprice(new BigDecimal(1100));
        pp.setHighseasonweekendprice(new BigDecimal(1200));
        pp.setLowseasonprice(new BigDecimal(1300));
        pp.setLowseasonweekendprice(new BigDecimal(1400));
        pp.setService("2p");
        sp.add(pp);
        pri.setServiceprice(sp);
        TableSeasonPrice ta = new TableSeasonPrice(StartWeek.onFriday);
        ta.setPeriods(oP);
        ta.setPriceList(pri);
        List<PaymentRowP> pr = ta.getPriceRows("1p", createD(2008, 9, 1), createD(2008, 9, 4));
        assertEquals(1, pr.size());
        int no = 0;
        for (PaymentRowP p : pr) {
            switch (no) {
                case 0:
                    eqD(p.getRowFrom(), 2008, 9, 1);
                    eqD(p.getRowTo(), 2008, 9, 4);
                    assertEquals(new BigDecimal(100), p.getCustomerPrice());
                    break;
                default:
                    fail();
            }
            no++;
        }

        pr = ta.getPriceRows("1p", createD(2008, 9, 4), createD(2008, 9, 7));
        no = 0;
        for (PaymentRowP p : pr) {
            switch (no) {
                case 0:
                    eqD(p.getRowFrom(), 2008, 9, 4);
                    eqD(p.getRowTo(), 2008, 9, 4);
                    assertEquals(new BigDecimal(100), p.getCustomerPrice());
                    break;
                case 1:
                    eqD(p.getRowFrom(), 2008, 9, 5);
                    eqD(p.getRowTo(), 2008, 9, 6);
                    assertEquals(new BigDecimal(200), p.getCustomerPrice());
                    break;
                case 2:
                    eqD(p.getRowFrom(), 2008, 9, 7);
                    eqD(p.getRowTo(), 2008, 9, 7);
                    assertEquals(new BigDecimal(100), p.getCustomerPrice());
                    break;
                default:
                    fail();
            }
            no++;
        }


        pr = ta.getPriceRows("2p", createD(2008, 10, 20), createD(2008, 10, 30));
        no = 0;
        for (PaymentRowP p : pr) {
            switch (no) {
                case 0:
                    eqD(p.getRowFrom(), 2008, 10, 20);
                    eqD(p.getRowTo(), 2008, 10, 23);
                    assertEquals(new BigDecimal(1300), p.getCustomerPrice());
                    break;
                case 1:
                    eqD(p.getRowFrom(), 2008, 10, 24);
                    eqD(p.getRowTo(), 2008, 10, 25);
                    assertEquals(new BigDecimal(1400), p.getCustomerPrice());
                    break;
                case 2:
                    eqD(p.getRowFrom(), 2008, 10, 26);
                    eqD(p.getRowTo(), 2008, 10, 30);
                    assertEquals(new BigDecimal(1300), p.getCustomerPrice());
                    break;
                default:
                    fail();
            }
            no++;
        }

        pr = ta.getPriceRows("1p", createD(2008, 9, 20), createD(2008, 9, 25));
        no = 0;
        for (PaymentRowP p : pr) {
            switch (no) {
                case 0:
                    eqD(p.getRowFrom(), 2008, 9, 20);
                    eqD(p.getRowTo(), 2008, 9, 25);
                    assertNull(p.getCustomerPrice());
                    break;
                default:
                    fail();
            }
            no++;
        }

        List<OfferSpecialPriceP> sCol = new ArrayList<OfferSpecialPriceP>();
        OfferSpecialPriceP oo = new OfferSpecialPriceP();
        oo.setSpecialperiod(new Long(1001));
        oo.setPrice(new BigDecimal(999));
        sCol.add(oo);
        pp1.setSpecialprice(sCol);

        pr = ta.getPriceRows("1p", createD(2008, 9, 20), createD(2008, 9, 25));
        no = 0;
        for (PaymentRowP p : pr) {
            switch (no) {
                case 0:
                    eqD(p.getRowFrom(), 2008, 9, 20);
                    eqD(p.getRowTo(), 2008, 9, 25);
                    assertEquals(new BigDecimal(999), p.getCustomerPrice());
                    break;
                default:
                    fail();
            }
            no++;
        }


    }
}
