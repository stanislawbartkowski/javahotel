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
package com.javahotel.db.hotelbase.jpa;

import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@Entity
public class OfferSpecialPrice extends AbstractIID {

    @Basic
    private BigDecimal price;
    @Basic(optional=false)
    private Long specialperiod;
    @ManyToOne(optional = false)
    @JoinColumn(name = "priceid", nullable = false)
    private OfferServicePrice priceid;


    public Long getSpecialperiod() {
		return specialperiod;
	}

	public void setSpecialperiod(Long specialperiod) {
		this.specialperiod = specialperiod;
	}

	public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public OfferServicePrice getPriceid() {
        return priceid;
    }

    public void setPriceid(OfferServicePrice priceid) {
        this.priceid = priceid;
    }
}
