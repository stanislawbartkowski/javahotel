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

import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.ICustomObject;
import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IGetSetVField;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.WChoosedLine;
import com.gwtmodel.table.controler.BoxActionMenuOptions;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.listdataview.GetVDataByIntegerSignal;
import com.gwtmodel.table.listdataview.GetVListSignal;
import com.gwtmodel.table.rdef.FormLineContainer;
import com.gwtmodel.table.rdef.IFormChangeListener;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.rdef.ITouchListener;

/**
 * @author hotel Static utility for making bringing object more easy
 */
public class SlU {

    private SlU() {
    }

    /**
     * Get IVModelData identified by index (integer)
     * 
     * @param dType
     *            IDataType
     * @param iSlo
     *            ISlotable
     * @param i
     *            index of IVModelData looked for
     * @return IVModelData (throws Exception if not found)
     */
    public static IVModelData getVDataByI(IDataType dType, ISlotable iSlo, int i) {
        ISlotCustom sl = new CustomStringDataTypeSlot(
                GetVDataByIntegerSignal.GETINTEGERSLOTSIGNAL, dType);
        IVModelData v = iSlo.getSlContainer()
                .getGetter(sl, new GetVDataByIntegerSignal(i)).getVData();
        return v;
    }

    /**
     * Get IFormLineView related to IVFiel
     * 
     * @param dType
     *            IDataType
     * @param iSlo
     *            ISlotable
     * @param v
     *            IVField to take IFormLineView
     * @return IVFormLineView or null
     */
    public static IFormLineView getVWidget(IDataType dType, ISlotable iSlo,
            IVField v) {
        return iSlo.getSlContainer().getGetterFormLine(dType, v);
    }

    public static void VWidgetChangeReadOnly(IDataType dType, ISlotable iSlo,
            IVField v, boolean readOnly) {
        IFormLineView vView = getVWidget(dType, iSlo, v);
        vView.setReadOnly(readOnly);
    }

    /**
     * Get List of IGetSetVField from list view
     * 
     * @param dType
     *            IDataType
     * @param iSlo
     *            ISlotable
     * @param rowNo
     *            Row number to get from list view
     * @return List of IGetSetVField
     */
    public static List<IGetSetVField> getVListFromEditTable(IDataType dType,
            ISlotable iSlo, int rowNo) {
        ISlotCustom sl = new CustomStringDataTypeSlot(
                GetVListSignal.GETVSIGNAL, dType);
        ISlotSignalContext i = iSlo.getSlContainer().getGetter(sl,
                new GetVListSignal(rowNo));
        GetVListSignal v = (GetVListSignal) i.getCustom();
        return v.getvList();
    }

    /**
     * Get WChoosedLine from slContext
     * 
     * @param slContext
     * @return WChooseLine
     */
    public static WChoosedLine getWChoosedLine(ISlotSignalContext slContext) {
        ICustomObject o = slContext.getCustom();
        return (WChoosedLine) o;
    }

    /**
     * Get IVModelData basing on WChoosedLine
     * 
     * @param dType
     *            IDataType
     * @param iSlo
     *            ISlotable
     * @param w
     *            WChoosedLine
     * @return IVModelData pointed by WChoosedLine
     */
    public static IVModelData getVDataByW(IDataType dType, ISlotable iSlo,
            WChoosedLine w) {
        return getVDataByI(dType, iSlo, w.getChoosedLine());
    }

    /**
     * Register subscriber listening for changes in the form
     * 
     * @param dType
     *            IDataType
     * @param iSlo
     *            ISlotable
     * @param v
     *            IVField of the field listened to
     * @param iSubscriber
     *            ISlotSignaller listener
     */
    public static void registerChangeFormSubscriber(IDataType dType,
            ISlotable iSlo, IVField v, ISlotListener iSubscriber) {
        iSlo.getSlContainer().registerSubscriber(dType, v, iSubscriber);

    }

    /**
     * Publish after change in field widget
     * 
     * @param dType
     *            IDataType
     * @param iSlo
     *            ISlotable
     * @param fie
     *            IVield changed
     * @param i
     *            Form widget changed
     * @param afterFocus
     *            if change was cause by focus
     */
    public static void publishValueChange(IDataType dType, ISlotable iSlo,
            IVField fie, IFormLineView i, boolean afterFocus) {
        iSlo.getSlContainer().publish(dType, fie, i, afterFocus);
    }

    /**
     * Retrieves 'afterFocus' value from ISlotSignalContext
     * 
     * @param slContext
     *            ISlotSignalContext
     * @return afterFocus value
     */
    public static boolean afterFocus(ISlotSignalContext slContext) {
        ICustomObject o = slContext.getCustom();
        CustomBoolValue b = (CustomBoolValue) o;
        return b.isBoolValue();
    }

    /**
     * Register Widget listener
     * 
     * @param dType
     *            IDataType
     * @param iSlo
     *            ISlotable
     * @param c
     *            Signaller waiting for widget
     */
    public static void registerWidgetListener0(IDataType dType, ISlotable iSlo,
            ISlotListener c) {
        iSlo.getSlContainer().registerSubscriber(dType, 0, c);
    }

    public static void publishWidget(IDataType dType, ISlotable iSlo, IGWidget g) {
        iSlo.getSlContainer().publish(dType, 0, g);
    }

    public static void startPublish0(ISlotable iSlo) {
        iSlo.startPublish(new CellId(0));
    }

    /**
     * Retrieves FormLineContainer from FormView
     * 
     * @param dType
     *            IDataType
     * @param iSlo
     *            ISlotable
     * @return FormLineContainer
     */
    public static FormLineContainer getFormLineContainer(IDataType dType,
            ISlotable iSlo) {
        return iSlo.getSlContainer().getGetterContainer(dType);

    }

    public static IDataListType getIDataListType(IDataType dType, ISlotable iSlo) {
        ISlotSignalContext sl = iSlo.getSlContainer().getGetterContext(dType,
                GetActionEnum.GetListData);
        return sl.getDataList();
    }

    /**
     * Publish Valid signal but containing string to be asked before proceeding
     * 
     * @param dType
     *            IDataType
     * @param iSlo
     *            ISlotable
     * @param slContext
     *            ISlotSignalContext (Widget/WSize whould be preserved)
     * @param ask
     *            String to be asked
     */
    public static void publishValidWithAsk(IDataType dType, ISlotable iSlo,
            ISlotSignalContext slContext, String ask) {
        SlotTypeFactory slTypeFactory = GwtGiniInjector.getI()
                .getSlotTypeFactory();
        SlotSignalContextFactory slContextFactory = GwtGiniInjector.getI()
                .getSlotSignalContextFactory();

        SlotType sl = slTypeFactory
                .construct(dType, DataActionEnum.ValidSignal);
        CustomStringValue c = new CustomStringValue(ask);
        ISlotSignalContext slC = slContextFactory.construct(sl, slContext, c);
        iSlo.getSlContainer().publish(slC);
    }

    /**
     * Publish PeristTypeEnum action
     * 
     * @param dType
     *            IDataType
     * @param iSlo
     *            ISlotable
     * @param slContext
     *            ISlotSignalContext (Widget/WSize should be preserved)
     * @param action
     *            DataActionEnum
     * @param persistTypeEnum
     *            PersistTypeEnum
     */
    public static void publishActionPersist(IDataType dType, ISlotable iSlo,
            ISlotSignalContext slContext, DataActionEnum action,
            PersistTypeEnum persistTypeEnum) {
        SlotTypeFactory slTypeFactory = GwtGiniInjector.getI()
                .getSlotTypeFactory();
        SlotSignalContextFactory slContextFactory = GwtGiniInjector.getI()
                .getSlotSignalContextFactory();

        SlotType sl = slTypeFactory.construct(dType, action);
        ISlotSignalContext slC = slContextFactory.construct(sl, slContext,
                persistTypeEnum);
        iSlo.getSlContainer().publish(slC);
    }

    private static class GetValueContainer implements IFormLineView {

        private final Object val;

        GetValueContainer(Object val) {
            this.val = val;
        }

        @Override
        public IVField getV() {
            return null;
        }

        @Override
        public Object getValObj() {
            return val;
        }

        @Override
        public void setValObj(Object o) {
        }

        @Override
        public Widget getGWidget() {
            return null;
        }

        @Override
        public void addChangeListener(IFormChangeListener cListener) {
        }

        @Override
        public void setReadOnly(boolean readOnly) {
        }

        @Override
        public void setHidden(boolean hidden) {

        }

        @Override
        public boolean isHidden() {
            return false;
        }

        @Override
        public void setInvalidMess(String errmess) {

        }

        @Override
        public void setGStyleName(String styleMess, boolean set) {

        }

        @Override
        public void setOnTouch(ITouchListener lTouch) {
        }

        @Override
        public int getChooseResult() {
            return 0;
        }

    }

    public static IFormLineView contructObjectValue(Object val) {
        return new GetValueContainer(val);
    }

    public static void publishActionResignWithWarning(IDataType dType,ISlotable iSlo,
            ISlotSignalContext slContext) {
        SlotSignalContextFactory slContextFactory = GwtGiniInjector.getI()
                .getSlotSignalContextFactory();
        SlotType sl = BoxActionMenuOptions.constructSAskBeforeRemoveSlotType(dType);
        CustomStringValue c = new CustomStringValue(
                "Na pewno rezygnujesz ? (Wszystkie zmiany przepadną)");
        ISlotSignalContext slC = slContextFactory.construct(sl, slContext, c);
        iSlo.getSlContainer().publish(slC);

    }

}
