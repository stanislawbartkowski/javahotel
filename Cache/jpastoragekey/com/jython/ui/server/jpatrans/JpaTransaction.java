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
package com.jython.ui.server.jpatrans;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.holder.SHolder;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;

public abstract class JpaTransaction {

    private final ITransactionContext iC;
    private final IGetLogMess lMess;

    private static final Logger log = Logger.getLogger(JpaTransaction.class
            .getName());

    protected JpaTransaction(ITransactionContextFactory eFactory) {
        this.iC = eFactory.construct();
        this.lMess = SHolder.getM();
    }

    protected abstract void dosth(EntityManager em);

    protected void makekeys() {
        iC.makekeys();
    }

    public void executeTran() {
        iC.beginTrans();
        boolean commited = false;
        try {
            dosth(iC.getManager());
            iC.commit();
            commited = true;
        } catch (Exception e) {
            log.log(Level.SEVERE, lMess.getMess(IErrorCode.ERRORCODE53,
                    ILogMess.JPATRANSACTIONEXCEPTION), e);
        } finally {
            if (!commited)
                iC.rollback();
            iC.close();
        }
    }
}
