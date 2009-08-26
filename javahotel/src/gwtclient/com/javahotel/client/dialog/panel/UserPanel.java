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
package com.javahotel.client.dialog.panel;

import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.IGwtWidget;
import com.javahotel.client.stackmenu.model.IStackButtonClick;
import com.javahotel.client.stackmenu.view.IStackMenuClicked;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class UserPanel {

    private final IResLocator rI;

    private void psetW(final Widget w) {
        rI.getPanel().setDCenter(w);
    }

    public UserPanel(final IResLocator rI,IUserPanelMenuFactory fPanel) {
        this.rI = rI;
        IStackMenuClicked iClicked = new IStackMenuClicked() {

            public void ClickedView(IStackButtonClick clicked) {
                psetW(clicked.getMWidget().getWidget());
            }
        };
        rI.getPanel().setDCenter(null);
        IGwtWidget wPanel = fPanel.getMenuPanel(rI, iClicked);
        rI.getPanel().setWest(wPanel.getMWidget().getWidget());
    }
}
