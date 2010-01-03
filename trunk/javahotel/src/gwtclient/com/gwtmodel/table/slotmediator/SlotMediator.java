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
package com.gwtmodel.table.slotmediator;

import java.util.ArrayList;
import java.util.List;

import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.ISlotCaller;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlotCallerType;
import com.gwtmodel.table.slotmodel.SlotRedirector;
import com.gwtmodel.table.slotmodel.SlotSubscriberType;
import com.gwtmodel.table.slotmodel.SlotType;

class SlotMediator extends AbstractSlotContainer implements ISlotMediator {

    private final List<C> slList = new ArrayList<C>();

    private class C {
        private final int cellId;
        private final ISlotable iSlo;

        C(int cellId, ISlotable iSlo) {
            this.cellId = cellId;
            this.iSlo = iSlo;
        }
    }

    private class GeneralListener implements ISlotSignaller {

        public void signal(ISlotSignalContext slContextP) {
            SlotType sl = slContextP.getSlType();
            ISlotSignalContext slContext = slContextP;
            // find redirector
            boolean notReplaced = true;
            while (notReplaced) {
                notReplaced = false;
                for (SlotRedirector re : slContainer.getListOfRedirectors()) {
                    if (re.getFrom().eq(sl)) {
                        sl = re.getTo();
                        slContext = contextReplace(sl, slContextP);
                        notReplaced = true;
                        break;
                    }
                }
            }
            
            for (SlotSubscriberType so : slContainer.getListOfSubscribers()) {
                if (sl.eq(so.getSlType())) {
                    so.getSlSignaller().signal(slContext);
                }
            }
        }
    }

    private class GeneralCaller implements ISlotCaller {

        public ISlotSignalContext call(ISlotSignalContext slContext) {
            SlotCallerType slCaller = slContainer.findCaller(slContext
                    .getSlType());
            if (slCaller == null) {
                return null;
            }
            return slCaller.getSlCaller().call(slContext);
        }
    }

    public void registerSlotContainer(int cellId, ISlotable iSlo) {
        slList.add(new C(cellId, iSlo));
        this.slContainer.getListOfSubscribers().addAll(
                iSlo.getSlContainer().getListOfSubscribers());
        this.slContainer.getListOfCallers().addAll(
                iSlo.getSlContainer().getListOfCallers());
        this.slContainer.getListOfRedirectors().addAll(
                iSlo.getSlContainer().getListOfRedirectors());
    }

    public void startPublish(int nullId) {
        ISlotSignaller sl = new GeneralListener();
        ISlotCaller slCaller = new GeneralCaller();
        for (C c : slList) {
            c.iSlo.getSlContainer().registerSlReceiver(slCaller);
            c.iSlo.getSlContainer().registerSlPublisher(sl);
        }
        this.slContainer.registerSlReceiver(slCaller);
        this.slContainer.registerSlPublisher(sl);
        for (C c : slList) {
            c.iSlo.startPublish(c.cellId);
        }
    }
    
}
