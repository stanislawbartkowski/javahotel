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
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.view.table.VListHeaderContainer;

public class SlotListContainer {

    private final List<SlotSubscriberType> listOfSubscribers;
    private final List<SlotCallerType> listOfCallers;
    private final List<SlotRedirector> listOfRedirectors;
    private ISlotCaller slCaller;
    private ISlotSignaller slSignaller;
    private final SlotTypeFactory slTypeFactory;
    private final SlotSignalContextFactory slContextFactory;

    @Inject
    public SlotListContainer(SlotTypeFactory slTypeFactory,
            SlotSignalContextFactory slContextFactory) {
        this.slTypeFactory = slTypeFactory;
        this.slContextFactory = slContextFactory;
        listOfSubscribers = new ArrayList<SlotSubscriberType>();
        listOfCallers = new ArrayList<SlotCallerType>();
        listOfRedirectors = new ArrayList<SlotRedirector>();
        registerSlReceiver(new GeneralCaller());
        registerSlPublisher(new GeneralListener());

    }

    public ISlotSignalContext contextReplace(SlotType slType,
            ISlotSignalContext iSlot) {
        return slContextFactory.construct(slType, iSlot);
    }

    private class GeneralListener implements ISlotSignaller {

        public void signal(ISlotSignalContext slContextP) {
            SlotType sl = slContextP.getSlType();
            ISlotSignalContext slContext = slContextP;
            // find redirector
            boolean notReplaced = true;
            while (notReplaced) {
                notReplaced = false;
                for (SlotRedirector re : getListOfRedirectors()) {
                    if (re.getFrom().eq(sl)) {
                        sl = re.getTo();
                        slContext = contextReplace(sl, slContextP);
                        notReplaced = true;
                        break;
                    }
                }
            }

            for (SlotSubscriberType so : getListOfSubscribers()) {
                if (sl.eq(so.getSlType())) {
                    so.getSlSignaller().signal(slContext);
                }
            }
        }
    }

    private class GeneralCaller implements ISlotCaller {

        public ISlotSignalContext call(ISlotSignalContext slContext) {
            SlotCallerType slCaller = findCaller(slContext.getSlType());
            if (slCaller == null) {
                return null;
            }
            return slCaller.getSlCaller().call(slContext);
        }
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

    public void publish(ISlotSignalContext slContext) {
        if (slSignaller == null) {
            return;
        }
        slSignaller.signal(slContext);
    }

    public void registerSlReceiver(ISlotCaller slCaller) {
        this.slCaller = slCaller;
    }

    public void registerSlPublisher(ISlotSignaller slSignaller) {
        this.slSignaller = slSignaller;
    }

    public List<SlotCallerType> getListOfCallers() {
        return listOfCallers;
    }

    public List<SlotSubscriberType> getListOfSubscribers() {
        return listOfSubscribers;
    }

    public void registerSubscriber(SlotType slType, ISlotSignaller slSignaller) {
        listOfSubscribers.add(new SlotSubscriberType(slType, slSignaller));
    }

    public void registerSubscriber(int cellId, ISlotSignaller slSignaller) {
        registerSubscriber(slTypeFactory.construct(cellId), slSignaller);
    }

    public void registerSubscriber(ClickButtonType.StandClickEnum eClick,
            ISlotSignaller slSignaller) {
        registerSubscriber(slTypeFactory.construct(eClick), slSignaller);
    }

    public void registerSubscriber(DataActionEnum dataActionEnum,
            IDataType dType, ISlotSignaller slSignaller) {
        registerSubscriber(slTypeFactory.construct(dataActionEnum, dType),
                slSignaller);
    }

    public void registerSubscriber(IDataType dType, IVField fie,
            ISlotSignaller slSignaller) {
        registerSubscriber(slTypeFactory.construct(dType, fie), slSignaller);
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

    private ISlotSignalContext callGet(SlotType slType) {
        ISlotSignalContext slContext = slContextFactory.construct(slType);
        return call(slContext);
    }

    private ISlotSignalContext callGet(SlotType slType, IVModelData mData) {
        ISlotSignalContext slContext = slContextFactory.construct(slType, mData);
        return call(slContext);
    }

    public IVModelData getGetterIVModelData(GetActionEnum getActionEnum,
            IDataType dType) {
        ISlotSignalContext slContext = getGetterContext(getActionEnum, dType);
        return slContext.getVData();
    }

    public IVField getGetterComboField(IDataType dType) {
        ISlotSignalContext slContext = getGetterContext(GetActionEnum.GetListComboField, dType);
        return slContext.getVField();

    }

    public IVModelData getGetterIVModelData(GetActionEnum getActionEnum,
            IDataType dType, IVModelData mData) {
        ISlotSignalContext slContext = getGetterContext(getActionEnum, dType,
                mData);
        return slContext.getVData();
    }

    public ISlotSignalContext getGetterContext(GetActionEnum getActionEnum,
            IDataType dType) {
        SlotType slType = slTypeFactory.construct(getActionEnum, dType);
        ISlotSignalContext slContext = callGet(slType);
        return slContext;
    }

    public ISlotSignalContext getGetterContext(SlotType slType,
            IVModelData mData) {
        ISlotSignalContext slContext = slContextFactory.construct(slType, mData);
        return slContext;
    }

    public ISlotSignalContext getGetterContext(GetActionEnum getActionEnum,
            IDataType dType, IVModelData mData) {
        SlotType slType = slTypeFactory.construct(getActionEnum, dType);
        ISlotSignalContext slContext = callGet(slType, mData);
        return slContext;
    }

    public ISlotSignalContext setGetter(GetActionEnum getActionEnum,
            IDataType dType, IVModelData vData, WSize wSize) {
        SlotType slType = slTypeFactory.construct(getActionEnum, dType);
        ISlotSignalContext sl = slContextFactory.construct(slType, vData, wSize);
        return sl;
    }

    public ISlotSignalContext setGetter(IDataType dType, IVField comboFie) {
        SlotType slType = slTypeFactory.construct(GetActionEnum.GetListComboField, dType);
        ISlotSignalContext sl = slContextFactory.construct(slType, comboFie);
        return sl;
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

    public void publish(int cellId, IGWidget gwtWidget) {
        publish(slContextFactory.construct(slTypeFactory.construct(cellId),
                gwtWidget));
    }

    public void publish(ClickButtonType bType, IGWidget gwtWidget) {
        publish(slContextFactory.construct(slTypeFactory.construct(bType),
                gwtWidget));
    }

    public void publish(IDataType dType, IVField fie, IFormLineView formLine) {
        publish(slContextFactory.construct(slTypeFactory.construct(dType, fie),
                formLine));
    }

    public void publish(DataActionEnum dataActionEnum, IDataType dType,
            PersistTypeEnum persistTypeEnum) {
        publish(slContextFactory.construct(slTypeFactory.construct(
                dataActionEnum, dType), persistTypeEnum));
    }

    public void publish(DataActionEnum dataActionEnum, IDataType dType,
            IVModelData vData) {
        publish(slContextFactory.construct(slTypeFactory.construct(
                dataActionEnum, dType), vData));
    }

    public void publish(DataActionEnum dataActionEnum, IDataType dType,
            WSize wSize) {
        publish(slContextFactory.construct(slTypeFactory.construct(
                dataActionEnum, dType), wSize));
    }

    public void publish(DataActionEnum dataActionEnum, IDataType dType,
            IDataListType dataList) {
        publish(slContextFactory.construct(slTypeFactory.construct(
                dataActionEnum, dType), dataList));
    }

    public void publish(DataActionEnum dataActionEnum, IDataType dType,
            IDataListType dataList, WSize wSize) {
        publish(slContextFactory.construct(slTypeFactory.construct(
                dataActionEnum, dType), dataList, wSize));
    }

    public void publish(DataActionEnum dataActionEnum, IDataType dType,
            ISlotSignalContext slContext) {
        publish(slContextFactory.construct(slTypeFactory.construct(
                dataActionEnum, dType), slContext));
    }

    public void publish(DataActionEnum dataActionEnum, IDataType dType) {
        publish(slContextFactory.construct(slTypeFactory.construct(
                dataActionEnum, dType)));
    }

    public void publish(DataActionEnum dataActionEnum, IDataType dType,
            InvalidateFormContainer errContainer) {
        publish(slContextFactory.construct(slTypeFactory.construct(
                dataActionEnum, dType), errContainer));
    }

    public void publish(IDataType dType, VListHeaderContainer vHeader) {
        publish(slContextFactory.construct(slTypeFactory.construct(
                DataActionEnum.ReadHeaderContainerSignal, dType), vHeader));
    }

    public void publish(SlotType slType) {
        publish(slContextFactory.construct(slType));
    }
}
