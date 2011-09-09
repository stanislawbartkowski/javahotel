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

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
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
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.view.table.VListHeaderContainer;

public final class SlotListContainer {

    private final List<SlotSubscriberType> listOfSubscribers;
    private final List<SlotCallerType> listOfCallers;
    private final List<SlotRedirector> listOfRedirectors;
    private final ISlotCaller slCaller;
    private final ISlotSignaller slSignaller;
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
        slCaller = new GeneralCaller();
        slSignaller = new GeneralListener();
    }

    public void replaceContainer(ISlotable iSlo) {
        SlotListContainer slo = iSlo.getSlContainer();
        listOfSubscribers.addAll(slo.listOfSubscribers);
        listOfCallers.addAll(slo.listOfCallers);
        listOfRedirectors.addAll(slo.listOfRedirectors);
        iSlo.replaceSlContainer(this);
    }

    public ISlotSignalContext contextReplace(SlotType slType,
            ISlotSignalContext iSlot) {
        return slContextFactory.construct(slType, iSlot);
    }

    private List<SlotSubscriberType> getList(SlotType sl) {
        @SuppressWarnings({ "unchecked", "rawtypes" })
        List<SlotSubscriberType> li = (List<SlotSubscriberType>) ((ArrayList) listOfSubscribers)
                .clone();
        List<SlotSubscriberType> outli = new ArrayList<SlotSubscriberType>();
        // to avoid concurrency exception
        for (SlotSubscriberType so : li) {
            if (sl.eq(so.getSlType())) {
                outli.add(so);
            }
        }
        return outli;

    }

    public int noListeners(SlotType sl) {
        List<SlotSubscriberType> li = getList(sl);
        return li.size();
    }

    private class GeneralListener implements ISlotSignaller {

        @Override
        public void signal(ISlotSignalContext slContextP) {
            SlotType sl = slContextP.getSlType();
            ISlotSignalContext slContext = slContextP;
            // find redirector
            boolean notReplaced = true;
            while (notReplaced) {
                notReplaced = false;
                for (SlotRedirector re : listOfRedirectors) {
                    if (re.getFrom().eq(sl)) {
                        sl = re.getTo();
                        slContext = contextReplace(sl, slContextP);
                        notReplaced = true;
                        break;
                    }
                }
            }

            // to avoid concurrency exception
            List<SlotSubscriberType> li = getList(slContext.getSlType());
            if (li.size() == 0) {
                LogT.getLS().info(
                        LogT.getT().sendSignalNotFound(
                                slContext.getSlType().toString()));
                return;
            }
            for (SlotSubscriberType so : li) {
                LogT.getLS().info(
                        LogT.getT().sendSignalLog(
                                slContext.getSlType().toString()));
                so.getSlSignaller().signal(slContext);
            }
        }
    }

    private class GeneralCaller implements ISlotCaller {

        @Override
        public ISlotSignalContext call(ISlotSignalContext slContext) {
            SlotCallerType slCaller = findCaller(slContext.getSlType());
            if (slCaller == null) {
                LogT.getLS().info(
                        LogT.getT().slCallerNotFound(
                                slContext.getSlType().toString()));
                return null;
            }
            LogT.getLS().info(
                    LogT.getT().slCaller(slContext.getSlType().toString()));
            return slCaller.getSlCaller().call(slContext);
        }
    }

    public ISlotSignalContext call(ISlotSignalContext slContext) {
        if (slCaller == null) {
            LogT.getLS().info(
                    LogT.getT().slCallerNull(slContext.getSlType().toString()));
            return null;
        }
        return slCaller.call(slContext);
    }

    public void publish(ISlotSignalContext slContext) {
        if (slSignaller == null) {
            LogT.getLS().info(
                    LogT.getT()
                            .publishLogNull(slContext.getSlType().toString()));
            return;
        }
        LogT.getLS().info(
                LogT.getT().publishLog(slContext.getSlType().toString()));
        slSignaller.signal(slContext);
    }

    public void removeSubscriber(SlotType sl) {
        boolean exist = true;
        while (exist) {
            exist = false;
            for (SlotSubscriberType so : listOfSubscribers) {
                if (sl.eq(so.getSlType())) {
                    listOfSubscribers.remove(so);
                    exist = true;
                    break;
                }
            }
        }
    }

    public void registerSubscriber(SlotType slType, ISlotSignaller slSignaller) {
        listOfSubscribers.add(new SlotSubscriberType(slType, slSignaller));
    }

    public void registerSubscriber(IDataType dType, CellId cellId,
            ISlotSignaller slSignaller) {
        registerSubscriber(slTypeFactory.construct(dType, cellId), slSignaller);
    }

    public void registerSubscriber(IDataType dType, int cellId,
            ISlotSignaller slSignaller) {
        registerSubscriber(slTypeFactory.construct(dType, cellId), slSignaller);
    }

    public void registerSubscriber(ClickButtonType.StandClickEnum eClick,
            ISlotSignaller slSignaller) {
        registerSubscriber(
                slTypeFactory.construct(new ClickButtonType(eClick)),
                slSignaller);
    }

    public void registerSubscriber(ClickButtonType bClick,
            ISlotSignaller slSignaller) {
        registerSubscriber(slTypeFactory.construct(bClick), slSignaller);
    }

    public void registerSubscriber(String stringButton,
            ISlotSignaller slSignaller) {
        registerSubscriber(slTypeFactory.construct(stringButton), slSignaller);
    }

    public void registerSubscriber(IDataType dType,
            DataActionEnum dataActionEnum, ISlotSignaller slSignaller) {
        registerSubscriber(slTypeFactory.construct(dataActionEnum, dType),
                slSignaller);
    }

    public void removeSubscriber(IDataType dType, DataActionEnum dataActionEnum) {
        removeSubscriber(slTypeFactory.construct(dataActionEnum, dType));
    }

    public void registerSubscriber(IDataType dType, IVField fie,
            ISlotSignaller slSignaller) {
        registerSubscriber(slTypeFactory.construct(dType, fie), slSignaller);
    }

    public void registerSubscriber(IDataType dType, ClickButtonType bType,
            ButtonAction bAction, ISlotSignaller slSignaller) {
        registerSubscriber(slTypeFactory.construct(dType, bType, bAction),
                slSignaller);
    }

    public void registerSubscriber(ISlotCustom i, ISlotSignaller slSignaller) {
        registerSubscriber(slTypeFactory.construct(i), slSignaller);
    }

    public void registerCaller(ISlotCustom i, ISlotCaller slCaller) {
        listOfCallers.add(new SlotCallerType(slTypeFactory.construct(i),
                slCaller));
    }

    public void registerCaller(SlotType slType, ISlotCaller slCaller) {
        listOfCallers.add(new SlotCallerType(slType, slCaller));
    }

    public void registerCaller(IDataType dType, GetActionEnum gEnum,
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
        ISlotSignalContext slContext = slContextFactory
                .construct(slType, mData);
        return call(slContext);
    }

    public ISlotSignalContext getGetter(ISlotCustom is, ICustomObject customO) {
        ISlotSignalContext slContext = slContextFactory.construct(
                slTypeFactory.construct(is), customO);
        return call(slContext);
    }

    public IVModelData getGetterIVModelData(IDataType dType,
            GetActionEnum getActionEnum, IVModelData mData) {
        ISlotSignalContext slContext = getGetterContext(dType, getActionEnum,
                mData);
        if (slContext == null) {
            return null;
        }
        return slContext.getVData();
    }

    public IVModelData getGetterIVModelData(IDataType dType,
            GetActionEnum getActionEnum) {
        ISlotSignalContext slContext = getGetterContext(dType, getActionEnum);
        if (slContext == null) {
            return null;
        }
        return slContext.getVData();
    }

    public IGWidget getHtmlWidget(CellId c) {
        SlotType slType = slTypeFactory.constructH(c);
        ISlotSignalContext co = slContextFactory.construct(slType);
        ISlotSignalContext slContext = call(co);
        if (slContext == null) {
            return null;
        }
        return slContext.getHtmlWidget();
    }

    public IVField getGetterComboField(IDataType dType) {
        ISlotSignalContext slContext = getGetterContext(dType,
                GetActionEnum.GetListComboField);
        return slContext.getVField();
    }

    public FormLineContainer getGetterContainer(IDataType dType) {
        ISlotSignalContext slContext = getGetterContext(dType,
                GetActionEnum.GetEditContainer);
        return slContext.getEditContainer();
    }

    public ISlotSignalContext getGetterContext(IDataType dType,
            FormLineContainer lContainer) {
        SlotType slType = slTypeFactory.construct(
                GetActionEnum.GetEditContainer, dType);
        ISlotSignalContext slContext = slContextFactory.construct(slType,
                lContainer);
        return slContext;
    }

    public ISlotSignalContext getGetterContext(IDataType dType,
            GetActionEnum getActionEnum, IVModelData vData) {
        SlotType slType = slTypeFactory.construct(getActionEnum, dType);
        ISlotSignalContext slContext = callGet(slType, vData);
        return slContext;
    }

    public ISlotSignalContext getGetterContext(IDataType dType,
            GetActionEnum getActionEnum) {
        SlotType slType = slTypeFactory.construct(getActionEnum, dType);
        ISlotSignalContext slContext = callGet(slType);
        return slContext;
    }

    public ISlotSignalContext getGetterCustom(ISlotCustom i) {
        SlotType slType = slTypeFactory.construct(i);
        ISlotSignalContext slContext = callGet(slType);
        return slContext;
    }

    public IFormLineView getGetterFormLine(IDataType d, IVField v) {
        SlotType slType = slTypeFactory.constructI(d);
        ISlotSignalContext slContext = slContextFactory.construct(slType, v);
        ISlotSignalContext slContext1 = call(slContext);
        if (slContext1 == null) {
            return null;
        }
        return slContext1.getChangedValue();
    }

    public ISlotSignalContext getGetterContext(SlotType slType,
            IVModelData mData) {
        ISlotSignalContext slContext = slContextFactory
                .construct(slType, mData);
        return slContext;
    }

    public ISlotSignalContext setGetter(IDataType dType,
            GetActionEnum getActionEnum, IVModelData vData, WSize wSize,
            IVField v) {
        SlotType slType = slTypeFactory.construct(getActionEnum, dType);
        ISlotSignalContext sl = slContextFactory.construct(slType, vData,
                wSize, v);
        return sl;
    }

    public ISlotSignalContext setGetter(IDataType dType, IVField comboFie) {
        SlotType slType = slTypeFactory.construct(
                GetActionEnum.GetListComboField, dType);
        ISlotSignalContext sl = slContextFactory.construct(slType, comboFie);
        return sl;
    }

    public ISlotSignalContext setGetter(SlotType slType, IFormLineView v) {
        ISlotSignalContext sl = slContextFactory.construct(slType, v);
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

    public void publish(IDataType dType, CellId cellId, IGWidget gwtWidget) {
        publish(slContextFactory.construct(
                slTypeFactory.construct(dType, cellId), gwtWidget));
    }

    public void publish(IDataType dType, int cellId, IGWidget gwtWidget) {
        publish(slContextFactory.construct(
                slTypeFactory.construct(dType, cellId), gwtWidget));
    }

    public void publish(ClickButtonType bType, IGWidget gwtWidget) {
        publish(slContextFactory.construct(slTypeFactory.construct(bType),
                gwtWidget));
    }

    public void publish(ISlotCustom is, ICustomObject customO) {
        publish(slContextFactory
                .construct(slTypeFactory.construct(is), customO));
    }

    public void publish(String customString, ICustomObject customO) {
        publish(slContextFactory.construct(
                slTypeFactory.construct(customString), customO));
    }

    public void publish(SlotType sl, ICustomObject customO) {
        publish(slContextFactory.construct(sl, customO));
    }

    public void publish(String stringButton, IGWidget gwtWidget) {
        publish(slContextFactory.construct(
                slTypeFactory.construct(stringButton), gwtWidget, stringButton));
    }

    public void publish(IDataType dType, IVField fie, IFormLineView formLine) {
        publish(slContextFactory.construct(slTypeFactory.construct(dType, fie),
                formLine));
    }

    public void publish(IDataType dType, ClickButtonType bType,
            ButtonAction bAction) {
        publish(slContextFactory.construct(slTypeFactory.construct(dType,
                bType, bAction)));
    }

    public void publish(IDataType dType, DataActionEnum dataActionEnum,
            PersistTypeEnum persistTypeEnum) {
        publish(slContextFactory
                .construct(slTypeFactory.construct(dataActionEnum, dType),
                        persistTypeEnum));
    }

    public void publish(IDataType dType, ISlotSignalContext slContext) {
        SlotType slType = slTypeFactory.construct(dType, slContext.getSlType());
        publish(slContextFactory.construct(slType, slContext));
    }

    public void publish(IDataType dType, DataActionEnum dataActionEnum,
            IVModelData vData) {
        publish(slContextFactory.construct(
                slTypeFactory.construct(dataActionEnum, dType), vData));
    }

    public void publish(IDataType dType, DataActionEnum dataActionEnum,
            IVModelData vData, PersistTypeEnum persistTypeEnum) {
        publish(slContextFactory.construct(
                slTypeFactory.construct(dataActionEnum, dType), vData,
                persistTypeEnum));
    }

    public void publish(IDataType dType, DataActionEnum dataActionEnum,
            WSize wSize) {
        publish(slContextFactory.construct(
                slTypeFactory.construct(dataActionEnum, dType), wSize));
    }

    public void publish(IDataType dType, DataActionEnum dataActionEnum,
            IDataListType dataList) {
        publish(slContextFactory.construct(
                slTypeFactory.construct(dataActionEnum, dType), dataList));
    }

    public void publish(IDataType dType, DataActionEnum dataActionEnum,
            IDataListType dataList, WSize wSize) {
        publish(slContextFactory
                .construct(slTypeFactory.construct(dataActionEnum, dType),
                        dataList, wSize));
    }

    public void publish(IDataType dType, DataActionEnum dataActionEnum,
            ISlotSignalContext slContext) {
        publish(slContextFactory.construct(
                slTypeFactory.construct(dataActionEnum, dType), slContext));
    }

    public void publish(IDataType dType, DataActionEnum dataActionEnum) {
        publish(slContextFactory.construct(slTypeFactory.construct(
                dataActionEnum, dType)));
    }

    public void publish(IDataType dType, DataActionEnum dataActionEnum,
            IOkModelData iOkModelData) {
        publish(slContextFactory.construct(
                slTypeFactory.construct(dataActionEnum, dType), iOkModelData));
    }

    public void publish(IDataType dType, DataActionEnum dataActionEnum,
            InvalidateFormContainer errContainer) {
        publish(slContextFactory.construct(
                slTypeFactory.construct(dataActionEnum, dType), errContainer));
    }

    public void publish(IDataType dType, VListHeaderContainer vHeader) {
        publish(slContextFactory.construct(slTypeFactory.construct(
                DataActionEnum.ReadHeaderContainerSignal, dType), vHeader));
    }

    public void publish(SlotType slType) {
        publish(slContextFactory.construct(slType));
    }

    public void publish(String buttonString) {
        publish(slContextFactory.construct(slTypeFactory
                .construct(buttonString)));
    }
}
