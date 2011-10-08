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

import java.util.List;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@Entity
public class OfferSeason extends AbstractDictionary {

    @Basic(optional=false)
    @Temporal(TemporalType.DATE)
    private Date startP;
      
    @Basic(optional=false)
    @Temporal(TemporalType.DATE)
    private Date endP;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "offerid")
    private List<OfferSeasonPeriod> periods;


    public Date getStartP() {
        return startP;
    }

    public void setStartP(final Date startP) {
        this.startP = startP;
    }

    public Date getEndP() {
        return endP;
    }

    public void setEndP(final Date endP) {
        this.endP = endP;
    }

    public List<OfferSeasonPeriod> getPeriods() {
        return periods;
    }

    public void setPeriods(final List<OfferSeasonPeriod> periods) {
        this.periods = periods;
    }
}
