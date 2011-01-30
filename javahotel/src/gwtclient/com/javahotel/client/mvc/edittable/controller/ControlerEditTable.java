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
package com.javahotel.client.mvc.edittable.controller;

import java.util.ArrayList;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.mvc.contrpanel.view.IContrButtonView;
import com.javahotel.client.mvc.crud.controler.ICrudRecordFactory;
import com.javahotel.client.mvc.edittable.view.EditTableViewFactory;
import com.javahotel.client.mvc.edittable.view.IEditTableView;
import com.javahotel.client.mvc.table.model.ColTitle;
import com.javahotel.client.mvc.table.model.ITableModel;
import com.javahotel.common.command.DictType;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class ControlerEditTable implements IControlerEditTable {

    private final IResLocator rI;
    private final DictData da;
    private final IEditTableView iView;
    private final ResGuestWidget r;

    ControlerEditTable(IResLocator rI, DictData da, final IContrButtonView cView) {
        this.rI = rI;
        this.da = da;
        ArrayList<ColTitle> c = HInjector.getI().getColListFactory().getEditColList(da);
        ITableModel mo = HInjector.getI().getTableModelFactory().getModel(c,
                null, "");
        r = new ResGuestWidget(rI, new DictData(DictType.CustomerList));
        iView = EditTableViewFactory.getView(rI, da, cView, mo, r);
        r.setIView(iView);
    }

    public IEditTableView getView() {
        return iView;
    }

    public void show() {
        iView.drawTable();
    }

    public void hide() {
    }

    public ICrudRecordFactory getFactory() {
        return null;
    }

    public ICrudRecordFactory getFactory(int row) {
        return r.getFactory(row);
    }

    public IMvcWidget getMWidget() {
        return iView.getMWidget();
    }
}
