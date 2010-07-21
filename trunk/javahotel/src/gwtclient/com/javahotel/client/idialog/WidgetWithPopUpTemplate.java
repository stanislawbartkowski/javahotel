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
package com.javahotel.client.idialog;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.Utils;
import com.javahotel.client.IResLocator;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("deprecation")
abstract class WidgetWithPopUpTemplate extends ExtendTextBox {

    abstract protected Widget getPopUpWidget();
    private PopupPanel pUp = null;
    final private ClickListener cL = new ClickListener() {

        public void onClick(final Widget arg0) {
            if (pUp == null) {
                Widget w = getPopUpWidget();
                int le = hPanel.getAbsoluteLeft() + hPanel.getOffsetWidth();
                int to = hPanel.getAbsoluteTop() + hPanel.getOffsetHeight();
                pUp = new PopupPanel(true);
                pUp.setPopupPosition(le, to);
                pUp.add(w);
            }
            pUp.show();
        }
    };

    protected WidgetWithPopUpTemplate(final IResLocator pLoc) {
        super(pLoc, false);
        String iPath = Utils.getImageHTML("calendar.gif");
        HTML dB = new HTML(iPath);
        dB.addStyleName("calendar-image");
        hPanel.add(dB);
        dB.addClickListener(cL);
    }
}
