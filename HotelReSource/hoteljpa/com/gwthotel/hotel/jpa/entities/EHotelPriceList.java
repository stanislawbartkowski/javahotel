/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
package com.gwthotel.hotel.jpa.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.jython.serversecurity.jpa.entities.EObjectDict;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "hotel", "name" }))
@NamedQueries({
        @NamedQuery(name = "findAllPriceLists", query = "SELECT x FROM EHotelPriceList x WHERE x.hotel = ?1"),
        @NamedQuery(name = "deleteAllPriceLists", query = "DELETE FROM EHotelPriceList x WHERE x.hotel = ?1"),
        @NamedQuery(name = "findOnePriceList", query = "SELECT x FROM EHotelPriceList x WHERE x.hotel = ?1 AND x.name = ?2") })
public class EHotelPriceList extends EObjectDict {

    @Temporal(TemporalType.DATE)
    private Date priceFrom;
    @Temporal(TemporalType.DATE)
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
