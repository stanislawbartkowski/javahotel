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
package com.javahotel.nmvc.factories.validate;

import java.util.List;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.SlotListContainer;
import com.javahotel.client.types.DataType;
import com.javahotel.client.types.DataUtil;
import com.javahotel.common.command.DictType;

/**
 * @author hotel
 * 
 */
class P {

    static private boolean validateCustom(IDataType dType) {
        DataType daType = (DataType) dType;
        if (!daType.isDictType()) {
            return false;
        }
        return daType.getdType() == DictType.PriceListDict;
    }

    static boolean publishValidSignalE(SlotListContainer slContainer,
            IDataType dType, List<InvalidateMess> errMess) {
        return publishValidSignal(slContainer, dType, errMess == null ? null
                : new InvalidateFormContainer(errMess));
    }

    static boolean publishValidSignal(SlotListContainer slContainer,
            IDataType dType, InvalidateFormContainer errContainer) {
        if (errContainer == null) {
            if (validateCustom(dType)) {
                slContainer.publish(DataUtil.constructValidateAgain(dType),
                        null);
            } else {
                slContainer.publish(DataActionEnum.ValidSignal, dType);
            }
            return true;
        } else {
            slContainer.publish(DataActionEnum.InvalidSignal, dType,
                    errContainer);
            return false;
        }
    }
}
