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
package com.jythonui.client.charts;

import java.math.BigDecimal;
import java.util.Date;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.visualizations.corechart.AreaChart;
import com.google.gwt.visualization.client.visualizations.corechart.BarChart;
import com.google.gwt.visualization.client.visualizations.corechart.ColumnChart;
import com.google.gwt.visualization.client.visualizations.corechart.CoreChart;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart;
import com.google.gwt.visualization.client.visualizations.corechart.ScatterChart;
import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.IDataType;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.common.CUtil;
import com.gwtmodel.table.slotmodel.AbstractSlotContainer;
import com.gwtmodel.table.slotmodel.CellId;
import com.gwtmodel.table.slotmodel.CustomStringSlot;
import com.gwtmodel.table.slotmodel.ISlotListener;
import com.gwtmodel.table.slotmodel.ISlotSignalContext;
import com.gwtmodel.table.smessage.IGetStandardMessage;
import com.jythonui.client.dialog.IDialogContainer;
import com.jythonui.client.injector.UIGiniInjector;
import com.jythonui.shared.ChartFormat;
import com.jythonui.shared.FieldItem;
import com.jythonui.shared.FieldValue;
import com.jythonui.shared.ListOfRows;
import com.jythonui.shared.RowContent;
import com.jythonui.shared.RowIndex;

class CharContainer extends AbstractSlotContainer {

    private final IDialogContainer iDialog;
    private final String id;
    private final IDataType publishType;
    private final ChartFormat fo;
    private final IGetStandardMessage iGetMessage;
    private CoreChart chart = null;

    private class Sync extends SynchronizeList {

        private ListOfRows ro;
        private CellId cellId;

        Sync() {
            super(2);
        }

        @Override
        protected void doTask() {
            drawChart(cellId, ro);
        }

    }

    private final Sync sy = new Sync();

    private void drawChart(CellId cellId, ListOfRows ro) {
        if (chart == null) {
            chart = createChart(ro);
            publish(publishType, cellId, new GWidget(chart));
            return;
        }
        chart.draw(createTable(ro), createOptions());
    }

    private Options startOpt() {
        Options o;
        switch (fo.getChartType()) {
        case PIE:
            PieChart.PieOptions p = PieChart.PieOptions.create();
            p.set3D(fo.isPie3D());
            o = p;
            break;
        default:
            o = Options.create();
        }
        o.setType(CoreChart.Type.valueOf(fo.getChartType().name()));
        return o;
    }

    private Options createOptions() {
        Options options = startOpt();
        options.setWidth(fo.getOptionsWidth());
        options.setHeight(fo.getOptionsHeight());
        // options.set3D(true);
        if (!CUtil.EmptyS(fo.getDisplayName()))
            options.setTitle(iGetMessage.getMessage(fo.getDisplayName()));
        return options;
    }

    private AbstractDataTable createTable(ListOfRows ro) {
        DataTable data = DataTable.create();
        ChartFormat fo = iDialog.getD().findChart(id);
        for (FieldItem i : fo.getColList()) {
            String s = null;
            if (!CUtil.EmptyS(i.getDisplayName()))
                s = iGetMessage.getMessage(i.getDisplayName());
            ColumnType t = ColumnType.STRING;
            switch (i.getFieldType()) {
            case BIGDECIMAL:
            case INT:
            case LONG:
                t = ColumnType.NUMBER;
                break;
            case BOOLEAN:
                t = ColumnType.BOOLEAN;
                break;
            case DATE:
                t = ColumnType.DATE;
                break;
            case DATETIME:
                t = ColumnType.DATETIME;
                break;
            }
            if (s != null)
                data.addColumn(t, s);
            else
                data.addColumn(t);
        }
        data.addRows(ro.getRowList().size());
        RowIndex ri = new RowIndex(fo.getColList());
        int row = 0;
        for (RowContent r : ro.getRowList()) {
            int i = -1;
            for (FieldItem it : ri.getColList()) {
                i++;
                FieldValue val = r.getRow(i);
                if (val.getValue() == null) {
                    data.setValueNull(row, i);
                    continue;
                }
                switch (it.getFieldType()) {
                case BIGDECIMAL:
                    BigDecimal b = val.getValueBD();
                    data.setValue(row, i, b.doubleValue());
                    break;
                case LONG:
                    Long l = val.getValueL();
                    data.setValue(row, i, l.intValue());
                    break;
                case INT:
                    Integer ival = val.getValueI();
                    data.setValue(row, i, ival.intValue());
                    break;
                case DATE:
                case DATETIME:
                    Date d = val.getValueD();
                    data.setValue(row, i, d);
                    break;
                case BOOLEAN:
                    Boolean bval = val.getValueB();
                    data.setValue(row, i, bval.booleanValue());
                    break;
                default:
                    data.setValue(row, i, val.getValueS());
                    break;

                } // switch
            } // for
            row++;
        } // for

        return data;
    }

    private AbstractDataTable createTestTable() {
        DataTable data = DataTable.create();
        data.addColumn(ColumnType.STRING, "Task");
        data.addColumn(ColumnType.NUMBER, "Hours per Day");
        data.addRows(2);
        data.setValue(0, 0, "Work");
        data.setValue(0, 1, 14);
        data.setValue(1, 0, "Sleep");
        data.setValue(1, 1, 10);
        return data;
    }

    private class DrawChart implements ISlotListener {

        @Override
        public void signal(ISlotSignalContext slContext) {
            DrawChartSignal si = (DrawChartSignal) slContext.getCustom();
            sy.ro = si.getValue();
            sy.signalDone();
        }

    }

    CharContainer(IDialogContainer iDialog, String id, IDataType publishType,
            IDataType dType) {
        this.iDialog = iDialog;
        this.id = id;
        this.dType = dType;
        this.publishType = publishType;
        this.fo = iDialog.getD().findChart(id);
        assert fo != null;
        iGetMessage = UIGiniInjector.getI().getGetStandardMessage();
        CustomStringSlot sl = DrawChartSignal.constructSlot(dType);
        this.registerSubscriber(sl, new DrawChart());
    }

    private CoreChart createChart(ListOfRows ro) {
        AbstractDataTable a = createTable(ro);
        Options o = createOptions();
        switch (fo.getChartType()) {
        case PIE:
            return new PieChart(a, o);
        case AREA:
            return new AreaChart(a, o);
        case BARS:
            return new BarChart(a, o);
        case LINE:
            return new LineChart(a, o);
        case SCATTER:
            return new ScatterChart(a, o);
        case COLUMNS:
            return new ColumnChart(a, o);
        }
        return null;
    }

    @Override
    public void startPublish(CellId cellId) {
        // Widget w = createChart();
        // publish(publishType, cellId, new GWidget(w));
        sy.cellId = cellId;
        sy.signalDone();

    }

}
