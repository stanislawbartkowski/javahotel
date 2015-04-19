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
package com.jythonui.datastore;

import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.jython.ui.server.datastore.IDateLineElem;
import com.jython.ui.server.datastore.IDateLineOp;

public class DateLineOp implements IDateLineOp {

    private final EntityManagerFactory factory;

    @Inject
    public DateLineOp(EntityManagerFactory factory) {
        this.factory = factory;
    }

    private EntityManager getEM() {
        return factory.createEntityManager();
    }

    private DateLineElem findElem(EntityManager em, Long id, Date dt) {
        Query q = em.createNamedQuery("findDateElem");
        q.setParameter(1, id);
        q.setParameter(2, dt);
        try {
            DateLineElem e = (DateLineElem) q.getSingleResult();
            if (e == null)
                return null;
            return e;
        } catch (NoResultException e) {
            return null;
        }

    }

    @Override
    public IDateLineElem findElem(Long id, Date da) {
        return findElem(getEM(), id, da);
    }

    @Override
    public void addormodifElem(final Long id, final Date da, final int numb,
            final String info) {
        DoTransaction comma = new DoTransaction(factory) {

            @Override
            void dosth(EntityManager em) {
                DateLineElem ele = findElem(em, id, da);
                if (ele == null) {
                    ele = new DateLineElem();
                    ele.setId(id);
                    ele.setDt(da);
                }
                ele.setDate(numb, info);
                em.persist(ele);
            }

        };
        comma.executeTran();
    }

    @Override
    public void removeElem(final Long id, final Date da) {
        DoTransaction comm = new DoTransaction(factory) {

            @Override
            void dosth(EntityManager em) {
                Query q = em.createNamedQuery("removeDateElem");
                q.setParameter(1, id);
                q.setParameter(2, da);
                q.executeUpdate();
            }

        };
        comm.executeTran();
    }

    @Override
    public void clearAll() {
        DoTransaction comm = new DoTransaction(factory) {

            @Override
            void dosth(EntityManager em) {
                Query q = em.createNamedQuery("removeAllElem");
                q.executeUpdate();
            }

        };
        comm.executeTran();

    }
}
