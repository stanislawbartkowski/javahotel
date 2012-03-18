/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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

import com.gwtmodel.table.*;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.view.table.VListHeaderContainer;

abstract public class AbstractSlotContainer implements ISlotable {

    private SlotContainerReference sReference;
    protected final SlotTypeFactory slTypeFactory;
    protected IDataType dType = null;
    protected final SlotSignalContextFactory slContextFactory;
    protected final ControlButtonFactory cButtonFactory;

    protected AbstractSlotContainer() {
        SlotListContainer slContainer = GwtGiniInjector.getI().getSlotListContainer();
        sReference = new SlotContainerReference(this, slContainer);
        slTypeFactory = GwtGiniInjector.getI().getSlotTypeFactory();
        slContextFactory = GwtGiniInjector.getI().getSlotSignalContextFactory();
        cButtonFactory = GwtGiniInjector.getI().getControlButtonFactory();
    }

    @Override
    public void setSlotContainerReference(SlotContainerReference sReference) {
        this.sReference = sReference;
    }

    @Override
    public SlotContainerReference getSlotContainerReference() {
        return sReference;
    }

    @Override
    public SlotListContainer getSlContainer() {
        return sReference.getSlContainer();
    }

    @Override
    public void setSlContainer(ISlotable sl) {
        sl.getSlContainer().addSlotLists(this);
        sl.getSlotContainerReference().replace(sReference);
    }

    protected void publish(ISlotSignalContext slContext) {
        sReference.getSlContainer().publish(slContext);
    }

    protected void publish(IDataType dType, int cellId, IGWidget gwtWidget) {
        sReference.getSlContainer().publish(dType, cellId, gwtWidget);
    }

    protected void publish(IDataType dType, CellId cellId, IGWidget gwtWidget) {
        sReference.getSlContainer().publish(dType, cellId, gwtWidget);
    }

    protected void publish(IDataType dType, DataActionEnum dataActionEnum,
            PersistTypeEnum persistTypeEnum) {
        sReference.getSlContainer().publish(dType, dataActionEnum,
                persistTypeEnum);
    }

    protected void publish(IDataType dType, DataActionEnum dataActionEnum,
            IVModelData vData, PersistTypeEnum persistTypeEnum) {
        sReference.getSlContainer().publish(dType, dataActionEnum, vData,
                persistTypeEnum);
    }

    protected void publish(ClickButtonType bType, IGWidget gwtWidget) {
        sReference.getSlContainer().publish(dType, bType, gwtWidget);
    }

    protected void publish(String stringButton, IGWidget gwtWidget) {
        sReference.getSlContainer().publish(stringButton, gwtWidget);
    }

    protected void publish(IDataType dType, DataActionEnum dataActionEnum,
            InvalidateFormContainer errContainer) {
        sReference.getSlContainer().publish(dType, dataActionEnum, errContainer);
    }

    protected void publish(IDataType dType, DataActionEnum dataActionEnum) {
        sReference.getSlContainer().publish(dType, dataActionEnum);
    }

    protected void publish(IDataType dType, DataActionEnum dataActionEnum,
            IDataListType dataList) {
        sReference.getSlContainer().publish(dType, dataActionEnum, dataList);
    }

    protected void publish(IDataType dType, DataActionEnum dataActionEnum,
            WSize wSize) {
        sReference.getSlContainer().publish(dType, dataActionEnum, wSize);
    }

    protected void publish(ISlotCustom is, ICustomObject customO) {
        sReference.getSlContainer().publish(is, customO);
    }

    protected void publish(SlotType sl, ICustomObject customO) {
        sReference.getSlContainer().publish(sl, customO);
    }

    protected void publish(IDataType dType, DataActionEnum dataActionEnum,
            IVModelData vData) {
        sReference.getSlContainer().publish(dType, dataActionEnum, vData);
    }

    protected void publish(IDataType dType, DataActionEnum dataActionEnum,
            IOkModelData iOkModelData) {
        sReference.getSlContainer().publish(dType, dataActionEnum, iOkModelData);
    }

    protected void publish(IDataType dType, VListHeaderContainer vHeader) {
        sReference.getSlContainer().publish(dType, vHeader);
    }

    protected void publish(String stringButton) {
        sReference.getSlContainer().publish(stringButton);
    }

    protected void publish(String stringButton, ICustomObject customO) {
        sReference.getSlContainer().publish(stringButton, customO);
    }

    protected void publish(IDataType dType, ClickButtonType bType,
            ButtonAction bAction) {
        sReference.getSlContainer().publish(dType, bType, bAction);
    }

    protected void registerSubscriber(SlotType slType,
            ISlotListener slSignaller) {
        sReference.getSlContainer().registerSubscriber(slType, slSignaller);
    }

    protected void registerSubscriber(IDataType dType, ClickButtonType.StandClickEnum eClick,
            ISlotListener slSignaller) {
        sReference.getSlContainer().registerSubscriber(dType, eClick, slSignaller);
    }

    protected void registerSubscriber(IDataType dType, IVField fie,
            ISlotListener slSignaller) {
        sReference.getSlContainer().registerSubscriber(dType, fie, slSignaller);
    }

    protected void registerSubscriber(IDataType dType,
            DataActionEnum dataActionEnum, ISlotListener slSignaller) {
        sReference.getSlContainer().registerSubscriber(dType, dataActionEnum,
                slSignaller);
    }

    protected void registerSubscriber(String stringButton,
            ISlotListener slSignaller) {
        sReference.getSlContainer().registerSubscriber(stringButton,
                slSignaller);
    }

    protected void registerSubscriber(IDataType dType, ClickButtonType bType,
            ButtonAction bAction, ISlotListener slSignaller) {
        sReference.getSlContainer().registerSubscriber(dType, bType, bAction,
                slSignaller);
    }

    protected void registerSubscriber(IDataType dType, int cellId,
            ISlotListener slSignaller) {
        sReference.getSlContainer().registerSubscriber(dType, cellId,
                slSignaller);
    }

    protected void registerSubscriber(IDataType dType, CellId cellId,
            ISlotListener slSignaller) {
        sReference.getSlContainer().registerSubscriber(dType, cellId,
                slSignaller);
    }

    protected void registerSubscriber(ISlotCustom i, ISlotListener slSignaller) {
        sReference.getSlContainer().registerSubscriber(i, slSignaller);
    }

    protected void registerCaller(IDataType dType, GetActionEnum gEnum,
            ISlotCallerListener slCaller) {
        sReference.getSlContainer().registerCaller(dType, gEnum, slCaller);
    }

    protected void registerCaller(SlotType slType, ISlotCallerListener slCaller) {
        sReference.getSlContainer().registerCaller(slType, slCaller);
    }

    protected void registerCaller(ISlotCustom i, ISlotCallerListener slCaller) {
        sReference.getSlContainer().registerCaller(i, slCaller);
    }

    protected ISlotSignalContext construct(IDataType dType,
            GetActionEnum getActionEnum, IVModelData vData, WSize wSize,
            IVField v) {
        return sReference.getSlContainer().setGetter(dType, getActionEnum,
                vData, wSize, v);
    }

    protected ISlotSignalContext construct(IDataType dType, IVField comboFie) {
        return sReference.getSlContainer().setGetter(dType, comboFie);
    }

    protected ISlotSignalContext construct(SlotType slType, IFormLineView v) {
        return sReference.getSlContainer().setGetter(slType, v);
    }

    protected ISlotSignalContext construct(IDataType dType,
            FormLineContainer lContainer) {
        return sReference.getSlContainer().getGetterContext(dType, lContainer);
    }

    protected ISlotSignalContext contextReplace(SlotType slType,
            ISlotSignalContext iSlot) {
        return sReference.getSlContainer().contextReplace(slType, iSlot);
    }

    protected ISlotSignalContext getGetterContext(IDataType dType,
            GetActionEnum getActionEnum) {
        return sReference.getSlContainer().getGetterContext(dType,
                getActionEnum);
    }

    protected IVModelData getGetterIVModelData(IDataType dType,
            GetActionEnum getActionEnum, IVModelData mData) {
        return sReference.getSlContainer().getGetterIVModelData(dType,
                getActionEnum, mData);
    }

    protected IVModelData getGetterIVModelData(IDataType dType,
            GetActionEnum getActionEnum) {
        return sReference.getSlContainer().getGetterIVModelData(dType,
                getActionEnum);
    }

    protected IGWidget getHtmlWidget(CellId c) {
        return sReference.getSlContainer().getHtmlWidget(c);
    }

    protected FormLineContainer getGetterContainer(IDataType dType) {
        return sReference.getSlContainer().getGetterContainer(dType);
    }

    protected void publish(IDataType dType, DataActionEnum dataActionEnum,
            ISlotSignalContext slContext) {
        sReference.getSlContainer().publish(dType, dataActionEnum, slContext);
    }

    protected void publish(IDataType dType, ISlotSignalContext slContext) {
        sReference.getSlContainer().publish(dType, slContext);
    }

    @Override
    public void startPublish(CellId cellId) {
    }
}
