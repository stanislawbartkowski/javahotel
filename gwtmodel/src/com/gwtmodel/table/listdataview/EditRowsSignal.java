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

import com.gwtmodel.table.ICustomObject;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.slotmodel.CustomStringDataTypeSlot;
import com.gwtmodel.table.slotmodel.CustomStringSlot;
import com.gwtmodel.table.slotmodel.SlotType;
import com.gwtmodel.table.slotmodel.SlotTypeFactory;
import com.gwtmodel.table.view.table.ChangeEditableRowsParam;
import java.util.List;

/**
 * @author hotel
 * 
 */
public class EditRowsSignal extends ChangeEditableRowsParam implements
        ICustomObject {

    /**
     * @param i
     * @param editable
     * @param eList
     */
    public EditRowsSignal(int row, boolean editable, ModifMode mode,
            List<IVField> eList) {
        super(row, editable, mode, eList);
    }
    
    private static final String EDIT_SIGNAL =  EditRowsSignal.class.getName() + "TABLE_PUBLIC_DATALIST_ENABLE_SIGNAL_ROW";
    
    public static SlotType constructEditRowSignal(IDataType dType) {
        CustomStringSlot slo = new CustomStringDataTypeSlot(EDIT_SIGNAL, dType);
        SlotTypeFactory tFactory = GwtGiniInjector.getI().getTablesFactories()
                .getSlTypeFactory();
        return tFactory.construct(slo);
    }


}
