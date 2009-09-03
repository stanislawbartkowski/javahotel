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

import com.javahotel.client.IResLocator;
import com.javahotel.client.panelcommand.EPanelCommand;
import com.javahotel.client.panelcommand.IPanelCommand;
import com.javahotel.client.panelcommand.PanelCommandFactory;
import com.javahotel.client.stackmenu.model.StackButtonElem;
import com.javahotel.client.stackmenu.model.StackButtonHeader;
import java.util.ArrayList;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("deprecation")
class AdminMenuFactory {

    private AdminMenuFactory() {
    }

    private static void addElem(IResLocator rI, ArrayList<StackButtonElem> v, EPanelCommand e) {
        String label = PanelCommandFactory.getPanelCommandLabel(rI, e);
        IPanelCommand i = PanelCommandFactory.getPanelCommand(rI, e);
        v.add(new StackButtonElem(label, i));

    }

    static ArrayList<StackButtonHeader> getAList(IResLocator rI) {

        ArrayList<StackButtonHeader> hList = new ArrayList<StackButtonHeader>();
        ArrayList<StackButtonElem> aList = new ArrayList<StackButtonElem>();
        addElem(rI, aList, EPanelCommand.ROOMSADMIN);
        hList.add(new StackButtonHeader("Admin", "people.gif", aList));

        aList = new ArrayList<StackButtonElem>();
        addElem(rI, aList, EPanelCommand.BOOKINGPANEL);
        addElem(rI, aList, EPanelCommand.BOOKING);
        addElem(rI, aList, EPanelCommand.PREPAID);
        hList.add(new StackButtonHeader("Rezerwacja", "reports.gif", aList));

        return hList;
    }
}
