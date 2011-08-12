/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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

import com.gwtmodel.table.ICustomObject;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IOkModelData;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.view.table.VListHeaderContainer;

abstract public class AbstractSlotContainer implements ISlotable {

    protected SlotListContainer slContainer;
    protected final SlotTypeFactory slTypeFactory;
    protected IDataType dType = null;

    protected AbstractSlotContainer() {
        slContainer = GwtGiniInjector.getI().getSlotListContainer();
        slTypeFactory = GwtGiniInjector.getI().getSlotTypeFactory();
    }

    @Override
    public SlotListContainer getSlContainer() {
        return slContainer;
    }

    @Override
    public void replaceSlContainer(SlotListContainer sl) {
        slContainer = sl;
    }

    protected void publish(ISlotSignalContext slContext) {
        slContainer.publish(slContext);
    }

    protected void publish(IDataType dType, int cellId, IGWidget gwtWidget) {
        slContainer.publish(dType, cellId, gwtWidget);
    }

    protected void publish(IDataType dType, CellId cellId, IGWidget gwtWidget) {
        slContainer.publish(dType, cellId, gwtWidget);
    }

    protected void publish(IDataType dType, DataActionEnum dataActionEnum,
            PersistTypeEnum persistTypeEnum) {
        slContainer.publish(dType, dataActionEnum, persistTypeEnum);
    }

    public void publish(IDataType dType, DataActionEnum dataActionEnum,
            IVModelData vData, PersistTypeEnum persistTypeEnum) {
        slContainer.publish(dType, dataActionEnum, vData, persistTypeEnum);
    }

    protected void publish(ClickButtonType bType, IGWidget gwtWidget) {
        slContainer.publish(bType, gwtWidget);
    }

    protected void publish(String stringButton, IGWidget gwtWidget) {
        slContainer.publish(stringButton, gwtWidget);
    }

    protected void publish(IDataType dType, DataActionEnum dataActionEnum,
            InvalidateFormContainer errContainer) {
        slContainer.publish(dType, dataActionEnum, errContainer);
    }

    protected void publish(IDataType dType, DataActionEnum dataActionEnum) {
        slContainer.publish(dType, dataActionEnum);
    }

    protected void publish(IDataType dType, DataActionEnum dataActionEnum,
            IDataListType dataList) {
        slContainer.publish(dType, dataActionEnum, dataList);
    }

    public void publish(IDataType dType, DataActionEnum dataActionEnum,
            WSize wSize) {
        slContainer.publish(dType, dataActionEnum, wSize);
    }

    public void publish(ISlotCustom is, ICustomObject customO) {
        slContainer.publish(is, customO);
    }

    protected void publish(IDataType dType, DataActionEnum dataActionEnum,
            IVModelData vData) {
        slContainer.publish(dType, dataActionEnum, vData);
    }

    protected void publish(IDataType dType, DataActionEnum dataActionEnum,
            IOkModelData iOkModelData) {
        slContainer.publish(dType, dataActionEnum, iOkModelData);
    }

    protected void publish(IDataType dType, IVField fie, IFormLineView formLine) {
        slContainer.publish(dType, fie, formLine);
    }

    protected void publish(IDataType dType, VListHeaderContainer vHeader) {
        slContainer.publish(dType, vHeader);
    }

    protected void publish(String stringButton) {
        slContainer.publish(stringButton);
    }

    protected void publish(String stringButton, ICustomObject customO) {
        slContainer.publish(stringButton, customO);
    }

    public void publish(IDataType dType, ClickButtonType bType,
            ButtonAction bAction) {
        slContainer.publish(dType, bType, bAction);
    }

    protected void registerSubscriber(SlotType slType,
            ISlotSignaller slSignaller) {
        slContainer.registerSubscriber(slType, slSignaller);
    }

    protected void registerSubscriber(ClickButtonType.StandClickEnum eClick,
            ISlotSignaller slSignaller) {
        slContainer.registerSubscriber(eClick, slSignaller);
    }

    protected void registerSubscriber(IDataType dType, IVField fie,
            ISlotSignaller slSignaller) {
        slContainer.registerSubscriber(dType, fie, slSignaller);
    }

    protected void registerSubscriber(IDataType dType,
            DataActionEnum dataActionEnum, ISlotSignaller slSignaller) {
        slContainer.registerSubscriber(dType, dataActionEnum, slSignaller);
    }

    protected void registerSubscriber(String stringButton,
            ISlotSignaller slSignaller) {
        slContainer.registerSubscriber(stringButton, slSignaller);
    }

    protected void registerSubscriber(IDataType dType, ClickButtonType bType,
            ButtonAction bAction, ISlotSignaller slSignaller) {
        slContainer.registerSubscriber(dType, bType, bAction, slSignaller);
    }

    protected void registerSubscriber(IDataType dType, int cellId,
            ISlotSignaller slSignaller) {
        slContainer.registerSubscriber(dType, cellId, slSignaller);
    }

    protected void registerSubscriber(IDataType dType, CellId cellId,
            ISlotSignaller slSignaller) {
        slContainer.registerSubscriber(dType, cellId, slSignaller);
    }

    protected void registerSubscriber(ISlotCustom i, ISlotSignaller slSignaller) {
        slContainer.registerSubscriber(i, slSignaller);
    }

    protected void registerCaller(IDataType dType, GetActionEnum gEnum,
            ISlotCaller slCaller) {
        slContainer.registerCaller(dType, gEnum, slCaller);
    }

    public void registerCaller(SlotType slType, ISlotCaller slCaller) {
        slContainer.registerCaller(slType, slCaller);
    }

    public void registerCaller(ISlotCustom i, ISlotCaller slCaller) {
        slContainer.registerCaller(i, slCaller);
    }

    protected ISlotSignalContext construct(IDataType dType,
            GetActionEnum getActionEnum, IVModelData vData, WSize wSize,
            IVField v) {
        return slContainer.setGetter(dType, getActionEnum, vData, wSize, v);
    }

    protected ISlotSignalContext construct(IDataType dType, IVField comboFie) {
        return slContainer.setGetter(dType, comboFie);
    }

    protected ISlotSignalContext construct(SlotType slType, IFormLineView v) {
        return slContainer.setGetter(slType, v);
    }

    protected ISlotSignalContext construct(IDataType dType,
            FormLineContainer lContainer) {
        return slContainer.getGetterContext(dType, lContainer);
    }

    protected ISlotSignalContext contextReplace(SlotType slType,
            ISlotSignalContext iSlot) {
        return slContainer.contextReplace(slType, iSlot);
    }

    protected ISlotSignalContext getGetterContext(IDataType dType,
            GetActionEnum getActionEnum) {
        return slContainer.getGetterContext(dType, getActionEnum);
    }

    protected IVModelData getGetterIVModelData(IDataType dType,
            GetActionEnum getActionEnum, IVModelData mData) {
        return slContainer.getGetterIVModelData(dType, getActionEnum, mData);
    }
    
    protected IVModelData getGetterIVModelData(IDataType dType,
            GetActionEnum getActionEnum) {
        return slContainer.getGetterIVModelData(dType, getActionEnum);
    }

    protected IGWidget getHtmlWidget(CellId c) {
        return slContainer.getHtmlWidget(c);
    }

    protected FormLineContainer getGetterContainer(IDataType dType) {
        return slContainer.getGetterContainer(dType);
    }

    protected void publish(IDataType dType, DataActionEnum dataActionEnum,
            ISlotSignalContext slContext) {
        slContainer.publish(dType, dataActionEnum, slContext);
    }

    protected void publish(IDataType dType, ISlotSignalContext slContext) {
        slContainer.publish(dType, slContext);
    }

    @Override
    public void startPublish(CellId cellId) {
    }
}
