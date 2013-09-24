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
package com.jython.ui.server.gaestoragekey;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.LoadResult;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.server.storage.registry.IStorageRealmRegistry;
import com.jythonui.shared.JythonUIFatal;

class GaeStorageRegistry implements IStorageRealmRegistry {

    static {
        ObjectifyService.register(RegistryEntry.class);
    }

    private final IGetLogMess gMess;

    GaeStorageRegistry(IGetLogMess gMess) {
        this.gMess = gMess;
    }

    @Override
    public void putEntry(String realm, String key, byte[] value) {
        RegistryEntry re = findR(realm, key);
        if (re == null) {
            re = new RegistryEntry();
            re.setRealM(realm);
            re.setKey(key);
        }
        re.setValue(value);
        ofy().save().entity(re).now();
    }

    private RegistryEntry findR(String realm, String key) {
        LoadResult<RegistryEntry> p = ofy().load().type(RegistryEntry.class)
                .filter("realM ==", realm).filter("key ==", key).first();
        if (p == null) {
            return null;
        }
        return p.now();
    }

    @Override
    public byte[] getEntry(String realm, String key) {
        RegistryEntry re = findR(realm, key);
        if (re == null)
            return null;
        return re.getValue();
    }

    @Override
    public void removeEntry(String realm, String key) {
        RegistryEntry re = findR(realm, key);
        if (re == null)
            return;
        ofy().delete().entity(re).now();
    }

    @Override
    public List<String> getKeys(String realm) {
        List<RegistryEntry> p = ofy().load().type(RegistryEntry.class)
                .filter("realM ==", realm).list();
        if (p == null) {
            return null;
        }
        List<String> res = new ArrayList<String>();
        for (RegistryEntry r : p) {
            res.add(r.getKey());
        }
        return res;
    }

    @Override
    public void addNewEntry(final String realM, final String key,
            final byte[] value) {
        // artificial to force uniqueness but no other idea for the time
        // being
        RegistryEntry re = findR(realM, key);
        if (re != null) {
            String mess = gMess.getMess(IErrorCode.ERRORCODE72,
                    ILogMess.DUPLICATEDREGISTRYENTRY, realM, key);
            throw new JythonUIFatal(mess);
        }

        ofy().transact(new VoidWork() {

            @Override
            public void vrun() {

                RegistryEntry re = new RegistryEntry();
                re.setRealM(realM);
                re.setKey(key);
                re.setValue(value);
                ofy().save().entity(re).now();
            }
        });
    }
}
