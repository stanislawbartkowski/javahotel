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
package com.javahotel.client.types;

import java.util.ArrayList;
import java.util.List;

import com.javahotel.client.PersonHotelRoles;

public class AccessRoles {

    private final List<PersonHotelRoles> li = new ArrayList<PersonHotelRoles>();

    public void addRole(String person, String hotel, String role) {

        PersonHotelRoles pe = null;
        for (PersonHotelRoles p : li) {
            if (p.eq(person, hotel)) {
                pe = p;
                break;
            }
        }
        if (pe == null) {
            pe = new PersonHotelRoles(person, hotel);
            li.add(pe);
        }
        pe.getRoles().add(role);
    }

    public List<PersonHotelRoles> getLi() {
        return li;
    }

}
