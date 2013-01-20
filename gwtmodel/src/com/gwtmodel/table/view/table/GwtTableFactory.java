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
package com.gwtmodel.table.view.table;

import com.gwtmodel.table.ICommand;
import com.gwtmodel.table.injector.WebPanelHolder;

public class GwtTableFactory {

    public IGwtTableView construct(IRowClick click, ICommand actionColumn,
            IGetCellValue gValue, INewEditLineFocus iLineFocus, ILostFocusEdit lostFocus,
            IColumnImage iImage) {
        switch (WebPanelHolder.getTableType()) {
            case GOOGLETABLE:
                return new GwtTableView(click);
            case GRIDTABLE:
                return new TableView(click);
            case PRESETABLE:
                return new PresentationTable(click, actionColumn, gValue,
                        iLineFocus, lostFocus, iImage);
        }
        return null;
    }

    public IGwtTableView constructTree(IRowClick click) {
        return new PresentationTree(click);
    }
}