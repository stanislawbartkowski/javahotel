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
package com.jythonui.server.ejb;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import com.google.inject.Inject;
import com.jython.serversecurity.AppInstanceId;
import com.jython.serversecurity.IOObjectAdmin;
import com.jython.serversecurity.OObject;
import com.jython.serversecurity.OObjectRoles;
import com.jython.serversecurity.Person;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.defa.GuiceInterceptor;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@EJB(name = ISharedConsts.COMMONOBJECTADMINJNDI, beanInterface = IOObjectAdmin.class)
@Remote
@Interceptors(value = { GuiceInterceptor.class })
public class OObjectAdminEJB implements IOObjectAdmin {

    private IOObjectAdmin iAdmin;
    
    @Inject
    public void inject(IOObjectAdmin iAdmin) {
        this.iAdmin = iAdmin;
    }

    @Override
    public List<Person> getListOfPersons(AppInstanceId i) {
        return iAdmin.getListOfPersons(i);
    }

    @Override
    public List<OObject> getListOfObjects(AppInstanceId i) {
        return iAdmin.getListOfObjects(i);
    }

    @Override
    public List<OObjectRoles> getListOfRolesForPerson(AppInstanceId i,
            String person) {
        return iAdmin.getListOfRolesForPerson(i, person);
    }

    @Override
    public List<OObjectRoles> getListOfRolesForObject(AppInstanceId i,
            String object) {
        return iAdmin.getListOfRolesForObject(i, object);
    }

    @Override
    public void addOrModifObject(AppInstanceId i, OObject object,
            List<OObjectRoles> roles) {
        iAdmin.addOrModifObject(i, object, roles);
    }

    @Override
    public void addOrModifPerson(AppInstanceId i, Person person,
            List<OObjectRoles> roles) {
        iAdmin.addOrModifPerson(i, person, roles);
    }

    @Override
    public void changePasswordForPerson(AppInstanceId i, String person,
            String password) {
        iAdmin.changePasswordForPerson(i, person, password);
    }

    @Override
    public boolean validatePasswordForPerson(AppInstanceId i, String person,
            String password) {
        return iAdmin.validatePasswordForPerson(i, person, password);
    }

    @Override
    public String getPassword(AppInstanceId i, String person) {
        return iAdmin.getPassword(i, person);
    }

    @Override
    public void clearAll(AppInstanceId i) {
        iAdmin.clearAll(i);

    }

    @Override
    public void removePerson(AppInstanceId i, String person) {
        iAdmin.removePerson(i, person);

    }

    @Override
    public void removeObject(AppInstanceId i, String object) {
        iAdmin.removeObject(i, object);
    }

}
