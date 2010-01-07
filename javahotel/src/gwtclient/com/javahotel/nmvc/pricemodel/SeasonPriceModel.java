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

    public List<BigDecimal> getPrices(OfferPriceP priceP, String service) {
        List<BigDecimal> va = new ArrayList<BigDecimal>();
        for (int i = 0; i <= ISeasonPriceModel.MAXSPECIALNO + pId.size(); i++) {
            va.add(null);
        }
        List<OfferServicePriceP> colO = priceP.getServiceprice();
        if (colO == null) {
            return va;
        }
        for (OfferServicePriceP pe : colO) {
            if (!pe.getService().equals(service)) {
                continue;
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
        }
        return va;
    }

    public void setPrices(OfferPriceP priceP, String service,
            List<BigDecimal> prices) {

    }

}
