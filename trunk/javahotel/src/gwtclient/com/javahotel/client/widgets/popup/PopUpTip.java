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
package com.javahotel.client.widgets.popup;

import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class PopUpTip {

    public static PopupPanel getPopupTip(Widget info) {
        PopupPanel pUp = new PopupPanel(true);
        pUp.setWidget(info);
        return pUp;
    }

    public static void drawPopupTip(int left, int top, Widget info) {
        PopupPanel pUp = getPopupTip(info);
        pUp.setPopupPosition(left, top);
        pUp.show();
    }
}
