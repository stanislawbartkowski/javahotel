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
package com.jythonui.server.ejb;

import java.util.List;

import com.jython.ui.shared.GetCreateModifTime;
import com.jythonui.server.storage.blob.IBlobHandler;

abstract class AbstractStorageBlobRegistry implements IBlobHandler {

    protected IBlobHandler iBlob;

    @Override
    public void clearAll(String realM) {
        iBlob.clearAll(realM);
    }

    @Override
    public void addBlob(String realM, String blobid, byte[] content) {
        iBlob.addBlob(realM, blobid, content);

    }

    @Override
    public void changeBlob(String realM, String blobid, byte[] content) {
        iBlob.changeBlob(realM, blobid, content);
    }

    @Override
    public byte[] findBlob(String realM, String blobid) {
        return iBlob.findBlob(realM, blobid);
    }

    @Override
    public void removeBlob(String realM, String blobid) {
        iBlob.removeBlob(realM, blobid);
    }

    @Override
    public List<String> findBlobs(String realM) {
        return iBlob.findBlobs(realM);
    }

    @Override
    public GetCreateModifTime getModifTime(String realM, String key) {
        return iBlob.getModifTime(realM, key);
    }

}