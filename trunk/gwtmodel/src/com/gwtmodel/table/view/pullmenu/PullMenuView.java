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
package com.gwtmodel.table.view.pullmenu;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.login.menudef.MenuPullContainer;
import com.gwtmodel.table.login.menudef.MenuPullDesc;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.view.controlpanel.IContrButtonView;
import com.gwtmodel.table.view.controlpanel.IControlClick;
import java.util.List;

/**
 *
 * @author perseus
 */
class PullMenuView implements IContrButtonView {

    private final MenuPullContainer menu;
    private final IControlClick click;
    private final MenuBar mbar;

    public void setEnable(ClickButtonType actionId, boolean enable) {
    }

    public void fillHtml(IGWidget g) {
    }

    public void emulateClick(ClickButtonType actionId) {
    }

    private class MenuCommand implements Command {

        private final ControlButtonDesc butt;

        MenuCommand(ControlButtonDesc butt) {
            this.butt = butt;
        }

        public void execute() {
            click.click(butt, mbar);
        }
    }

    private void addMenu(MenuBar menu, List<MenuPullDesc> bmenu) {
        for (MenuPullDesc de : bmenu) {
            if (de.isHeader()) {
                MenuBar b = new MenuBar();
                addMenu(b, de.getbList());
                menu.addItem(de.getDisplayName(), b);
            } else {
                ControlButtonDesc butt = de.getButt();
                menu.addItem(butt.getDisplayName(), new MenuCommand(butt));

            }
        }
    }

    PullMenuView(MenuPullContainer menu,
            IControlClick click) {
        this.menu = menu;
        this.click = click;
        mbar = new MenuBar();
        addMenu(mbar, menu.getMenu());
    }

    public Widget getGWidget() {
        return mbar;
    }
}
