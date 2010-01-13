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

import com.gwtmodel.table.IDataListType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.view.table.VListHeaderContainer;

public class SlotSignalContextFactory {

    private static class SlotSignalContext implements ISlotSignalContext {

        private final SlotType slType;
        private final IFormLineView changedValue;
        private final IValidateError validateError;
        private final IGWidget gwtWidget;
        private final IDataListType dataList;
        private final WSize wSize;
        private final IVModelData vData;
        private final PersistTypeEnum persistTypeEnum;
        private final VListHeaderContainer listHeader;

        public VListHeaderContainer getListHeader() {
            return listHeader;
        }

        SlotSignalContext(SlotType slType, IFormLineView changedValue,
                IValidateError validateError, IGWidget gwtWidget,
                IDataListType dataList, WSize wSize, IVModelData vData,
                PersistTypeEnum persistTypeEnum, VListHeaderContainer listHeader) {
            this.slType = slType;
            this.changedValue = changedValue;
            this.validateError = validateError;
            this.gwtWidget = gwtWidget;
            this.dataList = dataList;
            this.wSize = wSize;
            this.vData = vData;
            this.persistTypeEnum = persistTypeEnum;
            this.listHeader = listHeader;
        }

        public IVModelData getVData() {
            return vData;
        }

        public SlotType getSlType() {
            return slType;
        }

        public IFormLineView getChangedValue() {
            return changedValue;
        }

        public IValidateError getValidateError() {
            return validateError;
        }

        public IGWidget getGwtWidget() {
            return gwtWidget;
        }

        public IDataListType getDataList() {
            return dataList;
        }

        public WSize getWSize() {
            return wSize;
        }

        public PersistTypeEnum getPersistType() {
            return persistTypeEnum;

        }

    }

    public ISlotSignalContext construct(SlotType slType, IDataListType dataList) {
        return new SlotSignalContext(slType, null, null, null, dataList, null,
                null, null, null);
    }

    public ISlotSignalContext construct(SlotType slType, IVModelData vData,
            WSize wSize) {
        return new SlotSignalContext(slType, null, null, null, null, wSize,
                vData, null, null);
    }

    public ISlotSignalContext construct(SlotType slType) {
        return new SlotSignalContext(slType, null, null, null, null, null,
                null, null, null);
    }

    public ISlotSignalContext construct(SlotType slType, IFormLineView formLine) {
        return new SlotSignalContext(slType, formLine, null, null, null, null,
                null, null, null);
    }

    public ISlotSignalContext construct(SlotType slType,
            PersistTypeEnum persistTypeEnum) {
        return new SlotSignalContext(slType, null, null, null, null, null,
                null, persistTypeEnum, null);
    }

    public ISlotSignalContext construct(SlotType slType,
            VListHeaderContainer listHeader) {
        return new SlotSignalContext(slType, null, null, null, null, null,
                null, null, listHeader);
    }

    public ISlotSignalContext construct(SlotType slType, IGWidget gwtWidget) {
        return new SlotSignalContext(slType, null, null, gwtWidget, null, null,
                null, null, null);
    }

    public ISlotSignalContext construct(SlotType slType, IVModelData vData) {
        return new SlotSignalContext(slType, null, null, null, null, null,
                vData, null, null);
    }

    public ISlotSignalContext construct(SlotType slType, IValidateError vError) {
        return new SlotSignalContext(slType, null, vError, null, null, null,
                null, null, null);
    }

    public ISlotSignalContext construct(SlotType slType,
            ISlotSignalContext iSlot) {
        return new SlotSignalContext(slType, iSlot.getChangedValue(), iSlot
                .getValidateError(), iSlot.getGwtWidget(), iSlot.getDataList(),
                iSlot.getWSize(), iSlot.getVData(), iSlot.getPersistType(),
                iSlot.getListHeader());
    }
}
