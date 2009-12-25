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

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Panel;
import com.google.inject.Inject;
import com.javahotel.client.CommonUtil;
import com.javahotel.client.IImageGallery;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.IGwtWidget;
import com.javahotel.client.dialog.panel.IUserPanelMenuFactory;
import com.javahotel.client.panelcommand.EPanelCommand;
import com.javahotel.client.stackmenu.view.IStackMenuClicked;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */

public class UserPanelFactory implements IUserPanelMenuFactory {

    private final IResLocator rI;
    
    @Inject
    public UserPanelFactory(final IResLocator rI) {
        this.rI = rI;
    }
    
    public IGwtWidget getMenuPanel(final IStackMenuClicked iClicked) {
        // return new AdminShortcutMenu(rI, iClicked);
        return null;
    }

    public EPanelCommand getCentreWidget() {
        // return EPanelCommand.BOOKINGPANEL;
        // return EPanelCommand.TESTSCROLLSEASON;
        // return EPanelCommand.TESTSCROLLSEASONWIDGET;
        // return EPanelCommand.SEASON;
        // return EPanelCommand.CUSTOMERS;
        // return EPanelCommand.BOOKINGPANEL;
        // return EPanelCommand.BOOKING;
        // return EPanelCommand.PRICES;
        return EPanelCommand.TESTBOOKINGELEM;
    }

    public Panel getMenuPanel() {
        HorizontalPanel hp = new HorizontalPanel();
        MenuBar mp = new MenuBar();
        hp.add(mp);
        MenuBar menu = CreateMenuCommand.createMenu(rI, new EPanelCommand[] {
                EPanelCommand.BOOKINGPANEL, EPanelCommand.BOOKING,
                EPanelCommand.PREPAID, EPanelCommand.ROOMSADMIN });
        mp.addItem(CommonUtil.getImageHTML(IImageGallery.DOWNMENU), true, menu);
        return hp;
    }
}
