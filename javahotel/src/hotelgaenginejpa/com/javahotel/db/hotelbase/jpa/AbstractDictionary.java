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

import javax.persistence.Basic;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.google.appengine.api.datastore.Key;
import com.javahotel.db.jtypes.HId;

/**
 * @author hotel
 * 
 */
@MappedSuperclass
public class AbstractDictionary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key id;

    @Basic
    private Long hotelId;

    @Transient
    private RHotel hotel;

    @Basic(optional = false)
    private String name;
    @Basic
    private String description;

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

    public RHotel getHotel() {
        return hotel;
    }

    public void setHotel(RHotel hotel) {
        this.hotel = hotel;
    }

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

}
