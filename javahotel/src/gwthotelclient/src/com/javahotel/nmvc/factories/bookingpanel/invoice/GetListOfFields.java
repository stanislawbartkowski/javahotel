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
package com.javahotel.nmvc.factories.bookingpanel.invoice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.gwtmodel.table.AbstractListT;
import com.gwtmodel.table.IMapEntry;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;
import com.javahotel.common.toobject.IField;
import com.javahotel.common.toobject.InvoiceIssuerP;
import com.javahotel.common.toobject.InvoiceP;

/**
 * @author hotel
 * 
 */
public class GetListOfFields {

    private GetListOfFields() {

    }

    public static List<FormField> getListOfFields() {
        EditWidgetFactory eFactory;
        eFactory = GwtGiniInjector.getI().getEditWidgetFactory();

        // List<String> fList = MapFields.
        AbstractListT aI = MapInvoiceP.getM();
        List<FormField> foList = new ArrayList<FormField>();
        // prepare set of fields to be disabled
        // fields related to hotel data are disables
        Set<String> disabled = new HashSet<String>();
        Map<IField, String> iM = MapInvoiceDialog.getMapHotel();
        for (Entry<IField, String> s : iM.entrySet()) {
            disabled.add(s.getValue());
        }
        disabled.remove(iM.get(InvoiceIssuerP.F.paymentDays));
        disabled.add(InvoiceP.INVOICENUMBER);
        // --
        for (IMapEntry en : aI.getEntryList()) {
            IVField v = MapFields.mapS.get(en.getKey());
            IFormLineView e = eFactory.constructEditWidget(v);
            boolean readOnlyIfModif = false;
            boolean readOnlyIfAdd = false;
            if (disabled.contains(en.getKey())) {
                readOnlyIfModif = true;
                readOnlyIfAdd = true;
            }
            FormField fie = new FormField(null, e, null, readOnlyIfModif,
                    readOnlyIfAdd);
            fie.setHtmlId(en.getValue());
            foList.add(fie);
        }
        return foList;
    }

}
