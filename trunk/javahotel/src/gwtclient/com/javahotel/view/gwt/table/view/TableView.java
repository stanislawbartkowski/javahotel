/*
 * Copyright 2008 stanislawbartkowski@gmail.com
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
package com.javahotel.view.gwt.table.view;

import java.util.List;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.dialog.IMvcWidget;
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

/**
 * 
 * @author hotel
 */
@SuppressWarnings("deprecation")
class TableView implements ITableView {

	@SuppressWarnings("unused")
	private final IResLocator rI;
	private final ITableModel model;
	private final Grid g = new Grid();
	private AbstractTo aClicked;
	private final VerticalPanel vp = new VerticalPanel();
	private final Label header = new Label();
	private final ITableSignalClicked sC;
	private final ITableCallBackSetField iSet;
	private final IGetWidgetTableView iWidget;
	private final IContrButtonView cView;

	private void clickSignal(int row, int col) {
		if (sC != null) {
			IField f = null;
			Widget w = null;
			if (col != -1) {
				f = model.getCol(col);
				w = g.getWidget(row + 1, col);
			}
			ClickedContext co = new ClickedContext(row, col, f,
					WidgetSizeFactory.getW(w));
			sC.signal(co);
		}
	}

	public void invalidateClicked() {
		aClicked = null;
		clickSignal(-1, -1);
	}

	public Grid getGrid() {
		return g;
	}

	public void drawRow(int row) {
		pdrawRow(row);
	}

	public Widget getContrWidget() {
		if (cView == null) {
			return null;
		}
		return cView.getMWidget().getWidget();
	}

	public IMvcWidget getMWidget() {
		return new DefaultMvcWidget(vp);
	}

	private class ClickRowC implements TableListener {

		public void onCellClicked(final SourcesTableEvents arg0,
				final int arg1, final int arg2) {
			if (arg1 == 0) {
				return;
			}
			aClicked = model.getRow(arg1 - 1);
			clickSignal(arg1 - 1, arg2);
		}
	}

	TableView(final IResLocator rI, final IContrButtonView cView,
			final ITableModel model, final ITableSignalClicked sC,
			final ITableCallBackSetField iSet, final IGetWidgetTableView iWidget) {
		this.model = model;
		this.rI = rI;
		this.sC = sC;
		this.iSet = iSet;
		this.cView = cView;
		g.addTableListener(new ClickRowC());
		vp.add(header);
		if (model.getHeader() != null) {
			header.setText(model.getHeader());
		}
		vp.add(g);
		this.iWidget = iWidget;
	}

	private void drawTitle() {
		List<ColTitle> cTitle = model.colList();
		g.resizeColumns(cTitle.size());
		for (int i = 0; i < cTitle.size(); i++) {
			ColTitle c = cTitle.get(i);
			g.setText(0, i, c.getCTitle());
		}
	}

	private void setGridCell(final int rNo, final int cNo, final String val) {
		// Label l = new Label(val);
		Widget w = iWidget.getWidget(this, rNo, cNo, val);
		g.setWidget(rNo + 1, cNo, w);
	}

	private class SetValBack implements ITableSetField {

		private final int rNo;
		private final int cNo;

		SetValBack(final int rNo, final int cNo) {
			this.rNo = rNo;
			this.cNo = cNo;
		}

		public void setField(String val) {
			setGridCell(rNo, cNo, val);
		}
	}

	private void pdrawRow(int row) {
		List<ColTitle> cTitle = model.colList();
		for (int col = 0; col < cTitle.size(); col++) {
			ColTitle c = cTitle.get(col);
			String s = model.getField(row, c.getF());
			if (s != null) {
				setGridCell(row, col, s);
			} else if (iSet != null) {
				iSet.CallSetField(model, row, c.getF(),
						new SetValBack(row, col));
			}
		}
	}

	public void drawTable() {
		g.resizeRows(1 + model.rowNum());
		if (g.getColumnCount() == 0) {
			drawTitle();
		}

		for (int row = 0; row < model.rowNum(); row++) {
			pdrawRow(row);

		}
	}

	public ITableModel getModel() {
		return model;
	}

	public void show() {
	}

	public void hide() {
	}

	public AbstractTo getClicked() {
		return aClicked;
	}
}
