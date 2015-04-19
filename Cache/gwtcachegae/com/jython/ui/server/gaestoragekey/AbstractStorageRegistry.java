/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
import com.googlecode.objectify.VoidWork;
import com.jythonui.server.BUtil;
import com.jythonui.server.GetCreateModifTime;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.server.storage.registry.IStorageRealmRegistry;
import com.jythonui.shared.JythonUIFatal;

abstract class AbstractStorageRegistry implements IStorageRealmRegistry {

    private final IGetLogMess gMess;
    @SuppressWarnings("rawtypes")
    private final Class eClass;

    AbstractStorageRegistry(IGetLogMess gMess,
            @SuppressWarnings("rawtypes") Class eClass) {
        this.gMess = gMess;
        this.eClass = eClass;
    }

    abstract AbstractRegistryEntry construct();

    @Override
    public void putEntry(String realm, String key, byte[] value) {
        AbstractRegistryEntry re = findR(realm, key);
        if (re == null) {
            re = construct();
            re.setRealM(realm);
            re.setKey(key);
        }
        re.setValue(value);
        ofy().save().entity(re).now();
    }

    private AbstractRegistryEntry findR(String realm, String key) {
        LoadResult<AbstractRegistryEntry> p = ofy().load().type(eClass)
                .filter("realM ==", realm).filter("key ==", key).first();
        if (p == null) {
            return null;
        }
        return p.now();
    }

    @Override
    public byte[] getEntry(String realm, String key) {
        AbstractRegistryEntry re = findR(realm, key);
        if (re == null)
            return null;
        return re.getValue();
    }

    @Override
    public void removeEntry(String realm, String key) {
        AbstractRegistryEntry re = findR(realm, key);
        if (re == null)
            return;
        ofy().delete().entity(re).now();
    }

    @Override
    public List<String> getKeys(String realm) {
        List<AbstractRegistryEntry> p = ofy().load().type(eClass)
                .filter("realM ==", realm).list();
        if (p == null) {
            return null;
        }
        List<String> res = new ArrayList<String>();
        for (AbstractRegistryEntry r : p) {
            res.add(r.getKey());
        }
        return res;
    }

    @Override
    public void addNewEntry(final String realM, final String key,
            final byte[] value) {
        // artificial to force uniqueness but no other idea for the time
        // being
        AbstractRegistryEntry re = findR(realM, key);
        if (re != null) {
            String mess = gMess.getMess(IErrorCode.ERRORCODE72,
                    ILogMess.DUPLICATEDREGISTRYENTRY, realM, key);
            throw new JythonUIFatal(mess);
        }

        ofy().transact(new VoidWork() {

            @Override
            public void vrun() {

                AbstractRegistryEntry re = construct();
                re.setRealM(realM);
                re.setKey(key);
                re.setValue(value);
                BUtil.setCreateModif(null, re, true);
                ofy().save().entity(re).now();
            }
        });
    }

    @Override
    public GetCreateModifTime getModifTime(String realM, String key) {
        AbstractRegistryEntry re = findR(realM, key);
        GetCreateModifTime res = new GetCreateModifTime();
        if (re != null) {
            res.setCreationDate(re.getCreationDate());
            res.setModifDate(re.getModifDate());
        }
        return res;
    }

}
