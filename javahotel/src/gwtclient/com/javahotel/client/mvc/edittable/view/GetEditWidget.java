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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.ifield.ILineField;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.mvc.crud.controler.ICrudControler;
import com.javahotel.client.mvc.crud.controler.ICrudRecordFactory;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.client.mvc.record.model.IRecordDef;
import com.javahotel.client.mvc.record.model.RecordDefFactory;
import com.javahotel.client.mvc.record.model.RecordField;
import com.javahotel.client.mvc.record.view.IRecordView;
import com.javahotel.client.mvc.record.view.RecordViewFactory;
import com.javahotel.client.mvc.table.model.ITableModel;
import com.javahotel.client.mvc.table.view.IGetWidgetTableView;
import com.javahotel.client.mvc.table.view.ITableView;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.IField;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class GetEditWidget implements IGetWidgetTableView {

    private final ITableModel model;
    private final IResLocator rI;
    private final DictData da;
    private final Map<Integer, ST> ma;
    private final Set<IField> notEditable;
    private final IGetWidgetTableView customW;

    private class ST {

        IRecordView iView;
        ICrudRecordFactory fa;
    }

    ICrudRecordFactory getFactory(int row) {
        return ma.get(row).fa;
    }

    IRecordView getR(int row) {
        return ma.get(row).iView;
    }

    public void ExtractFields(final int row, final RecordModel mo) {
        IRecordView v = getR(row);
        ICrudRecordFactory fa = getFactory(row);
        v.extractFields(mo);
        AbstractTo a = model.getRow(row);
        for (RecordField re : fa.getDef()) {
            IField fi = re.getFie();
            if (notEditable.contains(fi)) {
                Object val = a.getF(fi);
                mo.getA().setF(fi, val);
            }
        }
    }

    GetEditWidget(final IResLocator rI, final DictData da,
            final ITableModel model, final IGetWidgetTableView customW) {
        this.rI = rI;
        this.model = model;
        this.da = da;
        this.customW = customW;
        ma = new HashMap<Integer, ST>();
        notEditable = HInjector.getI().getColListFactory().getNoEditableColList(da);
    }

    public Widget getWidget(final ITableView v, final int row,
            final int col, final String val) {
        if (customW != null) {
            Widget w = customW.getWidget(v, row, col, val);
            if (w != null) {
                return w;
            }
        }
        ST st = ma.get(row);
        if (st == null) {
            st = new ST();
            ICrudControler iCrud = HInjector.getI().getDictCrudControlerFactory().getCrud(da);
            st.fa = iCrud.getF();
            List<RecordField> li = st.fa.getDef();
            IRecordDef def = RecordDefFactory.getRecordDef(rI, "", li);
            st.iView = RecordViewFactory.getTableViewRecord(rI, null,def);
            ma.put(row, st);
        }
        IField fi = model.getCol(col);
        if (notEditable.contains(fi)) {
            Label l = new Label(val);
            return l;
        }
        for (RecordField re : st.iView.getModel().getFields()) {
            if (re.getFie() == fi) {
                ILineField e = re.getELine();
                e.setVal(val);
                return e.getMWidget().getWidget();
            }
        }
        return null;
    }
}
