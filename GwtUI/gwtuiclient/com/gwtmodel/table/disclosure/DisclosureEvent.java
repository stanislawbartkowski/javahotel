/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.disclosure;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.slotmodel.CustomObjectValue;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.ISlotCustom;

public class DisclosureEvent extends CustomObjectValue<Boolean> {

    DisclosureEvent(Boolean open) {
        super(open);
    }

    private static final String DISCLOSURE_EVENT = DisclosureEvent.class
            .getName() + "DISCLOSURE_EVENT";

    public static ISlotCustom constructSlot(IDataType dType) {
        return new CustomStringDataTypeSlot(dType, DISCLOSURE_EVENT);
    }
}