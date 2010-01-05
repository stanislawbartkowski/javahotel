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

import java.util.List;

import com.javahotel.client.mvc.recordviewdef.GetRecordDefFactory;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.OfferSeasonPeriodP;
import com.javahotel.common.toobject.SeasonPeriodT;

class SeasonPriceModel implements ISeasonPriceModel {

    private final OfferSeasonP seasonP;
    private final GetRecordDefFactory gFactory;

    SeasonPriceModel(GetRecordDefFactory gFactory, OfferSeasonP seasonP) {
        this.seasonP = seasonP;
        this.gFactory = gFactory;
    }

    public List<String> pricesNames() {

        List<String> names = gFactory.getStandPriceNames();

        for (OfferSeasonPeriodP p : seasonP.getPeriods()) {
            if (p.getPeriodT() == SeasonPeriodT.SPECIAL) {
                names.add(p.getDescription());
            }
        }
        return names;
    }

}
