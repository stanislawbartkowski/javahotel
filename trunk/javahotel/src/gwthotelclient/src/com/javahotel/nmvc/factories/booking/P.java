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
package com.javahotel.nmvc.factories.booking;

import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.javahotel.client.types.DataUtil;
import com.javahotel.client.types.HModelData;
import com.javahotel.common.toobject.AdvancePaymentP;
import com.javahotel.common.toobject.BookRecordP;
import com.javahotel.common.toobject.BookingP;
import com.javahotel.common.util.GetMaxUtil;

/**
 * @author hotel
 * 
 */
public class P {

    private P() {

    }

    static BookRecordP getBookR(IVModelData mData) {
        BookingP b = DataUtil.getData(mData);
        BookRecordP p = null;
        if (b.getBookrecords() != null) {
            p = GetMaxUtil.getLastBookRecord(b);
        }
        return p;
    }

    public static BookRecordP getBookR(ISlotSignalContext slContext) {
        IVModelData mData = slContext.getVData();
        return getBookR(mData);
    }

    static AdvancePaymentP getAdvanced(ISlotSignalContext slContext) {
        IVModelData mData = slContext.getVData();
        HModelData vData = (HModelData) mData;
        BookingP b = (BookingP) vData.getA();//
        AdvancePaymentP pa = null;
        if (b.getBill() != null) {
            pa = GetMaxUtil.getLastValidationRecord(b);
        }
        return pa;
    }

}
