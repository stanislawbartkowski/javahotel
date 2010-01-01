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
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.injector.GwtGiniInjector;

abstract public class AbstractSlotContainer implements ISlotable {

    protected final SlotListContainer slContainer;
    protected final SlotTypeFactory slTypeFactory;
    private final SlotSignalContextFactory slContextFactory;

    protected AbstractSlotContainer() {
        slContainer = GwtGiniInjector.getI().getSlotListContainer();
        slTypeFactory = GwtGiniInjector.getI().getSlotTypeFactory();
        slContextFactory = GwtGiniInjector.getI().getSlotSignalContextFactory();
    }

    public SlotListContainer getSlContainer() {
        return slContainer;
    }

    protected void publish(ISlotSignalContext slContext) {
        slContainer.publish(slContext);
    }

    protected void publish(int cellId, IGWidget gwtWidget) {
        slContainer.publish(cellId, gwtWidget);
    }

    protected void publish(DataActionEnum dataActionEnum, IDataType dType,
            PersistTypeEnum persistTypeEnum) {
        slContainer.publish(dataActionEnum, dType, persistTypeEnum);
    }

    protected void publish(ClickButtonType bType, IGWidget gwtWidget) {
        slContainer.publish(bType, gwtWidget);
    }

    protected void publish(DataActionEnum dataActionEnum, IDataType dType,
            InvalidateFormContainer errContainer) {
        slContainer.publish(dataActionEnum, dType, errContainer);
    }

    protected void publish(DataActionEnum dataActionEnum, IDataType dType) {
        slContainer.publish(dataActionEnum, dType);
    }

    protected void publish(DataActionEnum dataActionEnum, IDataType dType,
            DataListType dataList) {
        slContainer.publish(dataActionEnum, dType, dataList);
    }

    protected void publish(DataActionEnum dataActionEnum, IDataType dType,
            IVModelData vData) {
        slContainer.publish(dataActionEnum, dType, vData);
    }

    protected void registerSubscriber(SlotType slType,
            ISlotSignaller slSignaller) {
        slContainer.registerSubscriber(slType, slSignaller);
    }

    protected void registerSubscriber(ClickButtonType.StandClickEnum eClick,
            ISlotSignaller slSignaller) {
        slContainer.registerSubscriber(eClick, slSignaller);
    }

    protected void registerSubscriber(DataActionEnum dataActionEnum,
            IDataType dType, ISlotSignaller slSignaller) {
        slContainer.registerSubscriber(dataActionEnum, dType, slSignaller);
    }

    protected void registerSubscriber(int cellId, ISlotSignaller slSignaller) {
        slContainer.registerSubscriber(cellId, slSignaller);
    }

    protected void registerCaller(GetActionEnum gEnum, IDataType dType,
            ISlotCaller slCaller) {
        slContainer.registerCaller(gEnum, dType, slCaller);
    }

    protected ISlotSignalContext construct(GetActionEnum getActionEnum,
            IDataType dType, IVModelData vData, WSize wSize) {
        return slContainer.setGetter(getActionEnum, dType, vData, wSize);
    }

    protected ISlotSignalContext contextReplace(SlotType slType,
            ISlotSignalContext iSlot) {
        return slContextFactory.construct(slType, iSlot);
    }

    public ISlotSignalContext getGetterContext(GetActionEnum getActionEnum,
            IDataType dType) {
        return slContainer.getGetterContext(getActionEnum, dType);
    }

    public IVModelData getGetterIVModelData(GetActionEnum getActionEnum,
            IDataType dType, IVModelData mData) {
        return slContainer.getGetterIVModelData(getActionEnum, dType, mData);
    }

}
