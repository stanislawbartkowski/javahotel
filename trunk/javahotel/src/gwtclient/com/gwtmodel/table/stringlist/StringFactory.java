/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.stringlist;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.Empty;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.factories.IFormDefFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;

class StringFactory implements IFormDefFactory {

    private final String fieldName;
    private final String title;

    StringFactory(String fieldName, String title) {
        this.fieldName = fieldName;
        this.title = title;
    }

    public FormLineContainer construct(IDataType dType) {
        EditWidgetFactory eFactory = GwtGiniInjector.getI()
                .getEditWidgetFactory();
        List<FormField> di = new ArrayList<FormField>();
        IFormLineView textLine = eFactory.constructTextField(null);
        di.add(new FormField(fieldName, textLine, Empty.getFieldType()));
        return new FormLineContainer(di);
    }

    public String getFormTitle(IDataType dType) {
        return title;
    }

}