/*
 * Copyright 2011 stanislawbartkowski@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.ibm.sampledb.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.ibm.sampledb.shared.IRecord;
import com.ibm.sampledb.shared.GetField;
import com.ibm.sampledb.shared.GetField.FieldInfo;
import com.ibm.sampledb.shared.GetField.FieldType;
import com.ibm.sampledb.shared.GetField.FieldValue;
import com.ibm.sampledb.shared.ResourceInfo;

public class SampleGwt implements EntryPoint {

    /**
     * Create a remote service proxy to talk to the server-side Greeting
     * service.
     */
    private final SampleServiceAsync sampleService = GWT
            .create(SampleService.class);

    private final Message message = GWT.create(Message.class);

    private class CDraw implements DockMain.IAction {

        @Override
        public void execute() {
            drawTable();
        }
    }

    private class PrintR implements AsyncCallback<String> {

        @Override
        public void onFailure(Throwable caught) {
            String mess = message.ErrorMessage();
            HTML ha = new HTML(mess);
            dock.drawError(ha);
        }

        @Override
        public void onSuccess(String result) {
            lPrint.launchPrint(result);
        }

    }

    private class APrint implements DockMain.IAction {

        @Override
        public void execute() {
            List<IRecord> li = new ArrayList<IRecord>();
            for (IRecord e : dock.getList()) {
                li.add(e);
            }
            sampleService.printList(li, new PrintR());
        }

    }

    private final DockMain dock = new DockMain(new CDraw(),
            new APrint());
    private final LaunchPrint lPrint = new LaunchPrint();

    private class EDateColumn extends Column<IRecord, Date> {

        private final String field;

        EDateColumn(DateCell d, String keyName) {
            super(d);
            this.field = keyName;
        }

        @Override
        public Date getValue(IRecord object) {
            Date d = GetField.getValue(field, object).getdField();
            return d;
        }
    }

    private class ENumberColumn extends Column<IRecord, Number> {

        private final FieldInfo f;

        ENumberColumn(NumberCell d, FieldInfo field) {
            super(d);
            this.f = field;
        }

        @Override
        public Number getValue(IRecord object) {
            Number n;
            FieldValue val = GetField.getValue(f.getfId(), object);
            if (f.getfType() == FieldType.INTEGER) {
                n = val.getiField();
            } else {
                n = val.getnField();
            }

            return n;
        }
    }

    private Column constructDate(String keyName) {
        DateTimeFormat f = DateTimeFormat.getFormat("yyyy/MM/dd");
        DateCell d = new DateCell(f);
        return new EDateColumn(d, keyName);
    }

    private Column constructNumber(FieldInfo f) {
        NumberCell d = new NumberCell();
        return new ENumberColumn(d, f);
    }

    private Column constructInteger(FieldInfo f) {
        NumberFormat fo = NumberFormat.getFormat("#####");
        NumberCell d = new NumberCell(fo);
        return new ENumberColumn(d, f);
    }

    private class ETextColumn extends TextColumn<IRecord> {

        private final String field;

        ETextColumn(String keyName) {
            this.field = keyName;
        }

        @Override
        public String getValue(IRecord e) {

            String val = GetField.getValue(field, e).getsField();

            return val;
        }

    }

    class PutResource implements AsyncCallback<ResourceInfo> {

        @Override
        public void onFailure(Throwable caught) {
            String mess = message.ErrorMessage();
            HTML ha = new HTML(mess);
            dock.drawError(ha);
        }

        @Override
        public void onSuccess(ResourceInfo result) {
            dock.setResorceInfo(result);
            lPrint.setrInfo(result);
        }

    }

    class DrawList implements AsyncCallback<List<? extends IRecord>> {

        @Override
        public void onFailure(Throwable caught) {
            String mess = message.ErrorMessage();
            HTML ha = new HTML(mess);
            dock.drawError(ha);
        }

        @Override
        public void onSuccess(List<? extends IRecord> result) {
            CellTable<IRecord> table = dock.getTable();

            if (table.getColumnCount() == 0) {
                table.setWidth("95%", true);
                for (FieldInfo f : GetField.getfList()) {
                    String colKey = f.getfId();
                    Column co = null;
                    switch (f.getfType()) {
                    case NUMBER:
                        co = constructNumber(f);
                        break;
                    case DATE:
                        co = constructDate(colKey);
                        break;
                    case INTEGER:
                        co = constructInteger(f);
                        break;
                    case STRING:
                        co = new ETextColumn(colKey);
                        break;
                    }

                    table.addColumn(co, f.getfDescr());
                    table.setColumnWidth(co, f.getcSize(), Unit.PCT);
                }
            }
            table.setRowCount(result.size(), true);
            dock.drawList(result);
        }

    }

    private void drawTable() {
        ListBox lb = dock.getListBox();
        String orderBy = null;
        int sel = lb.getSelectedIndex();
        String val = lb.getItemText(sel);

        // extract ORDER BY value for sorting
        for (FieldInfo f : GetField.getfList()) {
            if (val.equals(f.getfDescr())) {
                orderBy = f.getfId();
            }
        }

        if (orderBy != null) {
            if (!dock.isIncOrder()) {
                orderBy += " DESC";
            }
        }
        lPrint.setOrderBy(orderBy);
        sampleService.getList(orderBy, new DrawList());
    }

    @Override
    public void onModuleLoad() {
        RootLayoutPanel.get().add(dock);
        drawTable();
        sampleService.getInfo(new PutResource());
    }

}
