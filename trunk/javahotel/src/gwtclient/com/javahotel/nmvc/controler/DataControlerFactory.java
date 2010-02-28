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
package com.javahotel.nmvc.controler;

import com.gwtmodel.table.controler.TableDataControlerFactory;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.javahotel.client.IResLocator;
import com.javahotel.nmvc.common.DataType;
import com.javahotel.nmvc.controler.impl.ClearHotelData;

public class DataControlerFactory {

    private DataControlerFactory() {
    }

    public static ISlotable constructDataControler(IResLocator rI,
            DataControlerEnum cEnum, DataType dType, CellId panelId) {
        switch (cEnum) {
        case DisplayList:
            TableDataControlerFactory tFactory = GwtGiniInjector.getI()
                    .getTableDataControlerFactory();
            return tFactory.constructDataControler(dType, panelId);
        case ClearDataHotel:
            return new ClearHotelData(rI, panelId);
        }
        return null;
    }
}
