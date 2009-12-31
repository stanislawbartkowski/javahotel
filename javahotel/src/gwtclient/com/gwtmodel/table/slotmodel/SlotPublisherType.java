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

import com.gwtmodel.table.injector.GwtGiniInjector;

class SlotPublisherType {

    private final SlotType slType;
    private final SlotRegisterSubscriber slRegisterSubscriber;
    private final SlotSignalContextFactory slContextFactory;

    private SlotPublisherType(SlotType slType,
            SlotRegisterSubscriber slRegisterSubscriber) {
        this.slType = slType;
        this.slRegisterSubscriber = slRegisterSubscriber;
        slContextFactory = GwtGiniInjector.getI().getSlotSignalContextFactory();
    }

    private SlotType getSlType() {
        return slType;
    }

    private SlotRegisterSubscriber getSlRegisterSubscriber() {
        return slRegisterSubscriber;
    }
    
    private void signal(ISlotSignalContext slContextP) {
        ISlotSignalContext slContext = slContextFactory.construct(slType, slContextP); 
        slRegisterSubscriber.signal(slContext);
    }

}
