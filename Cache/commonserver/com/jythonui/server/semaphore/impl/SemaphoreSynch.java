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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.inject.Inject;
import javax.inject.Named;

import com.jython.ui.shared.ISharedConsts;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.semaphore.ISemaphore;

public class SemaphoreSynch extends AbstractSemaphore implements ISemaphore {

    private Map<String, Long> semMap = new HashMap<String, Long>();
    // guardian of the semMap
    private final Lock lock = new ReentrantLock();

    @Inject
    public SemaphoreSynch(
            @Named(ISharedConsts.JYTHONMESSSERVER) IGetLogMess gMess) {
        super(gMess);
    }

    // do not use synchronized

    @Override
    public void wait(String semaphoreName, int expirationsec) {
        do {
            lock.lock();
            try {
                Long l = semMap.get(semaphoreName);
                if (l == null) {
                    semMap.put(semaphoreName, new Date().getTime()
                            + expMili(expirationsec));
                    return;
                }
                if (l.longValue() < new Date().getTime()) {
                    logExpire(semaphoreName);
                    semMap.put(semaphoreName, new Date().getTime()
                            + expMili(expirationsec));
                    return;
                }
            } finally {
                lock.unlock();
            }
            // important: lock is released here
            delay(semaphoreName);
        } while (true);
    }

    @Override
    public void signal(String sempahoreName) {
        lock.lock();
        try {
            semMap.remove(sempahoreName);
        } finally {
            lock.unlock();
        }
    }

}
