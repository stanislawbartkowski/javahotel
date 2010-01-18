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
package com.javahotel.nmvc.factories.impl;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.factories.IFormDefFactory;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;
import com.javahotel.common.command.DictType;
import com.javahotel.common.toobject.DictionaryP;
import com.javahotel.nmvc.common.DataType;
import com.javahotel.nmvc.common.VField;

public class RecordFormDefFactory implements IFormDefFactory {

    private final EditWidgetFactory eFactory;

    @Inject
    public RecordFormDefFactory(EditWidgetFactory eFactory) {
        this.eFactory = eFactory;
    }

    public String getTitle(IDataType dType) {
        DataType dd = (DataType) dType;
        DictType d = dd.getdType();
        switch (d) {
        case CustomerList:
            return "Kontrahent";
        }
        return null;

    }

    public FormLineContainer construct(IDataType dType) {
        DataType dd = (DataType) dType;
        DictType d = dd.getdType();
        List<FormField> fList = new ArrayList<FormField>();
        switch (d) {
        case CustomerList:
            IFormLineView name = eFactory.constructTextField();
            IFormLineView descr = eFactory.constructTextField();
            fList.add(new FormField("Symbol", name, new VField(
                    DictionaryP.F.name), false));
            fList.add(new FormField("Nazwa", descr, new VField(
                    DictionaryP.F.description)));
            break;
        default:
            return null;
        }

        return new FormLineContainer(fList, getTitle(dType));
    }

}
