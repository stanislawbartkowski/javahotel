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

import com.gwtmodel.table.ICustomObject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGetSetVField;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.WChoosedLine;
import com.gwtmodel.table.listdataview.GetVDataByIntegerSignal;
import com.gwtmodel.table.listdataview.GetVListSignal;
import com.gwtmodel.table.rdef.IFormLineView;

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
    public static void registerChangeFormSubscriber(IDataType dType, ISlotable iSlo,
            IVField v, ISlotSignaller iSubscriber) {
        iSlo.getSlContainer().registerSubscriber(dType, v, iSubscriber);

    }

}
