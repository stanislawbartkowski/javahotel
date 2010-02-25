/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.javahotel.nmvc.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gwtmodel.table.common.CUtil;

public class AccessRoles {

    public class PersonHotelRoles {
        private final String person;
        private final String hotel;
        private final List<String> roles;

        boolean eq(String person, String hotel) {
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

        PersonHotelRoles(String person, String hotel) {
            this.person = person;
            this.hotel = hotel;
            roles = new ArrayList<String>();
        }

    }

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
        pe.roles.add(role);
    }

    public List<PersonHotelRoles> getLi() {
        return li;
    }

}
