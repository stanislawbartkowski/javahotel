/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ISlotCaller;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.ISlotable;

class SlotMediator extends AbstractSlotContainer implements ISlotMediator {

    private final List<C> slList = new ArrayList<C>();
//    private final ISlotSignaller slSig = new GeneralListener();
//    private final ISlotCaller slCaller = new GeneralCaller();

    @Override
    public void addSlotContainer(CellId cellId, ISlotable iSlo) {
        slList.add(new C(cellId, iSlo));
    }

    private class C {

        private final CellId cellId;
        private final ISlotable iSlo;
        private final ISlotSignaller listener;
        private final ISlotCaller caller;

        C(CellId cellId, ISlotable iSlo) {
            this.cellId = cellId;
            this.iSlo = iSlo;
            listener = iSlo.getSlContainer().constructListener();
            caller = iSlo.getSlContainer().constructCaller();
        }
    }

//    private class GeneralListener implements ISlotSignaller {
//
//        private final ISlotSignaller liste = slContainer.constructListener();
//
//        @Override
//        public void signal(ISlotSignalContext slContext) {
//            liste.signal(slContext);
//            for (C c : slList) {
//                c.listener.signal(slContext);
//            }
//        }
//    }
//
//    private class GeneralCaller implements ISlotCaller {
//
//        private final ISlotCaller call = slContainer.constructCaller();
//
//        @Override
//        public ISlotSignalContext call(ISlotSignalContext slContext) {
//            ISlotSignalContext i = call.call(slContext);
//            if (i != null) {
//                return i;
//            }
//            for (C c : slList) {
//                i = c.caller.call(slContext);
//                if (i != null) {
//                    return i;
//                }
//            }
//            return null;
//        }
//    }

//    private class GeneralListener implements ISlotSignaller {
//
//        @Override
//        public void signal(ISlotSignalContext slContextP) {
//            SlotType sl = slContextP.getSlType();
//            ISlotSignalContext slContext = slContextP;
//            // find redirector
//            boolean notReplaced = true;
//            while (notReplaced) {
//                notReplaced = false;
//                for (SlotRedirector re : slContainer.getListOfRedirectors()) {
//                    if (re.getFrom().eq(sl)) {
//                        sl = re.getTo();
//                        slContext = contextReplace(sl, slContextP);
//                        notReplaced = true;
//                        break;
//                    }
//                }
//            }
//
//            for (SlotSubscriberType so : slContainer.getListOfSubscribers()) {
//                if (sl.eq(so.getSlType())) {
//                    so.getSlSignaller().signal(slContext);
//                }
//            }
//        }
//    }
//
//    private class GeneralCaller implements ISlotCaller {
//
//        @Override
//        public ISlotSignalContext call(ISlotSignalContext slContext) {
//            SlotCallerType slCaller = slContainer.findCaller(slContext
//                    .getSlType());
//            if (slCaller == null) {
//                return null;
//            }
//            return slCaller.getSlCaller().call(slContext);
//        }
//    }
    @Override
    public void registerSlotContainer(CellId cellId, ISlotable iSlo) {
//        slList.add(new C(cellId, iSlo));
        addSlotContainer(cellId, iSlo);
        this.slContainer.replaceContainer(iSlo);
//        this.slContainer.getListOfSubscribers().addAll(
//                iSlo.getSlContainer().getListOfSubscribers());
//        this.slContainer.getListOfCallers().addAll(
//                iSlo.getSlContainer().getListOfCallers());
//        this.slContainer.getListOfRedirectors().addAll(
//                iSlo.getSlContainer().getListOfRedirectors());
//        iSlo.replaceSlContainer(this.slContainer);
    }

    @Override
    public void startPublish(CellId nullId) {
//        for (C c : slList) {
//            c.iSlo.getSlContainer().registerSlReceiver(slCaller);
//            c.iSlo.getSlContainer().registerSlPublisher(slSig);
//        }
//        this.slContainer.registerSlReceiver(slCaller);
//        this.slContainer.registerSlPublisher(slSig);
        for (C c : slList) {
            c.iSlo.startPublish(c.cellId);
        }
    }

    @Override
    public void registerSlotContainer(ISlotable iSlo) {
        registerSlotContainer(null, iSlo);
    }

    @Override
    public void registerSlotContainer(int cellId, ISlotable iSlo) {
        registerSlotContainer(new CellId(cellId), iSlo);
    }
}
