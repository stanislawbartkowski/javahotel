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
package com.jython.ui.server.jpastoragekey;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.jython.ui.server.jpastoragekey.entity.RegistryEntry;
import com.jython.ui.server.jpatrans.ITransactionContext;
import com.jython.ui.server.jpatrans.ITransactionContextFactory;
import com.jython.ui.server.jpatrans.JpaTransaction;
import com.jythonui.server.storage.registry.IStorageRealmRegistry;

class StorageJpaRegistry implements IStorageRealmRegistry {

    private final ITransactionContextFactory eFactory;

    StorageJpaRegistry(ITransactionContextFactory eFactory) {
        this.eFactory = eFactory;
    }

    private abstract class doTransaction extends JpaTransaction {

        doTransaction() {
            super(eFactory);
        }

    }

    private RegistryEntry getE(EntityManager em, String realM, String key) {
        Query q = em.createNamedQuery("findRegistryEntry");
        q.setParameter(1, realM);
        q.setParameter(2, key);
        try {
            RegistryEntry e = (RegistryEntry) q.getSingleResult();
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

        GetEntry(String realm, String key) {
            this.realm = realm;
            this.key = key;
        }

        @Override
        protected void dosth(EntityManager em) {
            RegistryEntry r = getE(em, realm, key);
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
            RegistryEntry e = getE(em, realm, key);
            if (e == null) {
                e = new RegistryEntry();
                e.setRegistryRealm(realm);
                e.setRegistryEntry(key);
            }
            e.setValue(value);
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
            Query q = em.createNamedQuery("removeRegistryEntry");
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

}
