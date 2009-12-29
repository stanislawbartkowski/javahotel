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
import com.gwtmodel.table.DataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IVModelData;

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

    public void registerSubscriber(SlotType slType, ISlotSignaller slSignaller) {
        listOfSubscribers.add(new SlotSubscriberType(slType, slSignaller));
    }

    public void registerSubscriber(PersistEventEnum pEnum, IDataType dType,
            ISlotSignaller slSignaller) {
        SlotType slType = slTypeFactory.construct(pEnum, dType);
        registerSubscriber(slType, slSignaller);
    }

    public void registerSubscriber(int cellId, ISlotSignaller slSignaller) {
        registerSubscriber(slTypeFactory.construct(cellId), slSignaller);
    }

    public void registerSubscriber(ClickButtonType.StandClickEnum eClick,
            ISlotSignaller slSignaller) {
        registerSubscriber(slTypeFactory.construct(eClick), slSignaller);
    }

    public void registerSubscriber(ValidateActionType vEnum, IDataType dType,
            ISlotSignaller slSignaller) {
        registerSubscriber(slTypeFactory.construct(vEnum, dType), slSignaller);
    }

    public void registerSubscriber(ValidateActionType.ValidateActionEnum vEnum,
            ValidateActionType.ValidateType vType, IDataType dType,
            ISlotSignaller slSignaller) {
        SlotType slType = slTypeFactory.construct(new ValidateActionType(vEnum,
                vType), dType);
        registerSubscriber(slType, slSignaller);
    }

    public void registerSubscriber(ValidateActionType.ValidateActionEnum vEnum,
            IDataType dType, ISlotSignaller slSignaller) {
        SlotType slType = slTypeFactory.construct(
                new ValidateActionType(vEnum), dType);
        registerSubscriber(slType, slSignaller);
    }

    public SlotPublisherType registerPublisher(SlotType slType) {
        SlotRegisterSubscriber slRegister = new SlotRegisterSubscriber();
        SlotPublisherType slPublisher = new SlotPublisherType(slType,
                slRegister);
        listOfPublishers.add(slPublisher);
        return slPublisher;
    }

    public SlotPublisherType registerPublisher(int cellId) {
        return registerPublisher(slTypeFactory.construct(cellId));
    }

    public SlotPublisherType registerPublisher(ClickButtonType bType) {
        return registerPublisher(slTypeFactory.construct(bType));
    }

    public SlotPublisherType registerPublisher(PersistEventEnum pEnum,
            IDataType dType) {
        return registerPublisher(slTypeFactory.construct(pEnum, dType));
    }

    public SlotPublisherType registerPublisher(
            ValidateActionType.ValidateActionEnum vEnum, IDataType dType) {
        SlotType slType = slTypeFactory.construct(
                new ValidateActionType(vEnum), dType);
        return registerPublisher(slType);
    }

    protected SlotPublisherType registerPublisher(
            ValidateActionType.ValidateActionEnum vEnum,
            ValidateActionType.ValidateType vType, IDataType dType) {
        SlotType slType = slTypeFactory.construct(new ValidateActionType(vEnum,
                vType), dType);
        return registerPublisher(slType);
    }

    public void registerCaller(SlotType slType, ISlotCaller slCaller) {
        listOfCallers.add(new SlotCallerType(slType, slCaller));
    }

    public void registerCaller(GetActionEnum gEnum, IDataType dType,
            ISlotCaller slCaller) {
        SlotType slType = slTypeFactory.construct(gEnum, dType);
        registerCaller(slType, slCaller);
    }

    public void registerRedirector(SlotType from, SlotType to) {
        listOfRedirectors.add(new SlotRedirector(from, to));
    }

    private ISlotSignalContext call(SlotType slType) {
        ISlotSignalContext slContext = slSignalContext.constructContext(slType);
        return call(slContext);
    }

    public ISlotSignalContext callGetterList(IDataType dType) {
        SlotType slType = slTypeFactory.construct(GetActionEnum.GetListData,
                dType);
        return call(slType);
    }

    public IVModelData callGetterModelData(GetActionEnum eNum, IDataType dType) {
        SlotType slType = slTypeFactory.construct(eNum, dType);
        ISlotSignalContext slContext = call(slType);
        return slContext.getVData();
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

    public void publish(SlotPublisherType slPublisher) {
        slSignalContext.signal(slPublisher);
    }

    public void publish(SlotPublisherType slPublisher, IValidateError vError) {
        slSignalContext.signal(slPublisher, vError);
    }

    public void publish(SlotPublisherType slPublisher, IGWidget gwtWidget) {
        slSignalContext.signal(slPublisher, gwtWidget);
    }
    
    public void publish(SlotPublisherType slPublisher, DataListType dataList) {
        slSignalContext.signal(slPublisher, dataList);
    }
    
}
