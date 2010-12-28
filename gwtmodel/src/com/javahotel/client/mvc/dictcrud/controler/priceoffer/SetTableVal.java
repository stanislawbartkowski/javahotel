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
package com.javahotel.client.mvc.dictcrud.controler.priceoffer;

import java.math.BigDecimal;
import java.util.List;

import com.javahotel.client.IResLocator;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.mvc.checktable.view.IDecimalTableView;
import com.javahotel.client.mvc.seasonprice.model.ISpecialMap;
import com.javahotel.client.mvc.seasonprice.model.MapSpecialToI;
import com.javahotel.common.toobject.OfferPriceP;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class SetTableVal {

    private SetTableVal() {
    }

    private static class S implements ISpecialMap {

        private final IDecimalTableView tView;
        private final OfferPriceP oP;
        private final SetPriceForOffer off = HInjector.getI()
                .getSetPriceForOffer();

        S(IDecimalTableView tView, OfferPriceP oP) {
            this.tView = tView;
            this.oP = oP;
        }

        public void set(final List<MapSpecialToI> col) {
            List<String> rows = tView.getSRow();
            if (rows == null) {
                return;
            }
            for (int r = 0; r < rows.size(); r++) {
                String ss = rows.get(r);
                List<BigDecimal> va = off.createListPrice(col, oP, ss);
                tView.setRowVal(r, va);
            }
        }
    }

    static void setVal(IResLocator rI, GetSeasonSpecial sS,
            IDecimalTableView tView, OfferPriceP oP) {
        sS.runSpecial(oP.getSeason(), new S(tView, oP));
    }
}
