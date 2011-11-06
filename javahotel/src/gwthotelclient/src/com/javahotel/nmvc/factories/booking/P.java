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
import com.javahotel.common.toobject.BookingP;

/**
 * @author hotel
 * 
 */
public class P {

    private P() {

    }

    static BookingP getBookR(IVModelData mData) {
        BookingP b = DataUtil.getData(mData);
        return b;
    }

    public static BookingP getBookR(ISlotSignalContext slContext) {
        IVModelData mData = slContext.getVData();
        return getBookR(mData);
    }

}
