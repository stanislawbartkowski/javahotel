/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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

import java.util.List;

import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IGetSetVField;
import com.gwtmodel.table.WChoosedLine;

public interface IGwtTableView extends IGWidget {

    void refresh();

    WChoosedLine getClicked();

    void setClicked(int clickedno);

    IGwtTableModel getViewModel();

    void setModel(IGwtTableModel model);

    void setModifyRowStyle(IModifyRowStyle iMod);

    void setEditable(ChangeEditableRowsParam eParam);
    
    List<IGetSetVField> getVList(int rowno);

}
