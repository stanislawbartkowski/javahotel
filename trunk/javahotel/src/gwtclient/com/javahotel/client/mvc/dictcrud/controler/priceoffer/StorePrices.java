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

import com.javahotel.client.mvc.checktable.view.IDecimalTableView;
import com.javahotel.client.mvc.seasonprice.model.ISeasonPriceModel;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class StorePrices {

    private class C {

        final int row;
        final List<BigDecimal> val;

        C(int r) {
            this.row = r;
            val = new ArrayList<BigDecimal>();
        }
    }

    final private List<C> st = new ArrayList<C>();
    final private IDecimalTableView t;

    StorePrices(IDecimalTableView t) {

        this.t = t;
        List<String> rows = t.getSRow();
        if (rows != null) {
            for (int i = 0; i < rows.size(); i++) {
                C c = new C(i);
                List<BigDecimal> val = t.getRows(i);
                for (int r = 0; r <= ISeasonPriceModel.MAXSPECIALNO; r++) {
                    c.val.add(val.get(r));
                }
                st.add(c);
            }
        }
    }

    void restore() {
        for (C c : st) {
            t.setRowVal(c.row, c.val);
        }
    }
}
