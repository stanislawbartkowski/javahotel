/*
 * Copyright 2009 stanislawbartkowski@gmail.com 
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

import com.javahotel.client.mvc.record.view.helper.IInitDialog;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.mvc.apanel.IPanel;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.record.model.IRecordDef;
import com.javahotel.client.mvc.record.view.IAuxRecordPanel;
import com.javahotel.client.mvc.record.view.IRecordView;
import com.javahotel.client.mvc.util.MDialog;
import com.javahotel.client.mvc.validator.IErrorMessage;
import com.javahotel.client.widgets.popup.PopupUtil;
import com.javahotel.common.toobject.IField;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class RecordVDialog implements IRecordView {

    private final DialogBox dBox;
    private final IInitDialog iDialog;

    RecordVDialog(final IResLocator rI, final IInitDialog iDialog) {
        final VerticalPanel v = new VerticalPanel();
        final IPanel vP = PanelFactory.getGwtPanel(v);
        this.iDialog = iDialog;
        MDialog d = new MDialog(v, iDialog.getModel().getDTitle()) {

            @Override
            protected void addVP(VerticalPanel vp) {
                iDialog.initW(vP, null);
            }
        };
        dBox = d.getDBox();
    }

    public void setPos(Widget w) {
        PopupUtil.setPos(dBox, w);
    }

    RecordVDialog(final IResLocator rI, final IInitDialog iDialog,
            final Widget w) {
        final VerticalPanel v = new VerticalPanel();
        final IPanel vP = PanelFactory.getGwtPanel(v);
        this.iDialog = iDialog;
        MDialog d = new MDialog(v, iDialog.getModel().getDTitle()) {

            @Override
            protected void addVP(VerticalPanel vp) {
                vP.add(w);
                iDialog.initW(vP, null);
            }
        };
        dBox = d.getDBox();
    }

    public void show() {
        dBox.show();
    }

    public void hide() {
        dBox.hide();
    }

    public IAuxRecordPanel getAuxV() {
        return iDialog.getAuxV();
    }

    public IRecordDef getModel() {
        return iDialog.getModel();
    }

    public void showInvalidate(IErrorMessage col) {
        iDialog.showInvalidate(col);
    }

    public String getString(IField f) {
        return iDialog.getString(f);
    }

    public void setString(IField f, String val) {
        iDialog.setString(f, val);
    }

    public void extractFields(RecordModel mo) {
        iDialog.extractFields(mo);
    }

    public void setFields(RecordModel mo) {
        iDialog.setFields(mo);
    }

    public void changeMode(int actionMode) {
        iDialog.changeMode(actionMode);
    }

//    public IMvcWidget getMWidget() {
//        return new DefaultMvcWidget(dBox);
//    }
}
