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
package com.javahotel.common.tableprice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import com.javahotel.common.dateutil.DateUtil;
import com.javahotel.common.dateutil.GetPeriods;
import com.javahotel.common.dateutil.PeriodT;
import com.javahotel.common.dateutil.GetPeriods.StartWeek;
import com.javahotel.common.math.MathUtil;
import com.javahotel.common.seasonutil.CreateTableSeason;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.OfferSeasonPeriodP;
import com.javahotel.common.toobject.OfferServicePriceP;
import com.javahotel.common.toobject.OfferSpecialPriceP;
import com.javahotel.common.toobject.PaymentRowP;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class TableSeasonPrice {

    private List<PeriodT> periods;
    private final StartWeek sWeek;
    private OfferPriceP oP;

    public TableSeasonPrice(final StartWeek sWeek) {
        this.sWeek = sWeek;
        periods = null;
    }

    public void setPeriods(final OfferSeasonP oP) {
        periods = CreateTableSeason.createTable(oP, sWeek);
    }

    public void setPriceList(final OfferPriceP oP) {
        this.oP = oP;
    }
    
    private BigDecimal countP(BigDecimal price,Date dFrom,Date dTo) {
    	int no = DateUtil.noLodgings(dFrom, dTo);
    	return MathUtil.multI(price,no);    	
    }

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
                    default:
                        assert false : "Cannot be here";
                }
            }
            assert(price != null);
            BigDecimal pri = countP(price,pe.getFrom(),pe.getTo());
            pr.setCustomerPrice(pri);
            pr.setOfferPrice(pri);
            col.add(pr);
        }
        return col;
    }
}