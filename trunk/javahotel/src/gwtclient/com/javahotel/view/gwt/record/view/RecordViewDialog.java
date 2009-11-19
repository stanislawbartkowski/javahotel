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
package com.javahotel.view.gwt.record.view;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.ISetGwtWidget;
import com.javahotel.client.mvc.apanel.IPanel;
import com.javahotel.client.mvc.contrpanel.model.IContrPanel;
import com.javahotel.client.mvc.contrpanel.view.IControlClick;
import com.javahotel.client.mvc.record.model.IRecordDef;
import com.javahotel.client.mvc.record.view.IAuxRecordPanel;
import com.javahotel.client.mvc.util.MDialog;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */

// TODO : remove candidate
class RecordViewDialog extends VRecordView {

    private final DialogBox dBox;

    private RecordViewDialog(final IResLocator rI, ISetGwtWidget iSet,
            final DictData da, final IRecordDef model, final IContrPanel contr,
            final IControlClick co, final IAuxRecordPanel auxV) {
        super(rI, iSet, da, model, contr, co, auxV);
        final VerticalPanel v = new VerticalPanel();
        final IPanel vP = PanelFactory.getGwtPanel(v);
        MDialog d = new MDialog(v, model.getDTitle()) {

            @Override
            protected void addVP(VerticalPanel vp) {
                initW(vP, null);
            }
        };
        dBox = d.getDBox();
    }

    @Override
    public void show() {
        dBox.show();
    }

    @Override
    public void hide() {
        dBox.hide();
    }
}
