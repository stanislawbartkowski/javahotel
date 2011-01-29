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
package com.javahotel.client.mvc.checktable.view;

import java.util.List;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.GridCellType;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.mvc.gridmodel.model.GridModelViewFactory;
import com.javahotel.client.mvc.gridmodel.model.IGridBaseModel;
import com.javahotel.client.mvc.gridmodel.model.IGridModelView;
import com.javahotel.client.mvc.gridmodel.model.view.ColsHeader;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
abstract class AbstractCheckTable implements IGridBaseModel {

    protected final IGridModelView iModel;
    protected final IResLocator rI;

    AbstractCheckTable(IResLocator rI, GridCellType c) {
        this.rI = rI;
        GridModelViewFactory fa = HInjector.getI().getGridViewFactory();
//        
//        iModel = GridModelViewFactory.getModel(rI, c);
        iModel = fa.getModel(c);
    }

    public void reset() {
        iModel.reset();
    }

    public void setRows(List<String> rows) {
        iModel.setRows(rows);
    }

    public void setEnable(boolean enable) {
        iModel.setEnable(enable);
    }

    public void setCols(ColsHeader rowTitle,List<ColsHeader> cols) {
        iModel.setCols(rowTitle,cols);
    }

    public List<String> getSRow() {
        return iModel.getSRow();
    }

    public List<ColsHeader> getSCol() {
        return iModel.getSCol();
    }

    public int RowNo() {
        return iModel.RowNo();
    }

    public int ColNo() {
        return iModel.ColNo();
    }

    public IMvcWidget getMWidget() {
        return iModel.getMWidget();
    }

    public void show() {
        iModel.show();
    }

    public void hide() {
        iModel.hide();
    }
}
