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

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.Resources;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.ibm.sampledb.shared.EmployeeRecord;
import com.ibm.sampledb.shared.ResourceInfo;

class DockMain extends Composite {

    interface IAction {
        void execute();
    }

    interface MyUiBinder extends UiBinder<Widget, DockMain> {
    }

    private final IAction changeList;

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField
    ListBox listBox;

    @UiField
    RadioButton incRadio;

    @UiField
    RadioButton decRadio;

    @UiField(provided = true)
    SimplePager pager = new SimplePager(TextLocation.LEFT,
            (Resources) GWT.create(SimplePager.Resources.class), false, 0, true);

    @UiField(provided = true)
    final CellTable<EmployeeRecord> table = new CellTable<EmployeeRecord>();

    private final ListDataProvider<EmployeeRecord> dataProvider = new ListDataProvider<EmployeeRecord>();

    public ListBox getListBox() {
        return listBox;
    }

    private ResourceInfo rInfo = null;
    private List<EmployeeRecord> listToDraw = null;

    void setResorceInfo(ResourceInfo r) {
        this.rInfo = r;
        if (r.getCssS() != null) {
            addStyle(r.getCssS());
        }
        if (r.getJavaS() != null) {
            addScript(r.getJavaS());
        }
        if (r.isCustomRow()) {
            table.setRowStyles(new AddStyle());
        }
        drawResList();
    }

    public static native void addScript(String s) /*-{
		$wnd.addScript(s);
    }-*/;

    public static native void addStyle(String s) /*-{
		$wnd.addStyle(s);
    }-*/;

    /*
     * Takes in a trusted JSON String and evals it.
     * 
     * @param JSON String that you trust
     * 
     * @return JavaScriptObject that you can cast to an Overlay Type
     */
    public static native JavaScriptObject evalJson(String jsonStr) /*-{
		return eval(jsonStr);
    }-*/;

    public static JavaScriptObject parseJson(String jsonStr) {
        return evalJson("(" + jsonStr + ")");
    }

    public static native String jsAddStyle(JavaScriptObject o) /*-{
		return $wnd.jsAddStyle(o);
    }-*/;

    private class AddStyle implements RowStyles<EmployeeRecord> {

        @Override
        public String getStyleNames(EmployeeRecord row, int rowIndex) {
            CreateJson js = new CreateJson("Employee");
            js.addElem("firstName", row.getFirstname());
            js.addElem("lastName", row.getLastname());
            js.addElem("empno", row.getEmpno());
            js.addElem("midInit", row.getMidinit());
            js.addElem("workDept", row.getWorkdept());
            js.addElem("phoneNo", row.getPhoneno());
            js.addElem("hireDate", row.getHiredate().toString());
            js.addElem("job", row.getJob());
            js.addElem("eddLevel", new Integer(row.getEdlevel()).toString(),
                    true);
            js.addElem("sex", row.getSex());
            js.addElem("lastName", row.getLastname());
            js.addElem("birthDate", row.getBirthdate().toString());
            js.addElem("salary", row.getSalary().toPlainString(), true);
            js.addElem("comm", row.getComm().toPlainString(), true);
            js.addElem("bonus", row.getBonus().toPlainString(), true);
            String jsString = js.createJsonString();
            js.addElem("salary", row.getSalary().toPlainString(), true);
            return jsAddStyle(parseJson(jsString));
        }

    }

    DockMain(Map<String, String> col, IAction changeList) {
        initWidget(uiBinder.createAndBindUi(this));
        listBox.addItem("");
        for (Entry<String, String> e : col.entrySet()) {
            String[] s = e.getValue().split(",");
            listBox.addItem(s[0]);
        }
        this.changeList = changeList;
        dataProvider.addDataDisplay(table);
        pager.setDisplay(table);
        pager.setPageSize(20);
        pager.setPage(0);
    }

    CellTable<EmployeeRecord> getTable() {
        return table;
    }

    private void drawResList() {
        if (rInfo == null) {
            return;
        }
        if (listToDraw == null) {
            return;
        }
        List<EmployeeRecord> result = dataProvider.getList();
        table.setRowCount(listToDraw.size(), true);
        result.clear();
        result.addAll(listToDraw);

    }

    void drawList(List<EmployeeRecord> list) {
        listToDraw = list;
        drawResList();
    }

    void drawError(Widget w) {
        DockLayoutPanel dock = (DockLayoutPanel) this.getWidget();
        dock.clear();
        dock.add(w);
    }

    @UiHandler("listBox")
    public void onChange(ChangeEvent event) {
        changeList.execute();
    }

    @UiHandler({ "incRadio", "decRadio" })
    public void onClick(ClickEvent event) {
        changeList.execute();
    }

    boolean isIncOrder() {
        return incRadio.getValue();
    }

}
