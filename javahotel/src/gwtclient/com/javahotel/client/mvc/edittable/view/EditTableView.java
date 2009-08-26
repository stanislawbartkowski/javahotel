/*
 * Copyright 2009 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.mvc.edittable.view;

import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.mvc.contrpanel.view.IContrButtonView;
import com.javahotel.client.mvc.crud.controler.ICrudRecordFactory;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.record.view.IRecordView;
import com.javahotel.client.mvc.table.model.ITableModel;
import com.javahotel.client.mvc.table.view.IGetWidgetTableView;
import com.javahotel.client.mvc.table.view.ITableSetField;
import com.javahotel.client.mvc.table.view.TableViewFactory;
import com.javahotel.client.mvc.table.view.ITableView;
import com.javahotel.client.mvc.table.view.ITableCallBackSetField;
import com.javahotel.common.toobject.IField;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class EditTableView implements IEditTableView {

    private final IResLocator rI;
    private final ITableModel model;
    private final DictData da;
    private final GetEditWidget gWidget;
    private final ITableView iView;

    public IRecordView getR(int row) {
        return gWidget.getR(row);
    }

    public ICrudRecordFactory getFactory(int row) {
        return gWidget.getFactory(row);
    }

    public void ExtractFields(int row, RecordModel mo) {
        gWidget.ExtractFields(row, mo);
    }

    public IMvcWidget getMWidget() {
        return iView.getMWidget();
    }

    private class SetVal implements ITableCallBackSetField {

        public void CallSetField(ITableModel a, int row, IField f, ITableSetField iSet) {
            iSet.setField("");
        }
    }

    EditTableView(IResLocator rI, DictData da, final IContrButtonView cView,
            ITableModel model, IGetWidgetTableView customW) {
        this.rI = rI;
        this.da = da;
        this.model = model;
        gWidget = new GetEditWidget(rI, da, model, customW);
        iView = TableViewFactory.getGridView(rI, da, cView, model, null,
                new SetVal(), gWidget);
    }

    public void drawTable() {
        iView.drawTable();
    }

    public ITableModel getModel() {
        return model;
    }

    public void show() {
    }

    public void hide() {
    }
}
