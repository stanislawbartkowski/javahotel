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
package com.javahotel.db.hotelbase.jpa;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@Entity
public class OfferPrice extends AbstractDictionary {
    
	@Basic(optional = false)
	private String season;
        
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "offerid")
    private List<OfferServicePrice> serviceprice;


    public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public List<OfferServicePrice> getServiceprice() {
        return serviceprice;
    }

    public void setServiceprice(final List<OfferServicePrice> serviceprice) {
        this.serviceprice = serviceprice;
    }

    
}
