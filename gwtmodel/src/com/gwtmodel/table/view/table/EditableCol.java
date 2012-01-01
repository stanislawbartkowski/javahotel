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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVFieldSetFactory;

/**
 * @author hotel
 * 
 */
class EditableCol {

    private final Map<Integer, Set<IVField>> eList = new HashMap<Integer, Set<IVField>>();

    boolean isEditable(Integer i, IVField v) {
        Set<IVField> se = eList.get(i);
        if (se == null) {
            return false;
        }
        return se.contains(v);
    }

    void addEditData(ChangeEditableRowsParam eParam) {
        Set<IVField> se = eList.get(eParam.getI());
        List<IVField> vl = eParam.geteList();
        if (!eParam.isEditable()) {
            if (se == null) {
                return;
            }
            se.removeAll(vl);
            if (se.isEmpty()) {
                eList.remove(eParam.getI());
            }
            return;
        }
        if (se == null) {
            se = IVFieldSetFactory.construct();
            se.addAll(vl);
            eList.put(eParam.getI(), se);
        }
        se.addAll(vl);
    }

}
