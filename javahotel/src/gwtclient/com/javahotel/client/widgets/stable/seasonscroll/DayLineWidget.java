/*
 * Copyright 2009 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.widgets.stable.seasonscroll;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Label;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.dialog.IGwtWidget;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.widgets.stable.IDrawPartSeason;
import com.javahotel.common.dateutil.DateUtil;
import com.javahotel.common.scrollseason.model.DaySeasonScrollData;
import com.javahotel.common.scrollseason.model.MoveSkip;
import com.javahotel.common.scrollseason.model.PanelDesc;
import com.javahotel.common.scrollseason.model.YearMonthPe;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class DayLineWidget implements IGwtWidget {

    private final IResLocator iR;
//    private final HorizontalPanel hp;
    private final DaySeasonScrollData sData;
    private final IDrawPartSeason dPart;
    private final Grid g;
    private final int startG;

    DayLineWidget(final IResLocator pLoc, IDrawPartSeason dPart, final Grid g, int startG, 
            final Date todayC) {
        this.iR = pLoc;
        this.g = g;
        this.startG = startG;
        sData = new DaySeasonScrollData(todayC);
//        hp = new HorizontalPanel();
//        hp.setStyleName("day-scroll-panel");
        this.dPart = dPart;
    }

    private Label getNo(int no) {
        Label l = (Label) g.getWidget(0,startG+no);
//        Widget w = hp.getWidget(no);
        return l;
    }
    
    private void setC(int c, Label l) {
        g.setWidget(0,startG+c,l);
    }

    private final void drawNames() {
        for (int i = sData.getFirstD(); i <= sData.getLastD(); i++) {
            Date pe = sData.getD(i);
            String na = (pe.getMonth() + 1) + "/" + pe.getDate();
            Label la = getNo(i - sData.getFirstD());
            Date today = sData.getTodayC();
            if (DateUtil.eqDate(pe, today)) {
                // na = "[" + na + "]";
                la.setStyleName("today");
            } else {
                la.removeStyleName("today");
            }
            la.setText(na);
        }
        if (dPart != null) {
            dPart.draw(sData.getFirstD(),sData.getLastD());
        }
    }

    void refresh() {
        drawNames();
    }
    

    void createVPanel(List<Date> dList, int panelW) {
//        hp.clear();
        HTMLTable.RowFormatter fo = g.getRowFormatter();
        fo.setStyleName(0,"day-scroll-panel");
        g.resizeColumns(startG + panelW); 
        sData.createSPanel(dList, panelW);
//        hp.setSpacing(2);
        for (int i = 0; i < sData.getPeriodNo(); i++) {
            Label la = new Label("");
            setC(i,la);
            
//            hp.add(la);
        }
        drawNames();
    }

    public IMvcWidget getMWidget() {
        return new DefaultMvcWidget(g);
    }

    boolean moveD(MoveSkip m, int noD) {
        return sData.moveD(m, noD);
    }

    void gotoD(Date d) {
        sData.gotoD(d);
    }

    void gotoMonth(YearMonthPe m) {
        sData.gotoMonth(m);
    }

    public PanelDesc getPanelDesc() {
        return sData.getDayScrollStatus();
    }

    int getTodayM() {
        return sData.getTodayM();
    }

}
