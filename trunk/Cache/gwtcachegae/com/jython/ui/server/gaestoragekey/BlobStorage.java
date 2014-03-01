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
package com.jython.ui.server.gaestoragekey;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.googlecode.objectify.ObjectifyService;
import com.jython.ui.shared.ISharedConsts;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.storage.blob.IBlobHandler;

public class BlobStorage extends AbstractStorageRegistry implements
        IBlobHandler {

    static {
        ObjectifyService.register(BlobEntry.class);
    }

    @Inject
    public BlobStorage(@Named(ISharedConsts.JYTHONMESSSERVER) IGetLogMess gMess) {
        super(gMess,BlobEntry.class);
    }

    @Override
    public void clearAll(String realM) {
        List<String> keys = getKeys(realM);
        for (String k : keys)
            removeEntry(realM, k);

    }

    @Override
    public void addBlob(String realM, String blobid, byte[] content) {
        addNewEntry(realM, blobid, content);
    }

    @Override
    public void changeBlob(String realM, String blobid, byte[] content) {
        putEntry(realM, blobid, content);
    }

    @Override
    public byte[] findBlob(String realM, String blobid) {
        return getEntry(realM, blobid);
    }

    @Override
    public void removeBlob(String realM, String blobid) {
        removeEntry(realM, blobid);

    }

    @Override
    AbstractRegistryEntry construct() {
        return new BlobEntry();
    }

}
