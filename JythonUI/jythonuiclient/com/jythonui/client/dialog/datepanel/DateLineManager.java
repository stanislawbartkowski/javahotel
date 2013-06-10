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

import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtmodel.table.FieldDataType;
import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.ISuccess;
import com.gwtmodel.table.IVField;
import com.gwtmodel.table.IVModelData;
import com.gwtmodel.table.MutableInteger;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.common.dateutil.DateFormatUtil;
import com.gwtmodel.table.common.dateutil.DateUtil;
import com.gwtmodel.table.daytimeline.CalendarTable;
import com.gwtmodel.table.daytimeline.CalendarTable.PeriodType;
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
import com.gwtmodel.table.view.daytimetable.IDrawPartSeason;
import com.gwtmodel.table.view.daytimetable.IDrawPartSeasonContext;
import com.gwtmodel.table.view.daytimetable.IScrollSeason;
import com.gwtmodel.table.view.daytimetable.impl.WidgetScrollSeasonFactory;
import com.gwtmodel.table.view.table.GwtTableFactory;
import com.gwtmodel.table.view.table.IGwtTableModel;
import com.gwtmodel.table.view.table.IGwtTableView;
import com.gwtmodel.table.view.table.IListClicked;
import com.gwtmodel.table.view.table.IRowEditAction;
import com.jythonui.client.dialog.DialogContainer;
import com.jythonui.client.util.CreateForm;
import com.jythonui.client.util.RowVModelData;
import com.jythonui.shared.DateLine;
import com.jythonui.shared.DateLineVariables;
import com.jythonui.shared.RowContent;
import com.jythonui.shared.RowIndex;

public class DateLineManager {

    private final DialogContainer dContainer;
    private final GwtTableFactory gFactory;

    public DateLineManager(DialogContainer dContainer) {
        this.dContainer = dContainer;
        gFactory = GwtGiniInjector.getI().getGwtTableFactory();
    }

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

            // private class RowData extends AVModelData {
            //
            // private final int row;
            //
            // RowData(int row) {
            // this.row = row;
            // }
            //
            // @Override
            // public Object getF(IVField fie) {
            // CVField c = (CVField) fie;
            // if (c.cId == 0) {
            // RowContent ro = v.getLines().getRowList().get(row);
            // return ro.getRow(0).getValueS();
            // }
            // return null;
            // }
            //
            // @Override
            // public void setF(IVField fie, Object o) {
            // // empty
            //
            // }
            //
            // @Override
            // public boolean isValid(IVField fie) {
            // // not important
            // return true;
            // }
            //
            // @Override
            // public List<IVField> getF() {
            // return null;
            // }
            //
            // }

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

                }

                private class DColumn extends TextColumn<MutableInteger> {

                    @Override
                    public String getValue(MutableInteger row) {
                        // TODO Auto-generated method stub
                        return "room";
                    }

                }

                @Override
                public Header<Date> getHeader() {
                    return new DHeader();
                }

                @Override
                public Column<?, ?> getColumn() {
                    return new DColumn();
                }

            }

            @Override
            public VListHeaderContainer getHeaderList() {
                List<VListHeaderDesc> vList = CreateForm.constructColumns(
                        dList.getColList(), null);
                for (int i = 0; i < dList.getColNo(); i++) {
                    VListHeaderDesc vNagl = new VListHeaderDesc(new CustomH(i),
                            constructV(i));
                    vList.add(vNagl);
                }
                return new VListHeaderContainer(vList, dList.getDisplayName());
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
                if (sData != null) {
                    sData = parData;
                    iTable.refreshHeader();
                } else
                    sData = parData;
                iTable.refresh();
            }

        }

        private class Refresh implements ISlotListener {

            @Override
            public void signal(ISlotSignalContext slContext) {
                RefreshData rData = (RefreshData) slContext.getCustom();
                tModel.v = rData.getValue();
                iTable.refresh();
            }

        }

        DateLineSlot(IDataType publishdType, IDataType dType, DateLine dl,
                CellId cell) {
            this.publishType = publishdType;
            this.dType = dType;
            this.cell = cell;
            dList = dl;
            CustomStringSlot sl = RefreshData.constructRefreshData(dType);
            registerSubscriber(sl, new Refresh());
            iTable = gFactory.construct(null, null, null, null, null, null,
                    false);
            iTable.setPageSize(dl.getRowNo());
            iTable.setModel(tModel);
            rI = new RowIndex(dl.getColList());
        }

        @Override
        public void startPublish(CellId cellId) {
            WidgetScrollSeasonFactory wFactory = GwtGiniInjector.getI()
                    .getWidgetScrollSeasonFactory();
            iSeason = wFactory.getScrollSeason(new DrawPart(),
                    DateUtil.getToday());
            Date fDate = DateFormatUtil.toD(2013, 1, 1);
            Date lDate = DateFormatUtil.toD(2013, 12, 31);
            List<Date> listD = CalendarTable.listOfDates(fDate, lDate,
                    PeriodType.byDay);
            iSeason.createVPanel(listD, 20);
            sy.signalDone();
        }

    }

    public ISlotable contructSlotable(IDataType publishType, IDataType dType,
            DateLine dl, CellId cell) {
        return new DateLineSlot(publishType, dType, dl, cell);
    }

}
