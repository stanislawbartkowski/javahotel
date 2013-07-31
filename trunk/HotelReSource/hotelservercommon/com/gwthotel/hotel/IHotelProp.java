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
package com.gwthotel.hotel;

import java.util.List;

import com.gwthotel.admin.HotelId;
import com.gwthotel.shared.PropDescription;

public interface IHotelProp<T extends PropDescription> {

    List<T> getList(HotelId hotel);

    T addElem(HotelId hotel, T elem);

    void changeElem(HotelId hotel, T elem);

    void deleteElem(HotelId hotel, T elem);
    
    T findElem(HotelId hotel, String name);
}