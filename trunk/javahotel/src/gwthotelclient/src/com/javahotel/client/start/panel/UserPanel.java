/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.start.panel;

import com.google.gwt.user.client.ui.Panel;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.view.webpanel.IWebPanel;
import com.javahotel.client.IResLocator;
import com.javahotel.client.injector.HInjector;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class UserPanel {

    public static void draw(IUserPanelMenuFactory fPanel) {
        final IResLocator rI = HInjector.getI().getI();
        IWebPanel iW = GwtGiniInjector.getI().getWebPanel();
        IStackMenuClicked iClicked = new IStackMenuClicked() {

            @Override
            public void ClickedView(IPanelCommand clicked) {
                CommandDrawPanel.setC(rI, clicked, null);
            }
        };
        iW.setDCenter(null);
        IGWidget wPanel = fPanel.getMenuPanel(iClicked);
        EPanelCommand e = fPanel.getCentreWidget();
        if (wPanel != null) {
            iW.setWest(wPanel.getGWidget());
        }
        if (e != null) {
            IPanelCommandFactory pa = HInjector.getI().getPanelCommandFactory();
            IPanelCommand i = pa.construct(e);
            CommandDrawPanel.setC(rI, i, null);
        }
        Panel pa = fPanel.getMenuPanel();
        if (pa != null) {
            iW.setMenuPanel(pa);
        }
    }
}
