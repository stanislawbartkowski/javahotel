/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
package com.gwthotel.hotel.service.gae.entities;


import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.jython.ui.server.gae.security.entities.EObjectDict;

@Entity
public class EHotelPriceList extends EObjectDict {

    private Date priceFrom;
    private Date priceTo;

    public Date getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(Date priceFrom) {
        this.priceFrom = priceFrom;
    }

    public Date getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(Date priceTo) {
        this.priceTo = priceTo;
    }

}
