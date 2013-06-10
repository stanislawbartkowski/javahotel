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
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.jython.ui.server.jpastoragekey.entity.RegistryEntry;
import com.jythonui.server.storage.registry.IStorageRealmRegistry;

class StorageJpaRegistry implements IStorageRealmRegistry {

    private final EntityManagerFactory factory;

    StorageJpaRegistry(EntityManagerFactory factory) {
        this.factory = factory;
    }

    private abstract class doTransaction {

        abstract void dosth(EntityManager em);

        void executeTran() {
            EntityManager em = factory.createEntityManager();
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

    @Override
    public byte[] getEntry(String realm, String key) {
        EntityManager em = factory.createEntityManager();
        Query q = em.createNamedQuery("findRegistryEntry");
        q.setParameter(1, realm);
        q.setParameter(2, key);
        try {
            RegistryEntry e = (RegistryEntry) q.getSingleResult();
            if (e == null)
                return null;
            return e.getValue();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
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
        void dosth(EntityManager em) {
            RegistryEntry e = new RegistryEntry();
            e.setRegistryRealm(realm);
            e.setRegistryEntry(key);
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
        void dosth(EntityManager em) {
            Query q = em.createNamedQuery("findRegistryEntry");
            q.setParameter(1, realm);
            q.setParameter(2, key);
            try {
                RegistryEntry e = (RegistryEntry) q.getSingleResult();
                if (e != null)
                    em.remove(e);
            } catch (NoResultException e) {
                return;
            }
        }
    }

    @Override
    public void removeEntry(String realm, String key) {
        Remove command = new Remove(realm, key);
        command.executeTran();

    }

}
