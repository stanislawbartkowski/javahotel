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

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.javahotel.db.hotelbase.types.IHotelDictionary;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@MappedSuperclass
public abstract class AbstractDictionary extends AbstractPureDictionary implements IHotelDictionary {

    @ManyToOne(optional = false)
    @JoinColumn(name = "hotel_id", nullable = false)
    private RHotel hotel;

    @Override
    public RHotel getHotel() {
        return hotel;
    }

    @Override
    public void setHotel(RHotel hotel) {
        this.hotel = hotel;
    }
}
