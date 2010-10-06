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
package com.javahotel.client.dialog.panel;

import com.google.gwt.user.client.ui.Panel;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.view.webpanel.IWebPanel;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.IGwtWidget;
import com.javahotel.client.panelcommand.CommandDrawPanel;
import com.javahotel.client.panelcommand.EPanelCommand;
import com.javahotel.client.panelcommand.IPanelCommand;
import com.javahotel.client.panelcommand.PanelCommandFactory;
import com.javahotel.client.stackmenu.view.IStackMenuClicked;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class UserPanel {

    private final IResLocator rI;

    public UserPanel(final IResLocator rI, IUserPanelMenuFactory fPanel) {
        this.rI = rI;
        IWebPanel iW = GwtGiniInjector.getI().getWebPanel();
        IStackMenuClicked iClicked = new IStackMenuClicked() {

            public void ClickedView(IPanelCommand clicked) {
                CommandDrawPanel.setC(rI, clicked);
            }
        };
        iW.setDCenter(null);
        IGwtWidget wPanel = fPanel.getMenuPanel(iClicked);
        EPanelCommand e = fPanel.getCentreWidget();
        if (wPanel != null) {
            iW.setWest(wPanel.getMWidget().getWidget());
        }
        if (e != null) {
            IPanelCommand i = PanelCommandFactory.getPanelCommand(rI, e);
            CommandDrawPanel.setC(rI, i);
        }
        Panel pa = fPanel.getMenuPanel();
        if (pa != null) {
            iW.setMenuPanel(pa);
        }
    }
}
