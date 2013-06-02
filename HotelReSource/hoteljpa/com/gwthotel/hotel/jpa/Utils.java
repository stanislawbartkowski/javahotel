/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
package com.gwthotel.hotel.jpa;

import com.gwthotel.admin.jpa.PropUtils;
import com.gwthotel.hotel.jpa.entities.EHotelServices;
import com.gwthotel.hotel.services.HotelServices;
import com.gwthotel.shared.IHotelConsts;

public class Utils {

    private Utils() {
    }

    public static HotelServices toT(EHotelServices sou) {
        HotelServices ho = new HotelServices();
        PropUtils.copyToProp(ho, sou);
        ho.setNoPersons(sou.getNoPersons());
        ho.setAttr(IHotelConsts.VATPROP, sou.getVat());
        return ho;
    }

}
