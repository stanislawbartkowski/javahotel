/*
 * Copyright 2017 stanislawbartkowski@gmail.com  
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
package com.jythonui.client.dialog.datepanel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtmodel.table.DateUtil;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.IOkModelData;
import com.gwtmodel.table.ISuccess;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.MutableInteger;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.buttoncontrolmodel.ControlButtonDesc;
import com.gwtmodel.table.buttoncontrolmodel.ListOfControlDesc;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.DateFormat;
import com.gwtmodel.table.controlbuttonview.ControlButtonViewFactory;
import com.gwtmodel.table.controlbuttonview.IControlButtonView;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.listdataview.SearchTable;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.CustomStringSlot;
import com.gwtmodel.table.slotmodel.DataActionEnum;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.slotmodel.SlU;
import com.gwtmodel.table.smessage.IGetStandardMessage;
import com.gwtmodel.table.tabledef.IGHeader;
import com.gwtmodel.table.tabledef.VListHeaderContainer;
import com.gwtmodel.table.tabledef.VListHeaderDesc;
import com.gwtmodel.table.view.callback.CommonCallBack;
import com.gwtmodel.table.view.daytimetable.IDatePanelScroll;
import com.gwtmodel.table.view.daytimetable.IDrawPartSeason;
import com.gwtmodel.table.view.daytimetable.IDrawPartSeasonContext;
import com.gwtmodel.table.view.daytimetable.IScrollSeason;
import com.gwtmodel.table.view.table.GwtTableFactory;
import com.gwtmodel.table.view.table.IBoolHeaderClick;
import com.gwtmodel.table.view.table.IGetColSpan;
import com.gwtmodel.table.view.table.IGwtTableModel;
import com.gwtmodel.table.view.table.IGwtTableView;
import com.gwtmodel.table.view.table.IListClicked;
import com.gwtmodel.table.view.table.IRowEditAction;
import com.jythonui.client.IUIConsts;
import com.jythonui.client.M;
import com.jythonui.client.dialog.IDateLineManager;
import com.jythonui.client.dialog.IDialogContainer;
import com.jythonui.client.dialog.IPerformClickAction;
import com.jythonui.client.dialog.VField;
import com.jythonui.client.util.CreateForm;
import com.jythonui.client.util.CreateForm.ColumnsDesc;
import com.jythonui.client.util.ExecuteAction;
import com.jythonui.client.util.JUtils;
import com.jythonui.client.util.ListOfButt;
import com.jythonui.client.util.RowVModelData;
import com.jythonui.shared.DateLine;
import com.jythonui.shared.DateLineVariables;
import com.jythonui.shared.DialogFormat;
import com.jythonui.shared.DialogVariables;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.FormDef;
import com.jythonui.shared.ICommonConsts;
import com.jythonui.shared.ListOfRows;
import com.jythonui.shared.RowContent;
import com.jythonui.shared.RowIndex;

class DateLineManager implements IDateLineManager {

	private final IDialogContainer dContainer;
	private final GwtTableFactory gFactory;
	private final IGetStandardMessage iMess;

	// ---- variables
	private class AddVar {
		final String id;
		final FieldValue o;

		AddVar(String id, FieldValue o) {
			this.id = id;
			this.o = o;
		}
	}

	private String lastId = "";

	private final List<AddVar> aVar = new ArrayList<AddVar>();

	@Override
	public void addToVar(DialogVariables var, String buttonId) {
		var.setValueS(ICommonConsts.JDATELINEQUERYID, lastId);
		for (AddVar a : aVar) {
			var.setValue(a.id, a.o);
		}
	}

	DateLineManager(IDialogContainer dContainer) {
		this.dContainer = dContainer;
		gFactory = GwtGiniInjector.getI().getGwtTableFactory();
		iMess = GwtGiniInjector.getI().getStandardMessage();
	}

	interface HeaderTemplate extends SafeHtmlTemplates {

		@SafeHtmlTemplates.Template("<p class=\"header-date {0}\">{1} {2} <br/>{3}</p>")
		SafeHtml input(String addClass, int day, String month, String weekday);
	}

	private HeaderTemplate headerInput = GWT.create(HeaderTemplate.class);

	private class DateLineSlot extends AbstractSlotContainer {

		private final CellId cell;
		private IScrollSeason iSeason;
		private final Synch sy = new Synch();
		private final VerticalPanel vp = new VerticalPanel();
		private final IGwtTableView iTable;
		private final DateLine dList;
		private final IDataType publishType;
		private final TableModel tModel = new TableModel();
		private IDrawPartSeasonContext sData = null;
		private final RowIndex rColI;
		private final RowIndex rI;
		private DateLineVariables dVariables;
		private int rowCol;
		private final IPerformClickAction iClick;
		private final SpanColContainer span = new SpanColContainer();
		private IControlButtonView iView = null;
		private final IPerformClickAction customClick;

		private class CellData {
			final Object lId;
			final Date date;

			CellData(Object lId, Date date) {
				this.lId = lId;
				this.date = date;
			}
		}

		private CellData getCellData(Context context, MutableInteger i) {
			int col = context.getColumn();
			return getCellData(i, col);
		}

		private CellData getCellData(MutableInteger i, int col) {
			int row = i.intValue();
			Object lid = getId(row);
			Date dCol = sData.getD(sData.getFirstD() + col - rowCol);
			return new CellData(lid, dCol);
		}

		private RowContent findRowContent(CellData cData) {
			RowContent val = null;
			FieldItem fId = dList.getFieldId();
			if (dVariables != null)
				for (RowContent r : dVariables.getValues().getRowList()) {
					FieldValue rO = rI.get(r, fId.getId());
					int comp = FUtils.compareValue(cData.lId, rO.getValue(), fId.getFieldType(), fId.getAfterDot());
					if (comp != 0)
						continue;
					FieldValue d = rI.get(r, dList.getDateColId());
					comp = DateUtil.compareDate(cData.date, d.getValueD());
					if (comp != 0)
						continue;
					val = r;
					break;
				}
			return val;
		}

		private class DrawContent extends CommonCallBack<DialogVariables> {

			@Override
			public void onMySuccess(DialogVariables arg) {
				span.clear();
				dVariables = arg.getDatelineVariables().get(dList.getId());
				if (dVariables == null) {
					String mess = M.M().NoValuesRelatedTo(dContainer.getInfo().getDialog().getId(), dList.getId());
					Utils.errAlert(mess);
				}
				iTable.refresh();
			}

		}

		private class GetColSpan implements IGetColSpan {

			private final int spanNum;
			private final int hintNum;

			GetColSpan() {
				RowIndex rI = new RowIndex(dList.constructDataLine());
				spanNum = rI.getInde(ICommonConsts.JDATELINESPAN);
				hintNum = rI.getInde(ICommonConsts.JDATELINEHINT);
			}

			@Override
			public int get(MutableInteger rowNo, int colNo) {
				if (colNo < rowCol)
					return 0;
				CellData cData = getCellData(rowNo, colNo);
				RowContent r = findRowContent(cData);
				int spanC = 0;
				if (r != null) {
					FieldValue val = r.getRow(spanNum);
					if (val.getValue() != null && val.getValueI() > 1)
						spanC = val.getValueI();
				}
				if (spanC != 0)
					span.addSpanInfo(rowNo, colNo, spanC);
				return spanC;
			}

			@Override
			public String getHint(MutableInteger row, int colNo) {

				colNo = span.recalculateCol(row, colNo);
				CellData cData = getCellData(row, colNo);
				RowContent r = findRowContent(cData);
				FieldValue val = r.getRow(hintNum);
				if (val == null)
					return null;
				return iMess.getMessage(val.getValueS());
			}

		}

		private Object getId(int i) {
			FieldItem id = dList.getFieldId();
			IVModelData v = iTable.getViewModel().get(i);
			RowVModelData r = (RowVModelData) v;
			IVField vf = VField.construct(id);
			Object o = r.getF(vf);
			return o;
		}

		private void drawContent() {
			if (sData == null)
				return;
			if (iTable.getViewModel().getSize() == 0)
				return;
			DialogVariables var = dContainer.getiCon().getVariables(null);
			ListOfRows queryDateLine = var.getQueryDateLine();
			RowIndex rI = new RowIndex(dList.constructQueryLine());
			int first = sData.getFirstD();
			int last = sData.getLastD();
			Date firstD = sData.getD(first);
			Date lastD = sData.getD(last);
			FieldItem id = dList.getFieldId();
			for (int i = 0; i < iTable.getViewModel().getSize(); i++) {
				Object o = getId(i);
				RowContent row = rI.constructRow();
				FieldValue vId = new FieldValue();
				vId.setValue(id.getFieldType(), o, id.getAfterDot());
				rI.setRowField(row, id.getId(), vId);
				vId = new FieldValue();
				vId.setValue(firstD);
				rI.setRowField(row, ICommonConsts.JDATELINEQUERYFROM, vId);
				vId = new FieldValue();
				vId.setValue(lastD);
				rI.setRowField(row, ICommonConsts.JDATELINEQEURYTO, vId);
				queryDateLine.addRow(row);
			}
			var.setValueS(ICommonConsts.JDATELINEQUERYID, dList.getId());
			ExecuteAction.action(var, dContainer.getInfo().getDialog().getId(), ICommonConsts.JDATEACTIONGETVALUES,
					new DrawContent());
		}

		private class CVField implements IVField {

			private final int cId;

			CVField(int cId) {
				this.cId = cId;
			}

			@Override
			public boolean eq(IVField o) {
				CVField c = (CVField) o;
				return c.cId == cId;
			}

			@Override
			public FieldDataType getType() {
				return FieldDataType.constructString();
			}

			@Override
			public String getId() {
				return CUtil.NumbToS(cId);
			}

			@Override
			public String getLabel() {
				// TODO Auto-generated method stub
				return null;
			}

		}

		private IVField constructV(int i) {
			return new CVField(i);
		}

		private boolean isWeekend(Date d) {
			int weekday = DateUtil.weekDay(d);
			return ((weekday == 6) || (weekday == 0));
		}

		private boolean isToday(Date d) {
			return DateUtil.eqDate(DateUtil.getToday(), d);
		}

		private class TableModel implements IGwtTableModel {

			DateLineVariables v = null;

			@Override
			public void readChunkRange(int startw, int rangew, IVField sortC, boolean asc, ISuccess signal) {
			}

			@Override
			public IVModelData get(int row) {
				RowContent r = v.getLines().getRowList().get(row);
				return new RowVModelData(rColI, r);
			}

			@Override
			public long getSize() {
				if (v == null)
					return 0;
				return v.getLines().getRowList().size();
			}

			private class CustomH implements IGHeader {

				private final int col;

				CustomH(int col) {
					this.col = col;
				}

				private class DHeader extends Header<Date> {

					DHeader() {
						super(new DateCell());
					}

					@Override
					public Date getValue() {
						if (sData == null)
							return null;
						Date d = sData.getD(sData.getFirstD() + col);
						return d;
					}

					@Override
					public void render(Context context, SafeHtmlBuilder sb) {
						if (sData == null)
							return;
						Date d = sData.getD(sData.getFirstD() + col);
						// String[] weekdays =
						// LocaleInfo.getCurrentLocale().getDateTimeFormatInfo().weekdaysNarrow();
						String[] months = LocaleInfo.getCurrentLocale().getDateTimeFormatInfo().monthsShort();
						String[] weekdays = LocaleInfo.getCurrentLocale().getDateTimeFormatInfo().weekdaysShort();
						// sb.appendEscaped(months[DateFormatUtil.getM(d)-1]);
						// sb.appendEscaped(weekdays[d.getDay()]);
						int day = DateFormat.getD(d);
						String m = months[DateFormat.getM(d) - 1];
						String w = weekdays[d.getDay()];
						// Joiner join = Joiner.on(' ').skipNulls();
						// sb.append(headerInput.input(join.join(isToday(d) ?
						// IUIConsts.HEADER_TODAY : null,
						// isWeekend(d) ? IUIConsts.HEADER_WEEKEND : null), day,
						// m, w));
						String s = CUtil.joinS(' ', isToday(d) ? IUIConsts.HEADER_TODAY : null,
								isWeekend(d) ? IUIConsts.HEADER_WEEKEND : null);
						sb.append(headerInput.input(s, day, m, w));
					}

				}

				private class DataCell extends AbstractCell<MutableInteger> {

					private final int noC = dList.constructDataLine().size();

					DataCell() {
						super(BrowserEvents.CLICK);
					}

					@Override
					public void render(Context context, MutableInteger value, SafeHtmlBuilder sb) {
						CellData cData = getCellData(context, value);
						RowContent val = null;
						if (dVariables != null)
							val = findRowContent(cData);
						String formId = null;
						if (val == null)
							formId = dList.getDefaFile();
						else {
							FieldValue form = rI.get(val, dList.getForm());
							formId = form.getValueS();
						}
						FormDef fo = DialogFormat.findE(dList.getFormList(), formId);
						if (fo == null) {
							String mess = M.M().NoFormRelatedToValue(dContainer.getInfo().getDialog().getId(),
									dList.getId(), formId);
							Utils.errAlert(mess);
							return;
						}
						// number of additional arguments
						int noArgs = val == null ? 0 : val.getLength() - noC;
						if (noArgs == 0)
							sb.appendHtmlConstant(fo.getFormDef());
						else {
							StringBuffer buf = new StringBuffer(fo.getFormDef());
							for (int i = 0; i < noArgs; i++) {
								String s = "{" + i + "}";
								int pos = buf.indexOf(s);
								if (pos == -1)
									continue;
								FieldValue v = val.getRow(noC + i);
								String valS = FUtils.getValueS(v.getValue(), v.getType(), v.getAfterdot());
								if (valS == null)
									valS = "";
								buf.replace(pos, pos + 3, valS);
							}
							sb.appendHtmlConstant(buf.toString());
						}
					}

					public void onBrowserEvent(Context context, Element parent, MutableInteger value, NativeEvent event,
							ValueUpdater<MutableInteger> valueUpdater) {
						String eventType = event.getType();
						if (eventType.equals(BrowserEvents.CLICK)) {
							int col = context.getColumn();
							col = span.recalculateCol(value, col);
							CellData cData = getCellData(value, col);
							lastId = dList.getId();
							aVar.clear();
							FieldItem fId = dList.getFieldId();
							FieldValue idVal = new FieldValue();
							idVal.setValue(fId.getFieldType(), cData.lId, fId.getAfterDot());
							aVar.add(new AddVar(ICommonConsts.JDATELINELINEID, idVal));
							idVal = new FieldValue();
							idVal.setValue(cData.date);
							aVar.add(new AddVar(ICommonConsts.JDATELINEDATEID, idVal));
							iClick.click(ICommonConsts.JDATELINE_CELLACTION, new WSize(parent));
							// dContainer.
						}
						super.onBrowserEvent(context, parent, value, event, valueUpdater);

					}
				}

				private class DataColumn extends Column<MutableInteger, MutableInteger> {

					DataColumn() {
						super(new DataCell());
					}

					@Override
					public MutableInteger getValue(MutableInteger object) {
						return object;
					}

				}

				@Override
				public Header<Date> getHeader() {
					return new DHeader();
				}

				@Override
				public Column<?, ?> getColumn() {
					// return new DColumn();
					return new DataColumn();
				}

			}

			@Override
			public VListHeaderContainer getHeaderList() {
				ColumnsDesc desc = CreateForm.constructColumns(dList.getColList(), null, null);
				rowCol = desc.colvisNo;

				for (int i = 0; i < dList.getColNo(); i++) {
					VListHeaderDesc vNagl = new VListHeaderDesc(new CustomH(i), constructV(i));
					desc.hList.add(vNagl);
				}
				return new VListHeaderContainer(desc.hList, dList.getDisplayName(), dList.getRowNo(), null, null, null,
						desc.footList, 0, false);
			}

			@Override
			public IListClicked getIClicked() {
				return null;
			}

			@Override
			public boolean containsData() {
				return v != null;
			}

			@Override
			public boolean unSelectAtOnce() {
				return false;
			}

			@Override
			public int treeLevel(int row) {
				return 0;
			}

			@Override
			public IRowEditAction getRowEditAction() {
				return null;
			}

			@Override
			public String getClassName() {
				if (dList.getClassName() != null)
					return dList.getClassName();
				return ICommonConsts.DATEPANELCLASSDEFAULT;
			}

			@Override
			public String getClassNameForColumn(IVField v) {
				if (sData == null)
					return null;
				if (!(v instanceof CVField))
					return null;
				CVField c = (CVField) v;
				Date d = sData.getD(c.cId);
				// Joiner join = Joiner.on(' ').skipNulls();
				// return join.join(isToday(d) ? IUIConsts.CELL_COLUMN_TODAY :
				// null,
				// isWeekend(d) ? (IUIConsts.CELL_COLUMN_WEEKEND) : null);
				return CUtil.joinS(' ', isToday(d) ? IUIConsts.CELL_COLUMN_TODAY : null,
						isWeekend(d) ? (IUIConsts.CELL_COLUMN_WEEKEND) : null);
			}

			@Override
			public IBoolHeaderClick getBClicked() {
				// TODO Auto-generated method stub
				return null;
			}

		}

		private class Synch extends SynchronizeList {

			IGWidget w;
			IGWidget bView;

			Synch() {
				super(3);
			}

			@Override
			protected void doTask() {
				if (bView != null) {
					HorizontalPanel hp = new HorizontalPanel();
					hp.add(bView.getGWidget());
					hp.add(w.getGWidget());
					vp.add(hp);
				} else
					vp.add(w.getGWidget());
				vp.add(iTable.getGWidget());
				publish(publishType, cell, new GWidget(vp));
			}

		}

		class DrawPart implements IDrawPartSeason {

			@Override
			public void set(IGWidget w) {
				sy.w = w;
				sy.signalDone();
			}

			@Override
			public void refresh(IDrawPartSeasonContext parData) {
				if (sData == null) {
					sData = parData;
					iTable.refreshHeader();
				} else
					sData = parData;
				drawContent();
			}

		}

		private class Refresh implements ISlotListener {

			@Override
			public void signal(ISlotSignalContext slContext) {
				RefreshData rData = (RefreshData) slContext.getCustom();
				tModel.v = rData.getValue();
				drawContent();
			}

		}

		private class RequestForRefresh implements ISlotListener {

			@Override
			public void signal(ISlotSignalContext slContext) {
				drawContent();
			}
		}

		private class GotoDate implements ISlotListener {

			@Override
			public void signal(ISlotSignalContext slContext) {
				GotoDateSignal si = (GotoDateSignal) slContext.getCustom();
				iSeason.redraw(si.getValue());
			}

		}

		private class FindRow implements ISlotListener {

			@Override
			public void signal(ISlotSignalContext slContext) {
				IOkModelData iOk = slContext.getIOkModelData();
				if (!SearchTable.search(dType, iOk, iTable, true, false))
					Utils.errAlert(M.M().SearchFailed());
			}

		}

		private class GetBWidget implements ISlotListener {

			@Override
			public void signal(ISlotSignalContext slContext) {
				sy.bView = slContext.getGwtWidget();
				sy.signalDone();
			}

		}

		private class CustomClick implements ISlotListener {

			@Override
			public void signal(ISlotSignalContext slContext) {
				WSize w = new WSize(slContext.getGwtWidget());
				String s = slContext.getSlType().getButtonClick().getCustomButt();
				customClick.click(s, w);
			}

		}

		DateLineSlot(IDataType publishdType, IDataType dType, DateLine dl, CellId cell, IPerformClickAction iClick,
				IPerformClickAction customClick) {
			this.publishType = publishdType;
			this.dType = dType;
			this.cell = cell;
			dList = dl;
			this.customClick = customClick;
			String sButton = dl.getStandButt();
			if (!CUtil.EmptyS(sButton)) {
				ListOfButt.IGetButtons i = ListOfButt.constructList(dContainer.getD(), sButton);
				if (!i.getCustomList().isEmpty()) {
					ListOfControlDesc cButton = new ListOfControlDesc(i.getCustomList());
					iView = ControlButtonViewFactory.construct(dType, cButton, false);
					SlU.registerWidgetListener0(dType, iView, new GetBWidget());
					for (ControlButtonDesc b : i.getCustomList())
						iView.getSlContainer().registerSubscriber(dType, b.getActionId(), new CustomClick());
				}
			}
			CustomStringSlot sl = RefreshData.constructRefreshData(dType);
			registerSubscriber(sl, new Refresh());
			sl = RefreshData.constructRequestForRefreshData(dType);
			registerSubscriber(sl, new RequestForRefresh());
			registerSubscriber(GotoDateSignal.constructSlot(dType), new GotoDate());
			registerSubscriber(dType, DataActionEnum.FindRowBeginningList, new FindRow());
			iTable = gFactory.construct(null, null, null, null, null, null, false, new GetColSpan());
			iTable.setModel(tModel);
			rColI = new RowIndex(dl.getColList());
			rI = new RowIndex(dList.constructDataLine());
			this.iClick = iClick;
		}

		private int getyear(String param, int defa) {
			String s = Utils.getCValue(param);
			if (CUtil.EmptyS(s))
				return defa;
			int y = Utils.getNum(s);
			if (y < 2000 || y > 2100) {
				String mess = M.M().YearValueInvalid(param, s, y);
				Utils.errAlert(mess);
			}
			return y;
		}

		@Override
		public void startPublish(CellId cellId) {
			IDatePanelScroll wFactory = GwtGiniInjector.getI().getDatePanelScroll();
			Date today = DateUtil.getToday();
			iSeason = wFactory.getScrollSeason(new DrawPart(), today);
			int firstY = getyear(IUIConsts.DATELINE_STARTYEAR, IUIConsts.DATELINE_STARTYEARDEFAULT);
			int lastY = getyear(IUIConsts.DATELINE_ENDYEAR, IUIConsts.DATELINE_ENDYEARDEFAULT);
			if (firstY > lastY) {
				String s = M.M().FirstYearCannotBeGreateThenLastYear(firstY, lastY);
				Utils.errAlert(s);
			}
			if (DateFormat.getY(today) < firstY || DateFormat.getY(today) > lastY) {
				String s = M.M().CurrentDateNotInRange(DateFormat.getY(today), firstY, lastY);
				Utils.errAlert(s);
			}
			Date firstDate = DateFormat.toD(firstY, 1, 1);
			Date lastDate = DateFormat.toD(lastY, 12, 31);
			iSeason.createVPanel(firstDate, lastDate, dList.getColNo(), dList.getCurrentPos());
			sy.signalDone();
			if (iView == null) {
				sy.bView = null;
				sy.signalDone();
			} else
				SlU.startPublish0(iView);
		}

	}

	@Override
	public ISlotable contructSlotable(IDataType publishType, IDataType dType, DateLine dl, CellId cell,
			IPerformClickAction iClick, IPerformClickAction customClick) {
		return new DateLineSlot(publishType, dType, dl, cell, iClick, customClick);
	}

	@Override
	public void readVar(DialogVariables var, IReadVarContext iC) {

	}

}
