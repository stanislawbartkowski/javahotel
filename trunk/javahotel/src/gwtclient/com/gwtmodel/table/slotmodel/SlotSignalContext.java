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
package com.gwtmodel.table.slotmodel;

import com.google.inject.Inject;
import com.gwtmodel.table.DataListType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.WChoosedLine;

public class SlotSignalContext {

    private final SlotSignalContextFactory slContextFactory;

    @Inject
    public SlotSignalContext(SlotSignalContextFactory slContextFactory) {
        this.slContextFactory = slContextFactory;
    }

    public void signal(SlotPublisherType slPublisher, DataListType dataList) {
        ISlotSignalContext slContext = slContextFactory.construct(slPublisher
                .getSlType(), dataList);
        slPublisher.getSlRegisterSubscriber().signal(slContext);
    }

    public void signal(SlotPublisherType slPublisher, IGWidget gwtWidget) {
        ISlotSignalContext slContext = slContextFactory.construct(slPublisher
                .getSlType(), gwtWidget);
        slPublisher.getSlRegisterSubscriber().signal(slContext);
    }

    public void signal(SlotPublisherType slPublisher) {
        ISlotSignalContext slContext = slContextFactory.construct(slPublisher
                .getSlType());
        slPublisher.getSlRegisterSubscriber().signal(slContext);
    }

    public void signal(SlotSubscriberType slPublisher) {
        ISlotSignalContext slContext = slContextFactory.construct(slPublisher
                .getSlType());
        slPublisher.getSlSignaller().signal(slContext);
    }

    public ISlotSignalContext returngetter(ISlotSignalContext slContext,
            DataListType dataList, WChoosedLine choosedLine) {
        ISlotSignalContext sl = slContextFactory.construct(slContext
                .getSlType(), dataList, choosedLine);
        return sl;
    }
    
    public ISlotSignalContext returngetter(ISlotSignalContext slContext,
            IVModelData vData) {
        ISlotSignalContext sl = slContextFactory.construct(slContext
                .getSlType(), vData);
        return sl;
    }

    public ISlotSignalContext callgetter(SlotCallerType slCaller) {
        ISlotSignalContext sl = slContextFactory
                .construct(slCaller.getSlType());
        return slCaller.getSlCaller().call(sl);
    }
}
