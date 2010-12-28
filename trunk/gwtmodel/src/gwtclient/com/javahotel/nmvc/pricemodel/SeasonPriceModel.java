/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.javahotel.nmvc.pricemodel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.javahotel.client.mvc.recordviewdef.GetRecordDefFactory;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.OfferSeasonPeriodP;
import com.javahotel.common.toobject.OfferServicePriceP;
import com.javahotel.common.toobject.OfferSpecialPriceP;
import com.javahotel.common.toobject.SeasonPeriodT;

class SeasonPriceModel implements ISeasonPriceModel {

    private final OfferSeasonP seasonP;
    private final GetRecordDefFactory gFactory;
    private final List<Long> pId;

    SeasonPriceModel(GetRecordDefFactory gFactory, OfferSeasonP seasonP) {
        this.seasonP = seasonP;
        this.gFactory = gFactory;
        pId = new ArrayList<Long>();
    }

    private void setWid(int i, List<BigDecimal> val, BigDecimal b) {
        val.set(i, b);
    }

    public List<String> pricesNames() {

        List<String> names = gFactory.getStandPriceNames();

        for (OfferSeasonPeriodP p : seasonP.getPeriods()) {
            if (p.getPeriodT() == SeasonPeriodT.SPECIAL) {
                names.add(p.getDescription());
                pId.add(p.getPId());
            }
        }
        return names;
    }

    private OfferServicePriceP findServicePrice(OfferPriceP priceP,
            String service) {
        List<OfferServicePriceP> colO = priceP.getServiceprice();
        if (colO == null) {
            return null;
        }
        for (OfferServicePriceP pe : colO) {
            if (pe.getService().equals(service)) {
                return pe;
            }
        }
        return null;
    }

    public List<BigDecimal> getPrices(OfferPriceP priceP, String service) {
        List<BigDecimal> va = new ArrayList<BigDecimal>();
        for (int i = 0; i < noPrices(); i++) {
            va.add(null);
        }
        OfferServicePriceP pe = findServicePrice(priceP, service);
        if (pe == null) {
            return va;
        }
        setWid(ISeasonPriceModel.HIGHSEASON, va, pe.getHighseasonprice());
        setWid(ISeasonPriceModel.HIGHSEASONWEEKEND, va, pe
                .getHighseasonweekendprice());
        setWid(ISeasonPriceModel.LOWSEASON, va, pe.getLowseasonprice());
        setWid(ISeasonPriceModel.LOWSEASONWEEKEND, va, pe
                .getLowseasonweekendprice());
        for (int ii = 0; ii < pId.size(); ii++) {
            Long pid = pId.get(ii);
            for (OfferSpecialPriceP oo : pe.getSpecialprice()) {
                if (!pid.equals(oo.getSpecialperiod())) {
                    continue;
                }
                setWid(ISeasonPriceModel.MAXSPECIALNO + ii + 1, va, oo
                        .getPrice());
            }
        }
        return va;
    }

    public void setPrices(OfferPriceP priceP, String service,
            List<BigDecimal> prices) {

        List<OfferServicePriceP> colO = priceP.getServiceprice();
        if (colO == null) {
            colO = new ArrayList<OfferServicePriceP>();
            priceP.setServiceprice(colO);
        }
        OfferServicePriceP pe = findServicePrice(priceP, service);
        if (pe == null) {
            pe = new OfferServicePriceP();
            colO.add(pe);
        }

        pe.setService(service);
        pe.setHighseasonprice(prices.get(ISeasonPriceModel.HIGHSEASON));
        pe.setHighseasonweekendprice(prices
                .get(ISeasonPriceModel.HIGHSEASONWEEKEND));
        pe.setLowseasonprice(prices.get(ISeasonPriceModel.LOWSEASON));
        pe.setLowseasonweekendprice(prices
                .get(ISeasonPriceModel.LOWSEASONWEEKEND));
        List<OfferSpecialPriceP> se = new ArrayList<OfferSpecialPriceP>();
        int specno=0;
        for (int i = ISeasonPriceModel.MAXSPECIALNO + 1; i < prices.size(); i++) {
            BigDecimal b = prices.get(i);
            Long id = pId.get(specno);
            OfferSpecialPriceP pp = new OfferSpecialPriceP();
            pp.setSpecialperiod(id);
            pp.setPrice(b);
            se.add(pp);
            specno++;
        }
        pe.setSpecialprice(se);
    }

    public int noPrices() {
        return ISeasonPriceModel.MAXSPECIALNO + pId.size() + 1;
    }

}
