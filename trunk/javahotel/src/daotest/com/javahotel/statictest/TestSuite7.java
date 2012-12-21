/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
import com.javahotel.common.tableprice.TableSeasonPrice.RowDetailLevel;
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

    /*
     * Step 1: Create low period 2008/10/6 - 2008/10/30
     * Step 2: Create special period: 2008/09/20 - 2008/09/30
     * Step 3: Create pricing 2008/09/01 - 2008/10/30 and
     * Step 4: Set prices for service p1
     * Step 5: Set prices for service p2
     * Step 6: Create price table
     * Step 7: Get pricing : 2008/09/01 - 2008/09/04
     * Verification 1: 4 * 100 = 400, one element
     * Step 8: Get pricing :2008/09/04 - 2008/09/07
     * Verification 2: first element: one day, 100
     * Verification 3: two elements (weekend) : 2 * 200 = 400
     * Verification 4: one element : 100 
     * Step 9: Get pricing 2008/10/20 - 2008/10/30 
     * Verification 5: first element: 4 * 1300 = 5200
     * Verification 6: second element 2 * 1400 = 2800
     * Verification 7: third: 5 * 1300 : 6500 
     * --Now test special period.
     * Step 10: Get pricing 2008/09/20 - 2008/09/25 
     * Verification 8: null (special period and price not set)
     * Step 11: Set price for special period
     * Step 12: Again pricing for 2008/09/20 - 2008/09/25
     * Verification 8: 6 * 999 = 5994
     * -- now test getPriceRowsInOut
     * Step 13: Pricing for 2008/09/20 - 2008/09/25 (per period)
     * (last day one day less)
     * Verification 9: 5 * 999 = 4995
     * Step 14: Pricing for 2008/09/20 - 2008/09/25 (par day)
     * (last day one day less)
     * Verification 10: every day
     */

    @Test
    public void Test1() throws Exception {
        System.out.println("Test create table day more");
        OfferSeasonP oP = new OfferSeasonP();
        List<OfferSeasonPeriodP> per = new ArrayList<OfferSeasonPeriodP>();
        // Step 1
        OfferSeasonPeriodP spp = new OfferSeasonPeriodP();
        spp.setStartP(createD(2008, 10, 6));
        spp.setEndP(createD(2008, 10, 30));
        spp.setPeriodT(SeasonPeriodT.LOW);
        per.add(spp);
        // Step 2
        spp = new OfferSeasonPeriodP();
        spp.setStartP(createD(2008, 9, 20));
        spp.setEndP(createD(2008, 9, 25));
        spp.setPeriodT(SeasonPeriodT.SPECIAL);
        spp.setPId(new Long(1001));
        per.add(spp);
        oP.setPeriods(per);
        // Step 3
        oP.setStartP(createD(2008, 9, 1));
        oP.setEndP(createD(2008, 10, 30)); // day more
        OfferPriceP pri = new OfferPriceP();
        // Step 4
        List<OfferServicePriceP> sp = new ArrayList<OfferServicePriceP>();
        OfferServicePriceP pp1 = new OfferServicePriceP();
        pp1.setHighseasonprice(new BigDecimal(100));
        pp1.setHighseasonweekendprice(new BigDecimal(200));
        pp1.setLowseasonprice(new BigDecimal(300));
        pp1.setLowseasonweekendprice(new BigDecimal(400));
        pp1.setService("1p");
        sp.add(pp1);
        // Step 5
        OfferServicePriceP pp = new OfferServicePriceP();
        pp.setHighseasonprice(new BigDecimal(1100));
        pp.setHighseasonweekendprice(new BigDecimal(1200));
        pp.setLowseasonprice(new BigDecimal(1300));
        pp.setLowseasonweekendprice(new BigDecimal(1400));
        pp.setService("2p");
        sp.add(pp);
        pri.setServiceprice(sp);
        // Step 6
        TableSeasonPrice ta = new TableSeasonPrice(StartWeek.onFriday);
        ta.setPeriods(oP);
        ta.setPriceList(pri);
        // Step 7
        List<PaymentRowP> pr = ta.getPriceRows("1p", createD(2008, 9, 1),
                createD(2008, 9, 4));
        assertEquals(1, pr.size());
        int no = 0;
        for (PaymentRowP p : pr) {
            switch (no) {
            case 0:
                // Verification 1
                eqD(p.getRowFrom(), 2008, 9, 1);
                eqD(p.getRowTo(), 2008, 9, 4);
                assertEquals(new BigDecimal(400), p.getCustomerPrice());
                break;
            default:
                fail();
            }
            no++;
        }

        // Step 8
        pr = ta.getPriceRows("1p", createD(2008, 9, 4), createD(2008, 9, 7));
        no = 0;
        for (PaymentRowP p : pr) {
            switch (no) {
            case 0:
                // Verification 2
                eqD(p.getRowFrom(), 2008, 9, 4);
                eqD(p.getRowTo(), 2008, 9, 4);
                assertEquals(new BigDecimal(100), p.getCustomerPrice());
                break;
            case 1:
                // Verification 3
                eqD(p.getRowFrom(), 2008, 9, 5);
                eqD(p.getRowTo(), 2008, 9, 6);
                assertEquals(new BigDecimal(400), p.getCustomerPrice());
                break;
            case 2:
                // Verification 4
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
                // Verification 5
                eqD(p.getRowFrom(), 2008, 10, 20);
                eqD(p.getRowTo(), 2008, 10, 23);
                assertEquals(new BigDecimal(5200), p.getCustomerPrice());
                break;
            case 1:
                // Verification 6
                eqD(p.getRowFrom(), 2008, 10, 24);
                eqD(p.getRowTo(), 2008, 10, 25);
                assertEquals(new BigDecimal(2800), p.getCustomerPrice());
                break;
            case 2:
                // Verification 7
                eqD(p.getRowFrom(), 2008, 10, 26);
                eqD(p.getRowTo(), 2008, 10, 30);
                assertEquals(new BigDecimal(6500), p.getCustomerPrice());
                break;
            default:
                fail();
            }
            no++;
        }

        // Step 10
        pr = ta.getPriceRows("1p", createD(2008, 9, 20), createD(2008, 9, 25));
        no = 0;
        for (PaymentRowP p : pr) {
            switch (no) {
            case 0:
                // Verification 8
                eqD(p.getRowFrom(), 2008, 9, 20);
                eqD(p.getRowTo(), 2008, 9, 25);
                assertNull(p.getCustomerPrice());
                break;
            default:
                fail();
            }
            no++;
        }

        // Step 11
        List<OfferSpecialPriceP> sCol = new ArrayList<OfferSpecialPriceP>();
        OfferSpecialPriceP oo = new OfferSpecialPriceP();
        oo.setSpecialperiod(new Long(1001));
        oo.setPrice(new BigDecimal(999));
        sCol.add(oo);
        pp1.setSpecialprice(sCol);

        // Step 12
        pr = ta.getPriceRows("1p", createD(2008, 9, 20), createD(2008, 9, 25));
        no = 0;
        for (PaymentRowP p : pr) {
            switch (no) {
            case 0:
                eqD(p.getRowFrom(), 2008, 9, 20);
                eqD(p.getRowTo(), 2008, 9, 25);
                assertEquals(new BigDecimal(5994), p.getCustomerPrice());
                break;
            default:
                fail();
            }
            no++;
        }

        // Step 13
        pr = ta.getPriceRowsInOut("1p", createD(2008, 9, 20),
                createD(2008, 9, 25), RowDetailLevel.perPeriod);
        no = 0;
        for (PaymentRowP p : pr) {
            switch (no) {
            case 0:
                // Verification 9
                eqD(p.getRowFrom(), 2008, 9, 20);
                eqD(p.getRowTo(), 2008, 9, 24);
                assertEquals(new BigDecimal(4995), p.getCustomerPrice());
                assertEquals(new BigDecimal(999), p.getCustomerRate());
                break;
            default:
                fail();
            }
            no++;
        }
        assertEquals(1, no);

        // Step 14
        pr = ta.getPriceRowsInOut("1p", createD(2008, 9, 20),
                createD(2008, 9, 25), RowDetailLevel.perDay);
        no = 0;
        for (PaymentRowP p : pr) {
            switch (no) {
            case 0:
                // Verification 10
                eqD(p.getRowFrom(), 2008, 9, 20);
                eqD(p.getRowTo(), 2008, 9, 20);
                assertEquals(new BigDecimal(999), p.getOfferRate());
                assertEquals(new BigDecimal(999), p.getCustomerRate());
                break;
            case 1:
                eqD(p.getRowFrom(), 2008, 9, 21);
                eqD(p.getRowTo(), 2008, 9, 21);
                assertEquals(new BigDecimal(999), p.getOfferRate());
                assertEquals(new BigDecimal(999), p.getCustomerRate());
                break;
            case 2:
                eqD(p.getRowFrom(), 2008, 9, 22);
                eqD(p.getRowTo(), 2008, 9, 22);
                assertEquals(new BigDecimal(999), p.getOfferRate());
                assertEquals(new BigDecimal(999), p.getCustomerRate());
                break;
            case 3:
                eqD(p.getRowFrom(), 2008, 9, 23);
                eqD(p.getRowTo(), 2008, 9, 23);
                assertEquals(new BigDecimal(999), p.getOfferRate());
                assertEquals(new BigDecimal(999), p.getCustomerRate());
                break;
            case 4:
                eqD(p.getRowFrom(), 2008, 9, 24);
                eqD(p.getRowTo(), 2008, 9, 24);
                assertEquals(new BigDecimal(999), p.getOfferRate());
                assertEquals(new BigDecimal(999), p.getCustomerRate());
                break;
            default:
                fail();
            }
            no++;
        }
        assertEquals(5, no);

    }
}
