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
package com.jythonui.server.storage.seqimpl;

import com.jython.ui.shared.UObjects;
import com.jythonui.server.semaphore.ISemaphore;
import com.jythonui.server.storage.registry.IStorageRealmRegistry;
import com.jythonui.server.storage.seq.ISequenceRealmGen;

class SequenceRealmGen implements ISequenceRealmGen {

    private final IStorageRealmRegistry iReg;
    private final ISemaphore iSem;

    SequenceRealmGen(IStorageRealmRegistry iReg, ISemaphore iSem) {
        this.iReg = iReg;
        this.iSem = iSem;
    }

    @Override
    public Long genNext(String realM, String key) {
        String semName = realM + "-SEQUENCE-" + key;
        iSem.wait(semName, ISemaphore.DEFAULT);
        byte[] val = iReg.getEntry(realM, key);
        Long next;
        if (val == null) {
            next = new Long(0);
        } else {
            next = (Long) UObjects.get(val, key);
        }
        next = next + 1;
        iReg.putEntry(realM, key, UObjects.put(next, key));
        iSem.signal(semName);
        return next;
    }

    @Override
    public void remove(String realm, String key) {
        iReg.removeEntry(realm, key);
    }

}
