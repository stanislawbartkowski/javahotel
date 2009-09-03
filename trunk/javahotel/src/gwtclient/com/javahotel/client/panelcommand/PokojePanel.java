/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
import com.javahotel.client.dialog.IGwtWidget;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.view.IDrawTabPanel;
import java.util.ArrayList;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class PokojePanel implements IGwtWidget {

    public IMvcWidget getMWidget() {
        return tPanel.getMWidget();
    }
    private final IDrawTabPanel tPanel;

    PokojePanel(final IResLocator rI) {
        ArrayList<EPanelCommand> aList = new ArrayList<EPanelCommand>();
        aList.add(EPanelCommand.STANDARD);
        aList.add(EPanelCommand.FACILITY);
        aList.add(EPanelCommand.ROOMS);
        aList.add(EPanelCommand.VAT);
        aList.add(EPanelCommand.SERVICES);
        aList.add(EPanelCommand.SEASON);
        aList.add(EPanelCommand.PRICES);
        aList.add(EPanelCommand.CUSTOMERS);
        tPanel = rI.getView().getTabPanel(rI, aList);
    }
}
