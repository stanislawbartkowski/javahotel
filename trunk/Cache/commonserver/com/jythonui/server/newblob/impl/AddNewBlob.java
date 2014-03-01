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
package com.jythonui.server.newblob.impl;

import javax.inject.Inject;

import com.jythonui.server.newblob.IAddNewBlob;
import com.jythonui.server.storage.blob.IBlobHandler;
import com.jythonui.server.storage.seq.ISequenceRealmGen;

public class AddNewBlob implements IAddNewBlob {

    private final ISequenceRealmGen iSeq;
    private final IBlobHandler iBlob;

    @Inject
    public AddNewBlob(ISequenceRealmGen iSeq, IBlobHandler iBlob) {
        this.iSeq = iSeq;
        this.iBlob = iBlob;
    }

    @Override
    public String addNewBlob(String realM, String key, byte[] content) {
        Long bkey = iSeq.genNext(realM, key);
        String skey = key + "-" + bkey;
        iBlob.addBlob(realM, skey, content);
        return skey;
    }

}
