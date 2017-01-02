/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, softwarre 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package com.gwtmodel.table.view.table;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.RowHoverEvent;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.Resources;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.ICommand;
import com.gwtmodel.table.IConsts;
import com.gwtmodel.table.IGetSetVField;
import com.gwtmodel.table.ISuccess;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.InvalidateFormContainer;
import com.gwtmodel.table.InvalidateMess;
import com.gwtmodel.table.MutableInteger;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.WChoosedLine;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.DecimalUtils;
import com.gwtmodel.table.common.MaxI;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.mm.LogT;
import com.gwtmodel.table.smessage.IGetStandardMessage;
import com.gwtmodel.table.tabledef.VFooterDesc;
import com.gwtmodel.table.tabledef.VListHeaderContainer;
import com.gwtmodel.table.tabledef.VListHeaderDesc;
import com.gwtmodel.table.view.table.edit.IPresentationCellEdit;
import com.gwtmodel.table.view.table.edit.PresentationEditCellProvider;
import com.gwtmodel.table.view.table.util.ErrorLineInfo;
import com.gwtmodel.table.view.table.util.IGetField;
import com.gwtmodel.table.view.table.util.IStartEditRow;
import com.gwtmodel.table.view.table.util.IToRowNo;
//import com.gwtmodel.table.view.table.util.ErrorLineInfo;
import com.gwtmodel.table.view.util.PopUpHint;

/**
 * 
 * @author perseus
 */
class PresentationTable implements IGwtTableView {

	private final INewEditLineFocus iEditFocus;
	/**
	 * Call back when line is clicked.
	 */
	private final IRowClick iClick;
	/**
	 * Call back when column is clicked.
	 */
	private final ICommand iActionColumn;
	/**
	 * Call back function for raw cell columns.
	 */
	private final IGetCellValue gValue;
	/**
	 * The widget table in flesh.
	 */
	private final CellTable<MutableInteger> table = new CellTable<MutableInteger>();
	private IGwtTableModel model = null;
	/**
	 * If column definition has been provided.
	 */

	private boolean columnC = false;
	final SingleSelectionModel<MutableInteger> selectionModel = new SingleSelectionModel<MutableInteger>();
	private WChoosedLine wChoosed;
	private final SimplePager sPager = new SimplePager(TextLocation.CENTER,
			(Resources) GWT.create(SimplePager.Resources.class), false, 1000, true);
	private final VerticalPanel vPanel = new VerticalPanel();
	private final ListDataProvider<MutableInteger> dProvider = new ListDataProvider<MutableInteger>();
	private final AsyncDataProvider<MutableInteger> aProvider = new MyAsyncProvider();
	private boolean whilefind = false;
	private IModifyRowStyle iModRow = null;
	private final PresentationCellFactory fa;
	private final IPresentationCellEdit faEdit;
	private final PresentationFooterFactory footFactory;
	private boolean noWrap = false;
	private IVField sortCol = null;
	private boolean sortInc;
	private final ILostFocusEdit lostFocus;
	private final boolean async;
	private IGetStandardMessage iMess = GwtGiniInjector.getI().getStandardMessage();
	private final ColToVHeader coV = new ColToVHeader();

	private final IGetColSpan iSpan;

	private class MyAsyncProvider extends AsyncDataProvider<MutableInteger> {

		@Override
		protected void onRangeChanged(HasData<MutableInteger> display) {
			// Get the new range.
			final Range range = display.getVisibleRange();
			final int start = range.getStart();
			final int length = MaxI.min((int) model.getSize() - start, range.getLength());

			ISuccess iSignal = new ISuccess() {

				@Override
				public void success() {
					List<MutableInteger> mList = new ArrayList<MutableInteger>();
					for (int i = 0; i < length; i++)
						mList.add(new MutableInteger(i + start));
					updateRowData(start, mList);
				}
			};
			ColumnSortList sList = table.getColumnSortList();
			boolean asc = false;
			IVField sFie = null;
			if (sList.size() > 0) {
				asc = sList.get(0).isAscending();
				@SuppressWarnings("rawtypes")
				Column cSort = sList.get(0).getColumn();
				for (int i = 0; i < table.getColumnCount(); i++) {
					Column<MutableInteger, ?> co = table.getColumn(i);
					// compare references !
					if (co == cSort) {
						VListHeaderDesc v = model.getHeaderList().getVisHeList().get(i);
						sFie = v.getFie();
						break;
					}
				}
				if (sFie == null) {
					Utils.errAlert(LogT.getT().CannotRecgonizeSortColumn());
					return;
				}
			}
			model.readChunkRange(start, length, sFie, asc, iSignal);
		}
	}

	private class CurrentHoverTip {

		@SuppressWarnings("unused")
		MutableInteger key;
		@SuppressWarnings("unused")
		int col;
		boolean on = false;
		PopUpHint pHint = new PopUpHint();
	}

	private final CurrentHoverTip currentH = new CurrentHoverTip();

	public void setModifyRowStyle(IModifyRowStyle iMod) {
		this.iModRow = iMod;
		if (iMod != null) {
			table.setRowStyles(new TStyles());
		}
	}

	private boolean selectEnabled() {
		return iClick != null;
		// table.getRowContainer().
	}

	private void setWrapCol() {
		for (int i = 0; i < table.getColumnCount(); i++) {
			VListHeaderDesc he = coV.getV(i);
			if (he == null)
				continue;
			// Joiner join = Joiner.on(" ").skipNulls();
			String aClass = CUtil.joinS(' ', model.getClassNameForColumn(he.getFie()), he.getColumnClass(),
					noWrap ? IConsts.nowrapStyle : null);
			Column<MutableInteger, ?> co = table.getColumn(i);
			co.setCellStyleNames(aClass);
		}
	}

	/**
	 * Custom function for additional style for rows. Uses java script function.
	 * 
	 * @author hotel
	 * 
	 */
	private class TStyles implements RowStyles<MutableInteger> {

		// tr.wait-reply
		public String getStyleNames(MutableInteger row, int rowIndex) {
			IVModelData v = model.get(row.intValue());
			return iModRow.newRowStyle(v);
		}
	}

	private void setNewEditFocusLine(WChoosedLine w) {
		if (iEditFocus == null) {
			return;
		}
		iEditFocus.lineClicked(w);
	}

	private class StartEditLine implements IStartEditRow {

		@Override
		public void setEditRow(MutableInteger row, WSize w) {
			LogT.getLT().info(LogT.getT().TablePresentationSetEditRow(row.intValue()));
			setNewEditFocusLine(pgetClicked(row, null, w));
		}
	}

	/**
	 * Raised when the whole row was selected
	 * 
	 * @author hotel
	 * 
	 */
	private class SelectionChange implements SelectionChangeEvent.Handler {

		@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			LogT.getLT().info(LogT.getT().TablePresentationSelectionChangeNow(wChoosed.getChoosedLine()));
			MutableInteger sel = selectionModel.getSelectedObject();
			if (sel == null) {
				return;
			}
			// neither cell position nor column are available
			wChoosed = pgetClicked(sel, null, null);
			if (model.getIClicked() != null) {
				model.getIClicked().clicked(wChoosed);
			}
			if (selectEnabled()) {
				iClick.execute(whilefind);
				LogT.getLT().info(LogT.getT().TablePresentationSelectionChange(wChoosed.getChoosedLine()));
				setNewEditFocusLine(wChoosed);
			}
			if (model.unSelectAtOnce()) {
				selectionModel.setSelected(sel, false);
			}
			// synchronization by global variable to avoid execute while finding
			// not elegant solution but no better for the time being
			whilefind = false;
		}
	}

	private class PersistInTable implements ILostFocusEdit {

		@Override
		public void action(boolean before, int row, IVField v, WSize w) {
			if (!before) {
				List<IGetSetVField> l = getVList(row, true);
				for (IGetSetVField i : l) {
					if (i.getV().eq(v)) {
						Object o = i.getValObj();
						IVModelData vD = model.get(row);
						vD.setF(v, o);
					}
				}
			}
			if (lostFocus != null) {
				lostFocus.action(before, row, v, w);
			}
		}
	}

	private void hovercell(MutableInteger row, int col, WSize w, boolean focuson) {

		if (!focuson) {
			if (currentH.on) {
				currentH.on = false;
				currentH.pHint.actionOut();
			}
			return;
		}
		if (currentH.on) {
			currentH.pHint.actionOut();
		}
		int i = toVColNo(col);
		if (i == -1) {
			return;
		}
		ErrorLineInfo errInfo = faEdit.getErrorInfo();
		VListHeaderDesc he = model.getHeaderList().getVisHeList().get(i);
		IVModelData vData = model.get(row.intValue());
		String s = null;
		if (iSpan != null)
			s = iSpan.getHint(row, col);
		if (noWrap && faEdit.geteParam() == null) {
			// hint related to cell content only if no wrapping and not editing
			s = FUtils.getValueS(vData, he.getFie());
		}
		if (errInfo.isActive() && row.equals(errInfo.getKey())) {
			InvalidateMess me = errInfo.getErrContainer().findV(he.getFie());
			if (me != null) {
				s = me.getErrmess();
			}
		}
		// if (!CUtil.EmptyS(s)) {
		currentH.pHint.setMessage(s);
		// }

		/*
		 * if (faEdit.geteParam() != null) { return; } if (!noWrap) { return; }
		 */

		currentH.key = row;
		currentH.col = col;
		currentH.on = true;
		currentH.pHint.actionOver(w);
	}

	private class RowHover implements RowHoverEvent.Handler {

		@Override
		public void onRowHover(RowHoverEvent e) {
			Event event = e.getBrowserEvent();
			EventTarget eventTarget = event.getEventTarget();
			if (!Element.is(eventTarget)) {
				return;
			}
			final Element target = eventTarget.cast();
			TableCellElement targetTableCell = target.cast();
			// boolean isC = tableBuilder.isColumn(targetTableCell);
			int col = -1;
			while (targetTableCell != null) {
				try {
					col = targetTableCell.getCellIndex();
				} catch (Exception ee) {
					Element ele = targetTableCell.getParentElement();
					targetTableCell = ele.cast();
					continue;
				}
				break;
			}
			assert targetTableCell != null : LogT.getT().cannotBeNull();
			// important : test if col is not undefined
			if (Utils.isUndefined(col)) {
				LogT.getLT().fine(LogT.getT().ColumnCellUndefined());
				return;
			}
			boolean focuson = !e.isUnHover();
			WSize w = new WSize(target);
			int row = e.getHoveringRow().getSectionRowIndex();
			MutableInteger i = table.getVisibleItem(row);
			hovercell(i, col, w, focuson);
		}
	}

	PresentationTable(IRowClick iClick, ICommand actionColumn, IGetCellValue gValue, INewEditLineFocus iEditFocus,
			ILostFocusEdit lostFocus, IColumnImage iIma, boolean async, final IGetColSpan iSpan) {
		this.iSpan = iSpan;
		this.iEditFocus = iEditFocus;
		this.iClick = iClick;
		this.lostFocus = lostFocus;
		this.iActionColumn = actionColumn;
		this.gValue = gValue;
		this.async = async;
		selectionModel.addSelectionChangeHandler(new SelectionChange());
		// dList = dProvider.getList();
		// set selection only if iClick defined
		// thus avoid changing color while clicking
		// TODO: consider later
		// if (selectEnabled()) {
		table.setSelectionModel(selectionModel);
		// }
		sPager.setDisplay(table);
		if (!async) {
			dProvider.addDataDisplay(table);
		}
		setEmpty();
		ILostFocusEdit e = null;
		// if (!selectEnabled()) {
		// TODO: all the time move data from cells to table
		e = new PersistInTable();
		// }
		fa = new PresentationCellFactory(gValue);
		// faEdit = new PresentationEditCellFactory(e, table, new
		// StartEditLine(),
		// this, iIma);
		faEdit = PresentationEditCellProvider.contruct(e, table, new StartEditLine(), this, iIma);
		table.addRowHoverHandler(new RowHover());
		footFactory = new PresentationFooterFactory(fa, faEdit);
		if (iSpan != null) {
			SpanCellBuilder.IGetSpanColValue<MutableInteger> i = new SpanCellBuilder.IGetSpanColValue<MutableInteger>() {

				@Override
				public int get(MutableInteger rowNo, int colNo) {
					return iSpan.get(rowNo, colNo);
				}

			};
			table.setTableBuilder(new SpanCellBuilder<MutableInteger>(table, i));
		}
	}

	private class TColumnString extends TextColumn<MutableInteger> {

		private final IVField iF;
		private final FieldDataType fType;

		TColumnString(IVField iF, FieldDataType fType) {
			this.iF = iF;
			this.fType = fType;
		}

		@Override
		public String getValue(MutableInteger object) {
			IVModelData v = model.get(object.intValue());
			String key = FUtils.getValueS(v, iF);
			return fType.convertToString(key);
		}
	}

	/**
	 * Implementation of AbstractCell. The only purpose is to take over
	 * "clicked" event
	 * 
	 * @author hotel
	 * 
	 */
	private class A extends AbstractCell<SafeHtml> {

		A(SafeHtml a) {
			// activates onBrowserEvent
			super(BrowserEvents.CLICK);
		}

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context, SafeHtml value, SafeHtmlBuilder sb) {
			sb.append(value);
		}
	}

	private SafeHtml getEmpty() {
		return new SafeHtmlBuilder().appendEscaped("").toSafeHtml();
	}

	/**
	 * Display raw cell column. Call back function provides html (safe)
	 * 
	 * @author hotel
	 * 
	 */
	private class RawColumn extends Column<MutableInteger, SafeHtml> {

		private final IVField fie;

		RawColumn(IVField fie) {
			super(new A(getEmpty()));
			this.fie = fie;
		}

		@Override
		public String getCellStyleNames(Context context, MutableInteger object) {
			IVModelData v = model.get(object.intValue());
			return gValue.getCellStyleNames(v, fie);
		}

		@Override
		public SafeHtml getValue(MutableInteger object) {
			IVModelData v = model.get(object.intValue());
			return gValue.getValue(v, fie);
		}

		@Override
		public void onBrowserEvent(Context context, Element elem, final MutableInteger i, NativeEvent event) {
			// do not check event type, only clicked is raised here
			WSize wSize = new WSize(elem);
			// it is possible for cell to provide position of the single cell
			// not the whole row
			wChoosed = pgetClicked(i, fie, wSize);
			iActionColumn.execute();
		}
	}

	private class CoComparator implements Comparator<MutableInteger> {

		private final VListHeaderDesc he;

		CoComparator(VListHeaderDesc he) {
			this.he = he;
		}

		public int compare(MutableInteger o1, MutableInteger o2) {
			IVModelData v1 = model.get(o1.intValue());
			IVModelData v2 = model.get(o2.intValue());
			return FUtils.compareValue(v1, he.getFie(), v2, he.getFie(), true);
		}
	}

	private class AttachClass implements ActionCell.Delegate<MutableInteger> {

		private final IVField v;

		AttachClass(IVField v) {
			this.v = v;
		}

		@Override
		public void execute(MutableInteger i) {
			wChoosed = pgetClicked(i, v, null);
			iActionColumn.execute();
		}
	}

	@SuppressWarnings("unchecked")
	private void createHeader() {
		if (model == null) {
			return;
		}
		if (columnC) {
			return;
		}
		// 2015/05/01
		while (table.getColumnCount() > 0)
			table.removeColumn(0);
		VListHeaderContainer vo = model.getHeaderList();
		if (!CUtil.EmptyS(vo.getWidthDef())) {
			table.setWidth(vo.getWidthDef(), true);
		}
		List<VListHeaderDesc> li = vo.getVisHeList();
		int colNo = 0;
		for (VListHeaderDesc he : li) {
			boolean editable = he.isEditable();
			@SuppressWarnings("rawtypes")
			Header header = null;
			@SuppressWarnings("rawtypes")
			Column co = null;
			if (he.getgHeader() != null) {
				header = he.getgHeader().getHeader();
				co = he.getgHeader().getColumn();
				if (co == null) {
					co = new RawColumn(he.getFie());
				}
				// table.addColumn(co, header);
				// continue;
			}
			if (co == null) {
				FieldDataType fType = he.getFie().getType();
				if (he.getButtonAction() != null) {
					co = fa.constructActionButtonCell(he.getButtonAction(), he.getFie(), fType,
							new AttachClass(he.getFie()));
				} else if (fType.isConvertableToString()) {
					if (editable) {
						co = faEdit.constructEditTextCol(he);
					} else {
						co = new TColumnString(he.getFie(), fType);
					}
				} else {
					switch (fType.getType()) {
					case LONG:
					case BIGDECIMAL:
					case INT:
						if (editable) {
							co = faEdit.constructNumberCol(he);
						} else {
							co = fa.constructNumberCol(he.getFie());
						}
						break;
					case DATE:
						if (editable) {
							co = faEdit.constructDateEditCol(he);
						} else {
							co = fa.constructDateEditCol(he.getFie());
						}
						break;
					case BOOLEAN:
						if (editable) {
							co = faEdit.contructBooleanCol(he.getFie(), !selectEnabled());
						} else {
							co = fa.contructBooleanCol(he.getFie());
						}
						break;
					default:
						if (editable || he.isImageCol()) {
							co = faEdit.constructEditTextCol(he);
						} else {
							co = fa.constructTextCol(he.getFie(), he);
						}
						break;
					}
				}
				co.setSortable(true);
				// align
				switch (AlignCol.getCo(he.getAlign(), he.getFie().getType())) {
				case LEFT:
					co.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
					break;
				case RIGHT:
					co.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
					break;
				case CENTER:
					co.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
					break;
				}
			}
			// col width
			String width = he.getColWidth();
			if (!CUtil.EmptyS(width)) {
				String[] pa = width.split(";");
				if (pa.length == 1) {
					table.setColumnWidth(co, pa[0]);
				} else {
					double wi;
					try {
						wi = DecimalUtils.toDouble(pa[0]);
					} catch (NumberFormatException e) {
						String info = LogT.getT().InproperWidthInColumn(he.getHeaderString(), pa[0]);
						LogT.errorLogE(info, e);
						return;
					}
					Style.Unit u;
					try {
						u = Style.Unit.valueOf(pa[1]);
					} catch (IllegalArgumentException e) {
						String info = LogT.getT().InproperColumnUnit(he.getHeaderString(), pa[1]);
						LogT.errorLogE(info, e);
						return;
					}
					table.setColumnWidth(co, wi, u);
				}
			}
			//
			if (header == null && (he.isHidden() || he.getHeaderString() == null)) {
				// Important: for some reason the assert violation cause
				// breaking without Exception throwing
				// So additional error alert is displayed to avoid confusion
				Utils.errAlert(he.getFie().getId(), LogT.getT().HeaderNull());
				assert !he.isHidden() && he.getHeaderString() != null : LogT.getT().cannotBeNull();
			}

			VFooterDesc footer = null;
			if (vo.getFoList() != null) {
				for (VFooterDesc fo : vo.getFoList()) {
					if (fo.getFie().eq(he.getFie())) {
						footer = fo;
						break;
					}
				}
			}

			if (header == null) {
				header = footFactory.constructHeader(he, editable);
			}
			if (he.getHeaderClass() != null)
				header.setHeaderStyleNames(he.getHeaderClass());
			if (footer != null) {
				table.addColumn(co, header, footFactory.constructFooter(footer));
			} else {
				// table.addColumn(co, he.getHeaderString());
				table.addColumn(co, header);
			}

			if (!async) {
				ListHandler<MutableInteger> columnSortHandler = new ListHandler<MutableInteger>(dProvider.getList());
				columnSortHandler.setComparator(co, new CoComparator(he));
				table.addColumnSortHandler(columnSortHandler);
			} else {
				ColumnSortEvent.AsyncHandler sortHandler = new ColumnSortEvent.AsyncHandler(table);
				table.addColumnSortHandler(sortHandler);
			}
			// ColumnSortEvent.fire(myTable, myTable.getColumnSortList());
			// iMap.put(colNo, he);
			coV.addColDesc(colNo, he);
			colNo++;
		} // for
		columnC = true;
		setWrapCol();
		setSortCol();
	}

	private void setSortCol() {
		if (sortCol == null) {
			return;
		}
		if (!columnC) {
			return;
		}
		ColumnSortList sList = table.getColumnSortList();
		sList.clear();
		for (int i = 0; i < table.getColumnCount(); i++) {
			Column<MutableInteger, ?> co = table.getColumn(i);
			VListHeaderDesc v = model.getHeaderList().getVisHeList().get(i);
			if (v.getFie().eq(sortCol)) {
				ColumnSortList.ColumnSortInfo sInfo = new ColumnSortList.ColumnSortInfo(co, !sortInc);
				sList.push(sInfo);
				ColumnSortEvent.fire(table, sList);
				break;
			}

		}

	}

	private void drawRows() {
		if ((model == null) || !model.containsData()) {
			table.setRowCount(0, true);
			return;
		}
		int size = model.getHeaderList().getPageSize();
		if (size != 0) {
			table.setPageSize(size);
		}
		// table.getRowElement(0).getCells().getItem(10).setColSpan(colSpan);
		if (async) {
			// removing and adding table is necessary to force redraw
			sPager.setPage(0);
			table.setRowCount((int) model.getSize(), true);
			if (aProvider.getDataDisplays().contains(table)) {
				aProvider.removeDataDisplay(table);
			}
			aProvider.addDataDisplay(table);
			table.redraw();
		} else {
			dProvider.getList().clear();
			for (int i = 0; i < model.getSize(); i++) {
				dProvider.getList().add(new MutableInteger(i));
			}
		}

		// setSpan();

		table.setPageStart(setSelected().pStart);
		int aNo = vPanel.getWidgetCount();
		String title = model.getHeaderList().getListTitle();
		Label lTitle = null;
		if (title != null) {
			lTitle = new Label(iMess.getMessage(title));
		}
		int nNo = 1;
		if (lTitle != null) {
			nNo = 2;
		}
		boolean addPager = false;
		if ((size != 0) && model.getSize() > size) {
			nNo++;
			addPager = true;
		}
		if (nNo != aNo) {
			vPanel.clear();
			if (lTitle != null) {
				vPanel.add(lTitle);
			}
			vPanel.add(table);
			if (addPager) {
				vPanel.add(sPager);
			}
		}
	}

	private void setEmpty() {
		wChoosed = new WChoosedLine();
	}

	private class P {

		private final int pStart;
		@SuppressWarnings("unused")
		private final int pRow;

		P(int pStart, int pRow) {
			this.pStart = pStart;
			this.pRow = pRow;
		}
	}

	private P cPage(int li) {
		int pSize = table.getPageSize();
		int pStart = (li / pSize) * pSize;
		int pRow = li - pStart * pSize;
		return new P(pStart, pRow);
	}

	private P setSelected() {
		if (!wChoosed.isChoosed()) {
			table.setPageStart(0);
			return new P(0, 0);
		}
		int li = wChoosed.getChoosedLine();
		if (li >= model.getSize()) {
			table.setPageStart(0);
			setEmpty();
			return new P(0, 0);
		}
		return cPage(li);
	}

	@Override
	public void refresh() {
		createHeader();
		setWrapCol();
		drawRows();
	}

	@Override
	public WChoosedLine getClicked() {
		return wChoosed;
	}

	/**
	 * Creates WChoosedLine for selected/clicked. It can be later retrieved.
	 * Only one can be retrieved, next overwrite the previous
	 * 
	 * @param sel
	 *            Row (Integer) position
	 * @param v
	 *            Column to be clicked (if available)
	 * @param wSize
	 *            Cell position (if not null)
	 * @return
	 */
	private WChoosedLine pgetClicked(MutableInteger sel, IVField v, WSize wSize) {
		if (sel == null) {
			return new WChoosedLine();
		}
		int i = sel.intValue();
		WSize w = wSize;
		// cell position not defined, take whe position of the whole row
		if (wSize == null) {
			w = getRowWidget(i);
		}
		return new WChoosedLine(i, w, v);
	}

	@Override
	public IGwtTableModel getViewModel() {
		return model;
	}

	@Override
	public void setModel(IGwtTableModel model) {
		if (model.getClassName() != null)
			vPanel.setStyleName(model.getClassName());
		this.model = model;
		this.columnC = false;
		footFactory.setGModel(model);
		fa.setModel(model);
		faEdit.setModel(model);
		createHeader();
		drawRows();
	}

	@Override
	public Widget getGWidget() {
		return vPanel;
	}

	@Override
	public void setClicked(int clickedno, boolean whileFind) {
		int pStart = table.getPageStart();
		P p = cPage(clickedno);
		if (p.pStart != pStart) {
			table.setPageStart(p.pStart);
		}
		whilefind = whileFind;
		selectionModel.setSelected(new MutableInteger(clickedno), true);
		// selectionModel.
		// added 2012/08/20
		whilefind = false;
	}

	@Override
	public void setEditable(ChangeEditableRowsParam eParam) {
		ChangeEditableRowsParam actParam = faEdit.geteParam();
		ChangeEditableRowsParam.ModifMode actMode = null;
		if (actParam != null) {
			actMode = actParam.getMode();
		}
		if (eParam.addEditMode()) {
			if (actMode == null || actMode == ChangeEditableRowsParam.ModifMode.NORMALMODE) {
				faEdit.addActionColumn();
				coV.setActionCol(true);
			}
		}
		faEdit.setEditable(eParam);
		drawRows();
	}

	private int toVColNo(int col) {
		if (faEdit.geteParam() != null && faEdit.geteParam().addEditMode()) {
			return col - 1;
		}
		return col;

	}

	private class GetSet implements IGetSetVField {

		private final int rowno;
		private final boolean keepNull;
		private final VListHeaderDesc v;

		private GetSet(int rowno, boolean keepNull, VListHeaderDesc v) {
			this.rowno = rowno;
			this.keepNull = keepNull;
			this.v = v;
		}

		@Override
		public IVField getV() {
			return v.getFie();
		}

		private IGetField getI() {
			for (int i = 0; i < table.getColumnCount(); i++) {
				Column<?, ?> co = table.getColumn(i);
				Cell<?> ce = co.getCell();
				if (ce instanceof IGetField) {
					IGetField iGet = (IGetField) ce;
					if (iGet.getV().eq(getV()))
						return iGet;
				}
			}
			return null;
		}

		@Override
		public Object getValObj() {
			IGetField iGet = getI();
			Object o = null;
			if (iGet != null) {
				o = iGet.getValObj(new MutableInteger(rowno));
			}
			if (!keepNull && (o == null)) {
				IVModelData v = model.get(rowno);
				o = FUtils.getValue(v, getV());
			}
			return o;
		}

		@Override
		public void setValObj(Object o) {
			IGetField iGet = getI();
			if (iGet == null) {
				IVField fie = getV();
				String mess = LogT.getT().PresentationTableNullSetValObj(fie.getId());
				Utils.errAlertB(mess);
			}
			iGet.setValObj(new MutableInteger(rowno), o);
			table.redraw();
		}
	}

	private List<IGetSetVField> getVList(int rowno, boolean keepNull) {
		List<IGetSetVField> vList = new ArrayList<IGetSetVField>();
		for (VListHeaderDesc v : model.getHeaderList().getVisHeList()) {
			if (v.isEditable()) {
				vList.add(new GetSet(rowno, keepNull, v));
			}
		}
		return vList;
	}

	@Override
	public List<IGetSetVField> getVList(int rowno) {
		return getVList(rowno, false);
	}

	@Override
	public void removeSort() {
		if (table == null) {
			return;
		}
		ColumnSortList sortList = table.getColumnSortList();
		if (sortList == null) {
			return;
		}
		while (sortList.size() > 0) {
			// ColumnSortInfo i = sortList.get(0);
			// sortList.remove(i);
			sortList.clear();
		}

	}

	@Override
	public boolean isSorted() {
		if (table == null) {
			return false;
		}
		ColumnSortList sortList = table.getColumnSortList();
		return sortList.size() > 0;
	}

	@Override
	public int getPageSize() {
		if (table == null) {
			return -1;
		}
		return table.getPageSize();
	}

	@Override
	public void setPageSize(int pageSize) {
		table.setPageSize(pageSize);
	}

	@Override
	public WSize getRowWidget(int rowno) {
		MutableInteger sel = new MutableInteger(rowno);
		// cell position not defined, take the position of the whole row
		List<MutableInteger> vList = table.getVisibleItems();
		int inde = -1;
		boolean found = false;
		for (MutableInteger ine : vList) {
			inde++;
			if (ine.equals(sel)) {
				found = true;
				break;
			}
		}
		assert found : LogT.getT().RowSelectedNotFound();
		TableRowElement ro = table.getRowElement(inde);
		WSize w;
		if (ro == null) {
			w = new WSize(table.getAbsoluteTop(), table.getAbsoluteLeft(), 0, 0);
		} else {
			w = new WSize(ro.getAbsoluteTop(), ro.getAbsoluteLeft(), ro.getClientHeight(), ro.getClientWidth());
		}
		return w;
	}

	@Override
	public void removeRow(int rownum) {
		List<MutableInteger> iList = dProvider.getList();
		for (MutableInteger i : iList) {
			if (i.intValue() == rownum) {
				// unselect before removing
				// it is necessary to trigger selecting the same row next time
				selectionModel.setSelected(new MutableInteger(rownum), false);
				iList.remove(i);
				// 2013/07/27 : unselect
				setEmpty();
				break;
			}
		}
		for (MutableInteger i : iList) {
			if (i.intValue() > rownum) {
				i.dec();
			}
		}
	}

	@Override
	public void addRow(int rownum) {
		List<MutableInteger> iList = dProvider.getList();
		if (rownum == -1) {
			// add new at the end of table
			MutableInteger addN = new MutableInteger(iList.size());
			iList.add(addN);
		} else {
			// add in the the middle
			// modify key values increasing by one after insertion
			for (MutableInteger i : iList) {
				if (i.intValue() >= rownum) {
					i.inc();
				}
			}
			// add new element to provider
			iList.add(rownum, new MutableInteger(rownum));
		}
		// redraw table
		// it is possible the all value are left in the widget screen
		// refreshing should drop this values
		// table.redraw();
	}

	private class GetRowNo implements IToRowNo {

		@Override
		public int row(MutableInteger key) {
			List<MutableInteger> vList = table.getVisibleItems();
			for (int i = 0; i < vList.size(); i++) {
				if (vList.get(i).equals(key)) {
					return i;
				}
			}
			return -1;
		}
	}

	@Override
	public void showInvalidate(int rowno, InvalidateFormContainer errContainer) {
		faEdit.setErrorLineInfo(new MutableInteger(rowno), errContainer, new GetRowNo());
	}

	@Override
	public void setNoWrap(boolean noWrap) {
		this.noWrap = noWrap;
		setWrapCol();
		table.redraw();
	}

	@Override
	public boolean isNoWrap() {
		return noWrap;
	}

	@Override
	public void setSortColumn(IVField col, boolean inc) {
		sortCol = col;
		sortInc = inc;
		setSortCol();
	}

	@Override
	public void refreshFooter(IVModelData footerV) {
		footFactory.setFooterV(footerV);
		if (table != null) {
			table.redrawFooters();
		}
	}

	@Override
	public void refreshHeader() {
		table.redrawHeaders();
	}

	@Override
	public void redrawRow(int rowno) {
		List<MutableInteger> iList = dProvider.getList();
		int absRow = 0;
		for (MutableInteger i : iList) {
			if (i.intValue() == rowno) {
				table.redrawRow(absRow);
				return;
			}
			absRow++;
		}
	}
}
