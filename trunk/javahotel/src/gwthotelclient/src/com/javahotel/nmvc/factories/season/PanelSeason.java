/*
 * Copyright 2012 stanislawbartkowski@gmail.com 
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
package com.javahotel.nmvc.factories.season;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.common.PeriodT;
import com.gwtmodel.table.common.dateutil.DateUtil;
import com.gwtmodel.table.daytimeline.CalendarTable;
import com.gwtmodel.table.daytimeline.CalendarTable.PeriodType;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.MM;
import com.gwtmodel.table.view.daytimetable.IDrawPartSeason;
import com.gwtmodel.table.view.daytimetable.IDrawPartSeasonContext;
import com.gwtmodel.table.view.daytimetable.IScrollSeason;
import com.gwtmodel.table.view.daytimetable.impl.WidgetScrollSeasonFactory;
import com.gwtmodel.table.view.util.PopupTip;
import com.javahotel.client.ConfigParam;
import com.javahotel.client.M;
import com.javahotel.client.user.season.SeasonUtil;
import com.javahotel.common.dateutil.GetPeriods;
import com.javahotel.common.seasonutil.CreateTableSeason;
import com.javahotel.common.toobject.OfferSeasonP;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class PanelSeason {

    private final ComplexPanel controlP;
    private final IScrollSeason sCr;
    private OfferSeasonP oP;
    private Date d1;
    private Date d2;
    private List<Date> dLine;
    private List<PeriodT> coP;
    private static final int NO = 12;
    private final Grid g;

    PanelSeason(Grid g, final ComplexPanel controlP, final Date today) {
        this.g = g;
        this.controlP = controlP;
        WidgetScrollSeasonFactory wFactory = GwtGiniInjector.getI()
                .getWidgetScrollSeasonFactory();
        sCr = wFactory.getScrollSeason(new DrawC(), DateUtil.getToday());

    }

    private void setDayLabel(DayLabel l, Date da, boolean today) {
        PeriodT p = GetPeriods.findPeriod(da, coP);
        String sTyle = SeasonUtil.getStyleForDay(p);
        l.lUp.setStyleName(today ? SeasonUtil.getTodayStyle() : sTyle);
        l.lDown.setStyleName(sTyle);
        String sName = SeasonUtil.getName(p);
        l.setMessage(sName);
    }

    private class DrawC implements IDrawPartSeason {

        @Override
        public void setW(IGWidget i) {
            controlP.add(i.getGWidget());
        }

        private DayLabel getLabel(Widget w) {
            DayLabel hp = (DayLabel) w;
            return hp;
        }

        private void refreshOneColumn(int cNo, Date pe, boolean today) {
            String na = SeasonUtil.getDateName(pe);
            Widget w = g.getWidget(0, cNo);
            DayLabel la = getLabel(w);
            la.lUp.setText(na);
            String week = MM.getWeekdays()[DateUtil.weekDay(pe)];
            la.lDown.setText(week);
            setDayLabel(la, pe, today);
        }

        @Override
        public void refresh(IDrawPartSeasonContext sData) {
            for (int i = sData.getFirstD(); i <= sData.getLastD(); i++) {
                Date pe = sData.getD(i);
                Date today = sData.getTodayC();
                refreshOneColumn(i - sData.getFirstD(), pe,
                        DateUtil.eqDate(pe, today));
            }

        }
    }

    private String getName(final PeriodT pe) {
        String s = SeasonUtil.getName(null);
        return s;
    }

    private class SeasonDayInfo extends Composite {

        private final VerticalPanel vp = new VerticalPanel();

        private SeasonDayInfo(final Date da) {
            PeriodT p = GetPeriods.findPeriod(da, coP);
            assert p != null : M.M().MustFindPeriod();
            String na = getName(p);
            vp.add(new Label(na));
            initWidget(vp);
        }
    }

    public Widget getDInfo(final Date d) {
        return new SeasonDayInfo(d);
    }

    private void drawCa(final PeriodType pType, int actC) {
        dLine = CalendarTable.listOfDates(d1, d2, pType);
        coP = CreateTableSeason.createTable(oP, ConfigParam.getStartWeek());
        sCr.createVPanel(dLine, actC);
    }

    private class DayLabel extends PopupTip {

        private final HTML lUp = new HTML();
        private final Label lDown = new Label();
        private final VerticalPanel hp = new VerticalPanel();

        DayLabel() {
            hp.add(lUp);
            hp.add(lDown);
            initWidget(hp);
        }

    }

    public void drawPa(final OfferSeasonP oP, final PeriodType t) {
        this.oP = oP;
        controlP.clear();
        d1 = oP.getStartP();
        d2 = oP.getEndP();
        g.resizeColumns(NO);
        g.resizeRows(1);
        HTMLTable.RowFormatter fo = g.getRowFormatter();
        fo.setStyleName(0, "day-scroll-panel");
        g.resizeColumns(NO);
        for (int i = 0; i < NO; i++) {
            DayLabel hp = new DayLabel();
            g.setWidget(0, i, hp);
        }
        drawCa(t, NO);
    }

}