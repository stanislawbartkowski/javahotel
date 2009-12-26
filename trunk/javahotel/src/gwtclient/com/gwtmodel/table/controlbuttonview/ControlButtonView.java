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
package com.gwtmodel.table.controlbuttonview;

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.SlotPublisherType;
import com.gwtmodel.table.slotmodel.SlotSignalContext;
import com.gwtmodel.table.slotmodel.SlotType;
import com.gwtmodel.table.slotmodel.SlotTypeFactory;
import com.gwtmodel.table.view.controlpanel.ContrButtonViewFactory;
import com.gwtmodel.table.view.controlpanel.IContrButtonView;
import com.gwtmodel.table.view.controlpanel.IControlClick;

class ControlButtonView extends AbstractSlotContainer implements IControlButtonView {
    
    private final IContrButtonView vButton;
    
    private class Click implements IControlClick {

        public void click(ControlButtonDesc co, Widget w) { 
            SlotType slType = SlotTypeFactory.contructClickButton(co.getActionId());
            SlotPublisherType slPublisher = slContainer.findPublisher(slType);
            SlotSignalContext.signal(slPublisher);
        }        
    }
    
    ControlButtonView(ListOfControlDesc listButton, int cellId) {
        vButton = ContrButtonViewFactory.getView(listButton, new Click());
        // create publishers
        for (ControlButtonDesc bu : listButton.getcList()) {
            SlotType slType = SlotTypeFactory.contructClickButton(bu.getActionId());
            slContainer.addPublisher(slType);
        }
        createCallBackWidget(cellId);        
    }

    public void startPublish() {
        publishCallBack(vButton);
    }

}
