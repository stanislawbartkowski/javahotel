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
import com.javahotel.client.stackmenu.model.StackButtonElem;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class StackHeaderAddList {

    private StackHeaderAddList() {
    }

    private static void addElem(IResLocator rI, List<StackButtonElem> v,
            EPanelCommand e) {
        String label = PanelCommandFactory.getPanelCommandLabel(rI, e);
        IPanelCommand i = PanelCommandFactory.getPanelCommand(rI, e);
        v.add(new StackButtonElem(label, i));

    }

    public static List<StackButtonElem> CreateStackButtonList(IResLocator rI,
            EPanelCommand[] et) {

        List<StackButtonElem> aList = new ArrayList<StackButtonElem>();
        for (int i = 0; i < et.length; i++) {
            addElem(rI, aList, et[i]);
        }
        return aList;
    }
}
