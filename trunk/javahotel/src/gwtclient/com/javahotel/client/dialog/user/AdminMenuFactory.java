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
import com.javahotel.client.panelcommand.EPanelCommand;
import com.javahotel.client.panelcommand.StackHeaderAddList;
import com.javahotel.client.stackmenu.model.StackButtonElem;
import com.javahotel.client.stackmenu.model.StackButtonHeader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class AdminMenuFactory {

    private AdminMenuFactory() {
    }

    static List<StackButtonHeader> getAList(IResLocator rI) {

        List<StackButtonHeader> hList = new ArrayList<StackButtonHeader>();
        EPanelCommand[] et = {EPanelCommand.ROOMSADMIN};
        List<StackButtonElem> aList = StackHeaderAddList.CreateStackButtonList(rI, et);
        hList.add(new StackButtonHeader("Admin", "people.gif", aList));

        EPanelCommand[] et1 = {EPanelCommand.BOOKINGPANEL,
            EPanelCommand.BOOKING, EPanelCommand.PREPAID};
        aList = StackHeaderAddList.CreateStackButtonList(rI, et1);
        hList.add(new StackButtonHeader("Rezerwacja", "reports.gif", aList));

        return hList;
    }
}
