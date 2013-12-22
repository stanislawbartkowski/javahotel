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

import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IGetSetVField;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.WChoosedLine;
import com.gwtmodel.table.WSize;
import java.util.List;

public interface IGwtTableView extends IGWidget {
    
    void refresh();

    WChoosedLine getClicked();

    WSize getRowWidget(int rowno);

    void setClicked(int clickedno, boolean whileFind);

    IGwtTableModel getViewModel();

    void setModel(IGwtTableModel model);

    void setModifyRowStyle(IModifyRowStyle iMod);

    void setEditable(ChangeEditableRowsParam eParam);

    List<IGetSetVField> getVList(int rowno);

    void removeSort();

    boolean isSorted();

    void setPageSize(int pageSize);

    int getPageSize();

    void removeRow(int rownum);

    void addRow(int rownum);

    void showInvalidate(int rowno, InvalidateFormContainer errContainer);

    void setNoWrap(boolean noWrap);

    boolean isNoWrap();

    void setSortColumn(IVField col, boolean inc);

    void refreshFooter(IVModelData footer);

    void refreshHeader();

}
