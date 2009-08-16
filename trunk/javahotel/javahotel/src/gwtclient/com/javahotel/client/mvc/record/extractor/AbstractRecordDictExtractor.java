/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.mvc.record.extractor;

import com.javahotel.client.IResLocator;
import com.javahotel.client.mvc.checkmodel.ICheckDictModel;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.record.view.IRecordView;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.DictionaryP;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
abstract class AbstractRecordDictExtractor implements IRecordExtractor {

    private final IResLocator rI;
    private final DictType dt;

    AbstractRecordDictExtractor(IResLocator rI,DictType dt) {
        this.rI = rI;
        this.dt = dt;
    }

    public void toA(RecordModel mo, IRecordView view) {
        view.extractFields(mo);
    }

    protected abstract Collection<? extends DictionaryP> getDic(IRecordView view, RecordModel a);

    public void toView(IRecordView view, RecordModel a) {
        view.setFields(a);
        Collection<? extends DictionaryP> se = getDic(view, a);
        Collection<String> dic = new ArrayList<String>();
        if (se != null) {
            for (DictionaryP d : se) {
                dic.add(d.getName());
            }
        }
        ICheckDictModel i = (ICheckDictModel) a.getAuxData();
        i.setValues(dic);
    }
}
