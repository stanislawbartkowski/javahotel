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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.Resources;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.ibm.sampledb.shared.GetRowsInfo;
import com.ibm.sampledb.shared.OneRecord;
import com.ibm.sampledb.shared.RowFieldInfo;

class DockMain extends Composite {

	private final IAction changeList;
	private final IAction printA;

	interface MyUiBinder extends UiBinder<Widget, DockMain> {
	}

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
	final CellTable<OneRecord> table = new CellTable<OneRecord>();

	private final RecordPresentation rPresentation;

	public ListBox getListBox() {
		return listBox;
	}

	public RecordPresentation getrPresentation() {
		return rPresentation;
	}

	void setResorceInfo(GetRowsInfo r) {
		rPresentation.setRInfo(r);
		listBox.addItem("");
		// create listBox to select order by info
		for (RowFieldInfo f : r.getfList()) {
			listBox.addItem(f.getfDescr());
		}

	}

	DockMain(IAction changeList, IAction printA) {
		initWidget(uiBinder.createAndBindUi(this));
		this.changeList = changeList;
		this.printA = printA;
		rPresentation = new RecordPresentation(table, pager, false);
	}

	CellTable<OneRecord> getTable() {
		return table;
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
