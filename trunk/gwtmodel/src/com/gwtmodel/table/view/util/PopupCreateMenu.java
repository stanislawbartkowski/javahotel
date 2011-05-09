/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.view.util;

import java.util.List;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.view.controlpanel.IControlClick;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class PopupCreateMenu {

    private static class BCall implements Command {

        private final ControlButtonDesc bdef;
        private final Widget w;
        private final IControlClick co;

        private BCall(final ControlButtonDesc b, final Widget w,
                IControlClick co) {
            this.bdef = b;
            this.w = w;
            this.co = co;
        }

        public void execute() {
            co.click(bdef, w);
        }
    }

    public static MenuBar createMenu(final ListOfControlDesc coP,
            final IControlClick cli, final Widget w) {

        MenuBar menu = null;
        if (coP != null) {
            List<ControlButtonDesc> cL = coP.getcList();

            menu = new MenuBar(true);
            for (ControlButtonDesc b : cL) {
                BCall bc = new BCall(b, w, cli);
                menu.addItem(b.getDisplayName(), bc);
            }
        }
        return menu;
    }
    
    public static IGWidget createImageMenu(String imageHtml,final ListOfControlDesc coP,
            final IControlClick cli, final Widget w) {

        HorizontalPanel hp = new HorizontalPanel();
        MenuBar menu = createMenu(coP,cli,w);
        MenuBar mp = new MenuBar();
        hp.add(mp);
        mp.addItem(imageHtml, true, menu);
        return new GWidget(hp);
    }

//    HorizontalPanel hp = new HorizontalPanel();
//    MenuBar mp = new MenuBar();
//    hp.add(mp);
//    MenuBar menu = CreateMenuCommand.createMenu(rI, new EPanelCommand[] {
//            EPanelCommand.BOOKINGPANEL, EPanelCommand.BOOKING,
//            EPanelCommand.PREPAID, EPanelCommand.ROOMSADMIN, EPanelCommand.TESTBOOKING,
//            EPanelCommand.TESTBOOKINGELEM,EPanelCommand.TESTBOOKINGNEWELEM});
//    
//    mp.addItem(Utils.getImageHTML(IImageGallery.DOWNMENU), true, menu);
//    return hp;
    
}
