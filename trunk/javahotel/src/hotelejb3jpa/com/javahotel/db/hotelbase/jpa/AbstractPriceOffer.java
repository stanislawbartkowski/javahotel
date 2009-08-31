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

package com.javahotel.db.hotelbase.jpa;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@MappedSuperclass
public abstract class AbstractPriceOffer {

    @Basic
    private BigDecimal highseasonprice;
    @Basic
    private BigDecimal lowseasonprice;
    @Basic
    private BigDecimal highseasonweekendprice;
    @Basic
    private BigDecimal lowseasonweekendprice;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "priceid")
    private List<OfferSpecialPrice> specialprice;

    public BigDecimal getHighseasonprice() {
        return highseasonprice;
    }

    public void setHighseasonprice(BigDecimal highseasonprice) {
        this.highseasonprice = highseasonprice;
    }

    public BigDecimal getLowseasonprice() {
        return lowseasonprice;
    }

    public void setLowseasonprice(BigDecimal lowseasonprice) {
        this.lowseasonprice = lowseasonprice;
    }

    public BigDecimal getHighseasonweekendprice() {
        return highseasonweekendprice;
    }

    public void setHighseasonweekendprice(BigDecimal highseasonweekendprice) {
        this.highseasonweekendprice = highseasonweekendprice;
    }

    public BigDecimal getLowseasonweekendprice() {
        return lowseasonweekendprice;
    }

    public void setLowseasonweekendprice(BigDecimal lowseasonweekendprice) {
        this.lowseasonweekendprice = lowseasonweekendprice;
    }

    public List<OfferSpecialPrice> getSpecialprice() {
        return specialprice;
    }

    public void setSpecialprice(final List<OfferSpecialPrice> specialprice) {
        this.specialprice = specialprice;
    }

}
