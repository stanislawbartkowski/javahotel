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
package com.javahotel.client.dialog.user.tableseason;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.dialog.WidgetSizeFactory;
import com.javahotel.client.param.ConfigParam;
import com.javahotel.client.widgets.popup.PopUpWithClose;
import com.javahotel.client.widgets.stable.IDrawPartSeason;
import com.javahotel.client.widgets.stable.IScrollSeason;
import com.javahotel.client.widgets.stable.seasonscroll.WidgetScrollSeasonFactory;
import com.javahotel.common.dateutil.CalendarTable;
import com.javahotel.common.dateutil.DateFormatUtil;
import com.javahotel.common.dateutil.DateUtil;
import com.javahotel.common.dateutil.GetPeriods;
import com.javahotel.common.dateutil.PeriodT;
import com.javahotel.common.dateutil.CalendarTable.PeriodType;
import com.javahotel.common.seasonutil.CreateTableSeason;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.toobject.OfferSeasonPeriodP;
import com.javahotel.common.toobject.SeasonPeriodT;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
@SuppressWarnings("deprecation")
public class PanelSeason {

    private final ComplexPanel controlP;
    private final IResLocator rI;
    private final IScrollSeason sCr;
    private OfferSeasonP oP;
    private Date d1;
    private Date d2;
    private List<Date> dLine;
    private List<PeriodT> coP;
    private final Grid g;
    private final int startC;
    private final IDrawPartSeason drawI;

    public PanelSeason(final IResLocator rI, final Grid g,
            final ComplexPanel controlP, final int startC,
            final IDrawPartSeason drawI, final Date today) {
        this.rI = rI;
        this.g = g;
        this.startC = startC;
        this.controlP = controlP;
        this.drawI = drawI;
        sCr = WidgetScrollSeasonFactory.getScrollSeason(rI, new DrawC(), g,
                startC, DateUtil.getToday());
    }
    
    private void setDayLabel(Label l, int d) {
        Date da = dLine.get(d);
        PeriodT p = GetPeriods.findPeriod(da, coP);
        // PeriodT pType = coP.get(d);
        OfferSeasonPeriodP pp = (OfferSeasonPeriodP) p.getI();
        l.addClickListener(new IClick(p));
        String sTyle = "day_high_season";
        if (pp != null) {
            switch (pp.getPeriodT()) {
            case LOW:
                sTyle = "day_low_season";
                break;
            case SPECIAL:
                sTyle = "day_special_season";
                break;
            case LOWWEEKEND:
                sTyle = "day_lowweekend_season";
                break;
            case HIGHWEEKEND:
                sTyle = "day_highweekend_season";
                break;
            default:
                break;
            }
        }
        l.setStyleName(sTyle);
    }

    private class DrawC implements IDrawPartSeason {

        public void draw(final int sno, final int sto) {
            drawD(sno, sto);
            if (drawI != null) {
                drawI.draw(sno, sto);
            }
        }

        public void setGwtWidget(IMvcWidget i) {
//            int no = controlP.getWidgetCount();
            controlP.add(i.getWidget());
//            if (drawI != null) {
//                drawI.setGwtWidget(new DefaultMvcWidget(controlP));
//            }
        }

        public Widget getColumnEmpty(Label l) {
            VerticalPanel hp = new VerticalPanel();
            hp.add(l);
            Label la = new Label(".");
            hp.add(la);
            return hp;
        }

        public Label getLabel(Widget w) {
            VerticalPanel hp = (VerticalPanel) w;
            Label la = (Label) hp.getWidget(0);
            return la;
                
        }

        public void setColumn(Widget w, int c, Date d, Label l) {
            VerticalPanel hp = (VerticalPanel) w;
            Label la = (Label) hp.getWidget(1);
            setDayLabel(la,c);            
        }
    }

    private String getName(final PeriodT pe) {
        Object o = pe.getI();
        OfferSeasonPeriodP pp = (OfferSeasonPeriodP) o;
        SeasonPeriodT t = null;
        if (pp != null) {
            t = pp.getPeriodT();
        }
        String s = SeasonNames.getName(rI, t);
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
            PopUpWithClose aPanel = new PopUpWithClose(WidgetSizeFactory
                    .getW(arg0));
            VerticalPanel h = aPanel.getVP();
            String s = getName(pe);
            h.add(new Label(s));
            String s1 = DateFormatUtil.toS(pe.getFrom());
            String s2 = DateFormatUtil.toS(pe.getTo());
            h.add(new Label(s1 + " - " + s2));
            int noD = DateUtil.noLodgings(pe.getFrom(), pe.getTo());
            h.add(new Label(rI.getMessages().noSleeps(noD)));
        }
    }


    private void drawD(final int startno, final int endno) {

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
        if (d1 != null) {
            drawCa(t, 12);
        }
    }

    public List<Date> getDLine() {
        return dLine;
    }

    public int getStartNo() {
        return sCr.getStartNo();
    }

    public int getColNumber() {
        return g.getColumnCount() - startC;
    }
}
