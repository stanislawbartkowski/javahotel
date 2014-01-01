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
package com.gwthotel.admin;

import java.util.List;

public interface IHotelAdmin {

    List<Person> getListOfPersons(AppInstanceId i);

    List<Hotel> getListOfHotels(AppInstanceId i);

    List<HotelRoles> getListOfRolesForPerson(AppInstanceId i, String person);

    List<HotelRoles> getListOfRolesForHotel(AppInstanceId i, String hotel);

    void addOrModifHotel(AppInstanceId i, Hotel hotel, List<HotelRoles> roles);

    void addOrModifPerson(AppInstanceId i, Person person, List<HotelRoles> roles);

    void changePasswordForPerson(AppInstanceId i, String person, String password);

    boolean validatePasswordForPerson(AppInstanceId i, String person,
            String password);

    String getPassword(AppInstanceId i, String person);

    void clearAll(AppInstanceId i);

    void removePerson(AppInstanceId i, String person);
 
    void removeHotel(AppInstanceId i, String hotel);
}
