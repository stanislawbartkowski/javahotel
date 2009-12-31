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
package com.gwtmodel.table.composecontroller;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.injector.TablesFactories;
import com.gwtmodel.table.panelview.IPanelView;
import com.gwtmodel.table.panelview.PanelViewFactory;
import com.gwtmodel.table.slotmediator.ISlotMediator;
import com.gwtmodel.table.slotmediator.SlotMediatorFactory;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;

class ComposeController extends AbstractSlotContainer implements IComposeController {
    
    private final ISlotMediator slMediator;
    private final List<ComposeControllerType> cList = new ArrayList<ComposeControllerType>();
    private final IPanelView pView;
    
    ComposeController(TablesFactories tFactories) {
      slMediator = SlotMediatorFactory.construct();
      PanelViewFactory pViewFactory = tFactories.getpViewFactory();
      pView = pViewFactory.construct(0);
    }

    public void registerController(ComposeControllerType cType) {
        cList.add(cType);        
    }
    
    private class SendWidget implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {
            publish(slContext);
        }
        
    }

    public void startPublish(int cellId) {
        for (ComposeControllerType c : cList ) {
            int cId = -1;
            if (c.isPanelElem()) {
                cId = pView.addCellPanel(c.getRow(),c.getCell());
            }
            slMediator.registerSlotContainer(cId,c.getiSlot());            
        }
        pView.getSlContainer().registerSubscriber(cellId, new SendWidget());
        pView.startPublish(cellId);
        slMediator.startPublish(-1);
    }

}
