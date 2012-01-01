/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
package com.javahotel.nmvc.panel;

import com.google.gwt.user.client.ui.Panel;
import com.gwtmodel.table.IGWidget;
import com.javahotel.client.start.panel.EPanelCommand;
import com.javahotel.client.start.panel.IStackMenuClicked;
import com.javahotel.client.start.panel.IUserPanelMenuFactory;
import com.javahotel.client.start.panel.StackHeaderAddList;

/**
 * @author hotel
 * 
 */
class RoomsPanelFactory implements IUserPanelMenuFactory {

    public IGWidget getMenuPanel(final IStackMenuClicked iClicked) {
        EPanelCommand eList[] = { EPanelCommand.VAT, EPanelCommand.FACILITY,
                EPanelCommand.SERVICES, EPanelCommand.STANDARD,
                EPanelCommand.ROOMS, EPanelCommand.CUSTOMERS,
                EPanelCommand.SEASON, EPanelCommand.PRICES,
                EPanelCommand.INVOICEISSUER };
        return StackHeaderAddList.constructPanel(eList);
    }

    public EPanelCommand getCentreWidget() {
        return null;
    }

    public Panel getMenuPanel() {
        return null;
    }
}
