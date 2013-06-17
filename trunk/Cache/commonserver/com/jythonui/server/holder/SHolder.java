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
package com.jythonui.server.holder;

import javax.inject.Inject;
import javax.inject.Named;

import com.jython.ui.shared.ISharedConsts;
import com.jythonui.server.getmess.IGetLogMess;
import com.jythonui.server.storage.gensym.ISymGenerator;
import com.jythonui.server.storage.registry.IStorageRealmRegistry;
import com.jythonui.server.storage.seq.ISequenceRealmGen;

public class SHolder {

    private SHolder() {
    }

    @Inject
    private static ISequenceRealmGen iSeq;

    @Inject
    private static IStorageRealmRegistry iRegistry;
    
    @Inject
    private static ISymGenerator iGenerator;

    @Inject
    @Named(ISharedConsts.JYTHONMESSSERVER)
    private static IGetLogMess logMess;

    public static IGetLogMess getM() {
        return logMess;
    }

    public static ISequenceRealmGen getSequenceRealmGen() {
        return iSeq;
    }

    public static IStorageRealmRegistry getStorageRegistry() {
        return iRegistry;
    }
    
    public ISymGenerator getSymGenerator() {
        return iGenerator;
    }

}
