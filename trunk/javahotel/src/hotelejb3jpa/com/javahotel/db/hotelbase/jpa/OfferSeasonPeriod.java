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

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.javahotel.common.toobject.SeasonPeriodT;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@Entity
public class OfferSeasonPeriod extends AbstractIID  {
    
    
    @Basic(optional = false)
    private Long pId;
        
    @Basic
    private String description;

    @Basic(optional = false)
    @Temporal(TemporalType.DATE)
    private Date startP;
    @Basic(optional = false)
    @Temporal(TemporalType.DATE)
    private Date endP;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "offerid", nullable = false)
    private OfferSeason offerid;
    
    @Basic(optional = false)
    private SeasonPeriodT periodT;


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

    public OfferSeason getOfferid() {
        return offerid;
    }

    public void setOfferid(OfferSeason offerid) {
        this.offerid = offerid;
    }

    public SeasonPeriodT getPeriodT() {
        return periodT;
    }

    public void setPeriodT(SeasonPeriodT periodT) {
        this.periodT = periodT;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Long getPId() {
        return pId;
    }

    public void setPId(Long pId) {
        this.pId = pId;
    }

}
