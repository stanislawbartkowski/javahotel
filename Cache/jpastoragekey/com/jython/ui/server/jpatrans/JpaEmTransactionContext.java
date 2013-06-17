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
package com.jython.ui.server.jpatrans;

import javax.persistence.EntityManager;

public class JpaEmTransactionContext implements ITransactionContext {
    
    private final EntityManager em;

    public JpaEmTransactionContext(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getManager() {
        return em;
    }


    @Override
    public void beginTrans() {        
    }

    @Override
    public void commit() {
    }

    @Override
    public void rollback() {
        throw new RollBackException();
    }

    @Override
    public void makekeys() {
        em.flush();
    }

    @Override
    public void close() {
    }


}
