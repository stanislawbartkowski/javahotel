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
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IOkModelData;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.PersistTypeEnum;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.rdef.FormLineContainer;
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
        private final IVField vField;
        private final FormLineContainer lContainer;
        private final String stringButton;
        private final ICustomObject customO;
        private final IOkModelData iOkModelData;

        @Override
        public VListHeaderContainer getListHeader() {
            return listHeader;
        }

        SlotSignalContext(SlotType slType, IFormLineView changedValue,
                IValidateError validateError, IGWidget gwtWidget,
                IDataListType dataList, WSize wSize, IVModelData vData,
                PersistTypeEnum persistTypeEnum,
                VListHeaderContainer listHeader, IVField vField,
                FormLineContainer lContainer, String stringButton,
                ICustomObject customO, IOkModelData iOkModelData) {
            this.slType = slType;
            this.changedValue = changedValue;
            this.validateError = validateError;
            this.gwtWidget = gwtWidget;
            this.dataList = dataList;
            this.wSize = wSize;
            this.vData = vData;
            this.persistTypeEnum = persistTypeEnum;
            this.listHeader = listHeader;
            this.vField = vField;
            this.lContainer = lContainer;
            this.stringButton = stringButton;
            this.customO = customO;
            this.iOkModelData = iOkModelData;
        }

        @Override
        public IVModelData getVData() {
            return vData;
        }

        @Override
        public SlotType getSlType() {
            return slType;
        }

        @Override
        public IFormLineView getChangedValue() {
            return changedValue;
        }

        @Override
        public IValidateError getValidateError() {
            return validateError;
        }

        @Override
        public IGWidget getGwtWidget() {
            return gwtWidget;
        }

        @Override
        public IDataListType getDataList() {
            return dataList;
        }

        @Override
        public WSize getWSize() {
            return wSize;
        }

        @Override
        public PersistTypeEnum getPersistType() {
            return persistTypeEnum;

        }

        @Override
        public IVField getVField() {
            return vField;
        }

        @Override
        public FormLineContainer getEditContainer() {
            return lContainer;
        }

        /**
         * @param stringButton the stringButton to set
         */
        @Override
        public String getStringButton() {
            return stringButton;
        }

        @Override
        public IGWidget getHtmlWidget() {
            return getGwtWidget();
        }

        @Override
        public ICustomObject getCustom() {
            return customO;
        }

        @Override
        public IOkModelData getIOkModelData() {
            return iOkModelData;
        }
    }

    public ISlotSignalContext construct(SlotType slType, IDataListType dataList) {
        return new SlotSignalContext(slType, null, null, null, dataList, null,
                null, null, null, null, null, null, null, null);
    }

    public ISlotSignalContext construct(SlotType slType,
            IDataListType dataList, WSize wSize) {
        return new SlotSignalContext(slType, null, null, null, dataList, wSize,
                null, null, null, null, null, null, null, null);
    }

    public ISlotSignalContext construct(SlotType slType, IVModelData vData,
            WSize wSize) {
        return new SlotSignalContext(slType, null, null, null, null, wSize,
                vData, null, null, null, null, null, null, null);
    }

    public ISlotSignalContext construct(SlotType slType, WSize wSize) {
        return new SlotSignalContext(slType, null, null, null, null, wSize,
                null, null, null, null, null, null, null, null);
    }

    public ISlotSignalContext construct(SlotType slType) {
        return new SlotSignalContext(slType, null, null, null, null, null,
                null, null, null, null, null, null, null, null);
    }

    public ISlotSignalContext construct(SlotType slType, IFormLineView formLine) {
        return new SlotSignalContext(slType, formLine, null, null, null, null,
                null, null, null, null, null, null, null, null);
    }

    public ISlotSignalContext construct(SlotType slType,
            PersistTypeEnum persistTypeEnum) {
        return new SlotSignalContext(slType, null, null, null, null, null,
                null, persistTypeEnum, null, null, null, null, null, null);
    }

    public ISlotSignalContext construct(SlotType slType,
            VListHeaderContainer listHeader) {
        return new SlotSignalContext(slType, null, null, null, null, null,
                null, null, listHeader, null, null, null, null, null);
    }

    public ISlotSignalContext construct(SlotType slType, IGWidget gwtWidget,
            String stringButton) {
        return new SlotSignalContext(slType, null, null, gwtWidget, null, null,
                null, null, null, null, null, stringButton, null, null);
    }

    public ISlotSignalContext construct(SlotType slType, IGWidget gwtWidget) {
        return new SlotSignalContext(slType, null, null, gwtWidget, null, null,
                null, null, null, null, null, null, null, null);
    }

    public ISlotSignalContext construct(SlotType slType, IVModelData vData) {
        return new SlotSignalContext(slType, null, null, null, null, null,
                vData, null, null, null, null, null, null, null);
    }

    public ISlotSignalContext construct(SlotType slType, IVModelData vData,
            PersistTypeEnum persistTypeEnum) {
        return new SlotSignalContext(slType, null, null, null, null, null,
                vData, persistTypeEnum, null, null, null, null, null, null);
    }

    public ISlotSignalContext construct(SlotType slType, IValidateError vError) {
        return new SlotSignalContext(slType, null, vError, null, null, null,
                null, null, null, null, null, null, null, null);
    }

    public ISlotSignalContext construct(SlotType slType, IVField vField) {
        return new SlotSignalContext(slType, null, null, null, null, null,
                null, null, null, vField, null, null, null, null);
    }

    public ISlotSignalContext construct(SlotType slType,
            FormLineContainer lContainer) {
        return new SlotSignalContext(slType, null, null, null, null, null,
                null, null, null, null, lContainer, null, null, null);
    }

    public ISlotSignalContext construct(SlotType slType,
            ICustomObject customO) {
        return new SlotSignalContext(slType, null, null, null, null, null,
                null, null, null, null, null, null, customO, null);
    }

    public ISlotSignalContext construct(SlotType slType,
            IOkModelData iOkModelData) {
        return new SlotSignalContext(slType, null, null, null, null, null,
                null, null, null, null, null, null, null, iOkModelData);
    }

    public ISlotSignalContext construct(SlotType slType,
            ISlotSignalContext iSlot) {
        return new SlotSignalContext(slType, iSlot.getChangedValue(),
                iSlot.getValidateError(), iSlot.getGwtWidget(), iSlot.getDataList(),
                iSlot.getWSize(), iSlot.getVData(), iSlot.getPersistType(),
                iSlot.getListHeader(), iSlot.getVField(),
                iSlot.getEditContainer(), iSlot.getStringButton(),
                iSlot.getCustom(), iSlot.getIOkModelData());
    }

}
