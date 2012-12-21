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
package com.javahotel.client;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.common.CUtil;

/**
 * @author hotel
 * 
 */
public class PersonHotelRoles {
    private final String person;
    private final String hotel;
    private final List<String> roles;

    public boolean eq(String person, String hotel) {
        if (!CUtil.EqNS(this.person, person)) {
            return false;
        }
        if (!CUtil.EqNS(this.hotel, hotel)) {
            return false;
        }
        return true;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getPerson() {
        return person;
    }

    public String getHotel() {
        return hotel;
    }

    public PersonHotelRoles(String person, String hotel) {
        this.person = person;
        this.hotel = hotel;
        roles = new ArrayList<String>();
    }

}
