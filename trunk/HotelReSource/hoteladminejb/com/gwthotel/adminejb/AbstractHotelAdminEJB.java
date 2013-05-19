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

import com.gwthotel.admin.Hotel;
import com.gwthotel.admin.HotelRoles;
import com.gwthotel.admin.IHotelAdmin;
import com.gwthotel.admin.Person;

abstract public class AbstractHotelAdminEJB implements IHotelAdmin {

    // private IHotelAdmin iHotel = ServiceInjector.constructHotelAdmin();

    protected IHotelAdmin iHotel;

    @Override
    public List<Person> getListOfPersons() {
        return iHotel.getListOfPersons();
    }

    @Override
    public List<Hotel> getListOfHotels() {
        return iHotel.getListOfHotels();
    }

    @Override
    public List<HotelRoles> getListOfRolesForPerson(String person) {
        return iHotel.getListOfRolesForPerson(person);
    }

    @Override
    public List<HotelRoles> getListOfRolesForHotel(String hotel) {
        return iHotel.getListOfRolesForHotel(hotel);
    }

    @Override
    public void addOrModifHotel(Hotel hotel, List<HotelRoles> roles) {
        iHotel.addOrModifHotel(hotel, roles);

    }

    @Override
    public void addOrModifPerson(Person person, List<HotelRoles> roles) {
        iHotel.addOrModifPerson(person, roles);

    }

    @Override
    public void changePasswordForPerson(String person, String password) {
        iHotel.changePasswordForPerson(person, password);

    }

    @Override
    public boolean validatePasswordForPerson(String person, String password) {
        return iHotel.validatePasswordForPerson(person, password);
    }

    @Override
    public void clearAll() {
        iHotel.clearAll();
    }

    @Override
    public void removePerson(String person) {
        iHotel.removePerson(person);

    }

    @Override
    public void removeHotel(String hotel) {
        iHotel.removeHotel(hotel);
    }

    @Override
    public String getPassword(String person) {
        return iHotel.getPassword(person);
    }

}
