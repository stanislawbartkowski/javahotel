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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.javahotel.db.jtypes.HId;
import com.javahotel.db.jtypes.IId;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@Entity
public class OfferServicePrice extends AbstractPriceOffer implements IId {

    @Id
    @GeneratedValue
    private Long id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceDictionary service;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "offerid", nullable = false)
    private OfferPrice offerid;

    public HId getId() {
        return new HId(id);
    }

    public void setId(HId id) {
        this.id = id.getL();
    }

    public ServiceDictionary getService() {
        return service;
    }

    public void setService(ServiceDictionary service) {
        this.service = service;
    }

    public OfferPrice getOfferid() {
        return offerid;
    }

    public void setOfferid(OfferPrice offerid) {
        this.offerid = offerid;
    }


}
