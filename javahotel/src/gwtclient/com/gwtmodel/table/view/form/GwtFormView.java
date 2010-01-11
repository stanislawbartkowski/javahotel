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
package com.gwtmodel.table.view.form;

import java.util.List;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.factories.IDataFormConstructorAbstractFactory;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.ITouchListener;

class GwtFormView implements IGwtFormView {

    private final FormLineContainer fContainer;
    private final Widget g;
    final ErrorLineContainer eStore = new ErrorLineContainer();

    private void setListener() {

        ITouchListener ii = new ITouchListener() {

            public void onTouch() {
                eStore.clearE();
            }
        };
        for (FormField e : fContainer.getfList()) {
            e.getELine().setOnTouch(ii);
        }
    }

    GwtFormView(final FormLineContainer fContainer,
            IDataFormConstructorAbstractFactory.CType cType) {
        this.fContainer = fContainer;
        if (cType.getfConstructor() == null) {
          g = CreateFormView.construct(fContainer);
        }
        else {
          g = cType.getfConstructor().construct(fContainer);
        }        
        setListener();
    }

    public Widget getGWidget() {
        return g;
    }

    public void showInvalidate(InvalidateFormContainer errContainer) {
        List<InvalidateMess> col = errContainer.getErrMess();

        for (InvalidateMess m : col) {
            IVField mFie = m.getFie();
            for (FormField re : fContainer.getfList()) {
                if (re.getFie().eq(mFie)) {
                    eStore.setEMess(re, m);
                }
            }
        }
    }

}
