/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlU;
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

    static boolean publishValidSignalE(ISlotable iSlo, IDataType dType,
            List<InvalidateMess> errMess, ISlotSignalContext slContext) {
        return publishValidSignal(iSlo, dType, errMess == null ? null
                : new InvalidateFormContainer(errMess), slContext);
    }

    static boolean publishValidSignal(ISlotable iSlo, IDataType dType,
            InvalidateFormContainer errContainer, ISlotSignalContext slContext) {
        if (errContainer == null) {
            if (validateCustom(dType)) {
                iSlo.getSlContainer().publish(
                        DataUtil.constructValidateAgain(dType), null);
            } else {
                // slContainer.publish(dType, DataActionEnum.ValidSignal);
                SlU.publishDataAction(dType, iSlo, slContext,
                        DataActionEnum.ValidSignal);
            }
            return true;
        } else {
            iSlo.getSlContainer().publish(dType, DataActionEnum.InvalidSignal,
                    errContainer);
            return false;
        }
    }
}
