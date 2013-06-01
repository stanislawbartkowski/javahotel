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
package com.gwthotel.hotelejb;

import java.util.List;

import com.gwthotel.hotel.IHotelProp;
import com.gwthotel.shared.PropDescription;

// IHotelProp<HotelServices>

public class AbstractHotelEJB<T extends PropDescription> implements
        IHotelProp<T> {

    protected IHotelProp<T> service;

    @Override
    public List<T> getList(String hotel) {
        return service.getList(hotel);
    }

    @Override
    public void addElem(String hotel, T elem) {
        service.addElem(hotel, elem);
    }

    @Override
    public void changeElem(String hotel, T elem) {
        service.changeElem(hotel, elem);
    }

    @Override
    public void deleteElem(String hotel, T elem) {
        service.deleteElem(hotel, elem);
    }

    @Override
    public void deleteAll(String hotel) {
        service.deleteAll(hotel);
    }

}
