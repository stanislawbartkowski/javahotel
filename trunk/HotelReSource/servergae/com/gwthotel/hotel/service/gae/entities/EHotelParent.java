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
package com.gwthotel.hotel.service.gae.entities;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;
import com.gwthotel.admin.gae.entities.EHotel;

abstract class EHotelParent {
    
    @Id
    private Long id;

    @Parent
    private Key<EHotel> hotel;

    public void setHotel(EHotel ho) {
        hotel = Key.create(EHotel.class, ho.getId());
    }

    public boolean isHotelSet() {
        return hotel != null;
    }


}
