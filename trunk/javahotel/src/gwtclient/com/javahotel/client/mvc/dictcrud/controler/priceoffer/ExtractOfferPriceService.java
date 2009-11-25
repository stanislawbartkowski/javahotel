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
package com.javahotel.client.mvc.dictcrud.controler.priceoffer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.javahotel.client.mvc.checktable.view.IDecimalTableView;
import com.javahotel.client.mvc.seasonprice.model.ISeasonPriceModel;
import com.javahotel.client.mvc.seasonprice.model.MapSpecialToI;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.OfferServicePriceP;
import com.javahotel.common.toobject.OfferSpecialPriceP;

public class ExtractOfferPriceService {

    public void ExtractOfferPrice(OfferPriceP oP, IDecimalTableView tView,
            List<MapSpecialToI> col) {

        List<String> rows = tView.getSRow();
        List<OfferServicePriceP> off = new ArrayList<OfferServicePriceP>();
        for (int i = 0; i < rows.size(); i++) {
            OfferServicePriceP o = new OfferServicePriceP();
            o.setService(rows.get(i));
            List<BigDecimal> val = tView.getRows(i);
            o.setHighseasonprice(val.get(ISeasonPriceModel.HIGHSEASON));
            o.setHighseasonweekendprice(val
                    .get(ISeasonPriceModel.HIGHSEASONWEEKEND));
            o.setLowseasonprice(val.get(ISeasonPriceModel.LOWSEASON));
            o.setLowseasonweekendprice(val
                    .get(ISeasonPriceModel.LOWSEASONWEEKEND));
            List<OfferSpecialPriceP> se = new ArrayList<OfferSpecialPriceP>();
            for (int co = 0; co < col.size(); co++) {
                OfferSpecialPriceP pp = new OfferSpecialPriceP();
                pp.setSpecialperiod(col.get(co).getSpecId());
                pp.setPrice(val.get(ISeasonPriceModel.MAXSPECIALNO + co + 1));
                se.add(pp);
            }
            o.setSpecialprice(se);
            off.add(o);
        }
        oP.setServiceprice(off);
    }

}
