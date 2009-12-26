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

import java.util.ArrayList;
import java.util.List;

public class SlotListContainer {

    private final List<SlotPublisherType> listOfPublishers;
    private final List<SlotSubscriberType> listOfSubscribers;

    public SlotListContainer() {
        listOfPublishers = new ArrayList<SlotPublisherType>();
        listOfSubscribers = new ArrayList<SlotSubscriberType>();
    }

    public List<SlotPublisherType> getListOfPublishers() {
        return listOfPublishers;
    }

    public List<SlotSubscriberType> getListOfSubscribers() {
        return listOfSubscribers;
    }

    public void addSubscriber(SlotType slType, ISlotSignaller slSignaller) {
        listOfSubscribers.add(new SlotSubscriberType(slType, slSignaller));
    }

    public SlotPublisherType addPublisher(SlotType slType) {
        SlotRegisterSubscriber slRegister = new SlotRegisterSubscriber();
        SlotPublisherType slPublisher = new SlotPublisherType(slType,
                slRegister);
        listOfPublishers.add(slPublisher);
        return slPublisher;
    }

    public SlotPublisherType findPublisher(SlotType slType) {
        for (SlotPublisherType slo : listOfPublishers) {
            if (slType.eq(slo.getSlType())) {
                return slo;
            }
        }
        return null;
    }

    public SlotSubscriberType findSubscriber(SlotType slType) {
        for (SlotSubscriberType slo : listOfSubscribers) {
            if (slType.eq(slo.getSlType())) {
                return slo;
            }
        }
        return null;
    }

    public void registerSubscriber(SlotType slType, ISlotSignaller slSignaller) {
        SlotPublisherType slPublisher = findPublisher(slType);
        slPublisher.getSlRegisterSubscriber().register(slSignaller);
    }

    public void registerSubscriberGwt(int cellId, ISlotSignaller slSignaller) {
        registerSubscriber(SlotTypeFactory.constructCallBackWidget(cellId),
                slSignaller);
    }

}
