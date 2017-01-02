/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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
package com.gwtmodel.table.view.table.edit;

import com.google.gwt.user.cellview.client.Column;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.MutableInteger;
import com.gwtmodel.table.tabledef.VListHeaderDesc;
import com.gwtmodel.table.view.table.ChangeEditableRowsParam;
import com.gwtmodel.table.view.table.IGwtTableModel;
import com.gwtmodel.table.view.table.util.ErrorLineInfo;
import com.gwtmodel.table.view.table.util.IToRowNo;

public interface IPresentationCellEdit {

    ErrorLineInfo getErrorInfo();

    ChangeEditableRowsParam geteParam();

    @SuppressWarnings("rawtypes")
    Column constructEditTextCol(VListHeaderDesc he);

    @SuppressWarnings("rawtypes")
    Column constructNumberCol(VListHeaderDesc he);

    @SuppressWarnings("rawtypes")
    Column constructDateEditCol(VListHeaderDesc he);

    @SuppressWarnings("rawtypes")
    Column contructBooleanCol(IVField v, boolean handleSelection);

    void setModel(IGwtTableModel model);

    void addActionColumn();

    void setEditable(ChangeEditableRowsParam eParam);

    void setErrorLineInfo(MutableInteger key,
            InvalidateFormContainer errContainer, IToRowNo i);

}
