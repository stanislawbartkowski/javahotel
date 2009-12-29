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

import com.gwtmodel.table.DataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.injector.GwtGiniInjector;

abstract public class AbstractSlotContainer implements ISlotable {

    protected final SlotListContainer slContainer;
    protected SlotPublisherType slCallBackWidget;
    protected final SlotTypeFactory slTypeFactory;
    protected final SlotSignalContext slSignalContext;
    private final SlotSignalContextFactory slContextFactory;

    protected AbstractSlotContainer() {
        slContainer = GwtGiniInjector.getI().getSlotListContainer();
        slTypeFactory = GwtGiniInjector.getI().getSlotTypeFactory();
        slSignalContext = GwtGiniInjector.getI().getSlotSignalContext();
        slContextFactory = GwtGiniInjector.getI().getSlotSignalContextFactory();
    }

    public SlotListContainer getSlContainer() {
        return slContainer;
    }

    protected void registerPublisher(int cellId) {
        slCallBackWidget = slContainer.registerPublisher(cellId);
    }

    protected SlotPublisherType registerPublisher(SlotType slType) {
        return slContainer.registerPublisher(slType);
    }

    protected SlotPublisherType registerPublisher(
            ValidateActionType.ValidateActionEnum vEnum, IDataType dType) {
        return slContainer.registerPublisher(vEnum, dType);
    }

    protected SlotPublisherType registerPublisher(ClickButtonType bType) {
        return slContainer.registerPublisher(bType);
    }

    protected SlotPublisherType registerPublisher(PersistEventEnum pEnum,
            IDataType dType) {
        return slContainer.registerPublisher(pEnum, dType);
    }

    protected SlotPublisherType registerPublisher(
            ValidateActionType.ValidateActionEnum vEnum,
            ValidateActionType.ValidateType vType, IDataType dType) {
        return slContainer.registerPublisher(vEnum, vType, dType);
    }

    protected void findAndPublish(SlotType slType) {
        SlotPublisherType slPublisher = slContainer.findPublisher(slType);
        publish(slPublisher);        
    }
    
    protected void findAndPublish(SlotType slType,DataListType dataList) {
        SlotPublisherType slPublisher = slContainer.findPublisher(slType);
        slContainer.publish(slPublisher,dataList);        
    }
    
    protected void findAndPublish(PersistEventEnum eNum, IDataType dType) {
        findAndPublish(slTypeFactory.construct(eNum,dType));
    }
    
    protected void findAndPublish(PersistEventEnum eNum, IDataType dType, DataListType dataList) {
        findAndPublish(slTypeFactory.construct(eNum,dType),dataList);
    }
    
    protected void publish(IGWidget gwtWidget) {
        slContainer.publish(slCallBackWidget, gwtWidget);
    }

    protected void publish(SlotPublisherType slPublisher) {
        slContainer.publish(slPublisher);
    }

    protected void publish(SlotPublisherType slPublisher, IGWidget gwtWidget) {
        slContainer.publish(slPublisher, gwtWidget);
    }

    protected void publish(SlotPublisherType slPublisher, IValidateError vError) {
        slContainer.publish(slPublisher, vError);
    }

    protected void registerSubscriber(SlotType slType,
            ISlotSignaller slSignaller) {
        slContainer.registerSubscriber(slType, slSignaller);
    }

    protected void registerSubscriber(ClickButtonType.StandClickEnum eClick,
            ISlotSignaller slSignaller) {
        slContainer.registerSubscriber(eClick, slSignaller);
    }

    protected void registerSubscriber(ValidateActionType vEnum,
            IDataType dType, ISlotSignaller slSignaller) {
        slContainer.registerSubscriber(vEnum, dType, slSignaller);
    }

    protected void registerSubscriber(
            ValidateActionType.ValidateActionEnum vEnum,
            ValidateActionType.ValidateType vType, IDataType dType,
            ISlotSignaller slSignaller) {
        slContainer.registerSubscriber(vEnum, vType, dType, slSignaller);
    }

    protected void registerSubscriber(
            ValidateActionType.ValidateActionEnum vEnum, IDataType dType,
            ISlotSignaller slSignaller) {
        slContainer.registerSubscriber(vEnum, dType, slSignaller);
    }

    protected void registerSubscriber(int cellId, ISlotSignaller slSignaller) {
        slContainer.registerSubscriber(cellId, slSignaller);
    }

    protected void registerSubscriber(PersistEventEnum pEnum, IDataType dType,
            ISlotSignaller slSignaller) {
        slContainer.registerSubscriber(pEnum, dType, slSignaller);
    }

    protected void registerCaller(GetActionEnum gEnum, IDataType dType,
            ISlotCaller slCaller) {
        slContainer.registerCaller(gEnum, dType, slCaller);
    }

    protected ISlotSignalContext callGetterList(IDataType dType) {
        return slContainer.callGetterList(dType);
    }

    protected IVModelData callGetterModelData(GetActionEnum eNum,
            IDataType dType) {
        return slContainer.callGetterModelData(eNum, dType);
    }

    protected ISlotSignalContext contextReplace(SlotType slType,
            ISlotSignalContext iSlot) {
        return slContextFactory.construct(slType, iSlot);
    }

}
