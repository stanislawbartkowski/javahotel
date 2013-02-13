/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
    private static final String GET_PAGE_SIZE = "TABLE_PUBLIC_GET_PAGE_SIZE";
    private static final String SET_PAGE_SIZE = "TABLE_PUBLIC_SET_PAGE_SIZE";

    private ActionTableSignal() {
    }

    public static SlotType constructToTableSignal(IDataType dType) {
        CustomStringSlot slo = new CustomStringDataTypeSlot(dType,
                CHANGE_TO_TABLE_SIGNAL);
        SlotTypeFactory tFactory = GwtGiniInjector.getI().getTablesFactories()
                .getSlTypeFactory();
        return tFactory.construct(slo);
    }

    public static SlotType constructToTreeSignal(IDataType dType) {
        CustomStringSlot slo = new CustomStringDataTypeSlot(dType,
                CHANGE_TO_TREE_SIGNAL);
        SlotTypeFactory tFactory = GwtGiniInjector.getI().getTablesFactories()
                .getSlTypeFactory();
        return tFactory.construct(slo);
    }

    public static SlotType constructRemoveSortSignal(IDataType dType) {
        CustomStringSlot slo = new CustomStringDataTypeSlot(dType, REMOVE_SORT);
        SlotTypeFactory tFactory = GwtGiniInjector.getI().getTablesFactories()
                .getSlTypeFactory();
        return tFactory.construct(slo);
    }

    public static CustomStringSlot constructGetPageSizeSignal(IDataType dType) {
        return new CustomStringDataTypeSlot(dType, GET_PAGE_SIZE);
    }

    public static SlotType constructSetPageSizeSignal(IDataType dType) {
        CustomStringSlot slo = new CustomStringDataTypeSlot(dType,
                SET_PAGE_SIZE);
        SlotTypeFactory tFactory = GwtGiniInjector.getI().getTablesFactories()
                .getSlTypeFactory();
        return tFactory.construct(slo);
    }


}
