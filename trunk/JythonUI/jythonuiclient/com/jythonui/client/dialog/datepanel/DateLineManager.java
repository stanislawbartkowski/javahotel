/*
 * Copyright 2013 stanislawbartkowski@gmail.com 
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
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtmodel.table.FUtils;
import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.ISuccess;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.MutableInteger;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.Utils;
import com.gwtmodel.table.WSize;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.dateutil.DateFormatUtil;
import com.gwtmodel.table.common.dateutil.DateUtil;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.CustomStringSlot;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.slotmodel.ISlotable;
import com.gwtmodel.table.tabledef.IGHeader;
import com.gwtmodel.table.tabledef.VListHeaderContainer;
import com.gwtmodel.table.tabledef.VListHeaderDesc;
import com.gwtmodel.table.view.callback.CommonCallBack;
import com.gwtmodel.table.view.daytimetable.IDatePanelScroll;
import com.gwtmodel.table.view.daytimetable.IDrawPartSeason;
import com.gwtmodel.table.view.daytimetable.IDrawPartSeasonContext;
import com.gwtmodel.table.view.daytimetable.IScrollSeason;
import com.gwtmodel.table.view.table.GwtTableFactory;
import com.gwtmodel.table.view.table.IGwtTableModel;
import com.gwtmodel.table.view.table.IGwtTableView;
import com.gwtmodel.table.view.table.IListClicked;
import com.gwtmodel.table.view.table.IRowEditAction;
import com.jythonui.client.M;
import com.jythonui.client.dialog.DialogContainer;
import com.jythonui.client.dialog.IPerformClickAction;
import com.jythonui.client.dialog.VField;
import com.jythonui.client.util.CreateForm;
import com.jythonui.client.util.CreateForm.ColumnsDesc;
import com.jythonui.client.util.ExecuteAction;
import com.jythonui.client.util.RowVModelData;
import com.jythonui.client.variables.ISetGetVar;
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

public class DateLineManager implements ISetGetVar {

    private final DialogContainer dContainer;
    private final GwtTableFactory gFactory;

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

    public DateLineManager(DialogContainer dContainer) {
        this.dContainer = dContainer;
        gFactory = GwtGiniInjector.getI().getGwtTableFactory();
    }

    interface HeaderTemplate extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<p class=\"header-date\">{0} {1} <br/>{2}</p>")
        SafeHtml input(int day, String month, String weekday);
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
        private final RowIndex rI;
        private DateLineVariables dVariables;
        private int rowCol;
        private final IPerformClickAction iClick;

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
            int row = i.intValue();
            Object lid = getId(row);
            Date dCol = sData.getD(sData.getFirstD() + col - rowCol);
            return new CellData(lid, dCol);

        }

        private class DrawContent extends CommonCallBack<DialogVariables> {

            @Override
            public void onMySuccess(DialogVariables arg) {
                dVariables = arg.getDatelineVariables().get(dList.getId());
                if (dVariables == null) {
                    String mess = M.M().NoValuesRelatedTo(
                            dContainer.getInfo().getDialog().getId(),
                            dList.getId());
                    Utils.errAlert(mess);
                }
                iTable.refresh();
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
            ExecuteAction.action(var, dContainer.getInfo().getDialog().getId(),
                    ICommonConsts.JDATEACTIONGETVALUES, new DrawContent());
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

        }

        private IVField constructV(int i) {
            return new CVField(i);
        }

        private class TableModel implements IGwtTableModel {

            DateLineVariables v = null;

            @Override
            public void readChunkRange(int startw, int rangew, IVField sortC,
                    boolean asc, ISuccess signal) {
            }

            @Override
            public IVModelData get(int row) {
                RowContent r = v.getLines().getRowList().get(row);
                return new RowVModelData(rI, r);
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
                        String[] months = LocaleInfo.getCurrentLocale()
                                .getDateTimeFormatInfo().monthsShort();
                        String[] weekdays = LocaleInfo.getCurrentLocale()
                                .getDateTimeFormatInfo().weekdaysShort();
                        // sb.appendEscaped(months[DateFormatUtil.getM(d)-1]);
                        // sb.appendEscaped(weekdays[d.getDay()]);
                        int day = DateFormatUtil.getD(d);
                        String m = months[DateFormatUtil.getM(d) - 1];
                        String w = weekdays[d.getDay()];
                        sb.append(headerInput.input(day, m, w));
                    }

                }

                @SuppressWarnings("rawtypes")
                private class DataCell extends AbstractCell<MutableInteger> {

                    private final RowIndex rI = new RowIndex(
                            dList.constructDataLine());
                    private final int noC = dList.constructDataLine().size();

                    DataCell() {
                        super(BrowserEvents.CLICK);
                    }

                    @Override
                    public void render(Context context, MutableInteger value,
                            SafeHtmlBuilder sb) {
                        CellData cData = getCellData(context, value);
                        RowContent val = null;
                        FieldItem fId = dList.getFieldId();
                        if (dVariables != null)
                            for (RowContent r : dVariables.getValues()
                                    .getRowList()) {
                                FieldValue rO = rI.get(r, fId.getId());
                                FieldValue d = rI.get(r, dList.getDateColId());
                                int comp = FUtils.compareValue(cData.lId,
                                        rO.getValue(), fId.getFieldType(),
                                        fId.getAfterDot());
                                if (comp != 0)
                                    continue;
                                comp = DateUtil.compareDate(cData.date,
                                        d.getValueD());
                                if (comp != 0)
                                    continue;
                                val = r;
                                break;
                            }
                        String formId = null;
                        if (val == null) {
                            formId = dList.getDefaFile();
                        } else {
                            FieldValue form = rI.get(val, dList.getForm());
                            formId = form.getValueS();

                        }
                        FormDef fo = DialogFormat.findE(dList.getFormList(),
                                formId);
                        if (fo == null) {
                            String mess = M.M().NoFormRelatedToValue(
                                    dContainer.getInfo().getDialog().getId(),
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
                                String valS = FUtils.getValueS(v.getValue(),
                                        v.getType(), v.getAfterdot());
                                if (valS == null)
                                    valS = "";
                                buf.replace(pos, pos + 3, valS);
                            }
                            sb.appendHtmlConstant(buf.toString());
                        }
                    }

                    public void onBrowserEvent(Context context, Element parent,
                            MutableInteger value, NativeEvent event,
                            ValueUpdater<MutableInteger> valueUpdater) {
                        String eventType = event.getType();
                        if (eventType.equals(BrowserEvents.CLICK)) {
                            CellData cData = getCellData(context, value);
                            lastId = dList.getId();
                            aVar.clear();
                            FieldItem fId = dList.getFieldId();
                            FieldValue idVal = new FieldValue();
                            idVal.setValue(fId.getFieldType(), cData.lId,
                                    fId.getAfterDot());
                            aVar.add(new AddVar(ICommonConsts.JDATELINELINEID,
                                    idVal));
                            idVal = new FieldValue();
                            idVal.setValue(cData.date);
                            aVar.add(new AddVar(ICommonConsts.JDATELINEDATEID,
                                    idVal));
                            iClick.click(ICommonConsts.JDATELINE_CELLACTION,
                                    new WSize(parent));
                            // dContainer.
                        }
                        super.onBrowserEvent(context, parent, value, event,
                                valueUpdater);

                    }
                }

                private class DataColumn extends
                        Column<MutableInteger, MutableInteger> {

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
                ColumnsDesc desc = CreateForm.constructColumns(
                        dList.getColList(), null, null, null);
                rowCol = desc.colvisNo;

                for (int i = 0; i < dList.getColNo(); i++) {
                    VListHeaderDesc vNagl = new VListHeaderDesc(new CustomH(i),
                            constructV(i));
                    desc.hList.add(vNagl);
                }
                return new VListHeaderContainer(desc.hList,
                        dList.getDisplayName(), dList.getRowNo(), null, null,
                        null, desc.footList);
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

        }

        private class Synch extends SynchronizeList {

            IGWidget w;

            Synch() {
                super(2);
            }

            @Override
            protected void doTask() {
                vp.add(w.getGWidget());
                vp.add(iTable.getGWidget());
                publish(publishType, cell, new GWidget(vp));
            }

        }

        class DrawPart implements IDrawPartSeason {

            @Override
            public void setW(IGWidget w) {
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

        DateLineSlot(IDataType publishdType, IDataType dType, DateLine dl,
                CellId cell, IPerformClickAction iClick) {
            this.publishType = publishdType;
            this.dType = dType;
            this.cell = cell;
            dList = dl;
            CustomStringSlot sl = RefreshData.constructRefreshData(dType);
            registerSubscriber(sl, new Refresh());
            sl = RefreshData.constructRequestForRefreshData(dType);
            registerSubscriber(sl, new RequestForRefresh());
            iTable = gFactory.construct(null, null, null, null, null, null,
                    false);
            iTable.setModel(tModel);
            rI = new RowIndex(dl.getColList());
            this.iClick = iClick;
        }

        @Override
        public void startPublish(CellId cellId) {
            IDatePanelScroll wFactory = GwtGiniInjector.getI()
                    .getDatePanelScroll();
            iSeason = wFactory.getScrollSeason(new DrawPart(),
                    DateUtil.getToday());
            // TODO: parameter
            Date firstDate = DateFormatUtil.toD(2012, 1, 1);
            Date lastDate = DateFormatUtil.toD(2020, 12, 31);
            iSeason.createVPanel(firstDate, lastDate, dList.getColNo());
            sy.signalDone();
        }

    }

    public ISlotable contructSlotable(IDataType publishType, IDataType dType,
            DateLine dl, CellId cell, IPerformClickAction iClick) {
        return new DateLineSlot(publishType, dType, dl, cell, iClick);
    }

    @Override
    public void readVar(DialogVariables var) {

    }

}
