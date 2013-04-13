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
package com.jythonui.server.registry.object;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gwtmodel.commoncache.ICommonCache;
import com.jythonui.server.logmess.IErrorCode;
import com.jythonui.server.logmess.ILogMess;
import com.jythonui.server.logmess.LogMess;
import com.jythonui.server.registry.IStorageRegistry;

class ObjectRegistry implements ICommonCache {

    private final IStorageRegistry iRegistry;
    private static final Logger log = Logger.getLogger(ObjectRegistry.class
            .getName());

    ObjectRegistry(IStorageRegistry iRegistry) {
        this.iRegistry = iRegistry;

    }

    @Override
    public Object get(String key) {
        byte[] value = iRegistry.getEntry(key);
        if (value == null)
            return null;
        ByteArrayInputStream bu = new ByteArrayInputStream(value);
        ObjectInputStream inp;
        try {
            inp = new ObjectInputStream(bu);
            Object val = inp.readObject();
            inp.close();
            return val;
        } catch (IOException e) {
            log.log(Level.SEVERE, LogMess.getMess(IErrorCode.ERRORCODE19,
                    ILogMess.GETOBJECTREGISTRYERROR, key), e);

        } catch (ClassNotFoundException e) {
            log.log(Level.SEVERE, LogMess.getMess(IErrorCode.ERRORCODE20,
                    ILogMess.GETOBJECTREGISTRYERROR, key), e);
        }
        return null;
    }

    @Override
    public void put(String key, Object o) {
        ByteArrayOutputStream bu = new ByteArrayOutputStream();
        ObjectOutputStream outP;
        try {
            outP = new ObjectOutputStream(bu);
            outP.writeObject(o);
            outP.close();
        } catch (IOException e) {
            log.log(Level.SEVERE, LogMess.getMess(IErrorCode.ERRORCODE18,
                    ILogMess.PUTOBJECTREGISTRYERROR, key), e);
            return;
        }
        iRegistry.putEntry(key, bu.toByteArray());

    }

    @Override
    public void remove(String key) {
        iRegistry.removeEntry(key);

    }

}