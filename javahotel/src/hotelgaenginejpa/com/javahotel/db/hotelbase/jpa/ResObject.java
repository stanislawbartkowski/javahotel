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
 
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.javahotel.common.command.RRoom;
import com.javahotel.db.hotelbase.types.IHotelDictionary;
import com.javahotel.db.jtypes.HId;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@KeyCollectionObject(keyCollectionField = "keyFacilities", objectCollectionField = "facilities", classObject = RoomFacilities.class)
@KeyObjects(keyFields = { "hotelId", "rStandardKey" }, objectFields = {
		"hotel", "rStandard" })
@Entity
public class ResObject implements IHotelDictionary {

	public ResObject() {
		facilities = new ArrayList<RoomFacilities>();
	}

	public Long getRStandardKey() {
		return rStandardKey;
	}

	public void setRStandardKey(Long standardKey) {
		rStandardKey = standardKey;
	}

	public List<Long> getKeyFacilities() {
		return keyFacilities;
	}

	public void setKeyFacilities(List<Long> keyFacilities) {
		this.keyFacilities = keyFacilities;
	}

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
		// this.hotelId = AfterLoadActionFactory.getId(hotel);
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

	// ===================

	@Basic(optional = false)
	private RRoom rType;

	@Basic
	private Integer noPerson;

	@Basic
	private Integer maxPerson;

	@Transient
	private RoomStandard rStandard;

	@Basic
	private Long rStandardKey;

	@Transient
	private List<RoomFacilities> facilities;

	@Basic
	private List<Long> keyFacilities;

	public RRoom getRType() {
		return rType;
	}

	public void setRType(RRoom rType) {
		this.rType = rType;
	}

	public Integer getNoPerson() {
		return noPerson;
	}

	public void setNoPerson(Integer noPerson) {
		this.noPerson = noPerson;
	}

	public Integer getMaxPerson() {
		return maxPerson;
	}

	public void setMaxPerson(Integer maxPerson) {
		this.maxPerson = maxPerson;
	}

	public RoomStandard getRStandard() {
		return rStandard;
	}

	public void setRStandard(RoomStandard rStandard) {
		this.rStandard = rStandard;
	}

	public List<RoomFacilities> getFacilities() {
		return facilities;
	}

	public void setFacilities(final List<RoomFacilities> facilities) {
		this.facilities = facilities;
	}

}
