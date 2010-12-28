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

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.mvc.contrpanel.model.ContrButton;
import com.javahotel.client.mvc.contrpanel.model.IContrPanel;
import com.javahotel.client.mvc.contrpanel.view.IControlClick;
import java.util.List;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class PopupCreateMenu {

    private static class BCall implements Command {

        private final ContrButton bdef;
        private final Widget w;
        private final IControlClick co;

        private BCall(final ContrButton b, final Widget w, IControlClick co) {
            this.bdef = b;
            this.w = w;
            this.co = co;
        }

        public void execute() {
            co.click(bdef, w);
        }
    }

    public static MenuBar createMenu(final IContrPanel coP,
            final IControlClick cli, final Widget w) {

        MenuBar menu = null;
        if (coP != null) {
            List<ContrButton> cL = coP.getContr();

            menu = new MenuBar(true);
            for (ContrButton b : cL) {
                BCall bc = new BCall(b, w, cli);
                menu.addItem(b.getContrName(), bc);
            }
        }
        return menu;
    }
}
