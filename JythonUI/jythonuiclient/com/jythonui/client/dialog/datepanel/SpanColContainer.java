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
package com.jythonui.client.dialog.datepanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gwtmodel.table.MutableInteger;

class SpanColContainer {

    private final Map<Integer, Map<Integer, Integer>> map = new HashMap<Integer, Map<Integer, Integer>>();

    void clear() {
        map.clear();
    }

    void addSpanInfo(MutableInteger rowNo, int colNo, int spanNo) {
        Map<Integer, Integer> cList = map.get(rowNo.intValue());
        if (cList == null) {
            cList = new HashMap<Integer, Integer>();
            map.put(rowNo.intValue(), cList);
        }
        cList.put(colNo, spanNo);
    }

    int recalculateCol(MutableInteger rowNo, int col) {
        Map<Integer, Integer> cList = map.get(rowNo.intValue());
        if (cList == null)
            return col;
        int colP = 0;
        for (int c = 0; c < col; c++) {
            if (cList.get(colP) != null) {
                colP += cList.get(colP);
            } else
                colP++;
        }
        return colP;
    }

}
