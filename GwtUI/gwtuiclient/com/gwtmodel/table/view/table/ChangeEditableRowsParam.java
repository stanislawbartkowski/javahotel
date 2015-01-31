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
package com.gwtmodel.table.view.table;

import com.gwtmodel.table.IVField;
import java.util.List;

/**
 * @author hotel
 * 
 */
public class ChangeEditableRowsParam {

    public enum ModifMode {
        ADDCHANGEDELETEMODE, CHANGEMODE, NORMALMODE
    };

    public static int ALLROWS = -1;

    private final int row;
    private final boolean editable;
    private final List<IVField> eList;
    private final ModifMode mode;

    /**
     * @param i
     * @param editable
     * @param eList
     */
    public ChangeEditableRowsParam(int row, boolean editable, ModifMode mode,
            List<IVField> eList) {
        this.row = row;
        this.editable = editable;
        this.eList = eList;
        this.mode = mode;
    }

    /**
     * @return the editable
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     * @return the eList
     */
    public List<IVField> geteList() {
        return eList;
    }

    /**
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return the mode
     */
    ModifMode getMode() {
        return mode;
    }

    public boolean fullEdit() {
        return mode != ModifMode.NORMALMODE;
    }

}
