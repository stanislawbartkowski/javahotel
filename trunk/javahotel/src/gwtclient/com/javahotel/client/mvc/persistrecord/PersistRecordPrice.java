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
package com.javahotel.client.mvc.persistrecord;

import java.util.List;

import com.javahotel.client.IResLocator;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.mvc.checktable.view.IDecimalTableView;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.dictcrud.controler.priceoffer.ExtractOfferPriceService;
import com.javahotel.client.mvc.seasonprice.model.ISeasonPriceModel;
import com.javahotel.client.mvc.seasonprice.model.ISpecialMap;
import com.javahotel.client.mvc.seasonprice.model.MapSpecialToI;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.OfferPriceP;

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

        public void set(List<MapSpecialToI> col) {
            OfferPriceP oP = (OfferPriceP) mo.getA();
            ExtractOfferPriceService eService = HInjector.getI().getExtractOfferPriceService();
            eService.ExtractOfferPrice(oP, tView, col);
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
