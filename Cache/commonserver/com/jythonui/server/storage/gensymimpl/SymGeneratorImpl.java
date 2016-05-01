/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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
package com.jythonui.server.storage.gensymimpl;

import java.util.Date;

import com.jython.dateutil.DateFormatUtil;
import com.jythonui.server.UObjects;
import com.jythonui.server.storage.gensym.ISymGenerator;
import com.jythonui.server.storage.seq.ISequenceRealmGen;

class SymGeneratorImpl implements ISymGenerator {

    private final ISequenceRealmGen iSeq;

    SymGeneratorImpl(ISequenceRealmGen iSeq) {
        this.iSeq = iSeq;
    }

    @Override
    public String genSym(String realm, String key, String pattern) {
        Date today = DateFormatUtil.getToday(false);
        Long l = iSeq.genNext(realm, key);
        return UObjects.genName(pattern, l, today);
    }

    @Override
    public void clear(String realm, String key) {
        iSeq.remove(realm, key);

    }

}
