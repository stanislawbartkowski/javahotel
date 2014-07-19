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
package com.jython.serversecurity.persons;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.jython.serversecurity.AppInstanceId;
import com.jython.serversecurity.IOObjectAdmin;
import com.jython.serversecurity.OObject;
import com.jython.serversecurity.OObjectRoles;
import com.jython.serversecurity.Person;
import com.jythonui.server.ISharedConsts;

public class SecurityForPersons implements IOObjectAdmin {

    private final IOObjectAdmin iObject;

    private OObject getHolderObject(AppInstanceId i) {
        List<OObject> li = iObject.getListOfObjects(i);
        if (li.isEmpty()) {
            OObject o = new OObject();
            o.setName(ISharedConsts.SINGLEOBJECTHOLDER);
            iObject.addOrModifObject(i, o, new ArrayList<OObjectRoles>());
        }
        li = iObject.getListOfObjects(i);
        return li.get(0);
    }

    @Inject
    public SecurityForPersons(IOObjectAdmin iObject) {
        this.iObject = iObject;
    }

    @Override
    public List<Person> getListOfPersons(AppInstanceId i) {
        return iObject.getListOfPersons(i);
    }

    @Override
    public List<OObject> getListOfObjects(AppInstanceId i) {
        return null;
    }

    @Override
    public List<OObjectRoles> getListOfRolesForPerson(AppInstanceId i,
            String person) {
        return iObject.getListOfRolesForPerson(i, person);
    }

    @Override
    public List<OObjectRoles> getListOfRolesForObject(AppInstanceId i,
            String object) {
        return null;
    }

    @Override
    public void addOrModifObject(AppInstanceId i, OObject object,
            List<OObjectRoles> roles) {
        // do nothing
    }

    @Override
    public void addOrModifPerson(AppInstanceId i, Person person,
            List<OObjectRoles> roles) {
        OObject o = getHolderObject(i);
        List<OObjectRoles> nroles = new ArrayList<OObjectRoles>();
        for (OObjectRoles r : roles) {
            OObjectRoles ro = new OObjectRoles(o);
            ro.getRoles().addAll(r.getRoles());
            nroles.add(ro);

        }
        iObject.addOrModifPerson(i, person, nroles);

    }

    @Override
    public void changePasswordForPerson(AppInstanceId i, String person,
            String password) {
        iObject.changePasswordForPerson(i, person, password);
    }

    @Override
    public boolean validatePasswordForPerson(AppInstanceId i, String person,
            String password) {
        return iObject.validatePasswordForPerson(i, person, password);
    }

    @Override
    public String getPassword(AppInstanceId i, String person) {
        return iObject.getPassword(i, person);
    }

    @Override
    public void clearAll(AppInstanceId i) {
        iObject.clearAll(i);
    }

    @Override
    public void removePerson(AppInstanceId i, String person) {
        iObject.removePerson(i, person);

    }

    @Override
    public void removeObject(AppInstanceId i, String object) {
        // do nothing
    }

}
