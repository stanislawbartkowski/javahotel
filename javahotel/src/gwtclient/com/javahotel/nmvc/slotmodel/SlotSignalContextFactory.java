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
package com.javahotel.nmvc.slotmodel;

import com.javahotel.client.dialog.IGwtWidget;
import com.javahotel.nmvc.common.DataListType;
import com.javahotel.nmvc.common.DataType;

public class SlotSignalContextFactory {

    private SlotSignalContextFactory() {
    }

    private static class SlotSignalContext implements ISlotSignalContext {

        private final SlotType slType;
        private final String changedValue;
        private final IValidateError validateError;
        private final IGwtWidget gwtWidget;
        private final DataType dType;
        DataListType dataList;

        SlotSignalContext(SlotType slType, String changedValue,
                IValidateError validateError, IGwtWidget gwtWidget,
                DataType dType, DataListType dataList) {
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

        public IGwtWidget getGwtWidget() {
            return gwtWidget;
        }

        public DataType getdType() {
            return dType;
        }

        public DataListType getDataList() {
            return dataList;
        }

    }

    public static ISlotSignalContext construct(SlotType slType, DataType dType,
            DataListType dataList) {
        return new SlotSignalContext(slType, null, null, null, dType, dataList);
    }

    public static ISlotSignalContext construct(SlotType slType,
            IGwtWidget gwtWidget) {
        return new SlotSignalContext(slType, null, null, gwtWidget, null, null);
    }

    public static ISlotSignalContext construct(SlotType slType, DataType dType) {
        return new SlotSignalContext(slType, null, null, null, dType, null);
    }

}
