/*
 * Copyright 2012 stanislawbartkowski@gmail.com
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
package com.javahotel.nmvc.factories.season;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.ISetGWidget;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.factories.IDataFormConstructor;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.ICallContext;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.view.controlpanel.ContrButtonViewFactory;
import com.gwtmodel.table.view.controlpanel.IContrButtonView;
import com.gwtmodel.table.view.controlpanel.IControlClick;
import com.gwtmodel.table.view.util.CreateFormView;
import com.javahotel.client.M;

/**
 * 
 * @author hotel
 */
public class SeasonForm implements IDataFormConstructor {

    private final ContrButtonViewFactory bFactory;

    public SeasonForm() {
        bFactory = GwtGiniInjector.getI().getContrButtonViewFactory();
    }

    private class ButtList implements IControlClick {

        private final ICallContext iContext;

        ButtList(ICallContext i) {
            this.iContext = i;
        }

        @Override
        public void click(ControlButtonDesc co, Widget w) {
            iContext.iSlo().getSlContainer()
                    .publish(SeasonAddInfo.SHOWSEASONSTRING, new GWidget(w));
        }
    }

    @Override
    public void construct(ISetGWidget iSet, ICallContext iContext,
            FormLineContainer model) {
        Widget w = CreateFormView.construct(model.getfList());
        VerticalPanel v = new VerticalPanel();
        ControlButtonDesc b = new ControlButtonDesc(M.L().ShowSeasons(),
                new ClickButtonType("POKAZ"));
        List<ControlButtonDesc> li = new ArrayList<ControlButtonDesc>();
        li.add(b);
        ListOfControlDesc lo = new ListOfControlDesc(li);
        IContrButtonView bv = bFactory.getView(lo, new ButtList(iContext));
        v.add(w);
        v.add(bv.getGWidget());
        iSet.setW(new GWidget(w));
    }
}
