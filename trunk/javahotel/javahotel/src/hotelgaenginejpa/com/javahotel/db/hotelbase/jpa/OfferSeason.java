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

import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.google.appengine.api.datastore.Key;
import com.javahotel.db.hotelbase.types.IHotelDictionary;
import com.javahotel.db.jtypes.HId;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@Entity
@KeyObject(keyField="hotelId",objectField="hotel")
@ObjectCollection(objectCollectionField="periods")
public class OfferSeason implements IHotelDictionary {
	
	// ============================================= 
	// Abstract Dictionary 
	// =============================================
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Key id;

	public HId getId() {
		return new HId(id);
	}

	public void setId(HId id) {
		this.id = id.getId();
	}
	
	public Long getHotelId() {
		return hotelId;
	}

	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}

	@Basic
	private Long hotelId;

	@Transient
	private RHotel hotel;

	public RHotel getHotel() {
		return hotel;
	}

	public void setHotel(RHotel hotel) {
		this.hotel = hotel;
	}

	@Basic(optional = false)
	private String name;
	@Basic
	private String description;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

// ===================================================	

    @Basic(optional=false)
    @Temporal(TemporalType.DATE)
    private Date startP;
    
    @Basic(optional=false)
    @Temporal(TemporalType.DATE)
    private Date endP;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "offerid")
    private Collection<OfferSeasonPeriod> periods;

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

    public Collection<OfferSeasonPeriod> getPeriods() {
        return periods;
    }

    public void setPeriods(final Collection<OfferSeasonPeriod> periods) {
        this.periods = periods;
    }
}
