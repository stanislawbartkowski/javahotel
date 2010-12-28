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
package com.javahotel.view;

import java.util.List;

import com.gwtmodel.table.ICommand;
import com.gwtmodel.table.view.webpanel.IWebPanel;
import com.javahotel.client.IResLocator;
import com.javahotel.client.mvc.checkmodel.ICheckDictModel;
import com.javahotel.client.mvc.record.view.IRecordViewFactory;
import com.javahotel.client.mvc.table.view.IGetTableViewFactory;
import com.javahotel.client.panelcommand.EPanelCommand;
import com.javahotel.client.stackmenu.model.IStackMenuModel;
import com.javahotel.client.stackmenu.view.IStackMenuClicked;
import com.javahotel.client.stackmenu.view.IStackMenuView;
import com.javahotel.common.command.DictType;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public interface IViewInterface {

    int GWT = 0;
    int EXT = 1;

    IStackMenuView getStackView(IStackMenuModel sMode,
            IStackMenuClicked iClicked);

    IWebPanel getPanel(IResLocator rI, ICommand logOut);

    ICheckDictModel getModel(IResLocator rI, DictType d);

//    List<RecordField> getDef(IResLocator rI, DictData da);

//    MvcWindowSize getSize(DictData da);

    IDrawTabPanel getTabPanel(IResLocator rI, List<EPanelCommand> pList);

    IRecordViewFactory getViewFactory(IResLocator rI);

    IGetTableViewFactory getTableViewFactory(IResLocator rI);
}
