/*
 * Copyright 2016 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.view.ewidget;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.ICommand;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.ISetGWidget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.editc.IRequestForGWidget;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.view.util.PopupUtil;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("deprecation")
class WidgetWithPopUpTemplate {

    final private ISetGWidget is = new ISetGWidget() {

        @Override
        public void setW(IGWidget w) {
            pUp = new PopupPanel(true);
            PopupUtil.setPos(pUp, hPanel);
            pUp.add(w.getGWidget());
            pUp.show();

        }
    };
    private final IVField v;
    private final HorizontalPanel hPanel;
    private PopupPanel pUp = null;
    private final IRequestForGWidget iGet;
    private final HTML dB;
    private boolean isReadOnly = false;
    private final boolean refreshAlways;

    final private ClickListener cL = new ClickListener() {

        public void onClick(final Widget arg0) {
            if (isReadOnly) {
                return;
            }

            ICommand close = new ICommand() {

                @Override
                public void execute() {
                    hide();

                }

            };
            if ((pUp == null) || refreshAlways) {
                iGet.run(v, new WSize(arg0), is, close);
            } else {
                pUp.show();
            }
        }
    };

    void hide() {
        pUp.hide();
    }

    WidgetWithPopUpTemplate(IVField v, HorizontalPanel hPanel, String image,
            IRequestForGWidget i, boolean refreshAlways, String htmlName) {
        this.v = v;
        assert image != null : LogT.getT().cannotBeNull();
        this.iGet = i;
        this.hPanel = hPanel;
        this.refreshAlways = refreshAlways;
        String hName = htmlName;
        if (hName != null) {
            hName += "-helper";
        }
        String iPath = Utils.getImageHTML(image, hName);
        dB = new HTML(iPath);
        hPanel.add(dB);
        dB.addClickListener(cL);
    }

    void setReadOnly(boolean readOnly) {
        this.isReadOnly = readOnly;
    }
}
