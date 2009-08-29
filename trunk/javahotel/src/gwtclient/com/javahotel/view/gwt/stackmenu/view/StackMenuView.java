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
package com.javahotel.view.gwt.stackmenu.view;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.CommonUtil;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.panelcommand.IPanelCommand;
import com.javahotel.client.stackmenu.model.IStackMenuModel;
import com.javahotel.client.stackmenu.model.StackButtonElem;
import com.javahotel.client.stackmenu.model.StackButtonHeader;
import com.javahotel.client.stackmenu.view.IStackMenuClicked;
import com.javahotel.client.stackmenu.view.IStackMenuView;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("deprecation")
class StackMenuView implements IStackMenuView {

    private final IStackMenuModel sMode;
    private final IStackMenuClicked iCLicked;
    private final StackPanel stackPanel = new StackPanel();

    private String createHeaderHTML(final String imageUrl, final String caption) {
        String iPath = CommonUtil.getImageHTML(imageUrl);

        return "<table align='left'><tr>" + iPath + "<td style='vertical-align:middle'><b style='white-space:nowrap'>" + caption + "</b></td>" + "</tr></table>";
    }

    private void createMenu() {
        for (StackButtonHeader he : sMode.getHList()) {

            VerticalPanel vp = new VerticalPanel();
            for (final StackButtonElem bu : he.getEList()) {
                ClickListener cli = new ClickListener() {

                    public void onClick(Widget sender) {
                        IPanelCommand c = bu.getBClick();
                        c.beforeDrawAction();
                        iCLicked.ClickedView(c);
                        c.drawAction();
                    }
                };
                Button but = new Button(bu.getBName());
                but.addStyleName("stack-button");
                but.addClickListener(cli);
                vp.add(but);
            }
            stackPanel.add(vp, createHeaderHTML(he.getIName(),
                    he.getHName()), true);
        }
        stackPanel.showStack(0);
    }

    StackMenuView(IStackMenuModel sMode, IStackMenuClicked iClicked) {
        this.sMode = sMode;
        this.iCLicked = iClicked;
        createMenu();
    }

    public IMvcWidget getMWidget() {
        return new DefaultMvcWidget(stackPanel);
    }
}
