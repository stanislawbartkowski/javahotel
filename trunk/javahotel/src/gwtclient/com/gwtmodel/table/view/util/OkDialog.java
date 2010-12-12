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
package com.gwtmodel.table.view.util;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonFactory;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.view.controlpanel.ContrButtonViewFactory;
import com.gwtmodel.table.view.controlpanel.IContrButtonView;
import com.gwtmodel.table.view.controlpanel.IControlClick;

/**
 *
 * @author perseus
 */
public class OkDialog extends ModalDialog {

    private final String kom;

    @Override
    protected void addVP(VerticalPanel vp) {
        vp.add(new Label(kom));
    }

    public OkDialog(String kom, String title,
            final ICloseAction ok) {
        super(new VerticalPanel(), null);
        this.kom = kom;
        if (title == null) {
            ITableCustomFactories fa = GwtGiniInjector.getI().getTableFactoriesContainer();
            IGetCustomValues va = fa.getGetCustomValues();
            title = va.getCustomValue(IGetCustomValues.INFO);
        }
        setTitle(title);

        create();

        ControlButtonFactory fa = GwtGiniInjector.getI().getControlButtonFactory();
        ListOfControlDesc yesB = fa.constructOkButton();

        IControlClick cli = new IControlClick() {

            @Override
            public void click(ControlButtonDesc co, Widget w) {
                hide();
                if (ok != null) {
                    ok.onClose();
                }
            }
        };

        ContrButtonViewFactory ba = GwtGiniInjector.getI().getContrButtonViewFactory();
        IContrButtonView vButton = ba.getView(yesB, cli);

        vP.add(vButton.getGWidget());
    }
}
