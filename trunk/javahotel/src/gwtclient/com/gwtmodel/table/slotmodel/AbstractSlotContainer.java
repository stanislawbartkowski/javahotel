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

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
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

    protected void createCallBackWidget(int cellId) {
        slCallBackWidget = slContainer.addPublisher(slTypeFactory
                .constructCallBackWidget(cellId));
    }

    protected void publishCallBack(IGWidget gwtWidget) {
        slSignalContext.signal(slCallBackWidget, gwtWidget);
    }

    protected void publish(SlotPublisherType slPublisher) {
        slSignalContext.signal(slPublisher);
    }

    protected void publish(SlotPublisherType slPublisher, IValidateError vError) {
        slSignalContext.signal(slPublisher, vError);
    }

    protected void addSubscriber(SlotType slType, ISlotSignaller slSignaller) {
        slContainer.addSubscriber(slType, slSignaller);
    }

    protected void addSubscriber(ClickButtonType.StandClickEnum eClick,
            ISlotSignaller slSignaller) {
        SlotType slType = slTypeFactory.constructClickButton(eClick);
        addSubscriber(slType, slSignaller);
    }

    protected void addCallerList(ListEventEnum listEvEnum, IDataType dType,
            ISlotCaller slCaller) {
        SlotType slType = slTypeFactory.construct(listEvEnum, dType);
        slContainer.addCaller(slType, slCaller);
    }

    protected ISlotSignalContext callGetterList(IDataType dType) {
        SlotType slType = slTypeFactory.construct(ListEventEnum.GetListData,
                dType);
        ISlotSignalContext slContext = slContextFactory.construct(slType);
        return slContainer.call(slContext);
    }

    protected ISlotSignalContext callGetterModelData(IDataType dType) {
        SlotType slType = slTypeFactory.construct(GetActionEnum.ModelVData,
                dType);
        ISlotSignalContext slContext = slContextFactory.construct(slType);
        return slContainer.call(slContext);
    }

    protected SlotPublisherType addPublisher(SlotType slType) {
        return slContainer.addPublisher(slType);
    }

    protected ISlotSignalContext contextReplace(SlotType slType,
            ISlotSignalContext iSlot) {
        return slContextFactory.construct(slType, iSlot);
    }

    protected SlotPublisherType addPublisher(ValidateActionEnum vENum,
            IDataType dType) {
        SlotType slType = slTypeFactory.construct(
                ValidateActionEnum.ValidationPassed, dType);
        return addPublisher(slType);
    }

}
