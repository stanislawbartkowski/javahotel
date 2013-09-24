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
package com.jythonui.server.semaphore.impl;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.jython.ui.shared.ISharedConsts;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.server.semaphore.ISemaphore;

abstract class AbstractSemaphore {

    protected final static String SEMAPHORE_REALM = "SEMAPHORE_REALM";
    protected final IGetLogMess gMess;
    protected static final Logger log = Logger
            .getLogger(AbstractSemaphore.class.getName());

    protected int expMili(int expirationsec) {

        if (expirationsec == ISemaphore.DEFAULT) {
            expirationsec = ISharedConsts.DEFEXPIRATIONSEC;
        }
        return expirationsec * 1000;
    }

    AbstractSemaphore(IGetLogMess gMess) {
        this.gMess = gMess;
    }

    protected void logExpire(String semaphoreName) {
        String logmess1 = gMess.getMessN(ILogMess.SEMAPHOREEXPIRETIME,
                semaphoreName);
        log.info(logmess1);
    }

    protected void delay(String semaphoreName) {
        String logMess = gMess.getMessN(ILogMess.SEMAPHOREWAIT, semaphoreName);
        log.info(logMess);
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
        }
    }

}
