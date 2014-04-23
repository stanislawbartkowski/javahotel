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
package com.jython.ui.server.jpastoragekey;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.jython.ui.server.jpastoragekey.entity.AbstractRegistryEntry;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;
import com.jython.ui.server.jpatrans.JpaTransaction;
import com.jythonui.server.BUtil;
import com.jythonui.server.GetCreateModifTime;
import com.jythonui.server.storage.registry.IStorageRealmRegistry;

abstract class AbstractStorageJpaRegistry implements IStorageRealmRegistry {

    private final ITransactionContextFactory eFactory;

    private final String[] queries;

    static final int FINDENTRY = 0;
    static final int REMOVEENTRY = 1;
    static final int LISTENTRY = 2;

    abstract AbstractRegistryEntry construct();

    AbstractStorageJpaRegistry(ITransactionContextFactory eFactory,
            String[] queries) {
        this.eFactory = eFactory;
        this.queries = queries;
    }

    private abstract class doTransaction extends JpaTransaction {

        doTransaction() {
            super(eFactory);
        }
    }

    private AbstractRegistryEntry getE(EntityManager em, String realM,
            String key) {
        Query q = em.createNamedQuery(queries[FINDENTRY]);
        q.setParameter(1, realM);
        q.setParameter(2, key);
        try {
            AbstractRegistryEntry e = (AbstractRegistryEntry) q
                    .getSingleResult();
            if (e == null)
                return null;
            return e;
        } catch (NoResultException e) {
            return null;
        }
    }

    private class GetEntry extends doTransaction {

        private final String realm;
        private final String key;
        byte[] res;
        AbstractRegistryEntry r;

        GetEntry(String realm, String key) {
            this.realm = realm;
            this.key = key;
        }

        @Override
        protected void dosth(EntityManager em) {
            r = getE(em, realm, key);
            if (r == null)
                res = null;
            else
                res = r.getValue();
        }

    }

    @Override
    public byte[] getEntry(String realm, String key) {
        GetEntry comma = new GetEntry(realm, key);
        comma.executeTran();
        return comma.res;
    }

    private class Put extends doTransaction {

        private final byte[] value;
        private final String key;
        private final String realm;

        Put(String realm, String key, byte[] value) {
            this.key = key;
            this.value = value;
            this.realm = realm;
        }

        @Override
        public void dosth(EntityManager em) {
            AbstractRegistryEntry e = getE(em, realm, key);
            boolean create = false;
            if (e == null) {
                e = construct();
                e.setRegistryRealm(realm);
                e.setRegistryEntry(key);
                create = true;
            }
            e.setValue(value);
            BUtil.setCreateModif(null, e, create);
            em.persist(e);
        }

    }

    @Override
    public void putEntry(String realm, String key, byte[] value) {
        Put command = new Put(realm, key, value);
        command.executeTran();
    }

    class Remove extends doTransaction {

        private final String key;
        private final String realm;

        Remove(String realm, String key) {
            this.key = key;
            this.realm = realm;
        }

        @Override
        public void dosth(EntityManager em) {
            Query q = em.createNamedQuery(queries[REMOVEENTRY]);
            q.setParameter(1, realm);
            q.setParameter(2, key);
            q.executeUpdate();
        }
    }

    @Override
    public void removeEntry(String realm, String key) {
        Remove command = new Remove(realm, key);
        command.executeTran();

    }

    @Override
    public List<String> getKeys(final String realM) {
        final List<String> resList = new ArrayList<String>();
        doTransaction comma = new doTransaction() {

            @Override
            protected void dosth(EntityManager em) {
                Query q = em.createNamedQuery(queries[LISTENTRY]);
                q.setParameter(1, realM);
                List<String> eList = q.getResultList();
                resList.addAll(eList);
            }

        };
        comma.executeTran();
        return resList;
    }

    @Override
    public void addNewEntry(final String realM, final String key,
            final byte[] value) {
        doTransaction trans = new doTransaction() {

            @Override
            protected void dosth(EntityManager em) {
                AbstractRegistryEntry e = construct();
                e.setRegistryRealm(realM);
                e.setRegistryEntry(key);
                e.setValue(value);
                BUtil.setCreateModif(null, e, true);
                em.persist(e);
            }

        };
        trans.executeTran();
    }

    @Override
    public GetCreateModifTime getModifTime(String realM, String key) {
        final GetEntry comma = new GetEntry(realM, key);
        comma.executeTran();
        GetCreateModifTime res = new GetCreateModifTime();
        if (comma.r != null) {
            res.setCreationDate(comma.r.getCreationDate());
            res.setModifDate(comma.r.getModifDate());

        }
        return res;
    }

}
