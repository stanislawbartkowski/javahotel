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
package com.gwtmodel.table.view.form;

import java.util.List;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.factories.IDataFormConstructorAbstractFactory;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.rdef.FormField;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.ITouchListener;
import com.gwtmodel.table.view.ErrorLineContainer;
import com.gwtmodel.table.view.util.CreateFormView;

class GwtFormView implements IGwtFormView {

    private final FormLineContainer fContainer;
    private final Widget gg;
    final ErrorLineContainer eStore = new ErrorLineContainer();
    private HTMLPanel hp;

    private void setListener() {

        ITouchListener ii = new ITouchListener() {

            @Override
            public void onTouch() {
                eStore.clearE();
            }
        };
        for (FormField e : fContainer.getfList()) {
            e.getELine().setOnTouch(ii);
        }
    }

    GwtFormView(ICallContext iContext, final FormLineContainer fContainer,
            IDataFormConstructorAbstractFactory.CType cType) {
        this.fContainer = fContainer;
        if (cType.getfConstructor() == null) {
            if (fContainer.getHtml() == null) {
                gg = CreateFormView.construct(fContainer.getfList());
            } else {
                gg = CreateFormView.setHtml(fContainer.getHtml(),
                        fContainer.getfList());
            }
        } else {
            gg = cType.getfConstructor().construct(iContext, fContainer);
        }
        hp = null;
        setListener();
    }

    @Override
    public Widget getGWidget() {
        if (hp == null) {
            return gg;
        }
        return hp;
    }

    @Override
    public void showInvalidate(InvalidateFormContainer errContainer) {
        List<InvalidateMess> col = errContainer.getErrMess();

        boolean something = false;
        for (InvalidateMess m : col) {
            IVField mFie = m.getFie();
            for (FormField re : fContainer.getfList()) {
                if ((mFie == null) || re.getFie().eq(mFie)) {
                    eStore.setEMess(re.getELine(), m);
                    something = true;
                }
            }
        }
        if (!something) {
            FormField re = fContainer.getfList().get(0);
            InvalidateMess m = col.get(0);
            eStore.setEMess(re.getELine(), m);
        }
    }

    @Override
    public void fillHtml(IGWidget gw) {
        Widget w = gw.getGWidget();
        HTMLPanel pa = (HTMLPanel) w;
        hp = CreateFormView.setHtml(pa, fContainer.getfList());
    }
}
