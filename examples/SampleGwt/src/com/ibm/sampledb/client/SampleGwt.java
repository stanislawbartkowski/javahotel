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

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.ibm.sampledb.shared.EmployeeRecord;

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

    private final DockMain dock = new DockMain(message.columns(), new CDraw());

    private class EDateColumn extends Column<EmployeeRecord, Date> {

        private final String field;

        EDateColumn(DateCell d, String keyName) {
            super(d);
            this.field = keyName;
        }

        @Override
        public Date getValue(EmployeeRecord object) {
            Date d = null;
            if (field.equals("HIREDATE")) {
                d = object.getHiredate();
            } else if (field.equals("BIRTHDATE")) {
                d = object.getBirthdate();
            }
            ;
            return d;
        }
    }

    private class ENumberColumn extends Column<EmployeeRecord, Number> {

        private final String field;

        ENumberColumn(NumberCell d, String keyName) {
            super(d);
            this.field = keyName;
        }

        @Override
        public Number getValue(EmployeeRecord object) {
            Number n = null;
            if (field.equals("SALARY")) {
                n = object.getSalary();
            } else if (field.equals("BONUS")) {
                n = object.getBonus();

            } else if (field.equals("COMM")) {
                n = object.getComm();

            } else if (field.equals("EDLEVEL")) {
                n = object.getEdlevel();
            }
            return n;
        }
    }

    private Column constructDate(String keyName) {
        DateTimeFormat f = DateTimeFormat.getFormat("yyyy/MM/dd");
        DateCell d = new DateCell(f);
        return new EDateColumn(d, keyName);
    }

    private Column constructNumber(String keyName) {
        NumberCell d = new NumberCell();
        return new ENumberColumn(d, keyName);
    }

    private Column constructInteger(String keyName) {
        NumberFormat fo = NumberFormat.getFormat("#####");
        NumberCell d = new NumberCell(fo);
        return new ENumberColumn(d, keyName);
    }

    private class ETextColumn extends TextColumn<EmployeeRecord> {

        private final String field;

        ETextColumn(String keyName) {
            this.field = keyName;
        }

        @Override
        public String getValue(EmployeeRecord e) {

            String val = "";

            if (field.equals("EMPNO")) {
                val = e.getEmpno();
            } else if (field.equals("FIRSTNME")) {
                val = e.getFirstname();
            } else if (field.equals("LASTNAME")) {
                val = e.getLastname();
            } else if (field.equals("WORKDEPT")) {
                val = e.getWorkdept();
            } else if (field.equals("PHONENO")) {
                val = e.getPhoneno();
            } else if (field.equals("SEX")) {
                val = e.getSex();
            } else if (field.equals("MIDINIT")) {
                val = e.getMidinit();
            } else if (field.equals("JOB")) {
                val = e.getJob();
            }

            return val;
        }

    }

    class DrawList implements AsyncCallback<List<EmployeeRecord>> {

        @Override
        public void onFailure(Throwable caught) {
            String mess = message.ErrorMessage();
            HTML ha = new HTML(mess);
            dock.drawError(ha);
        }

        @Override
        public void onSuccess(List<EmployeeRecord> result) {
            CellTable<EmployeeRecord> table = dock.getTable();

            if (table.getColumnCount() == 0) {
                table.setWidth("95%", true);
                Map<String, String> col = message.columns();
                for (Entry<String, String> c : col.entrySet()) {
                    String colKey = c.getKey();
                    String colN = c.getValue();
                    String[] cc = colN.split(",");
                    Column co;
                    if (colKey.equals("SALARY") || colKey.equals("BONUS")
                            || colKey.equals("COMM")) {
                        co = constructNumber(colKey);
                    } else if (colKey.equals("BIRTHDATE")
                            || colKey.equals("HIREDATE")) {
                        co = constructDate(colKey);
                    } else if (colKey.equals("EDLEVEL")) {
                        co = constructInteger(colKey);
                    } else {
                        co = new ETextColumn(colKey);
                    }

                    table.addColumn(co, cc[0]);
                    table.setColumnWidth(co, new Integer(cc[1].trim()),
                            Unit.PCT);
                }
            }
            table.setRowCount(result.size(), true);
            dock.drawList(result);
        }

    }

    private final void drawTable() {
        ListBox lb = dock.getListBox();
        String orderBy = null;
        int sel = lb.getSelectedIndex();
        String val = lb.getItemText(sel);
        Map<String, String> col = message.columns();
        for (Entry<String, String> e : col.entrySet()) {
            String vals = e.getValue();
            String[] cc = vals.split(",");
            if (val.equals(cc[0].trim())) {
                orderBy = e.getKey();
            }
        }

        if (orderBy != null) {
            if (!dock.isIncOrder()) {
                orderBy += " DESC";
            }
        }
        sampleService.getList(orderBy, new DrawList());
    }

    @Override
    public void onModuleLoad() {
        RootLayoutPanel.get().add(dock);
        drawTable();
    }

}
