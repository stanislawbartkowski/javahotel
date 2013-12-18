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

import com.gwtmodel.table.ISuccess;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.tabledef.VListHeaderContainer;

public interface IGwtTableModel {

    void readChunkRange(int startw, int rangew, IVField sortC, boolean asc,
            ISuccess signal);

    IVModelData get(int row);

    long getSize();

    VListHeaderContainer getHeaderList();

    IListClicked getIClicked();

    boolean containsData();

    boolean unSelectAtOnce();

    int treeLevel(int row);

    IRowEditAction getRowEditAction();

    String getClassName();

    String getClassNameForColumn(IVField v);
}
