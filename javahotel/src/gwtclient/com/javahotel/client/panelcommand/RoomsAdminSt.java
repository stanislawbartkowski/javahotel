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
package com.javahotel.client.panelcommand;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.stackmenu.model.IStackMenuModel;
import com.javahotel.client.stackmenu.model.StackButtonElem;
import com.javahotel.client.stackmenu.model.StackButtonHeader;
import com.javahotel.client.stackmenu.model.StackMenuModelFactory;
import com.javahotel.client.stackmenu.view.IStackMenuClicked;
import com.javahotel.client.stackmenu.view.IStackMenuView;
import com.javahotel.client.stackmenu.view.StackMenuViewFactory;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class RoomsAdminSt implements IPanelCommand {

    private IStackMenuView iView;
    private final IResLocator rI;

    RoomsAdminSt(IResLocator rI) {
        this.rI = rI;
    }

    public IMvcWidget getWestWidget() {
        EPanelCommand[] et = {
            EPanelCommand.STANDARD, EPanelCommand.FACILITY, EPanelCommand.ROOMS,
            EPanelCommand.VAT, EPanelCommand.SERVICES, EPanelCommand.SEASON,
            EPanelCommand.PRICES, EPanelCommand.CUSTOMERS};
        List<StackButtonHeader> hList = new ArrayList<StackButtonHeader>();
        List<StackButtonElem> aList = StackHeaderAddList.CreateStackButtonList(rI, et);
        hList.add(new StackButtonHeader("Admin", "people.gif", aList));

        IStackMenuModel iModel = StackMenuModelFactory.getModel(hList);

        IStackMenuClicked iClicked = new IStackMenuClicked() {

            public void ClickedView(IPanelCommand clicked) {
                CommandDrawPanel.setC(rI, clicked, false);
            }
        };

        iView = StackMenuViewFactory.getStackView(rI, iModel, iClicked);
        return iView.getMWidget();
    }

    public void beforeDrawAction(ISetGwtWidget iSet) {
        iSet.setGwtWidget(null);
    }

    public void drawAction() {
    }
}
