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
package com.gwthotel.admin.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.gwthotel.admin.Hotel;
import com.gwthotel.admin.HotelRoles;
import com.gwthotel.admin.IHotelAdmin;
import com.gwthotel.admin.Person;
import com.gwthotel.admin.jpa.entities.EDictEntry;
import com.gwthotel.admin.jpa.entities.EHotel;
import com.gwthotel.admin.jpa.entities.EPersonPassword;
import com.gwthotel.admin.jpa.entities.EPersonRoles;
import com.gwthotel.mess.IHMess;
import com.gwthotel.shared.PropDescription;
import com.jythonui.server.getmess.IGetLogMess;

class HotelAdminJpa implements IHotelAdmin {

    private final EntityManagerFactory eFactory;
    private static final Logger log = Logger.getLogger(HotelAdminJpa.class
            .getName());
    private final IGetLogMess lMess;

    public HotelAdminJpa(EntityManagerFactory eFactory,
            IGetLogMess lMess) {
        this.eFactory = eFactory;
        this.lMess = lMess;
    }

    private abstract class doTransaction {

        abstract void dosth(EntityManager em);

        protected void commitandbegin(EntityManager em) {
            em.getTransaction().commit();
            em.getTransaction().begin();
        }

        void executeTran() {
            EntityManager em = eFactory.createEntityManager();
            em.getTransaction().begin();
            boolean commited = false;
            try {
                dosth(em);
                em.getTransaction().commit();
                commited = true;
            } finally {
                if (!commited)
                    em.getTransaction().rollback();
                em.close();
            }
        }
    }

    private void copyToProp(PropDescription dest, EDictEntry sou) {
        dest.setName(sou.getName());
        dest.setDescription(sou.getDescription());
    }

    private void copyToEDict(EDictEntry dest, PropDescription sou) {
        dest.setName(sou.getName());
        dest.setDescription(sou.getDescription());
    }

    private void addRole(List<HotelRoles> resList, PropDescription object,
            EPersonRoles p) {
        HotelRoles role = new HotelRoles(object);
        for (String r : p.getRoles()) {
            role.getRoles().add(r);
        }
        resList.add(role);

    }

    private EHotel getHotelByName(EntityManager em, String name) {
        Query q = em.createNamedQuery("findHotelByName");
        q.setParameter(1, name);
        try {
            EHotel hote = (EHotel) q.getSingleResult();
            return hote;
        } catch (NoResultException e) {
            return null;
        }
    }

    private EPersonPassword getPersonByName(EntityManager em, String name) {
        Query q = em.createNamedQuery("findPersonByName");
        q.setParameter(1, name);
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

        GetListOfRolesForPerson(String person) {
            this.person = person;
        }

        @Override
        void dosth(EntityManager em) {
            EPersonPassword pers = getPersonByName(em, person);
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
                copyToProp(ho, h);
                addRole(resList, ho, p);
            }
        }

    }

    @Override
    public List<HotelRoles> getListOfRolesForPerson(String person) {
        GetListOfRolesForPerson com = new GetListOfRolesForPerson(person);
        com.executeTran();
        return com.resList;
    }

    private class GetListOfRolesForHotel extends doTransaction {

        private final String hotel;
        private List<HotelRoles> resList = new ArrayList<HotelRoles>();

        GetListOfRolesForHotel(String hotel) {
            this.hotel = hotel;
        }

        @Override
        void dosth(EntityManager em) {
            EHotel hote = getHotelByName(em, hotel);
            if (hotel == null) {
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
                copyToProp(ho, h);
                addRole(resList, ho, p);
            }
        }

    }

    @Override
    public List<HotelRoles> getListOfRolesForHotel(String hotel) {
        GetListOfRolesForHotel comm = new GetListOfRolesForHotel(hotel);
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

        AddModifHotel(Hotel hotel, List<HotelRoles> roles) {
            this.hotel = hotel;
            this.roles = roles;
        }

        @Override
        void dosth(EntityManager em) {
            EHotel hote = getHotelByName(em, hotel.getName());
            if (hote == null) {
                hote = new EHotel();
            }
            copyToEDict(hote, hotel);
            em.persist(hote);
            commitandbegin(em);
            Query q = em.createNamedQuery("removeRolesForHotel");
            q.setParameter(1, hote.getId());
            q.executeUpdate();
            for (HotelRoles rol : roles) {
                Person pe = (Person) rol.getObject();
                String person = pe.getName();
                EPersonPassword pers = getPersonByName(em, person);
                modifPersonRoles(em, hote, pers, rol);
            }
        }
    }

    @Override
    public void addOrModifHotel(Hotel hotel, List<HotelRoles> roles) {
        AddModifHotel comma = new AddModifHotel(hotel, roles);
        comma.executeTran();
    }

    private class AddModifPerson extends doTransaction {

        private final Person person;
        private final List<HotelRoles> roles;

        AddModifPerson(Person person, List<HotelRoles> roles) {
            this.person = person;
            this.roles = roles;
        }

        @Override
        void dosth(EntityManager em) {
            EPersonPassword pe = getPersonByName(em, person.getName());
            if (pe == null) {
                pe = new EPersonPassword();
            }
            copyToEDict(pe, person);
            em.persist(pe);
            commitandbegin(em);
            Query q = em.createNamedQuery("removeRolesForPerson");
            q.setParameter(1, pe.getId());
            q.executeUpdate();
            for (HotelRoles rol : roles) {
                Hotel ho = (Hotel) rol.getObject();
                String hotel = ho.getName();
                EHotel hote = getHotelByName(em, hotel);
                modifPersonRoles(em, hote, pe, rol);
            }
        }
    }

    @Override
    public void addOrModifPerson(Person person, List<HotelRoles> roles) {
        AddModifPerson comma = new AddModifPerson(person, roles);
        comma.executeTran();
    }

    private class ChangePassword extends doTransaction {

        private final String person;
        private final String password;

        ChangePassword(String person, String password) {
            this.person = person;
            this.password = password;
        }

        @Override
        void dosth(EntityManager em) {
            EPersonPassword pe = getPersonByName(em, person);
            pe.setPassword(password);
            em.persist(pe);
        }
    }

    @Override
    public void changePasswordForPerson(String person, String password) {
        ChangePassword comma = new ChangePassword(person, password);
        comma.executeTran();
    }

    private boolean emptyS(String s) {
        return (s == null) || s.equals("");
    }

    private class ValidatePassword extends doTransaction {

        private final String person;
        private final String password;
        boolean ok = false;

        ValidatePassword(String person, String password) {
            this.person = person;
            this.password = password;
        }

        @Override
        void dosth(EntityManager em) {
            EPersonPassword pe = getPersonByName(em, person);
            if (pe == null)
                return;
            if (emptyS(pe.getPassword()))
                return;
            ok = password.equals(pe.getPassword());
        }
    }

    @Override
    public boolean validatePasswordForPerson(String person, String password) {
        ValidatePassword comma = new ValidatePassword(person, password);
        comma.executeTran();
        return comma.ok;
    }

    private class GetListOfPersons extends doTransaction {

        private final List<Person> pList = new ArrayList<Person>();

        @Override
        void dosth(EntityManager em) {
            Query q = em.createNamedQuery("findAllPersons");
            List<EPersonPassword> resList = q.getResultList();
            for (EPersonPassword e : resList) {
                Person pe = new Person();
                copyToProp(pe, e);
                pList.add(pe);
            }
        }

    }

    @Override
    public List<Person> getListOfPersons() {
        GetListOfPersons comma = new GetListOfPersons();
        comma.executeTran();
        return comma.pList;
    }

    private class GetListOfHotels extends doTransaction {
        private final List<Hotel> hList = new ArrayList<Hotel>();

        @Override
        void dosth(EntityManager em) {
            Query q = em.createNamedQuery("findAllHotels");
            List<EHotel> resList = q.getResultList();
            for (EHotel e : resList) {
                Hotel ho = new Hotel();
                copyToProp(ho, e);
                hList.add(ho);
            }

        }

    }

    @Override
    public List<Hotel> getListOfHotels() {
        GetListOfHotels comma = new GetListOfHotels();
        comma.executeTran();
        return comma.hList;
    }

    private class DeleteAll extends doTransaction {

        @Override
        void dosth(EntityManager em) {
            String[] namedQ = new String[] { "removeAllRoles",
                    "removeAllHotels", "removeAllPersons" };
            for (String s : namedQ) {
                Query q = em.createNamedQuery(s);
                q.executeUpdate();
            }
        }
    }

    @Override
    public void clearAll() {
        log.info(lMess.getMessN(IHMess.CLEANALLADMIN));
        DeleteAll comm = new DeleteAll();
        comm.executeTran();
    }

    private class RemovePerson extends doTransaction {

        private final String person;

        RemovePerson(String person) {
            this.person = person;
        }

        @Override
        void dosth(EntityManager em) {
            EPersonPassword pe = getPersonByName(em, person);
            Query q = em.createNamedQuery("removeRolesForPerson");
            q.setParameter(1, pe.getId());
            q.executeUpdate();
            em.remove(pe);
        }

    }

    @Override
    public void removePerson(String person) {
        RemovePerson comma = new RemovePerson(person);
        comma.executeTran();
    }

    private class RemoveHotel extends doTransaction {

        private final String hotel;

        RemoveHotel(String hotel) {
            this.hotel = hotel;
        }

        @Override
        void dosth(EntityManager em) {
            EHotel hote = getHotelByName(em, hotel);
            Query q = em.createNamedQuery("removeRolesForHotel");
            q.setParameter(1, hote.getId());
            q.executeUpdate();
            em.remove(hote);
        }

    }

    @Override
    public void removeHotel(String hotel) {
        RemoveHotel comma = new RemoveHotel(hotel);
        comma.executeTran();
    }
    
    private class GetPassword extends doTransaction {

        private final String person;
        String password;
        
        GetPassword(String person) {
            this.person = person;
            password = null;
        }

        @Override
        void dosth(EntityManager em) {
            EPersonPassword pe = getPersonByName(em, person);
            if (pe == null)
                return;
            if (emptyS(pe.getPassword()))
                return;
            password = pe.getPassword();            
        }
    }

    @Override
    public String getPassword(String person) {
        GetPassword comm = new GetPassword(person);
        comm.executeTran();
        return comm.password;
    }

}
