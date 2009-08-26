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
package com.javahotel.client.dialog.user;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.IGwtWidget;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.dialog.tabpanel.IDrawTabPanel;
import com.javahotel.client.dialog.tabpanel.TabPanelElem;
import com.javahotel.common.command.DictType;
import java.util.ArrayList;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class PokojePanel implements IGwtWidget  {

    public IMvcWidget getMWidget() {
        return tPanel.getMWidget();
    }

    private final IDrawTabPanel tPanel;

    private final ArrayList<TabPanelElem> pList = new ArrayList<TabPanelElem>();
    private final IResLocator rI;

    private void initP() {
        pList.add(new TabPanelElem("Standard", DictType.RoomStandard));
        pList.add(new TabPanelElem("Wyposażenie", DictType.RoomFacility));
        pList.add(new TabPanelElem("Pokoje", DictType.RoomObjects));
        pList.add(new TabPanelElem("Vat", DictType.VatDict));
        pList.add(new TabPanelElem("Lista usług", DictType.ServiceDict));
        pList.add(new TabPanelElem("Sezony", DictType.OffSeasonDict));
        pList.add(new TabPanelElem("Cenniki", DictType.PriceListDict));
        pList.add(new TabPanelElem("Klienci", DictType.CustomerList));
    }
    PokojePanel(final IResLocator rI) {
        this.rI = rI;
        initP();
        tPanel = rI.getView().getTabPanel(rI, pList);
    }
}
