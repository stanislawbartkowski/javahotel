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
package com.gwtmodel.table.view.ewidget;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.factories.ITableCustomFactories;
import com.gwtmodel.table.view.util.PopupUtil;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("deprecation")
class WidgetWithPopUpTemplate {

    interface ISetWidget {

        void setWidget(Widget w);
    }

    interface IGetP {

        void getPopUp(Widget startW, ISetWidget iSet);
    }
    final private ISetWidget is = new ISetWidget() {

        public void setWidget(Widget w) {
            pUp = new PopupPanel(true);
            PopupUtil.setPos(pUp, hPanel);
            pUp.add(w);
            pUp.show();
        }
    };
    private final HorizontalPanel hPanel;
    private PopupPanel pUp = null;
    private final IGetP iGet;
    final private ClickListener cL = new ClickListener() {

        public void onClick(final Widget arg0) {
            if (pUp == null) {
                iGet.getPopUp(arg0, is);
            } else {
                pUp.show();
            }
        }
    };

    void hide() {
        pUp.hide();
    }

    WidgetWithPopUpTemplate(ITableCustomFactories tFactories, HorizontalPanel hPanel,
            String image, IGetP i) {
        this.iGet = i;
        this.hPanel = hPanel;
        String iPath = Utils.getImageHTML(image + ".gif");
        HTML dB = new HTML(iPath);
        hPanel.add(dB);
        dB.addClickListener(cL);
    }
}
