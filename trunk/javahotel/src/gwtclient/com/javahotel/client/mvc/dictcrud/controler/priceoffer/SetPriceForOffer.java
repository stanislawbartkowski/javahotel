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
package com.javahotel.client.mvc.dictcrud.controler.priceoffer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.javahotel.client.mvc.seasonprice.model.ISeasonPriceModel;
import com.javahotel.client.mvc.seasonprice.model.MapSpecialToI;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.OfferServicePriceP;
import com.javahotel.common.toobject.OfferSpecialPriceP;

public class SetPriceForOffer {

    private void setWid(int i, List<BigDecimal> val, BigDecimal b) {
        val.set(i, b);
    }

    public List<BigDecimal> createListPrice(List<MapSpecialToI> col, OfferPriceP oP,
            String service) {

        List<BigDecimal> va = new ArrayList<BigDecimal>();
        int si = ISeasonPriceModel.MAXSPECIALNO + col.size() + 1;
        for (int i = 0; i < si; i++) {
            va.add(null);
        }
        List<OfferServicePriceP> colO = oP.getServiceprice();
        if (colO != null) {
            for (OfferServicePriceP pe : colO) {
                if (pe.getService().equals(service)) {
                    setWid(ISeasonPriceModel.HIGHSEASON, va, pe
                            .getHighseasonprice());
                    setWid(ISeasonPriceModel.HIGHSEASONWEEKEND, va, pe
                            .getHighseasonweekendprice());
                    setWid(ISeasonPriceModel.LOWSEASON, va, pe
                            .getLowseasonprice());
                    setWid(ISeasonPriceModel.LOWSEASONWEEKEND, va, pe
                            .getLowseasonweekendprice());
                    for (int ii = 0; ii < col.size(); ii++) {
                        Long pid = col.get(ii).getSpecId();
                        for (OfferSpecialPriceP oo : pe.getSpecialprice()) {
                            if (pid.equals(oo.getSpecialperiod())) {
                                setWid(ISeasonPriceModel.MAXSPECIALNO + ii + 1,
                                        va, oo.getPrice());
                            }
                        }
                    }
                }
            }
        }
        return va;
    }
}