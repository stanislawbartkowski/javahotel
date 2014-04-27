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

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Parent;
import com.jython.ui.server.gae.security.entities.EDictionary;
import com.jython.ui.server.gae.security.entities.EInstance;
import com.jython.ui.server.gae.security.entities.EObject;

public abstract class EHotelDict extends EDictionary {

    @Parent
    private Key<EObject> hotel;

    public void setHotel(EObject ho) {
        Key<EInstance> pa = ho.getI();
        hotel = Key.create(pa, EObject.class, ho.getId());
    }

    public boolean isHotelSet() {
        return hotel != null;
    }

}
