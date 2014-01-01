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

import com.jython.ui.server.datastore.IDateRecord;
import com.jython.ui.server.datastore.IDateRecordOp;

public class DateRecordOp implements IDateRecordOp {

    private final EntityManagerFactory factory;

    @Inject
    public DateRecordOp(EntityManagerFactory factory) {
        this.factory = factory;
    }

    private DateRecord findR(EntityManager em, Long id) {
        Query q = em.createNamedQuery("DateRecord_findDateElem");
        q.setParameter(1, id);
        try {
            DateRecord e = (DateRecord) q.getSingleResult();
            if (e == null)
                return null;
            return e;
        } catch (NoResultException e) {
            return null;
        }

    }

    private class ClearAll extends DoTransaction {

        ClearAll() {
            super(factory);
        }

        @Override
        void dosth(EntityManager em) {
            Query q = em.createNamedQuery("DateRecord_removeAllElem");
            q.executeUpdate();
        }

    }

    @Override
    public void clearAll() {
        ClearAll com = new ClearAll();
        com.executeTran();
    }

    @Override
    public List<IDateRecord> getList() {
        Query q = factory.createEntityManager().createQuery(
                "select t from DateRecord t");
        @SuppressWarnings("unchecked")
        List<IDateRecord> pList = q.getResultList();
        return pList;
    }

    private class AddRecord extends DoTransaction {

        private final IDateRecord re;

        AddRecord(IDateRecord re) {
            super(factory);
            this.re = re;
        }

        @Override
        void dosth(EntityManager em) {
            em.persist(re);
        }

    }

    @Override
    public Long addRecord(IDateRecord re) {
        AddRecord comm = new AddRecord(re);
        comm.executeTran();
        return comm.re.getId();
    }

    private class ChangeRecord extends DoTransaction {
        private final IDateRecord re;

        ChangeRecord(IDateRecord re) {
            super(factory);
            this.re = re;
        }

        @Override
        void dosth(EntityManager em) {
            IDateRecord r = findR(em, re.getId());
            r.setDates(re.getD1(), re.getD2());
            em.persist(r);
        }

    }

    @Override
    public void changeRecord(IDateRecord re) {
        ChangeRecord comm = new ChangeRecord(re);
        comm.executeTran();
    }

    private class RemoveRecord extends DoTransaction {
        private final Long id;

        RemoveRecord(Long id) {
            super(factory);
            this.id = id;
        }

        @Override
        void dosth(EntityManager em) {
            Query q = em.createNamedQuery("DateRecord_removeDateElem");
            q.setParameter(1, id);
            q.executeUpdate();
        }
    }

    @Override
    public void removeRecord(Long id) {
        RemoveRecord comm = new RemoveRecord(id);
        comm.executeTran();
    }

    @Override
    public IDateRecord construct() {
        return new DateRecord();
    }

    @Override
    public IDateRecord findRecord(Long id) {
        return findR(factory.createEntityManager(), id);
    }

}
