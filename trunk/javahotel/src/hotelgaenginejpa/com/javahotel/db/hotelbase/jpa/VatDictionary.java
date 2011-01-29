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
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.javahotel.db.hotelbase.types.IHotelDictionary;
import com.javahotel.db.jtypes.HId;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@Entity
@KeyObject(keyField="hotelId",objectField="hotel")
public class VatDictionary implements IHotelDictionary {

	// ============================================= 
	// Abstract Dictionary 
	// =============================================
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long id;

	public HId getId() {
		return new HId(id);
	}

	public void setId(HId id) {
		this.id = id.getL();
	}
	
	public Long getHotelId() {
		return hotelId;
	}

	public void setHotelId(Long hotelId) {
		this.hotelId = hotelId;
	}

	public void setId(Long id) {
		this.id = id;
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

    @Basic
    private BigDecimal vatPercent;
    @Basic(optional = false)
    private boolean defVat;

    public VatDictionary() {
        defVat = false;
    }

    public BigDecimal getVatPercent() {
        return vatPercent;
    }

    public void setVatPercent(BigDecimal vatPercent) {
        this.vatPercent = vatPercent;
    }

    public boolean isDefVat() {
        return defVat;
    }

    public void setDefVat(boolean defVat) {
        this.defVat = defVat;
    }
}
