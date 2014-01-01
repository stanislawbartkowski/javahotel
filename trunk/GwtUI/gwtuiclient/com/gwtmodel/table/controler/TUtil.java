/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.controler;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.listdataview.IsBooleanSignalNow;
import com.gwtmodel.table.slotmodel.CustomStringSlot;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotable;

/**
 * @author hotel
 * 
 */
class TUtil {

    private TUtil() {
    }

    static boolean isBoolProp(ISlotable publishSlo, CustomStringSlot slType) {
        ISlotSignalContext rContext = publishSlo.getSlContainer()
                .getGetterCustom(slType);
        IsBooleanSignalNow ret = (IsBooleanSignalNow) rContext.getCustom();
        return ret.isBoolInfo();

    }

    static boolean isTreeView(ISlotable publishSlo, IDataType ddType) {
        return TUtil.isBoolProp(publishSlo,
                IsBooleanSignalNow.constructSlotGetTreeView(ddType));
    }

}
