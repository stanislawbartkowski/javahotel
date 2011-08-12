/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.view.util.ClickPopUp;
import com.javahotel.client.ConfigParam;
import com.javahotel.client.IResLocator;
import com.javahotel.client.user.season.SeasonUtil;
import com.javahotel.client.user.widgets.stable.IDrawPartSeason;
import com.javahotel.client.user.widgets.stable.IScrollSeason;
import com.javahotel.client.user.widgets.stable.impl.WidgetScrollSeasonFactory;
import com.javahotel.common.dateutil.CalendarTable;
import com.javahotel.common.dateutil.CalendarTable.PeriodType;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.dateutil.DateUtil;
import com.javahotel.common.dateutil.GetPeriods;
import com.javahotel.common.dateutil.PeriodT;
import com.javahotel.common.scrollseason.model.DaySeasonScrollData;
import com.javahotel.common.seasonutil.CreateTableSeason;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.OfferSeasonPeriodP;
import com.javahotel.common.toobject.SeasonPeriodT;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("deprecation")
class PanelSeason {

    private final ComplexPanel controlP;
    private final IResLocator rI;
    private final IScrollSeason sCr;
    private OfferSeasonP oP;
    private Date d1;
    private Date d2;
    private List<Date> dLine;
    private List<PeriodT> coP;
    private static final int NO = 12;
    private final Grid g;

    PanelSeason(final IResLocator rI, Grid g, final ComplexPanel controlP,
            final Date today) {
        this.rI = rI;
        this.g = g;
        this.controlP = controlP;
        sCr = WidgetScrollSeasonFactory.getScrollSeason(new DrawC(),
                DateUtil.getToday());

    }

    private void setDayLabel(Label l, Date da) {
        PeriodT p = GetPeriods.findPeriod(da, coP);
        l.addClickListener(new IClick(p));
        String sTyle = SeasonUtil.getStyleForDay(p);
        l.setStyleName(sTyle);
    }

    private class DrawC implements IDrawPartSeason {

        @Override
        public void setW(IGWidget i) {
            controlP.add(i.getGWidget());
        }

        private Label getLabel(Widget w, int pa) {
            VerticalPanel hp = (VerticalPanel) w;
            Label la = (Label) hp.getWidget(pa);
            return la;
        }

        private void refreshOneColumn(int cNo, Date pe, boolean today) {
            String na = SeasonUtil.getDateName(pe);
            Widget w = g.getWidget(0, cNo);
            Label la = getLabel(w, 0);
            if (today) {
                la.setStyleName(SeasonUtil.getTodayStyle());
            } else {
                la.removeStyleName(SeasonUtil.getTodayStyle());
            }
            la.setText(na);
            la = getLabel(w, 1);
            setDayLabel(la, pe);
        }

        @Override
        public void refresh(DaySeasonScrollData sData) {
            for (int i = sData.getFirstD(); i <= sData.getLastD(); i++) {
                Date pe = sData.getD(i);
                Date today = sData.getTodayC();
                refreshOneColumn(i - sData.getFirstD(), pe,
                        DateUtil.eqDate(pe, today));
            }

        }
    }

    private String getName(final PeriodT pe) {
        // Object o = pe.getI();
        // OfferSeasonPeriodP pp = (OfferSeasonPeriodP) o;
        // SeasonPeriodT t = null;
        // if (pp != null) {
        // t = pp.getPeriodT();
        // }
        String s = SeasonUtil.getName(pe);
        return s;
    }

    private class SeasonDayInfo extends Composite {

        private final VerticalPanel vp = new VerticalPanel();

        private SeasonDayInfo(final Date da) {
            PeriodT p = GetPeriods.findPeriod(da, coP);
            assert p != null : "Must find period";
            String na = getName(p);
            vp.add(new Label(na));
            initWidget(vp);
        }
    }

    public Widget getDInfo(final Date d) {
        return new SeasonDayInfo(d);
    }

    private class IClick implements ClickListener {

        private final PeriodT pe;

        IClick(PeriodT pe) {
            this.pe = pe;
        }

        public void onClick(final Widget arg0) {
            // VerticalPanel h = new VerticalPanel();
            // String s = getName(pe);
            // do not remove
            Widget w = SeasonUtil.createPeriodPopUp(pe);
            new ClickPopUp(arg0, w);
            // h.add(new Label(s));
            // String s1 = DateFormatUtil.toS(pe.getFrom());
            // String s2 = DateFormatUtil.toS(pe.getTo());
            // h.add(new Label(s1 + " - " + s2));
            // int noD = DateUtil.noLodgings(pe.getFrom(), pe.getTo());
            // h.add(new Label(rI.getMessages().noSleeps(noD)));
        }
    }

    private void drawCa(final PeriodType pType, int actC) {
        dLine = CalendarTable.listOfDates(d1, d2, pType);
        coP = CreateTableSeason.createTable(oP, ConfigParam.getStartWeek());
        sCr.createVPanel(dLine, actC);
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
            VerticalPanel hp = new VerticalPanel();
            hp.add(new Label());
            hp.add(new HTML("&nbsp;"));
            g.setWidget(0, i, hp);
        }
        drawCa(t, NO);
    }

}
