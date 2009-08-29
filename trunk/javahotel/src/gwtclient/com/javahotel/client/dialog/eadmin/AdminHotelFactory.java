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
package com.javahotel.client.dialog.eadmin;

import java.util.ArrayList;

import com.javahotel.client.IResLocator;
import com.javahotel.client.panelcommand.EPanelCommand;
import com.javahotel.client.panelcommand.PanelCommandFactory;
import com.javahotel.client.stackmenu.model.StackButtonElem;
import com.javahotel.client.stackmenu.model.StackButtonHeader;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class AdminHotelFactory {

    static ArrayList<StackButtonHeader> getAList(IResLocator rI) {
        ArrayList<StackButtonHeader> hList = new ArrayList<StackButtonHeader>();
        ArrayList<StackButtonElem> aList = new ArrayList<StackButtonElem>();
        EPanelCommand eList[] = {EPanelCommand.PERSON, EPanelCommand.HOTEL, EPanelCommand.REMOVEDATA};
        for (int i = 0; i < eList.length; i++) {
            aList.add(new StackButtonElem(PanelCommandFactory.getPanelCommandLabel(rI, eList[i]),
                    PanelCommandFactory.getPanelCommand(rI, eList[i])));
        }
        hList.add(new StackButtonHeader("Admin", "people.gif", aList));
        return hList;

    }
}
