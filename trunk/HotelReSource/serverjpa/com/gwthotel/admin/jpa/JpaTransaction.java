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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.gwthotel.mess.IHError;
import com.gwthotel.mess.IHMess;
import com.jythonui.server.getmess.IGetLogMess;

public abstract class JpaTransaction {

    private final EntityManagerFactory eFactory;
    private final IGetLogMess lMess;

    private static final Logger log = Logger.getLogger(JpaTransaction.class
            .getName());

    protected JpaTransaction(EntityManagerFactory eFactory, IGetLogMess lMess) {
        this.eFactory = eFactory;
        this.lMess = lMess;
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
        } catch (Exception e) {
            log.log(Level.SEVERE, lMess.getMess(IHError.HERROR006,
                    IHMess.JPATRANSACTIONEXCEPTION), e);
        } finally {
            if (!commited)
                em.getTransaction().rollback();
            em.close();
        }
    }
}
