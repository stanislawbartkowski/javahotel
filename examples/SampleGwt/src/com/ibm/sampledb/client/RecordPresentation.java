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

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.ibm.sampledb.shared.GetField;
import com.ibm.sampledb.shared.GetField.FieldType;
import com.ibm.sampledb.shared.GetField.FieldValue;
import com.ibm.sampledb.shared.GetRowsInfo;
import com.ibm.sampledb.shared.IResourceType;
import com.ibm.sampledb.shared.OneRecord;
import com.ibm.sampledb.shared.RowFieldInfo;

class RecordPresentation {

	private GetRowsInfo rInfo;
	private final CellTable<OneRecord> table;
	private final SimplePager pager;
	private final ListDataProvider<OneRecord> dataProvider = new ListDataProvider<OneRecord>();
	private List<OneRecord> listToDraw = null;
	private OneRecord selected;

	public OneRecord getSelected() {
		return selected;
	}

	public GetRowsInfo getrInfo() {
		return rInfo;
	}

	public CellTable<OneRecord> getTable() {
		return table;
	}

	public List<OneRecord> getList() {
		return dataProvider.getList();
	}

	private static native void jsAlert() /*-{
		jsAlert();
	}-*/;

	private static native void addScript(String s) /*-{
		$wnd.addScript(s);
	}-*/;

	private static native void addStyle(String s) /*-{
		$wnd.addStyle(s);
	}-*/;

	private static native String callJsStringFun(String jsonFun, String paramS) /*-{
		return $wnd.eval(jsonFun + '(\'' + paramS + '\')');
	}-*/;

	RecordPresentation(CellTable<OneRecord> table, SimplePager pager,
			boolean selectable) {
		this.table = table;
		this.pager = pager;
		dataProvider.addDataDisplay(table);
		pager.setDisplay(table);
		pager.setPageSize(20);
		pager.setPage(0);
		selected = null;
		if (selectable) {
			final SingleSelectionModel<OneRecord> selectionModel = new SingleSelectionModel<OneRecord>();
			table.setSelectionModel(selectionModel);
			selectionModel
					.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
						public void onSelectionChange(SelectionChangeEvent event) {
							selected = selectionModel.getSelectedObject();
						}
					});

		}
	}

	private class EDateColumn extends Column<OneRecord, Date> {

		private final String field;

		EDateColumn(DateCell d, String keyName) {
			super(d);
			this.field = keyName;
		}

		@Override
		public Date getValue(OneRecord object) {
			FieldValue val = GetField.getValue(field, rInfo, object);
			Date d = val.getdField();
			return d;
		}
	}

	private class ENumberColumn extends Column<OneRecord, Number> {

		private final RowFieldInfo f;

		ENumberColumn(NumberCell d, RowFieldInfo field) {
			super(d);
			this.f = field;
		}

		@Override
		public Number getValue(OneRecord object) {
			Number n;
			FieldValue val = GetField.getValue(f, object);
			if (f.getfType() == FieldType.INTEGER) {
				n = val.getiField();
			} else {
				n = val.getnField();
			}

			return n;
		}
	}

	private class AttachClass implements ActionCell.Delegate<OneRecord> {

		@Override
		public void execute(OneRecord object) {
			AttachmentDialog a = new AttachmentDialog(object, rInfo);
			PopupDraw p = new PopupDraw(a, false);
			p.draw(100, 100);
		}

	}

	private class ActionNumberCell extends ActionCell<OneRecord> {

		private final RowFieldInfo f;

		ActionNumberCell(RowFieldInfo f) {
			super("", new AttachClass());
			this.f = f;
		}

		@Override
		public void render(Cell.Context context, OneRecord value,
				SafeHtmlBuilder sb) {
			FieldValue numb = GetField.getValue(f, value);
			sb.appendHtmlConstant("<strong>");
			sb.append(numb.getiField());
			sb.appendHtmlConstant("</strong>");
		}

	}

	private class ButtonColumn extends Column<OneRecord, OneRecord> {

		public ButtonColumn(ActionNumberCell cell) {
			super(cell);
		}

		@Override
		public OneRecord getValue(OneRecord object) {
			return object;
		}
	}

	private Column<OneRecord, ?> constructDate(RowFieldInfo fi) {
		DateTimeFormat f = DateTimeFormat.getFormat("yyyy/MM/dd");
		DateCell d = new DateCell(f);
		return new EDateColumn(d, fi.getfId());
	}

	private Column<OneRecord, ?> constructNumber(RowFieldInfo f) {
		NumberCell d = new NumberCell();
		return new ENumberColumn(d, f);
	}

	private Column<OneRecord, ?> constructInteger(RowFieldInfo f) {
		NumberFormat fo = NumberFormat.getFormat("#####");
		NumberCell d = new NumberCell(fo);
		return new ENumberColumn(d, f);
	}

	private Column<OneRecord, ?> constructAction(RowFieldInfo f) {
		ActionNumberCell ce = new ActionNumberCell(f);
		return new ButtonColumn(ce);
	}

	private class ETextColumn extends TextColumn<OneRecord> {

		private final String field;

		ETextColumn(String keyName) {
			this.field = keyName;
		}

		@Override
		public String getValue(OneRecord e) {

			FieldValue val = GetField.getValue(field, rInfo, e);
			String vals = val.getsField();

			return vals;
		}

	}

	private Column<OneRecord, ?> constructTextColumn(RowFieldInfo f) {
		return new ETextColumn(f.getfId());
	}

	private class AddStyle implements RowStyles<OneRecord> {

		private final String jsFun;

		AddStyle(String jsFun) {
			this.jsFun = jsFun;
		}

		@Override
		public String getStyleNames(OneRecord row, int rowIndex) {
			CreateJson js = new CreateJson("Employee");
			for (RowFieldInfo f : rInfo.getfList()) {
				String fId = f.getfId();
				FieldValue v = GetField.getValue(f, row);
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

	void setRInfo(GetRowsInfo rInfo) {
		this.rInfo = rInfo;
		table.setWidth(rInfo.getResource().getTableWidth(), true);
		for (RowFieldInfo f : rInfo.getfList()) {
			if (f.getfDescr() == null) {
				continue;
			}
			Column<OneRecord, ?> co = null;
			switch (f.getfType()) {
			case NUMBER:
				co = constructNumber(f);
				break;
			case DATE:
				co = constructDate(f);
				break;
			case INTEGER:
				if (f.getfId().equals(IResourceType.NOATTACH)) {
					co = constructAction(f);
				} else {
					co = constructInteger(f);
				}
				break;
			case STRING:
				co = constructTextColumn(f);
				break;
			}
			table.addColumn(co, f.getfDescr());
			table.setColumnWidth(co, f.getcSize(), Unit.PCT);
		}
		if (rInfo.getResource().getCssS() != null) {
			addStyle(rInfo.getResource().getCssS());
		}
		if (rInfo.getResource().getJavaS() != null) {
			addScript(rInfo.getResource().getJavaS());
		}
		if (rInfo.getResource().isCustomRow()) {
			table.setRowStyles(new AddStyle(rInfo.getResource()
					.getJsAddRowFunc()));
		}
		drawResList();

	}

	private void drawResList() {
		if (rInfo == null) {
			return;
		}
		if (listToDraw == null) {
			return;
		}
		List<OneRecord> result = dataProvider.getList();
		table.setRowCount(listToDraw.size(), true);
		result.clear();
		result.addAll(listToDraw);

	}

	void drawList(List<OneRecord> list) {
		listToDraw = (List<OneRecord>) list;
		drawResList();
	}

}
