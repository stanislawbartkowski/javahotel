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

import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.mvc.auxabstract.ResRoomGuest;
import com.javahotel.client.mvc.table.view.IGetWidgetTableView;
import com.javahotel.client.mvc.table.view.ITableView;
import com.javahotel.common.toobject.IField;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DictData;
import com.javahotel.client.mvc.controller.onearecord.IOneARecord;
import com.javahotel.client.mvc.crud.controler.ICrudRecordFactory;
import com.javahotel.client.mvc.dictcrud.controler.DictCrudIOneRecordFactory;
import com.javahotel.client.mvc.edittable.view.IEditTableView;
import java.util.HashMap;
import java.util.Map;
import com.javahotel.client.mvc.record.view.IRecordView;
import com.javahotel.client.mvc.table.model.ITableFilter;
import com.javahotel.common.command.CustomerType;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.CustomerP;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class ResGuestWidget implements IGetWidgetTableView {

    private final IResLocator rI;
    private IEditTableView iView;
    private final Map<Integer, IOneARecord> ma;
    private final DictData da;

    ICrudRecordFactory getFactory(int row) {
        IOneARecord a = ma.get(row);
        return a.getFactory();
    }

    ResGuestWidget(IResLocator rI, DictData da) {
        this.rI = rI;
        ma = new HashMap<Integer, IOneARecord>();
        this.da = da;
    }

    public Widget getWidget(ITableView view, int row, int col, String val) {
        IField fi = view.getModel().getCol(col);
        if (fi != ResRoomGuest.F.choosebutt) {
            return null;
        }
        IOneARecord a = ma.get(row);
        if (a == null) {
            IRecordView iV = iView.getR(row);
            IRecordView cView = HInjector.getI().getDictCrudIOneRecordFactory()
                    .createNViewCopy(da, iV);
            a = HInjector.getI().getDictCrudIOneRecordFactory().getTableLineR(
                    da, cView, new F());
            ma.put(row, a);
        }
        return a.getMWidget().getWidget();
    }

    /**
     * @param iView
     *            the iView to set
     */
    public void setIView(IEditTableView iView) {
        this.iView = iView;
    }

    private class F implements ITableFilter {

        public boolean isOk(AbstractTo a) {
            CustomerP c = (CustomerP) a;
            return (c.getCType() == CustomerType.Person);
        }
    }
}
