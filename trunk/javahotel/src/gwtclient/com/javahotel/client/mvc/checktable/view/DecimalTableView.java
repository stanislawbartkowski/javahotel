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
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class DecimalTableView extends AbstractCheckTable implements IDecimalTableView {

    DecimalTableView(IResLocator rI) {
        super(rI, GridCellType.NUMERIC);
    }

    public void setRowVal(int row, ArrayList<BigDecimal> vals) {
        iModel.setRowVal(row, vals);
    }

    public void setColVal(int col, ArrayList<BigDecimal> vals) {
        iModel.setColVal(col, vals);
    }

    public ArrayList<BigDecimal> getRows(int row) {
        return (ArrayList<BigDecimal>) iModel.getRows(row);
    }

    public ArrayList<BigDecimal> getCols(int col) {
        return (ArrayList<BigDecimal>) iModel.getCols(col);
    }

}
