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
package com.javahotel.common.tableprice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gwtmodel.table.common.PeriodT;
import com.gwtmodel.table.common.dateutil.DateUtil;
import com.javahotel.common.dateutil.GetPeriods;
import com.javahotel.common.dateutil.GetPeriods.StartWeek;
import com.javahotel.common.seasonutil.CreateTableSeason;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.OfferSeasonPeriodP;
import com.javahotel.common.toobject.OfferServicePriceP;
import com.javahotel.common.toobject.OfferSpecialPriceP;
import com.javahotel.common.toobject.PaymentRowP;

/**
 * Class used for price calculation
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class TableSeasonPrice {

    /** List of periods. */
    private List<PeriodT> periods;
    /** Parameter: beginning of the weekend (Friday or Sutarday). */
    private final StartWeek sWeek;
    /** List of prices and periods. */
    private OfferPriceP oP;

    public TableSeasonPrice(final StartWeek sWeek) {
        this.sWeek = sWeek;
        periods = null;
    }

    /**
     * Setter for periods
     * 
     * @param oP
     *            OfferSeasonP
     */
    public void setPeriods(final OfferSeasonP oP) {
        periods = CreateTableSeason.createTable(oP, sWeek);
    }

    /**
     * Setter for price list
     * 
     * @param oP
     *            OfferPriceP
     */
    public void setPriceList(final OfferPriceP oP) {
        this.oP = oP;
    }

    /**
     * Get price calculation
     * 
     * @param service
     *            Service to calculate
     * @param dFrom
     *            Period beginning
     * @param dTo
     *            Period end
     * @return List of detailed payments
     */
    public List<PaymentRowP> getPriceRows(final String service,
            final Date dFrom, final Date dTo) {
        OfferServicePriceP seP = null;
        for (OfferServicePriceP p : oP.getServiceprice()) {
            if (p.getService().equals(service)) {
                seP = p;
                break;
            }
        }
        PeriodT p = new PeriodT(dFrom, dTo, null);
        List<PeriodT> out = GetPeriods.get(p, periods);
        List<PaymentRowP> col = new ArrayList<PaymentRowP>();
        for (PeriodT pe : out) {
            PaymentRowP pr = new PaymentRowP();
            pr.setRowFrom(pe.getFrom());
            pr.setRowTo(pe.getTo());
            if (seP == null) {
                continue;
            }
            BigDecimal price = seP.getHighseasonprice();
            if (pe.getI() != null) {
                OfferSeasonPeriodP sp = (OfferSeasonPeriodP) pe.getI();
                switch (sp.getPeriodT()) {
                case LOW:
                    price = seP.getLowseasonprice();
                    break;
                case LOWWEEKEND:
                    price = seP.getLowseasonweekendprice();
                    break;
                case HIGHWEEKEND:
                    price = seP.getHighseasonweekendprice();
                    break;
                case SPECIAL:
                    price = null;
                    if (seP.getSpecialprice() != null) {
                        OfferSpecialPriceP speP = null;
                        for (OfferSpecialPriceP pp : seP.getSpecialprice()) {
                            if (pp.getSpecialperiod().equals(sp.getPId())) {
                                speP = pp;
                                break;
                            }
                        }
                        if (speP != null) {
                            price = speP.getPrice();
                        }
                    }
                    break;
                default:
                    assert false : "Cannot be here";
                }
            }
            assert price != null : "Price cannot be null";
            pr.setCustomerRate(price);
            pr.setOfferRate(price);
            col.add(pr);
        }
        return col;
    }

    public enum RowDetailLevel {
        perDay, perPeriod
    };

    /**
     * Get price calculation for check in and out
     * 
     * @param service
     *            Service to calculate
     * @param checkIn
     *            check in date
     * 
     * @param checkOut
     *            check out date Period end
     * @param RowDetailLevel
     *            dLevel Detail level : perDay or perPeriod
     * @return List of detailed payments
     */
    public List<PaymentRowP> getPriceRowsInOut(final String service,
            final Date checkIn, final Date checkOut, RowDetailLevel dLevel) {
        Date d = DateUtil.PrevDayD(checkOut);
        List<PaymentRowP> pList = getPriceRows(service, checkIn, d);
        if (dLevel == RowDetailLevel.perPeriod) {
            return pList;
        }
        // split into days
        List<PaymentRowP> aList = new ArrayList<PaymentRowP>();
        for (PaymentRowP pa : pList) {
            Date dstart = pa.getRowFrom();
            do {
                PaymentRowP p = new PaymentRowP();
                p.setRowFrom(dstart);
                p.setRowTo(dstart);
                p.setCustomerRate(pa.getCustomerRate());
                p.setOfferRate(pa.getOfferRate());
                aList.add(p);
                dstart = DateUtil.NextDayD(dstart);
            } while (DateUtil.compareDate(dstart, pa.getRowTo()) != 1);
        }
        return aList;
    }

}