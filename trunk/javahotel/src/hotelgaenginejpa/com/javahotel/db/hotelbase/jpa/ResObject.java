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
import javax.persistence.Transient;

import com.javahotel.common.command.RRoom;
import com.javahotel.db.hotelbase.types.IHotelDictionary;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@KeyCollectionObject(keyCollectionField = "keyFacilities", objectCollectionField = "facilities", classObject = RoomFacilities.class)
@KeyObjects(keyFields = { "hotelId", "rStandardKey" }, objectFields = {
        "hotel", "rStandard" })
@Entity
public class ResObject extends AbstractDictionary implements IHotelDictionary {

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

    @Basic(optional = false)
    private RRoom rType;

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
