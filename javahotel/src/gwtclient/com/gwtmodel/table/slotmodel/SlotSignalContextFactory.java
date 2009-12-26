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

public class SlotSignalContextFactory {

    private SlotSignalContextFactory() {
    }

    private static class SlotSignalContext implements ISlotSignalContext {

        private final SlotType slType;
        private final String changedValue;
        private final IValidateError validateError;
        private final IGWidget gwtWidget;
        private final IDataType dType;
        private DataListType dataList;

        SlotSignalContext(SlotType slType, String changedValue,
                IValidateError validateError, IGWidget gwtWidget,
                IDataType dType, DataListType dataList) {
            this.slType = slType;
            this.changedValue = changedValue;
            this.validateError = validateError;
            this.gwtWidget = gwtWidget;
            this.dType = dType;
            this.dataList = dataList;
        }

        public SlotType getSlType() {
            return slType;
        }

        public String getChangedValue() {
            return changedValue;
        }

        public IValidateError getValidateError() {
            return validateError;
        }

        public IGWidget getGwtWidget() {
            return gwtWidget;
        }

        public IDataType getdType() {
            return dType;
        }

        public DataListType getDataList() {
            return dataList;
        }

    }

    public static ISlotSignalContext construct(SlotType slType, IDataType dType,
            DataListType dataList) {
        return new SlotSignalContext(slType, null, null, null, dType, dataList);
    }
    
    public static ISlotSignalContext construct(SlotType slType) {
        return new SlotSignalContext(slType, null, null, null, null, null);
    }

    public static ISlotSignalContext construct(SlotType slType,
            IGWidget gwtWidget) {
        return new SlotSignalContext(slType, null, null, gwtWidget, null, null);
    }

    public static ISlotSignalContext construct(SlotType slType, IDataType dType) {
        return new SlotSignalContext(slType, null, null, null, dType, null);
    }

}
