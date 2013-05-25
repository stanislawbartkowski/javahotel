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

import com.gwthotel.hotel.services.HotelServices;
import com.gwthotel.hotel.services.IHotelServices;

public class AbstractHotelServicesEJB implements IHotelServices {

    protected IHotelServices iServices;

    @Override
    public List<HotelServices> getList(String hotel) {
        return iServices.getList(hotel);
    }

    @Override
    public void addElem(String hotel, HotelServices elem) {
        iServices.addElem(hotel, elem);

    }

    @Override
    public void changeElem(String hotel, HotelServices elem) {
        iServices.changeElem(hotel, elem);

    }

    @Override
    public void deleteElem(String hotel, HotelServices elem) {
        iServices.deleteElem(hotel, elem);

    }

    @Override
    public void deleteAll(String hotel) {
        iServices.deleteAll(hotel);

    }

}
