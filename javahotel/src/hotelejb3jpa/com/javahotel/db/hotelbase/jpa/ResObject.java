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

import com.javahotel.common.command.RRoom;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@Entity
public class ResObject extends AbstractDictionary {
    
    @Basic(optional=false)
    private RRoom rType;
    
    @Basic
    private Integer noPerson;
    
    @Basic
    private Integer maxPerson;
    
    @ManyToOne
    @JoinColumn(name = "standard_id", nullable = false)
    private RoomStandard rStandard;
    
    @ManyToMany
    private List<RoomFacilities> facilities;


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
