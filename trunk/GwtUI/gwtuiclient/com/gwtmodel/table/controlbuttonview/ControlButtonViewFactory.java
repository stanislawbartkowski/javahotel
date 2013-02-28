/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.controlbuttonview;

import javax.inject.Inject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.login.menudef.MenuPullContainer;
import com.gwtmodel.table.view.controlpanel.ContrButtonViewFactory;
import com.gwtmodel.table.view.pullmenu.PullMenuFactory;

public class ControlButtonViewFactory {

    private final ContrButtonViewFactory vFactory;
    private final PullMenuFactory menuFactory;

    @Inject
    public ControlButtonViewFactory(ContrButtonViewFactory vFactory, PullMenuFactory menuFactory) {
        this.vFactory = vFactory;
        this.menuFactory = menuFactory;
    }

    public IControlButtonView construct(IDataType dType,
            ListOfControlDesc listButton) {
        return new ControlButtonView(vFactory, listButton, dType, true);
    }

    public IControlButtonView construct(IDataType dType, MenuPullContainer menu) {
        return new ControlButtonView(menuFactory, menu, dType);
    }
}
