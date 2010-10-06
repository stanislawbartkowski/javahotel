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
package com.javahotel.client.mvc.util;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.dialog.IWidgetSize;
import com.javahotel.client.dialog.WidgetSizeFactory;
import com.javahotel.common.util.MaxI;

abstract class AbstractDialog {

    protected DialogBox dBox;

    public PopupPanel getW() {
        return dBox;
    }

    public void show(final Widget wi) {
        dBox.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
            public void setPosition(int offsetWidth, int offsetHeight) {
                final IWidgetSize w = WidgetSizeFactory.getW(wi);
                int maxwi = Window.getClientWidth();
                int maxhei = Window.getClientHeight();
                int t = w.getTop() + w.getHeight();
                int l = w.getLeft();

                int left = MaxI.min(maxwi - offsetWidth, l);
                int top = MaxI.min(maxhei - offsetHeight, t);
                dBox.setPopupPosition(left, top);
            }
        });
    }

}
