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
package com.javahotel.client.mvc.persistrecord;

import com.javahotel.client.IResLocator;
import com.javahotel.client.mvc.checktable.view.IDecimalTableView;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.seasonprice.model.ISeasonPriceModel;
import com.javahotel.client.mvc.seasonprice.model.ISpecialMap;
import com.javahotel.client.mvc.seasonprice.model.MapSpecialToI;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.OfferPriceP;
import com.javahotel.common.toobject.OfferServicePriceP;
import com.javahotel.common.toobject.OfferSpecialPriceP;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class PersistRecordPrice extends PersistRecordDict {

    PersistRecordPrice(final IResLocator rI) {
        super(rI, DictType.PriceListDict);

    }

    private class setPrice implements ISpecialMap {

        private final IDecimalTableView tView;
        private final int action;
        private final RecordModel mo;
        IPersistResult ires;

        setPrice(IDecimalTableView tView, int action, RecordModel a, IPersistResult ires) {
            this.tView = tView;
            this.action = action;
            this.mo = a;
            this.ires = ires;
        }

        public void set(ArrayList<MapSpecialToI> col) {
            ArrayList<String> rows = tView.getSRow();
            List<OfferServicePriceP> off = new ArrayList<OfferServicePriceP>();
            for (int i = 0; i < rows.size(); i++) {
                OfferServicePriceP o = new OfferServicePriceP();
                o.setService(rows.get(i));
                ArrayList<BigDecimal> val = tView.getRows(i);
                o.setHighseasonprice(val.get(ISeasonPriceModel.HIGHSEASON));
                o.setHighseasonweekendprice(val.get(ISeasonPriceModel.HIGHSEASONWEEKEND));
                o.setLowseasonprice(val.get(ISeasonPriceModel.LOWSEASON));
                o.setLowseasonweekendprice(val.get(ISeasonPriceModel.LOWSEASONWEEKEND));
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
            OfferPriceP oP = (OfferPriceP) mo.getA();
            oP.setServiceprice(off);
            ipersist(action, mo, ires);
        }
    }

    public void persist(int action, RecordModel a, IPersistResult ires) {
        OfferPriceP oP = (OfferPriceP) a.getA();
        ISeasonPriceModel iS = (ISeasonPriceModel) a.getAuxData();
        setPrice se = new setPrice(iS.getT(), action, a, ires);
        iS.setSpecial(oP.getSeason(), se);
    }
}
