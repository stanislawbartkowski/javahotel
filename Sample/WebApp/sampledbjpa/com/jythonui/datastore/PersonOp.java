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
package com.jythonui.datastore;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.jython.ui.server.datastore.IPerson;
import com.jython.ui.server.datastore.IPersonOp;

/**
 * @author hotel
 * 
 */
public class PersonOp implements IPersonOp {

    private final EntityManagerFactory factory;

    @Inject
    public PersonOp(EntityManagerFactory factory) {
        this.factory = factory;
    }

    private EntityManager getEM() {
        return factory.createEntityManager();
    }

    @Override
    public void savePerson(final IPerson p) {
        new DoTransaction(factory) {

            @Override
            void dosth(EntityManager em) {
                em.persist(p);

            }

        }.executeTran();
    }

    @Override
    public void clearAll() {
        new DoTransaction(factory) {

            @Override
            void dosth(EntityManager em) {
                List<IPerson> li = getAllPersons(em);
                for (IPerson p : li) {
                    em.remove(p);
                }

            }
        }.executeTran();
    }

    private List<IPerson> getAllPersons(EntityManager em) {
        Query q = em.createQuery("select t from Person t");
        @SuppressWarnings("unchecked")
        List<IPerson> pList = q.getResultList();
        return pList;

    }

    @Override
    public List<IPerson> getAllPersons() {
        return getAllPersons(getEM());
    }

    private IPerson findP(EntityManager em, IPerson p) {
        IPerson o = em.find(Person.class, p.getId());
        return o;
    }

    @Override
    public void removePerson(final IPerson p) {
        new DoTransaction(factory) {

            @Override
            void dosth(EntityManager em) {
                IPerson o = findP(em, p);
                em.remove(o);

            }
        }.executeTran();
    }

    @Override
    public void changePerson(final IPerson p) {
        new DoTransaction(factory) {

            @Override
            void dosth(EntityManager em) {
                IPerson o = findP(em, p);
                o.setPersonName(p.getPersonName());
                o.setPersonNumb(p.getPersonNumb());
                em.persist(o);
            }
        }.executeTran();
    }

    @Override
    public IPerson findPersonByNumb(String pNumb) {
        Query q = getEM().createQuery(
                "select t from Person t where t.personNumb=?1");
        q.setParameter(1, pNumb);
        try {
            return (IPerson) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public IPerson construct() {
        return new Person();
    }

}
