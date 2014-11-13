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
package com.jython.serversecurity;

import java.util.List;

public interface IOObjectAdmin {

    List<Person> getListOfPersons(AppInstanceId i);

    List<OObject> getListOfObjects(AppInstanceId i);

    List<OObjectRoles> getListOfRolesForPerson(AppInstanceId i, String person);

    List<OObjectRoles> getListOfRolesForObject(AppInstanceId i, String object);

    void addOrModifObject(AppInstanceId i, OObject object,
            List<OObjectRoles> roles);

    void addOrModifPerson(AppInstanceId i, Person person,
            List<OObjectRoles> roles);

    void changePasswordForPerson(AppInstanceId i, String person, String password);

    boolean validatePasswordForPerson(AppInstanceId i, String person,
            String password);

    String getPassword(AppInstanceId i, String person);

    void clearAll(AppInstanceId i);

    void removePerson(AppInstanceId i, String person);

    void removeObject(AppInstanceId i, String object);
}
