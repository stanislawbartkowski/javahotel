/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
package com.gwthotel.hotel.service.gae.entities;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;
import com.gwthotel.admin.gae.entities.EHotel;
import com.gwthotel.admin.gae.entities.EInstance;

abstract class EHotelParent {

    @Id
    private Long id;

    @Parent
    private Key<EHotel> hotel;

    public void setHotel(EHotel ho) {
        Key<EInstance> pa = ho.getI();
        hotel = Key.create(pa, EHotel.class, ho.getId());
    }

    public boolean isHotelSet() {
        return hotel != null;
    }
    
    private String creationPerson;

    private String modifPerson;

    private Date creationDate;

    private Date modifDate;

    public String getCreationPerson() {
        return creationPerson;
    }

    public void setCreationPerson(String creationPerson) {
        this.creationPerson = creationPerson;
    }

    public String getModifPerson() {
        return modifPerson;
    }

    public void setModifPerson(String modifPerson) {
        this.modifPerson = modifPerson;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModifDate() {
        return modifDate;
    }

    public void setModifDate(Date modifDate) {
        this.modifDate = modifDate;
    }

    public Long getId() {
        return id;
    }


}
