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
package com.javahotel.nmvc.factories;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.factories.IDataValidateAction;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotSignaller;
import com.gwtmodel.table.slotmodel.SlotPublisherType;
import com.gwtmodel.table.slotmodel.SlotType;
import com.gwtmodel.table.slotmodel.ValidateActionEnum;
import com.javahotel.nmvc.common.DataType;

class ValidateAction extends AbstractSlotContainer implements
        IDataValidateAction {

    private final IDataType dType;
    private final SlotPublisherType slPassed;

    private class ValidateA implements ISlotSignaller {

        public void signal(ISlotSignalContext slContext) {            
            slPassed.signal(slContext);
        }
    }

    ValidateAction(IDataType dType) {
        this.dType = dType;
        SlotType slType = slTypeFactory.construct(ValidateActionEnum.Validate,
                dType);
        addSubscriber(slType, new ValidateA());
        slType = slTypeFactory.construct(ValidateActionEnum.ValidationPassed,
                dType);
        slPassed = addPublisher(slType);
    }

    public void startPublish() {
    }

}
