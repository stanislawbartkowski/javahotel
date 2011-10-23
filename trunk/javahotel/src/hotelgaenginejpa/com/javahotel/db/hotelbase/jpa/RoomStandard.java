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

import com.javahotel.db.hotelbase.types.IHotelDictionary;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@KeyCollectionObject(keyCollectionField = "keyServices", objectCollectionField = "services", classObject = ServiceDictionary.class)
@KeyObject(keyField = "hotelId", objectField = "hotel")
@Entity
public class RoomStandard extends AbstractDictionary implements
        IHotelDictionary {

    public RoomStandard() {
        services = new ArrayList<ServiceDictionary>();
    }

    public List<Long> getKeyServices() {
        return keyServices;
    }

    public void setKeyServices(List<Long> keyServices) {
        this.keyServices = keyServices;
    }

    @Transient
    private List<ServiceDictionary> services;

    @Basic
    private List<Long> keyServices;

    public List<ServiceDictionary> getServices() {
        return services;
    }

    public void setServices(final List<ServiceDictionary> services) {
        this.services = services;
    }

}
