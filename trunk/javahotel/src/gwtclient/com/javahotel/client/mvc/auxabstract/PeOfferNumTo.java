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
package com.javahotel.client.mvc.auxabstract;

import com.javahotel.common.toobject.IField;
import com.javahotel.common.toobject.OfferSeasonPeriodP;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class PeOfferNumTo extends NumAbstractTo {

    public PeOfferNumTo() {
        super(new OfferSeasonPeriodP(), OfferSeasonPeriodP.F.values());
    }

    @Override
    public Class getT(IField f) {
        OfferSeasonPeriodP o = (OfferSeasonPeriodP) a;
        return o.getT(f);
    }

    public Integer getLp() {
        OfferSeasonPeriodP oP = (OfferSeasonPeriodP) getO();
        return new Integer(oP.getPId().intValue());
   }

    public void setLp(Integer lP) {
        OfferSeasonPeriodP oP = (OfferSeasonPeriodP) getO();
        Long l = new Long(lP.longValue());
        oP.setPId(l);
    }
}
