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
package com.javahotel.view.gwt.record.view;

import java.util.Set;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.ISetGwtWidget;
import com.javahotel.client.mvc.apanel.IPanel;
import com.javahotel.client.mvc.contrpanel.model.IContrPanel;
import com.javahotel.client.mvc.contrpanel.view.ContrButtonViewFactory;
import com.javahotel.client.mvc.contrpanel.view.IContrButtonView;
import com.javahotel.client.mvc.contrpanel.view.IControlClick;
import com.javahotel.client.mvc.record.model.IRecordDef;
import com.javahotel.client.mvc.record.view.IAuxRecordPanel;
import com.javahotel.client.mvc.record.view.ICreateViewContext;
import com.javahotel.client.mvc.record.view.IRecordView;
import com.javahotel.client.mvc.record.view.helper.IInitDialog;
import com.javahotel.common.toobject.IField;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class VRecordView extends AbstractRecordView implements IInitDialog {

    // private Widget vP;
    private final IContrPanel contr;
    private final IControlClick co;
    private final IAuxRecordPanel auxV;
    private final ISetGwtWidget iSet;

    VRecordView(final IResLocator rI, ISetGwtWidget iSet, final DictData da,
            final IRecordDef model, final IContrPanel contr,
            final IControlClick co, final IAuxRecordPanel auxV) {
        super(rI, model);
        this.iSet = iSet;
        this.contr = contr;
        this.co = co;
        this.auxV = auxV;
    }

    protected void createWDialog(final VerticalPanel iPanel,
            final Set<IField> filter) {
        DefaultView.createWDialog(iPanel, model, filter, eStore);
    }

    private class ContextCustom implements ICreateViewContext {

        private final IPanel ip;

        ContextCustom(IPanel ip) {
            this.ip = ip;
        }

        public void createDefaultDialog(VerticalPanel vp) {
            createWDialog(vp, null);
        }

        public void createDefaultDialog(VerticalPanel vp, IRecordDef model) {
            DefaultView.createWDialog(vp, model, null, eStore);
        }

        public IPanel getIPanel() {
            return ip;
        }

        public IRecordDef getModel() {
            return model;
        }

        public IRecordView getRecordView() {
            return VRecordView.this;
        }

    }

    public void initW(final IPanel vP, final Widget iW) {
        Widget iWidget;
        IContrButtonView i = null;
        if (contr != null) {
            i = ContrButtonViewFactory.getView(rI, contr, co);
        }

        boolean custom = false;

        if (auxV != null) {
            custom = auxV.getCustomView(iSet, new ContextCustom(vP), i);
        }

        if (custom) {
            return;
        }

        if (iW == null) {
            VerticalPanel iPanel = new VerticalPanel();
            createWDialog(iPanel, null);
            iWidget = iPanel;
        } else {
            iWidget = iW;
        }

        if (auxV != null) {
            HorizontalPanel hp = new HorizontalPanel();
            hp.add(iWidget);
            hp.add(auxV.getMWidget().getWidget());
            vP.add(hp);
        } else {
            vP.add(iWidget);
        }

        if (i != null) {
            vP.add(i.getMWidget().getWidget());
        }

        Widget vvP = vP.getPanel();
        iSet.setGwtWidget(new DefaultMvcWidget(vvP));

    }

    public IAuxRecordPanel getAuxV() {
        return auxV;
    }

    public void addEmptyList() {
    }

}
