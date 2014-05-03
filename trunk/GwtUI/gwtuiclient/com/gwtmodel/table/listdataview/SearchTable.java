/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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
import com.gwtmodel.table.IOkModelData;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.WChoosedLine;
import com.gwtmodel.table.injector.LogT;
import com.gwtmodel.table.view.table.IGwtTableView;

public class SearchTable {

    private SearchTable() {
    }

    public static boolean search(IDataType dType, IOkModelData iOk,
            IGwtTableView tableView, boolean begin, boolean next) {
        assert iOk != null : LogT.getT().FilterCannotbeNull();
        WChoosedLine w = tableView.getClicked();
        int aLine = -1;
        if (w.isChoosed() && !begin) {
            aLine = w.getChoosedLine() - 1;
        }
        if (next) {
            aLine++;
        }
        boolean found = false;
        // order in while predicate evaluation is important !
        while (!found && (++aLine < tableView.getViewModel().getSize())) {
            IVModelData v = tableView.getViewModel().get(aLine);
            found = iOk.OkData(v);
        }
        if (!found)
            return false;
        tableView.setClicked(aLine, true);
        return true;
    }

}
