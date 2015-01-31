package com.jython.serversecurity.jpa;

/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.google.inject.Inject;
import com.gwtmodel.table.common.CUtil;
import com.jython.serversecurity.AppInstanceId;
import com.jython.serversecurity.IOObjectAdmin;
import com.jython.serversecurity.OObject;
import com.jython.serversecurity.OObjectRoles;
import com.jython.serversecurity.Person;
import com.jython.serversecurity.jpa.entities.EDictEntry;
import com.jython.serversecurity.jpa.entities.EObject;
import com.jython.serversecurity.jpa.entities.EPersonPassword;
import com.jython.serversecurity.jpa.entities.EPersonRoles;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;
import com.jython.ui.server.jpatrans.JpaTransaction;
import com.jythonui.server.BUtil;
import com.jythonui.server.ISharedConsts;
import com.jythonui.server.UtilHelper;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.shared.RMap;

public class OObjectAdminJpa extends UtilHelper implements IOObjectAdmin {

    private final ITransactionContextFactory iC;
    private final IGetLogMess lMess;

    @Inject
    public OObjectAdminJpa(ITransactionContextFactory iC,
            @Named(ISharedConsts.JYTHONMESSSERVER) IGetLogMess lMess) {
        this.iC = iC;
        this.lMess = lMess;
    }

    private abstract class doTransaction extends JpaTransaction {

        final AppInstanceId i;

        private doTransaction(AppInstanceId i) {
            super(iC);
            this.i = i;
            if (i.getId() == null) {
                String mess = lMess.getMess(IErrorCode.ERRORCODE85,
                        ILogMess.INSTANCEIDCANNOTNENULLHERE);
                errorLog(mess);
            }
        }
    }

    private void addRole(List<OObjectRoles> resList, RMap object, EPersonRoles p) {
        OObjectRoles role = new OObjectRoles(object);
        for (String r : p.getRoles()) {
            role.getRoles().add(r);
        }
        resList.add(role);

    }

    private EObject getObjectByName(EntityManager em, AppInstanceId i,
            String name) {
        Query q = em.createNamedQuery("findObjectByName");
        q.setParameter(1, i.getId());
        q.setParameter(2, name);
        try {
            EObject hote = (EObject) q.getSingleResult();
            return hote;
        } catch (NoResultException e) {
            return null;
        }
    }

    private EPersonPassword getPersonByName(EntityManager em, AppInstanceId i,
            String name) {
        Query q = em.createNamedQuery("findPersonByName");
        q.setParameter(1, i.getId());
        q.setParameter(2, name);
        try {
            EPersonPassword pers = (EPersonPassword) q.getSingleResult();
            return pers;
        } catch (NoResultException e) {
            return null;
        }
    }

    private class GetListOfRolesForPerson extends doTransaction {

        private final String person;
        private List<OObjectRoles> resList = new ArrayList<OObjectRoles>();

        GetListOfRolesForPerson(AppInstanceId i, String person) {
            super(i);
            this.person = person;
        }

        @Override
        protected void dosth(EntityManager em) {
            EPersonPassword pers = getPersonByName(em, i, person);
            if (pers == null) {
                resList = null;
                return;
            }
            Query q = em.createNamedQuery("getListOfRolesForPerson");
            q.setParameter(1, pers);
            List<EPersonRoles> list = q.getResultList();
            for (EPersonRoles p : list) {
                // Query q1 = em.createNamedQuery("findObjectByLong");
                // q1.setParameter(1, p.getObject());
                // EObject h = (EObject) q1.getSingleResult();
                EObject h = p.getObject();
                OObject ho = new OObject();
                PropUtils.copyToProp(ho, h);
                addRole(resList, ho, p);
            }
        }

    }

    @Override
    public List<OObjectRoles> getListOfRolesForPerson(AppInstanceId i,
            String person) {
        GetListOfRolesForPerson com = new GetListOfRolesForPerson(i, person);
        com.executeTran();
        return com.resList;
    }

    private class GetListOfRolesForOObject extends doTransaction {

        private final String Object;
        private List<OObjectRoles> resList = new ArrayList<OObjectRoles>();

        GetListOfRolesForOObject(AppInstanceId i, String Object) {
            super(i);
            this.Object = Object;
        }

        @Override
        protected void dosth(EntityManager em) {
            EObject hote = getObjectByName(em, i, Object);
            if (hote == null) {
                resList = null;
                return;
            }
            Query q = em.createNamedQuery("getListOfRolesForObject");
            q.setParameter(1, hote);
            List<EPersonRoles> list = q.getResultList();
            for (EPersonRoles p : list) {
                // Query q1 = em.createNamedQuery("findPersonByLong");
                // q1.setParameter(1, p.);
                // EPersonPassword h = (EPersonPassword) q1.getSingleResult();
                // EPersonPassword h = em.find(EPersonPassword.class,
                // p.getId());
                EPersonPassword h = p.getPerson();
                Person ho = new Person();
                PropUtils.copyToProp(ho, h);
                addRole(resList, ho, p);
            }
        }

    }

    @Override
    public List<OObjectRoles> getListOfRolesForObject(AppInstanceId i,
            String Object) {
        GetListOfRolesForOObject comm = new GetListOfRolesForOObject(i, Object);
        comm.executeTran();
        return comm.resList;
    }

    private void modifPersonRoles(EntityManager em, EObject object,
            EPersonPassword pe, OObjectRoles role) {
        EPersonRoles eRoles = new EPersonRoles();
        eRoles.setObject(object);
        eRoles.setPerson(pe);
        ArrayList<String> roles = new ArrayList<String>();
        for (String ro : role.getRoles()) {
            roles.add(ro);
        }
        eRoles.setRoles(roles);
        em.persist(eRoles);

    }

    private class AddModifOObject extends doTransaction {

        private final OObject OObject;
        private final List<OObjectRoles> roles;

        AddModifOObject(AppInstanceId i, OObject OObject,
                List<OObjectRoles> roles) {
            super(i);
            this.OObject = OObject;
            this.roles = roles;
        }

        @Override
        protected void dosth(EntityManager em) {
            EObject hote = getObjectByName(em, i, OObject.getName());
            boolean create = false;
            if (hote == null) {
                hote = new EObject();
                hote.setInstanceId(i.getId());
                create = true;
            }
            PropUtils.copyToEDict(hote, OObject);
            BUtil.setCreateModif(i.getPerson(), hote, create);
            em.persist(hote);
            makekeys();
            Query q = em.createNamedQuery("removeRolesForObject");
            q.setParameter(1, hote);
            q.executeUpdate();
            for (OObjectRoles rol : roles) {
                Person pe = (Person) rol.getObject();
                String person = pe.getName();
                EPersonPassword pers = getPersonByName(em, i, person);
                modifPersonRoles(em, hote, pers, rol);
            }
        }
    }

    @Override
    public void addOrModifObject(AppInstanceId i, OObject Object,
            List<OObjectRoles> roles) {
        AddModifOObject comma = new AddModifOObject(i, Object, roles);
        comma.executeTran();
    }

    private class AddModifPerson extends doTransaction {

        private final Person person;
        private final List<OObjectRoles> roles;

        AddModifPerson(AppInstanceId i, Person person, List<OObjectRoles> roles) {
            super(i);
            this.person = person;
            this.roles = roles;
        }

        @Override
        protected void dosth(EntityManager em) {
            EPersonPassword pe = getPersonByName(em, i, person.getName());
            boolean create = false;
            if (pe == null) {
                pe = new EPersonPassword();
                pe.setInstanceId(i.getId());
                create = true;
            }
            PropUtils.copyToEDict(pe, person);
            BUtil.setCreateModif(i.getPerson(), pe, create);
            em.persist(pe);
            makekeys();
            Query q = em.createNamedQuery("removeRolesForPerson");
            q.setParameter(1, pe);
            q.executeUpdate();
            for (OObjectRoles rol : roles) {
                OObject ho = (OObject) rol.getObject();
                String OObject = ho.getName();
                EObject hote = getObjectByName(em, i, OObject);
                modifPersonRoles(em, hote, pe, rol);
            }
        }
    }

    @Override
    public void addOrModifPerson(AppInstanceId i, Person person,
            List<OObjectRoles> roles) {
        AddModifPerson comma = new AddModifPerson(i, person, roles);
        comma.executeTran();
    }

    private class ChangePassword extends doTransaction {

        private final String person;
        private final String password;

        ChangePassword(AppInstanceId i, String person, String password) {
            super(i);
            this.person = person;
            this.password = password;
        }

        @Override
        protected void dosth(EntityManager em) {
            EPersonPassword pe = getPersonByName(em, i, person);
            pe.setPassword(password);
            em.persist(pe);
        }
    }

    @Override
    public void changePasswordForPerson(AppInstanceId i, String person,
            String password) {
        ChangePassword comma = new ChangePassword(i, person, password);
        comma.executeTran();
    }

    private class ValidatePassword extends doTransaction {

        private final String person;
        private final String password;
        boolean ok = false;

        ValidatePassword(AppInstanceId i, String person, String password) {
            super(i);
            this.person = person;
            this.password = password;
        }

        @Override
        protected void dosth(EntityManager em) {
            EPersonPassword pe = getPersonByName(em, i, person);
            if (pe == null)
                return;
            if (CUtil.EmptyS(pe.getPassword()))
                return;
            ok = password.equals(pe.getPassword());
        }
    }

    @Override
    public boolean validatePasswordForPerson(AppInstanceId i, String person,
            String password) {
        ValidatePassword comma = new ValidatePassword(i, person, password);
        comma.executeTran();
        return comma.ok;
    }

    private class GetListOfPersons extends doTransaction {

        private final List<Person> pList = new ArrayList<Person>();

        GetListOfPersons(AppInstanceId i) {
            super(i);
        }

        @Override
        protected void dosth(EntityManager em) {
            Query q = em.createNamedQuery("findAllPersons");
            q.setParameter(1, i.getId());
            List<EPersonPassword> resList = q.getResultList();
            for (EPersonPassword e : resList) {
                Person pe = new Person();
                PropUtils.copyToProp(pe, e);
                pList.add(pe);
            }
        }

    }

    @Override
    public List<Person> getListOfPersons(AppInstanceId i) {
        GetListOfPersons comma = new GetListOfPersons(i);
        comma.executeTran();
        return comma.pList;
    }

    private class GetListOfOObjects extends doTransaction {
        private final List<OObject> hList = new ArrayList<OObject>();

        GetListOfOObjects(AppInstanceId i) {
            super(i);
        }

        @Override
        protected void dosth(EntityManager em) {
            Query q = em.createNamedQuery("findAllObjects");
            q.setParameter(1, i.getId());
            List<EObject> resList = q.getResultList();
            for (EObject e : resList) {
                OObject ho = new OObject();
                PropUtils.copyToProp(ho, e);
                hList.add(ho);
            }

        }

    }

    @Override
    public List<OObject> getListOfObjects(AppInstanceId i) {
        GetListOfOObjects comma = new GetListOfOObjects(i);
        comma.executeTran();
        return comma.hList;
    }

    private class DeleteAll extends doTransaction {

        DeleteAll(AppInstanceId i) {
            super(i);
        }

        @Override
        protected void dosth(EntityManager em) {

            String[] findAllQ = { "findAllPersons", "findAllObjects" };
            String[] removeQ = { "removeRolesForPerson", "removeRolesForObject" };
            for (int k = 0; k < findAllQ.length; k++) {
                Query q = em.createNamedQuery(findAllQ[k]);
                q.setParameter(1, i.getId());
                List<EDictEntry> resList = q.getResultList();
                for (EDictEntry e : resList) {
                    Query q1 = em.createNamedQuery(removeQ[k]);
                    q1.setParameter(1, e);
                    q1.executeUpdate();
                }
            }

            String[] namedQ = new String[] { "removeAllObjects",
                    "removeAllPersons" };
            for (String s : namedQ) {
                Query q = em.createNamedQuery(s);
                q.setParameter(1, i.getId());
                q.executeUpdate();
            }
        }
    }

    @Override
    public void clearAll(AppInstanceId i) {
        info(lMess.getMessN(ILogMess.CLEANALLADMIN));
        DeleteAll comm = new DeleteAll(i);
        comm.executeTran();
    }

    private class RemovePerson extends doTransaction {

        private final String person;

        RemovePerson(AppInstanceId i, String person) {
            super(i);
            this.person = person;
        }

        @Override
        protected void dosth(EntityManager em) {
            EPersonPassword pe = getPersonByName(em, i, person);
            Query q = em.createNamedQuery("removeRolesForPerson");
            q.setParameter(1, pe);
            q.executeUpdate();
            em.remove(pe);
        }

    }

    @Override
    public void removePerson(AppInstanceId i, String person) {
        RemovePerson comma = new RemovePerson(i, person);
        comma.executeTran();
    }

    private class RemoveOObject extends doTransaction {

        private final String OObject;

        RemoveOObject(AppInstanceId i, String OObject) {
            super(i);
            this.OObject = OObject;
        }

        @Override
        protected void dosth(EntityManager em) {
            EObject hote = getObjectByName(em, i, OObject);
            Query q = em.createNamedQuery("removeRolesForObject");
            q.setParameter(1, hote);
            q.executeUpdate();
            em.remove(hote);
        }

    }

    @Override
    public void removeObject(AppInstanceId i, String OObject) {
        RemoveOObject comma = new RemoveOObject(i, OObject);
        comma.executeTran();
    }

    private class GetPassword extends doTransaction {

        private final String person;
        String password;

        GetPassword(AppInstanceId i, String person) {
            super(i);
            this.person = person;
            password = null;
        }

        @Override
        protected void dosth(EntityManager em) {
            EPersonPassword pe = getPersonByName(em, i, person);
            if (pe == null)
                return;
            if (CUtil.EmptyS(pe.getPassword()))
                return;
            password = pe.getPassword();
        }
    }

    @Override
    public String getPassword(AppInstanceId i, String person) {
        GetPassword comm = new GetPassword(i, person);
        comm.executeTran();
        return comm.password;
    }

}
