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
package com.javahotel.client.dialog.user;

import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.mvc.contrpanel.model.ContrButton;
import com.javahotel.client.mvc.contrpanel.model.ContrButtonFactory;
import com.javahotel.client.mvc.contrpanel.model.IContrPanel;
import com.javahotel.client.mvc.contrpanel.view.IControlClick;
import com.javahotel.client.panelcommand.CommandDrawPanel;
import com.javahotel.client.panelcommand.EPanelCommand;
import com.javahotel.client.panelcommand.IPanelCommand;
import com.javahotel.client.panelcommand.PanelCommandFactory;
import com.javahotel.client.widgets.popup.PopupCreateMenu;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class CreateMenuCommand {

    private CreateMenuCommand() {
    }

    private static class ExecuteC implements IControlClick {

        private final EPanelCommand[] eMenu;
        private final IResLocator rI;

        ExecuteC(IResLocator rI, EPanelCommand[] e) {
            this.eMenu = e;
            this.rI = rI;
        }

        public void click(ContrButton co, Widget w) {
            int i = co.getActionId();
            EPanelCommand e = eMenu[i];
            IPanelCommand iP = PanelCommandFactory.getPanelCommand(rI, e);
            CommandDrawPanel.setC(rI, iP);
        }
    }

    static MenuBar createMenu(IResLocator rI, EPanelCommand[] eMenu) {
        List li = new ArrayList<ContrButton>();
        for (int i = 0; i < eMenu.length; i++) {
            String label = PanelCommandFactory.getPanelCommandLabel(rI,
                    eMenu[i]);
            ContrButton bu = new ContrButton(null, label, i);
            li.add(bu);
        }
        IContrPanel pa = ContrButtonFactory.getContr(rI, li);
        MenuBar menu = PopupCreateMenu.createMenu(pa, new ExecuteC(rI, eMenu),
                null);
        return menu;
    }
}
