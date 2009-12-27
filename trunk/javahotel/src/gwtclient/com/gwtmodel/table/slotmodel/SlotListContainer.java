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

import com.google.inject.Inject;
import com.gwtmodel.table.IDataType;

public class SlotListContainer {

    private final List<SlotPublisherType> listOfPublishers;
    private final List<SlotSubscriberType> listOfSubscribers;
    private final List<SlotCallerType> listOfCallers;
    private final List<SlotRedirector> listOfRedirectors;
    private ISlotCaller slCaller;
    private final SlotTypeFactory slTypeFactory;
    private final SlotSignalContext slSignalContext;

    @Inject
    public SlotListContainer(SlotTypeFactory slTypeFactory,
            SlotSignalContext slSignalContext) {
        this.slTypeFactory = slTypeFactory;
        this.slSignalContext = slSignalContext;
        listOfPublishers = new ArrayList<SlotPublisherType>();
        listOfSubscribers = new ArrayList<SlotSubscriberType>();
        listOfCallers = new ArrayList<SlotCallerType>();
        listOfRedirectors = new ArrayList<SlotRedirector>();
    }

    public List<SlotRedirector> getListOfRedirectors() {
        return listOfRedirectors;
    }

    public ISlotSignalContext call(ISlotSignalContext slContext) {
        if (slCaller == null) {
            return null;
        }
        return slCaller.call(slContext);
    }

    public void registerSlReceiver(ISlotCaller slCaller) {
        this.slCaller = slCaller;
    }

    public List<SlotCallerType> getListOfCallers() {
        return listOfCallers;
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

    public SlotCallerType findCaller(SlotType slType) {
        for (SlotCallerType slo : listOfCallers) {
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
        registerSubscriber(slTypeFactory.constructCallBackWidget(cellId),
                slSignaller);
    }

    public void addCaller(SlotType slType, ISlotCaller slCaller) {
        listOfCallers.add(new SlotCallerType(slType, slCaller));
    }

    public ISlotSignalContext callGetterList(IDataType dType) {
        SlotType slType = slTypeFactory.construct(ListEventEnum.GetListData,
                dType);
        SlotCallerType slo = findCaller(slType);
        return slSignalContext.callgetter(slo);
    }

    public void addRedirector(SlotType from, SlotType to) {
        listOfRedirectors.add(new SlotRedirector(from, to));
    }

}
