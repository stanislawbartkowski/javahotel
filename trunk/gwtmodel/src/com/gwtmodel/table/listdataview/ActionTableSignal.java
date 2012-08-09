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
package com.gwtmodel.table.listdataview;

import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.CustomStringSlot;
import com.gwtmodel.table.slotmodel.SlotType;
import com.gwtmodel.table.slotmodel.SlotTypeFactory;

/**
 * @author hotel
 * 
 */
public class ActionTableSignal {

    private static final String CHANGE_TO_TABLE_SIGNAL = "TABLE_PUBLIC_CHANGE_TO_TABLE_SIGNAL";
    private static final String CHANGE_TO_TREE_SIGNAL = "TABLE_PUBLIC_CHANGE_TO_TREE_SIGNAL";
    private static final String REMOVE_SORT = "TABLE_PUBLIC_REMOVE_SORT";

    private ActionTableSignal() {
    }

    public static SlotType constructToTableSignal(IDataType dType) {
        CustomStringSlot slo = new CustomStringDataTypeSlot(
                CHANGE_TO_TABLE_SIGNAL, dType);
        SlotTypeFactory tFactory = GwtGiniInjector.getI().getTablesFactories()
                .getSlTypeFactory();
        return tFactory.construct(slo);
    }

    public static SlotType constructToTreeSignal(IDataType dType) {
        CustomStringSlot slo = new CustomStringDataTypeSlot(
                CHANGE_TO_TREE_SIGNAL, dType);
        SlotTypeFactory tFactory = GwtGiniInjector.getI().getTablesFactories()
                .getSlTypeFactory();
        return tFactory.construct(slo);
    }

    public static SlotType constructRemoveSortSignal(IDataType dType) {
        CustomStringSlot slo = new CustomStringDataTypeSlot(REMOVE_SORT, dType);
        SlotTypeFactory tFactory = GwtGiniInjector.getI().getTablesFactories()
                .getSlTypeFactory();
        return tFactory.construct(slo);
    }

}
