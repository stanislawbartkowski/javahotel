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
package com.javahotel.nmvc.common;

import com.javahotel.client.dialog.IGwtWidget;
import com.javahotel.nmvc.slotmodel.SlotListContainer;
import com.javahotel.nmvc.slotmodel.SlotPublisherType;
import com.javahotel.nmvc.slotmodel.SlotSignalContext;
import com.javahotel.nmvc.slotmodel.SlotTypeFactory;

abstract public class AbstractSlotContainer implements ISlotable {

    protected final SlotListContainer slContainer;
    protected SlotPublisherType slCallBackWidget;

    protected AbstractSlotContainer() {
        slContainer = new SlotListContainer();
    }

    public SlotListContainer getSlContainer() {
        return slContainer;
    }

    protected void createCallBackWidget(int cellId) {
        slCallBackWidget = slContainer.addPublisher(SlotTypeFactory
                .constructCallBackWidget(cellId));
    }

    protected void publishCallBack(IGwtWidget gwtWidget) {
        SlotSignalContext.signal(slCallBackWidget, gwtWidget);
    }

}
