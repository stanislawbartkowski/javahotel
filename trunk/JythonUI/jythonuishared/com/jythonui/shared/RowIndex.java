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
package com.jythonui.shared;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hotel
 *
 */
/**
 * This class is not transportable and can contain final attributes
 * 
 * @author hotel
 * 
 */
public class RowIndex {

    private final List<FieldItem> colList;
    private final Map<String, Integer> iMap = new HashMap<String, Integer>();
    private final FieldItem[] fArray;

    public RowIndex(List<FieldItem> colList) {
        this.colList = colList;
        int i = 0;
        fArray = new FieldItem[rowSize()];
        for (FieldItem f : colList) {
            iMap.put(f.getId(), new Integer(i));
            fArray[i] = f;
            i++;
        }
    }

    public int rowSize() {
        return colList.size();
    }

    public FieldItem getI(int i) {
        return fArray[i];
    }

    public FieldItem getI(String id) {
        return fArray[iMap.get(id)];
    }
    
    public boolean isField(String id) {
        return iMap.containsKey(id);
    }

    public FieldValue get(RowContent r, String s) {
        return r.getRow(iMap.get(s));
    }

    public void setRowField(RowContent r, String id, FieldValue v) {
        r.setRow(iMap.get(id), v);
    }

    public RowContent constructRow() {
        RowContent r = new RowContent();
        r.setRowSize(rowSize());
        for (int i = 0; i < fArray.length; i++) {
            FieldValue v = new FieldValue();
            v.setValue(fArray[i].getFieldType(), null,fArray[i].getAfterDot());
            r.setRow(i, v);
        }
        return r;
    }

}