/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.menudef.MenuPullContainer;
import com.gwtmodel.table.menudef.MenuPullDesc;
import com.gwtmodel.table.slotmodel.ClickButtonType;
import com.gwtmodel.table.view.controlpanel.IContrButtonView;
import com.gwtmodel.table.view.controlpanel.IControlClick;
import com.gwtmodel.table.view.util.CreateFormView.IGetButtons;

import java.util.List;

/**
 *
 * @author perseus
 */
class PullMenuView implements IContrButtonView {

    private final MenuPullContainer menu;
    private final IControlClick click;
    private final MenuBar mbar;

    @Override
    public void setEnable(ClickButtonType actionId, boolean enable) {
    }

    @Override
    public void emulateClick(ClickButtonType actionId) {
    }

    @Override
    public void setHtml(IGWidget g) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void fillHtml(HTMLPanel pa) {
        throw new UnsupportedOperationException("Not supported yet.");
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

    @Override
    public Widget getGWidget() {
        return mbar;
    }

    @Override
    public void setHidden(ClickButtonType actionId, boolean hidden) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public IGetButtons construct() {
        // TODO Auto-generated method stub
        return null;
    }
}
