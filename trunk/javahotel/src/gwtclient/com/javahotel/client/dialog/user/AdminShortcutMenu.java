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
// TODO: not in use

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.IGwtWidget;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.stackmenu.model.IStackMenuModel;
import com.javahotel.client.stackmenu.model.StackButtonHeader;
import com.javahotel.client.stackmenu.model.StackMenuModelFactory;
import com.javahotel.client.stackmenu.view.IStackMenuClicked;
import com.javahotel.client.stackmenu.view.IStackMenuView;
import com.javahotel.client.stackmenu.view.StackMenuViewFactory;
import java.util.List;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class AdminShortcutMenu implements IGwtWidget {

    private final IStackMenuView iView;

    AdminShortcutMenu(IResLocator rI, IStackMenuClicked iClicked) {
        List<StackButtonHeader> hList = AdminMenuFactory.getAList(rI);
        IStackMenuModel iModel = StackMenuModelFactory.getModel(hList);
        iView = StackMenuViewFactory.getStackView(rI, iModel, iClicked);
    }

    public IMvcWidget getMWidget() {
        return iView.getMWidget();
    }

}
