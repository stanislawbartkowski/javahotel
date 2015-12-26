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

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
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
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.MutableInteger;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.factories.IGetCustomValues;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.tabledef.IColumnImageSelect;
import com.gwtmodel.table.tabledef.VListHeaderDesc;
import com.gwtmodel.table.view.table.ILostFocusEdit;
import com.gwtmodel.table.view.table.util.EditableCol;
import com.gwtmodel.table.view.table.util.ErrorLineInfo;
import com.gwtmodel.table.view.table.util.IGetField;
import com.gwtmodel.table.view.table.util.IStartEditRow;

/**
 * @author hotel
 *
 */
class PresentationImageChooseFactory extends PresentationEditCellHelper {

	PresentationImageChooseFactory(ErrorLineInfo errorInfo, CellTable<MutableInteger> table, ILostFocusEdit lostFocus,
			EditableCol eCol, IStartEditRow iStartEdit) {
		super(errorInfo, table, lostFocus, eCol, iStartEdit);

	}

	@SuppressWarnings("rawtypes")
	private class HasEditStringCell implements HasCell {

		private final EditStringCell iCell;

		HasEditStringCell(VListHeaderDesc he) {
			iCell = new EditStringCell(he);
		}

		@Override
		public Cell getCell() {
			return iCell;
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
	private class HasStringButtonCellImage implements HasCell {

		private final IVField v;
		private final VListHeaderDesc he;
		private final StringImageCell bCell;
		private final HasEditStringCell hasString;

		HasStringButtonCellImage(VListHeaderDesc he, HasEditStringCell hasString) {
			this.v = he.getFie();
			this.he = he;
			bCell = new StringImageCell(new Delegate(), he);
			this.hasString = hasString;
		}

		class Delegate implements ActionCell.Delegate<String> {

			WSize lastRendered = null;
			Context lastContext = null;

			private class SetString implements IColumnImageSelect.IExecuteSetString {

				@Override
				public void setString(String s) {
					EditStringCell eCell = (EditStringCell) hasString.getCell();
					eCell.setValObj((MutableInteger) lastContext.getKey(), s);
					modifUpdate(false, lastContext.getKey(), v, lastRendered);
					removeErrorStyle();
					table.redrawRow(lastContext.getIndex());
				}
			}

			@Override
			public void execute(String object) {
				if (lastRendered == null) {
					return;
				}
				IColumnImageSelect.IExecuteSetString i = new SetString();
				he.getiColSelect().executeImage(object, lastContext.getIndex(), lastRendered, i);
			}
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

	private class StringImageCell extends ActionCell<String> {

		private final IVField v;
		private final VListHeaderDesc he;
		private final HasStringButtonCellImage.Delegate delegate;

		StringImageCell(HasStringButtonCellImage.Delegate delegate, VListHeaderDesc he) {
			super("", delegate);
			this.he = he;
			this.v = he.getFie();
			this.delegate = delegate;
		}

		@Override
		public void onBrowserEvent(Context context, Element parent, String value, NativeEvent event,
				ValueUpdater<String> valueUpdater) {
			delegate.lastRendered = new WSize(parent);
			delegate.lastContext = context;
			super.onBrowserEvent(context, parent, value, event, valueUpdater);
		}

		@Override
		public void render(Context context, String value, SafeHtmlBuilder sb) {
			Object key = context.getKey();
			MutableInteger i = (MutableInteger) key;
			boolean editenabled = eCol.isEditable(i.intValue(), v);
			if (editenabled) {
				String ima = he.getiColSelect().getImage();
				if (ima == null) {
					IGetCustomValues c = GwtGiniInjector.getI().getCustomValues();
					ima = c.getCustomValue(IGetCustomValues.IMAGEFORLISTHELP) + ".gif";
				}
				String s = Utils.getImageHTML(ima);
				SafeHtml html = SafeHtmlUtils.fromTrustedString(s);
				sb.append(html);
			}
		}
	}

	private List<HasCell<String, ?>> createList(HasCell<String, ?> c1, HasCell<String, ?> c2) {
		List<HasCell<String, ?>> rList = new ArrayList<HasCell<String, ?>>();
		rList.add(c1);
		rList.add(c2);
		return rList;
	}

	private class CellPickerString extends CompositeCell<String> implements IGetField {

		private final EditStringCell dCell;
		private final IVField v;

		@SuppressWarnings("unchecked")
		public CellPickerString(IVField v, HasEditStringCell dateCell, HasStringButtonCellImage cellImage) {
			super(createList(dateCell, cellImage));
			this.dCell = (EditStringCell) dateCell.getCell();
			this.v = v;
		}

		@Override
		public Object getValObj(MutableInteger key) {
			return dCell.getValObj(key);
		}

		@Override
		public void setValObj(MutableInteger key, Object o) {
			dCell.setValObj(key, o);
		}

		@Override
		public IVField getV() {
			return v;
		}
	}

	@SuppressWarnings("rawtypes")
	Column construct(VListHeaderDesc he) {
		HasEditStringCell c1 = new HasEditStringCell(he);
		HasStringButtonCellImage c2 = new HasStringButtonCellImage(he, c1);
		CellPickerString ceCell = new CellPickerString(he.getFie(), c1, c2);
		return new TColumnEdit(he.getFie(), ceCell);
	}
}
