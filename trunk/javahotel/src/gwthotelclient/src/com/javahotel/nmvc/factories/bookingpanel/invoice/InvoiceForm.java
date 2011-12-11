/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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
package com.javahotel.nmvc.factories.bookingpanel.invoice;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.ISetGWidget;
import com.gwtmodel.table.factories.IDataFormConstructor;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.readres.ISetResText;
import com.gwtmodel.table.view.util.CreateFormView;
import com.javahotel.client.IResLocator;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.types.DataType;

/**
 * @author hotel
 * 
 */
public class InvoiceForm implements IDataFormConstructor {

    private final IResLocator rI;
    private static final String INVOICEHTML = "invoice/invoice1.html";

    public InvoiceForm() {
        rI = HInjector.getI().getI();
    }

    private class setHtmlWidget implements ISetResText {

        private final ISetGWidget iSet;
        private final FormLineContainer model;

        setHtmlWidget(ISetGWidget iSet, FormLineContainer model) {
            this.iSet = iSet;
            this.model = model;
        }

        @Override
        public void setResText(String s) {
            HTMLPanel pa = CreateFormView.setHtml(s, model.getfList());
            iSet.setW(new GWidget(pa));
        }

    }

    @Override
    public void construct(ISetGWidget iSet, ICallContext iContext,
            FormLineContainer model) {
        rI.readRes().readRes(new setHtmlWidget(iSet, model), INVOICEHTML);
    }

}
