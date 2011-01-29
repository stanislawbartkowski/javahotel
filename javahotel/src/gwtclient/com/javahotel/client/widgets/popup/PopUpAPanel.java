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
package com.javahotel.client.widgets.popup;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.javahotel.client.mvc.contrpanel.model.IContrPanel;
import com.javahotel.client.mvc.contrpanel.view.IControlClick;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class PopUpAPanel extends Composite {

    private final VerticalPanel hP = new VerticalPanel();

    public VerticalPanel getVP() {
        return hP;
    }

    public PopUpAPanel(final ICloseAction c, final IContrPanel coP,
            final IControlClick cli) {

        ICloseAction iC = new ICloseAction() {

            public void setVisible(boolean visible) {
                if (c == null) {
                    hP.setVisible(visible);
                } else {
                    c.setVisible(visible);
                }
            }
        };
        MenuBar menu = null;
        MenuBar bmenu = null;
        if (coP != null) {
            bmenu = new MenuBar();
            menu = PopupCreateMenu.createMenu(coP, cli, bmenu);
        }
        PopupUtil.addClose(hP, iC, menu, bmenu);
        initWidget(hP);
    }
}
