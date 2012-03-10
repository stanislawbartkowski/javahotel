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
package com.gwtmodel.table.view.table;

import com.gwtmodel.table.IVField;
import java.util.List;

/**
 * @author hotel
 * 
 */
public class ChangeEditableRowsParam {

    private final int i;
    private final boolean editable;
    private final List<IVField> eList;

    /**
     * @param i
     * @param editable
     * @param eList
     */
    public ChangeEditableRowsParam(int i, boolean editable, List<IVField> eList) {
        this.i = i;
        this.editable = editable;
        this.eList = eList;
    }

    /**
     * @return the i
     */
    int getI() {
        return i;
    }

    /**
     * @return the editable
     */
    boolean isEditable() {
        return editable;
    }

    /**
     * @return the eList
     */
    List<IVField> geteList() {
        return eList;
    }

}
