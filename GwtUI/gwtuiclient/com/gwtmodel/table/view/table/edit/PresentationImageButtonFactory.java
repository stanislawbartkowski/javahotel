/*
 * Copyright 2015 stanislawbartkowski@gmail.com 
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
package com.gwtmodel.table.view.table.edit;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Optional;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.gwtmodel.table.IConsts;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.MutableInteger;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.tabledef.VListHeaderDesc;
import com.gwtmodel.table.view.table.IColumnImage;
import com.gwtmodel.table.view.table.ILostFocusEdit;
import com.gwtmodel.table.view.table.util.EditableCol;
import com.gwtmodel.table.view.table.util.ErrorLineInfo;
import com.gwtmodel.table.view.table.util.IGetField;
import com.gwtmodel.table.view.table.util.IStartEditRow;

/**
 * @author hotel
 * 
 */
class PresentationImageButtonFactory extends PresentationEditCellHelper {

	private final IColumnImage iIma;

	PresentationImageButtonFactory(ErrorLineInfo errorInfo, CellTable<MutableInteger> table, ILostFocusEdit lostFocus,
			EditableCol eCol, IStartEditRow iStartEdit, IColumnImage iIma) {
		super(errorInfo, table, lostFocus, eCol, iStartEdit);
		this.iIma = iIma;
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	private class HasButtonCellImage implements HasCell {

		private final IVField v;
		private final VListHeaderDesc he;
		private final ButtonImageCell bCell;

		HasButtonCellImage(VListHeaderDesc he, int no) {
			this.v = he.getFie();
			this.he = he;
			bCell = new ButtonImageCell(he, no, new Click(he, no));
		}

		@Override
		public Cell getCell() {
			return bCell;
		}

		@Override
		public FieldUpdater getFieldUpdater() {
			return null;
		}

		@Override
		public Object getValue(Object object) {
			return object;
		}
	}

	@SuppressWarnings("rawtypes")
	private class Click implements ActionCell.Delegate {

		private final VListHeaderDesc he;
		private final int imno;
		private WSize lastRendered;
		private int rowno;

		Click(VListHeaderDesc he, int imno) {
			this.imno = imno;
			this.he = he;
		}

		@Override
		public void execute(Object object) {
			iIma.click(lastRendered, rowno, he.getFie(), imno);
		}

	}

	@SuppressWarnings({ "rawtypes" })
	private class ButtonImageCell extends ActionCell {

		private final IVField v;
		private final VListHeaderDesc he;
		private final int no;
		private final Click cl;

		@SuppressWarnings("unchecked")
		ButtonImageCell(VListHeaderDesc he, int no, Click cl) {
			super("", cl);
			this.he = he;
			this.v = he.getFie();
			this.no = no;
			this.cl = cl;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void onBrowserEvent(Context context, Element parent, Object value, NativeEvent event,
				ValueUpdater valueUpdater) {
			cl.lastRendered = new WSize(parent);
			Object key = context.getKey();
			MutableInteger i = (MutableInteger) key;
			cl.rowno = i.intValue();
			super.onBrowserEvent(context, parent, value, event, valueUpdater);
		}

		@Override
		public void render(Context context, Object value, SafeHtmlBuilder sb) {
			Object key = context.getKey();
			MutableInteger i = (MutableInteger) key;
			int rowno = i.intValue();
			Optional<String[]> li = iIma.getImageButton(rowno, v);

			String ima = null;
			if (li != null) {
				if (!li.isPresent())
					return;
				ima = li.get()[no];
			}
			if (ima == null) {
				IGetCustomValues c = GwtGiniInjector.getI().getCustomValues();
				ima = c.getCustomValue(IGetCustomValues.IMAGEFORLISTHELP);
			}
			String s;
			if (IConsts.EMPTYIM.equals(ima))
				s = "";
			else
				s = Utils.getImageHTML(ima);
			SafeHtml html = SafeHtmlUtils.fromTrustedString(s);
			sb.append(html);
		}
	}

	private List<HasCell<?, ?>> createList(VListHeaderDesc he) {
		int imNo = he.getImageNo();
		List<HasCell<?, ?>> rList = new ArrayList<HasCell<?, ?>>();
		for (int no = 0; no < imNo; no++) {
			HasButtonCellImage h = new HasButtonCellImage(he, no);
			rList.add(h);

		}
		return rList;
	}

	@SuppressWarnings("rawtypes")
	private class ButtonImageList extends CompositeCell implements IGetField {

		private Object o;

		@SuppressWarnings("unchecked")
		public ButtonImageList(VListHeaderDesc he) {
			super(createList(he));
		}

		@Override
		public Object getValObj(MutableInteger key) {
			return o;
		}

		@Override
		public void setValObj(MutableInteger key, Object o) {
			this.o = o;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	Column constructImageButton(VListHeaderDesc he) {
		Cell ce = new ButtonImageList(he);
		return new TColumnEdit(he.getFie(), ce);
	}

}
