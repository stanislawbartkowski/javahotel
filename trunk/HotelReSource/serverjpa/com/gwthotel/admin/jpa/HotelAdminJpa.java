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
package com.gwthotel.admin.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.gwthotel.admin.AppInstanceId;
import com.gwthotel.admin.Hotel;
import com.gwthotel.admin.HotelRoles;
import com.gwthotel.admin.IHotelAdmin;
import com.gwthotel.admin.Person;
import com.gwthotel.admin.jpa.entities.EDictEntry;
import com.gwthotel.admin.jpa.entities.EHotel;
import com.gwthotel.admin.jpa.entities.EPersonPassword;
import com.gwthotel.admin.jpa.entities.EPersonRoles;
import com.gwthotel.mess.IHError;
import com.gwthotel.mess.IHMess;
import com.gwthotel.shared.PropDescription;
import com.gwtmodel.table.common.CUtil;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;
import com.jython.ui.server.jpatrans.JpaTransaction;
import com.jythonui.server.BUtil;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.shared.JythonUIFatal;

class HotelAdminJpa implements IHotelAdmin {

    private final ITransactionContextFactory iC;
    private static final Logger log = Logger.getLogger(HotelAdminJpa.class
            .getName());
    private final IGetLogMess lMess;

    public HotelAdminJpa(ITransactionContextFactory iC, IGetLogMess lMess) {
        this.iC = iC;
        this.lMess = lMess;
    }

    private abstract class doTransaction extends JpaTransaction {

        final AppInstanceId i;

        private doTransaction(AppInstanceId i) {
            super(iC);
            this.i = i;
            if (i.getId() == null) {
                String mess = lMess.getMess(IHError.HERROR009,
                        IHMess.INSTANCEIDCANNOTNENULLHERE);
                log.severe(mess);
                throw new JythonUIFatal(mess);
            }
        }
    }

    private void addRole(List<HotelRoles> resList, PropDescription object,
            EPersonRoles p) {
        HotelRoles role = new HotelRoles(object);
        for (String r : p.getRoles()) {
            role.getRoles().add(r);
        }
        resList.add(role);

    }

    private EHotel getHotelByName(EntityManager em, AppInstanceId i, String name) {
        Query q = em.createNamedQuery("findHotelByName");
        q.setParameter(1, i.getId());
        q.setParameter(2, name);
        try {
            EHotel hote = (EHotel) q.getSingleResult();
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
        private List<HotelRoles> resList = new ArrayList<HotelRoles>();

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
            q.setParameter(1, pers.getId());
            List<EPersonRoles> list = q.getResultList();
            for (EPersonRoles p : list) {
                Query q1 = em.createNamedQuery("findHotelByLong");
                q1.setParameter(1, p.getHotel());
                EHotel h = (EHotel) q1.getSingleResult();
                Hotel ho = new Hotel();
                PropUtils.copyToProp(ho, h);
                addRole(resList, ho, p);
            }
        }

    }

    @Override
    public List<HotelRoles> getListOfRolesForPerson(AppInstanceId i,
            String person) {
        GetListOfRolesForPerson com = new GetListOfRolesForPerson(i, person);
        com.executeTran();
        return com.resList;
    }

    private class GetListOfRolesForHotel extends doTransaction {

        private final String hotel;
        private List<HotelRoles> resList = new ArrayList<HotelRoles>();

        GetListOfRolesForHotel(AppInstanceId i, String hotel) {
            super(i);
            this.hotel = hotel;
        }

        @Override
        protected void dosth(EntityManager em) {
            EHotel hote = getHotelByName(em, i, hotel);
            if (hote == null) {
                resList = null;
                return;
            }
            Query q = em.createNamedQuery("getListOfRolesForHotel");
            q.setParameter(1, hote.getId());
            List<EPersonRoles> list = q.getResultList();
            for (EPersonRoles p : list) {
                Query q1 = em.createNamedQuery("findPersonByLong");
                q1.setParameter(1, p.getPerson());
                EPersonPassword h = (EPersonPassword) q1.getSingleResult();
                Person ho = new Person();
                PropUtils.copyToProp(ho, h);
                addRole(resList, ho, p);
            }
        }

    }

    @Override
    public List<HotelRoles> getListOfRolesForHotel(AppInstanceId i, String hotel) {
        GetListOfRolesForHotel comm = new GetListOfRolesForHotel(i, hotel);
        comm.executeTran();
        return comm.resList;
    }

    private void modifPersonRoles(EntityManager em, EHotel hote,
            EPersonPassword pe, HotelRoles role) {
        EPersonRoles eRoles = new EPersonRoles();
        eRoles.setHotel(hote.getId());
        eRoles.setPerson(pe.getId());
        ArrayList<String> roles = new ArrayList<String>();
        for (String ro : role.getRoles()) {
            roles.add(ro);
        }
        eRoles.setRoles(roles);
        em.persist(eRoles);

    }

    private class AddModifHotel extends doTransaction {

        private final Hotel hotel;
        private final List<HotelRoles> roles;

        AddModifHotel(AppInstanceId i, Hotel hotel, List<HotelRoles> roles) {
            super(i);
            this.hotel = hotel;
            this.roles = roles;
        }

        @Override
        protected void dosth(EntityManager em) {
            EHotel hote = getHotelByName(em, i, hotel.getName());
            boolean create = false;
            if (hote == null) {
                hote = new EHotel();
                hote.setInstanceId(i.getId());
                create = true;
            }
            PropUtils.copyToEDict(hote, hotel);
            BUtil.setCreateModif(i.getPerson(), hote, create);
            em.persist(hote);
            makekeys();
            Query q = em.createNamedQuery("removeRolesForHotel");
            q.setParameter(1, hote.getId());
            q.executeUpdate();
            for (HotelRoles rol : roles) {
                Person pe = (Person) rol.getObject();
                String person = pe.getName();
                EPersonPassword pers = getPersonByName(em, i, person);
                modifPersonRoles(em, hote, pers, rol);
            }
        }
    }

    @Override
    public void addOrModifHotel(AppInstanceId i, Hotel hotel,
            List<HotelRoles> roles) {
        AddModifHotel comma = new AddModifHotel(i, hotel, roles);
        comma.executeTran();
    }

    private class AddModifPerson extends doTransaction {

        private final Person person;
        private final List<HotelRoles> roles;

        AddModifPerson(AppInstanceId i, Person person, List<HotelRoles> roles) {
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
            q.setParameter(1, pe.getId());
            q.executeUpdate();
            for (HotelRoles rol : roles) {
                Hotel ho = (Hotel) rol.getObject();
                String hotel = ho.getName();
                EHotel hote = getHotelByName(em, i, hotel);
                modifPersonRoles(em, hote, pe, rol);
            }
        }
    }

    @Override
    public void addOrModifPerson(AppInstanceId i, Person person,
            List<HotelRoles> roles) {
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

    private class GetListOfHotels extends doTransaction {
        private final List<Hotel> hList = new ArrayList<Hotel>();

        GetListOfHotels(AppInstanceId i) {
            super(i);
        }

        @Override
        protected void dosth(EntityManager em) {
            Query q = em.createNamedQuery("findAllHotels");
            q.setParameter(1, i.getId());
            List<EHotel> resList = q.getResultList();
            for (EHotel e : resList) {
                Hotel ho = new Hotel();
                PropUtils.copyToProp(ho, e);
                hList.add(ho);
            }

        }

    }

    @Override
    public List<Hotel> getListOfHotels(AppInstanceId i) {
        GetListOfHotels comma = new GetListOfHotels(i);
        comma.executeTran();
        return comma.hList;
    }

    private class DeleteAll extends doTransaction {

        DeleteAll(AppInstanceId i) {
            super(i);
        }

        @Override
        protected void dosth(EntityManager em) {

            String[] findAllQ = { "findAllPersons", "findAllHotels" };
            String[] removeQ = { "removeRolesForPerson", "removeRolesForHotel" };
            for (int k = 0; k < findAllQ.length; k++) {
                Query q = em.createNamedQuery(findAllQ[k]);
                q.setParameter(1, i.getId());
                List<EDictEntry> resList = q.getResultList();
                for (EDictEntry e : resList) {
                    Query q1 = em.createNamedQuery(removeQ[k]);
                    q1.setParameter(1, e.getId());
                    q1.executeUpdate();
                }
            }

            String[] namedQ = new String[] { "removeAllHotels",
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
        log.info(lMess.getMessN(IHMess.CLEANALLADMIN));
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
            q.setParameter(1, pe.getId());
            q.executeUpdate();
            em.remove(pe);
        }

    }

    @Override
    public void removePerson(AppInstanceId i, String person) {
        RemovePerson comma = new RemovePerson(i, person);
        comma.executeTran();
    }

    private class RemoveHotel extends doTransaction {

        private final String hotel;

        RemoveHotel(AppInstanceId i, String hotel) {
            super(i);
            this.hotel = hotel;
        }

        @Override
        protected void dosth(EntityManager em) {
            EHotel hote = getHotelByName(em, i, hotel);
            Query q = em.createNamedQuery("removeRolesForHotel");
            q.setParameter(1, hote.getId());
            q.executeUpdate();
            em.remove(hote);
        }

    }

    @Override
    public void removeHotel(AppInstanceId i, String hotel) {
        RemoveHotel comma = new RemoveHotel(i, hotel);
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
