package com.javahotel.view.gwt.table.view;

import java.util.ArrayList;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.Selection;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.visualizations.Table;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.dialog.IWidgetSize;
import com.javahotel.client.dialog.WidgetSizeFactory;
import com.javahotel.client.mvc.contrpanel.view.IContrButtonView;
import com.javahotel.client.mvc.table.model.ColTitle;
import com.javahotel.client.mvc.table.model.ITableModel;
import com.javahotel.client.mvc.table.view.IGetWidgetTableView;
import com.javahotel.client.mvc.table.view.ITableCallBackSetField;
import com.javahotel.client.mvc.table.view.ITableSetField;
import com.javahotel.client.mvc.table.view.ITableSignalClicked;
import com.javahotel.client.mvc.table.view.ITableView;
import com.javahotel.client.mvc.table.view.ITableSignalClicked.ClickedContext;
import com.javahotel.common.toobject.AbstractTo;
import com.javahotel.common.toobject.IField;

class GTableView implements ITableView {

	private final IResLocator rI;
	private final ITableModel model;
	private AbstractTo aClicked;
	private final Label header = new Label();
	private final ITableSignalClicked sC;
	private final ITableCallBackSetField iSet;
	private final IGetWidgetTableView iWidget;
	private final IContrButtonView cView;
	private final Table ta;
	private DataTable data;

	GTableView(final IResLocator rI, final IContrButtonView cView,
			final ITableModel model, final ITableSignalClicked sC,
			final ITableCallBackSetField iSet, final IGetWidgetTableView iWidget) {
		this.model = model;
		this.rI = rI;
		this.sC = sC;
		this.iSet = iSet;
		this.cView = cView;
		if (model.getHeader() != null) {
			header.setText(model.getHeader());
		}
		this.iWidget = iWidget;
		ta = new Table(null, Table.Options.create());
		ta.addSelectHandler(new H(ta));

	}

	private class SetValBack implements ITableSetField {

		private final int rNo;
		private final int cNo;

		SetValBack(final int rNo, final int cNo) {
			this.rNo = rNo;
			this.cNo = cNo;
		}

		public void setField(String val) {
			data.setValue(rNo, cNo, val);
		}
	}

	private void drawrow(int row) {
		AbstractTo ii = model.getRow(row);
		ArrayList<ColTitle> co = model.colList();
		for (int c = 0; c < co.size(); c++) {
			ColTitle cl = co.get(c);
			String val = ii.getS(cl.getF());
			if (val != null) {
				data.setValue(row, c, val);
			} else if (iSet != null) {
				iSet
						.CallSetField(model, row, cl.getF(), new SetValBack(
								row, c));
			}
		}
	}

	private void getTable() {
		data = DataTable.create();
		ArrayList<ColTitle> co = model.colList();
		// if (iModel.isHeaders()) {
		for (ColTitle c : co) {
			data.addColumn(ColumnType.STRING, c.getCTitle());
		}
		// }
		data.addRows(model.rowNum());
		for (int i = 0; i < model.rowNum(); i++) {
			drawrow(i);
		}
	}

	private IWidgetSize getS(int row, int col) {
		Element etd = ta.getElement(); // TD
		// search first TR
		while ((etd != null) && (!etd.getTagName().equals("TR"))) {
			etd = etd.getFirstChildElement(); // TABLE
		}

		int no = 0;
		int top = 0;
		int left = 0;
		int height = 0;
		int width = 0;
		Element etr = etd;
		while (etr != null) {
			etr = etr.getNextSiblingElement();
			if (no == row) {
				break;
			}
			no++;
		}
		if (etr != null) {
			Element echild = etr.getFirstChildElement();
			int cno = 0;
			while ((echild != null) && (cno != col)) {
				echild = echild.getNextSiblingElement();
				cno++;
			}
			if (echild != null) {
				top = echild.getAbsoluteTop();
				left = echild.getAbsoluteLeft();
				height = echild.getOffsetHeight();
				width = echild.getOffsetWidth();
			}
		}
		return WidgetSizeFactory.getW(top, left, height, width);
	}

	private void clickSignal(int row, int col) {
		if (sC != null) {
			IField f = null;
			IWidgetSize w = null;
			if (col != -1) {
				f = model.getCol(col);
				w = getS(row, col);
			}
			ClickedContext co = new ClickedContext(row, col, f, w);
			sC.signal(co);
		}
	}

	private class H extends SelectHandler {

		private final Table ta;

		H(Table ta) {
			this.ta = ta;
		}

		@SuppressWarnings( { "unchecked", "static-access" })
		@Override
		public void onSelect(SelectEvent event) {
			JsArray<Selection> sel = ta.getSelections();
			Selection se = null;
			for (int i = 0; i < sel.length(); i++) {
				se = sel.get(i);
			}
			if (se == null) {
				return;
			}
			aClicked = null;
			int row = 0;
			if (se.isRow() || se.isCell()) {
				row = se.getRow();
				aClicked = model.getRow(row);
			}
			int col = 0;
			if (se.isColumn() || se.isCell()) {
				col = se.getColumn();
			}
			if (sC != null) {
				// unselect
				sel = (JsArray<Selection>) sel.createArray();
				ta.setSelections(sel);
			}
			clickSignal(row, col);
		}
	}

	public void drawRow(int row) {
		drawrow(row);
	}

	public void drawTable() {
		getTable();
		ta.draw(data);
	}

	public AbstractTo getClicked() {
		return aClicked;
	}

	public Widget getContrWidget() {
		if (cView == null) {
			return null;
		}
		return cView.getMWidget().getWidget();
	}

	public IMvcWidget getMWidget() {
		return new DefaultMvcWidget(ta);
	}

	public Grid getGrid() {
		return null;
	}

	public ITableModel getModel() {
		return model;
	}

	public void invalidateClicked() {
		aClicked = null;
	}

	public void hide() {

	}

	public void show() {
	}

}
