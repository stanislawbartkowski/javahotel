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
package com.jythonui.server.semaphore.impl;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;

import com.jythonui.server.ISharedConsts;
import com.jythonui.server.UObjects;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.server.semaphore.ISemaphore;
import com.jythonui.server.storage.registry.IStorageRealmRegistry;

public class SemaphoreRegistry extends AbstractSemaphore implements ISemaphore {

    private final IStorageRealmRegistry iReg;

    @Inject
    public SemaphoreRegistry(IStorageRealmRegistry iReg,
            @Named(ISharedConsts.JYTHONMESSSERVER) IGetLogMess gMess) {
        super(gMess);
        this.iReg = iReg;
    }

    private byte[] currentTimeStamp(int expirationsec, String semaphoreName) {
        Long timeStamp = new Date().getTime() + expMili(expirationsec);
        byte[] val = UObjects.put(timeStamp, semaphoreName);
        return val;
    }

    @Override
    public void wait(String semaphoreName, int expirationsec) {
        do {
            try {
                iReg.addNewEntry(SEMAPHORE_REALM, semaphoreName,
                        currentTimeStamp(expirationsec, semaphoreName));
                // ok
                return;
            } catch (Exception e) {

            }
            String logmess = gMess.getMessN(ILogMess.SEMAPHOREEXCEPTION,
                    semaphoreName);
            info(logmess);
            byte[] val1 = iReg.getEntry(SEMAPHORE_REALM, semaphoreName);
            if (val1 == null)
                continue;
            Long timeR = (Long) UObjects.get(val1, semaphoreName);
            if (timeR.longValue() < new Date().getTime()) {
                // expires - simple enter new expiration date
                logExpire(semaphoreName);
                iReg.putEntry(SEMAPHORE_REALM, semaphoreName,
                        currentTimeStamp(expirationsec, semaphoreName));
                return;
            }
            delay(semaphoreName);
        } while (true);

    }

    @Override
    public void signal(String semaphoreName) {
        iReg.removeEntry(SEMAPHORE_REALM, semaphoreName);
    }

}
