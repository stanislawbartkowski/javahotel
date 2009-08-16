/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.mvc.checktable.view;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.GridCellType;
import java.util.ArrayList;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class CheckTableView extends AbstractCheckTable implements ICheckTableView {


    CheckTableView(IResLocator rI) {
        super(rI,GridCellType.BOOLEAN);
    }


    public void setRowVal(int row, ArrayList<Boolean> vals) {
        iModel.setRowVal(row, vals);
    }

    public void setColVal(int col, ArrayList<Boolean> vals) {
        iModel.setColVal(col, vals);
    }

    public ArrayList<Boolean> getRows(int row) {
        return (ArrayList<Boolean>) iModel.getRows(row);
    }

    public ArrayList<Boolean> getCols(int col) {
        return (ArrayList<Boolean>) iModel.getCols(col);
    }

}
