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
package com.javahotel.view.gwt;

import java.util.List;

import com.gwtmodel.table.ICommand;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.view.webpanel.IWebPanel;
import com.javahotel.client.IResLocator;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.mvc.checkmodel.ICheckDictModel;
import com.javahotel.client.mvc.record.view.IRecordViewFactory;
import com.javahotel.client.mvc.table.view.IGetTableViewFactory;
import com.javahotel.client.panelcommand.EPanelCommand;
import com.javahotel.client.stackmenu.model.IStackMenuModel;
import com.javahotel.client.stackmenu.view.IStackMenuClicked;
import com.javahotel.client.stackmenu.view.IStackMenuView;
import com.javahotel.common.command.DictType;
import com.javahotel.view.IDrawTabPanel;
import com.javahotel.view.IViewInterface;
import com.javahotel.view.gwt.checkmodel.view.CheckDictModelFactory;
import com.javahotel.view.gwt.record.view.GwtRecordViewFactory;
import com.javahotel.view.gwt.stackmenu.view.StackMenuViewFactory;
import com.javahotel.view.gwt.tabpanel.GwtTabPanelFactory;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class GwtGetViewFactory {

    private GwtGetViewFactory() {
    }

    private static class Vie implements IViewInterface {
        

        public IStackMenuView getStackView(IStackMenuModel sMode,
                IStackMenuClicked iClicked) {
            return StackMenuViewFactory.getStackView(sMode, iClicked);
        }

        public IWebPanel getPanel(IResLocator rI, ICommand logOut) {
            return GwtGiniInjector.getI().getWebPanel();
        }

        public ICheckDictModel getModel(IResLocator rI, DictType d) {
            return CheckDictModelFactory.getModel(rI, d);
        }

        public IDrawTabPanel getTabPanel(IResLocator rI,
                List<EPanelCommand> pList) {
            return GwtTabPanelFactory.getPanel(rI, pList);
        }

        public IRecordViewFactory getViewFactory(IResLocator rI) {
            return GwtRecordViewFactory.getFa();
        }

        public IGetTableViewFactory getTableViewFactory(IResLocator rI) {
            return HInjector.getI().getViewTableFactory();
        }

    }

    public static IViewInterface getView() {

        return new Vie();

    }
}
