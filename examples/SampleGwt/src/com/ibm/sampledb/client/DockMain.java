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

import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.ibm.sampledb.shared.IRecord;
import com.ibm.sampledb.shared.GetField;
import com.ibm.sampledb.shared.GetField.FieldInfo;
import com.ibm.sampledb.shared.GetField.FieldType;
import com.ibm.sampledb.shared.GetField.FieldValue;
import com.ibm.sampledb.shared.ResourceInfo;

class DockMain extends Composite {

    interface IAction {
        void execute();
    }

    interface MyUiBinder extends UiBinder<Widget, DockMain> {
    }

    private final IAction changeList;
    private final IAction printA;

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField
    ListBox listBox;

    @UiField
    RadioButton incRadio;

    @UiField
    RadioButton decRadio;

    @UiField
    Button printB;

    @UiField(provided = true)
    SimplePager pager = new SimplePager(TextLocation.LEFT,
            (Resources) GWT.create(SimplePager.Resources.class), false, 0, true);

    @UiField(provided = true)
    final CellTable<IRecord> table = new CellTable<IRecord>();

    private final ListDataProvider<IRecord> dataProvider = new ListDataProvider<IRecord>();

    public ListBox getListBox() {
        return listBox;
    }

    private ResourceInfo rInfo = null;
    private List<IRecord> listToDraw = null;

    void setResorceInfo(ResourceInfo r) {
        this.rInfo = r;
        if (r.getCssS() != null) {
            addStyle(r.getCssS());
        }
        if (r.getJavaS() != null) {
            addScript(r.getJavaS());
        }
        if (r.isCustomRow()) {
            table.setRowStyles(new AddStyle(r.getJsAddRowFunc()));
        }
        drawResList();
    }

    public static native void jsAlert() /*-{
		jsAlert();
    }-*/;

    public static native void addScript(String s) /*-{
		$wnd.addScript(s);
    }-*/;

    public static native void addStyle(String s) /*-{
		$wnd.addStyle(s);
    }-*/;

    public static native String callJsStringFun(String jsonFun, String paramS) /*-{
		return $wnd.eval(jsonFun + '(\'' + paramS + '\')');
    }-*/;

    private class AddStyle implements RowStyles<IRecord> {

        private final String jsFun;

        AddStyle(String jsFun) {
            this.jsFun = jsFun;
        }

        @Override
        public String getStyleNames(IRecord row, int rowIndex) {
            CreateJson js = new CreateJson("Employee");
            for (FieldInfo f : GetField.getfList()) {
                String fId = f.getfId();
                FieldValue v = GetField.getValue(fId, row);
                String val = v.getString(f);
                boolean number = true;
                if ((f.getfType() == FieldType.STRING)
                        || (f.getfType() == FieldType.DATE)) {
                    number = false;
                }
                js.addElem(fId, val, number);
            }
            String jsString = js.createJsonString();
            return callJsStringFun(jsFun, jsString);
        }

    }

    DockMain(IAction changeList, IAction printA) {
        initWidget(uiBinder.createAndBindUi(this));
        listBox.addItem("");
        // create listBox to select order by info
        for (FieldInfo f : GetField.getfList()) {
            listBox.addItem(f.getfDescr());
        }
        this.changeList = changeList;
        this.printA = printA;
        dataProvider.addDataDisplay(table);
        pager.setDisplay(table);
        pager.setPageSize(20);
        pager.setPage(0);
    }

    CellTable<IRecord> getTable() {
        return table;
    }

    List<IRecord> getList() {
        return dataProvider.getList();
    }

    private void drawResList() {
        if (rInfo == null) {
            return;
        }
        if (listToDraw == null) {
            return;
        }
        List<IRecord> result = dataProvider.getList();
        table.setRowCount(listToDraw.size(), true);
        result.clear();
        result.addAll(listToDraw);

    }

    void drawList(List<? extends IRecord> list) {
        listToDraw = (List<IRecord>) list;
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

    @UiHandler("printB")
    void onPrintBClick(ClickEvent event) {
        if (printA != null) {
            printA.execute();
        }
    }
}
