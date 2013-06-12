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
package com.gwthotel.adminejb;

import java.util.List;

import com.gwthotel.admin.AppInstanceId;
import com.gwthotel.admin.Hotel;
import com.gwthotel.admin.HotelRoles;
import com.gwthotel.admin.IHotelAdmin;
import com.gwthotel.admin.Person;

abstract class AbstractHotelAdminEJB implements IHotelAdmin {

    protected IHotelAdmin iHotel;

    @Override
    public List<Person> getListOfPersons(AppInstanceId i) {
        return iHotel.getListOfPersons(i);
    }

    @Override
    public List<Hotel> getListOfHotels(AppInstanceId i) {
        return iHotel.getListOfHotels(i);
    }

    @Override
    public List<HotelRoles> getListOfRolesForPerson(AppInstanceId i,
            String person) {
        return iHotel.getListOfRolesForPerson(i, person);
    }

    @Override
    public List<HotelRoles> getListOfRolesForHotel(AppInstanceId i, String hotel) {
        return iHotel.getListOfRolesForHotel(i, hotel);
    }

    @Override
    public void addOrModifHotel(AppInstanceId i, Hotel hotel,
            List<HotelRoles> roles) {
        iHotel.addOrModifHotel(i, hotel, roles);

    }

    @Override
    public void addOrModifPerson(AppInstanceId i, Person person,
            List<HotelRoles> roles) {
        iHotel.addOrModifPerson(i, person, roles);

    }

    @Override
    public void changePasswordForPerson(AppInstanceId i, String person,
            String password) {
        iHotel.changePasswordForPerson(i, person, password);

    }

    @Override
    public boolean validatePasswordForPerson(AppInstanceId i, String person,
            String password) {
        return iHotel.validatePasswordForPerson(i, person, password);
    }

    @Override
    public void clearAll(AppInstanceId i) {
        iHotel.clearAll(i);
    }

    @Override
    public void removePerson(AppInstanceId i, String person) {
        iHotel.removePerson(i, person);

    }

    @Override
    public void removeHotel(AppInstanceId i, String hotel) {
        iHotel.removeHotel(i, hotel);
    }

    @Override
    public String getPassword(AppInstanceId i, String person) {
        return iHotel.getPassword(i, person);
    }

}
