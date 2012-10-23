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
    private static final String GET_PAGE_SIZE = "TABLE_PUBLIC_GET_PAGE_SIZE";
    private static final String SET_PAGE_SIZE = "TABLE_PUBLIC_SET_PAGE_SIZE";
    private static final String GETISTREEVIEWNOW = "TABLE_PUBLIC_GET_TABLE_TREE_VIEW_SIGNAL_NOW";
    private static final String GETISTABLETREEENABLED = "TABLE_PUBLIC_GET_TABLE_TREE_ENABLED";
    private static final String GETISTABLEFILTER = "TABLE_PUBLIC_GET_TABLE_IS_FILTER";
    private static final String GETISTABLESORTED = "TABLE_PUBLIC_GET_TABLE_IS_SORTED";
    private static final String GETVSIGNAL = "TABLE_PUBLIC_LISTDATAVIEW-GETVSIGNAL";

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

    public static CustomStringSlot constructGetPageSizeSignal(IDataType dType) {
        return new CustomStringDataTypeSlot(GET_PAGE_SIZE, dType);
    }

    public static SlotType constructSetPageSizeSignal(IDataType dType) {
        CustomStringSlot slo = new CustomStringDataTypeSlot(SET_PAGE_SIZE,
                dType);
        SlotTypeFactory tFactory = GwtGiniInjector.getI().getTablesFactories()
                .getSlTypeFactory();
        return tFactory.construct(slo);
    }

    public static CustomStringSlot constructSlotGetTreeView(IDataType dType) {
        return new CustomStringDataTypeSlot(GETISTREEVIEWNOW, dType);
    }

    public static CustomStringSlot constructSlotGetTableTreeEnabled(
            IDataType dType) {
        return new CustomStringDataTypeSlot(GETISTABLETREEENABLED, dType);
    }

    public static CustomStringSlot constructSlotGetTableIsFilter(IDataType dType) {
        return new CustomStringDataTypeSlot(GETISTABLEFILTER, dType);
    }

    public static CustomStringSlot constructSlotGetTableIsSorted(IDataType dType) {
        return new CustomStringDataTypeSlot(GETISTABLESORTED, dType);
    }

    public static CustomStringSlot constructSlotGetVSignal(IDataType dType) {
        return new CustomStringDataTypeSlot(GETVSIGNAL, dType);
    }
}
