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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public abstract class JpaTransaction {

    private final EntityManagerFactory eFactory;

    protected JpaTransaction(EntityManagerFactory eFactory) {
        this.eFactory = eFactory;
    }

    protected abstract void dosth(EntityManager em);

    protected void commitandbegin(EntityManager em) {
        em.getTransaction().commit();
        em.getTransaction().begin();
    }

    public void executeTran() {
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
