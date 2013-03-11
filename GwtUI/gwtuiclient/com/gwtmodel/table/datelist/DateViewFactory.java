/*
 *  Copyright 2013 stanislawbartkowski@gmail.com
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package com.gwtmodel.table.datelist;

import com.gwtmodel.table.IVField;
import com.gwtmodel.table.factories.IFormDefFactory;
import com.gwtmodel.table.factories.IFormTitleFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.injector.MM;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author hotel
 */
class DateViewFactory implements IFormTitleFactory, IFormDefFactory {

    private final String title;

    DateViewFactory(String title) {
        this.title = title;
    }

    @Override
    public FormLineContainer construct(ICallContext iContext) {
        EditWidgetFactory eFactory = GwtGiniInjector.getI()
                .getEditWidgetFactory();
        List<FormField> di = new ArrayList<FormField>();
        IVField vfrom = new DatePeriodField(DatePeriodField.F.DATEFROM);
        IVField vto = new DatePeriodField(DatePeriodField.F.DATETO);
        IVField vcomment = new DatePeriodField(DatePeriodField.F.COMMENT);
        IFormLineView dFrom = eFactory.construcDateBoxCalendar(vfrom, null);
        IFormLineView dTo = eFactory.construcDateBoxCalendar(vto, null);
        IFormLineView comment = eFactory.constructTextField(vcomment, null);
        di.add(new FormField(MM.getL().From(), dFrom));
        di.add(new FormField(MM.getL().To(), dTo));
        di.add(new FormField(MM.getL().Description(), comment));
        return new FormLineContainer(di);
    }

    @Override
    public String getFormTitle(ICallContext iContext) {
        return title;
    }
}
