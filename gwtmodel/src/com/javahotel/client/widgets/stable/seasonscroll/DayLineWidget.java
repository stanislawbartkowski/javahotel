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
import com.google.gwt.user.client.ui.Widget;
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
    private final DaySeasonScrollData sData;
    private final IDrawPartSeason dPart;
    private final Grid g;
    private final int startG;

    DayLineWidget(final IResLocator pLoc, IDrawPartSeason dPart, final Grid g,
            int startG, final Date todayC) {
        this.iR = pLoc;
        this.g = g;
        this.startG = startG;
        sData = new DaySeasonScrollData(todayC);
        this.dPart = dPart;
    }

    private Widget getNo(int no) {
        Widget l = g.getWidget(0, startG + no);
        return l;
    }
    
    int getStartNo() {
        return sData.getFirstD();
    }

    private void setC(int c, Widget w) {
        g.setWidget(0, startG + c, w);
    }

    private final void drawNames() {
        for (int i = sData.getFirstD(); i <= sData.getLastD(); i++) {
            Date pe = sData.getD(i);
            String na = (pe.getMonth() + 1) + "/" + pe.getDate();
            Widget w = getNo(i - sData.getFirstD());
            Label la = dPart.getLabel(w);
            Date today = sData.getTodayC();
            if (DateUtil.eqDate(pe, today)) {
                la.setStyleName("today");
            } else {
                la.removeStyleName("today");
            }
            la.setText(na);
            dPart.setColumn(w, i, pe, la);
        }
        if (dPart != null) {
            dPart.draw(sData.getFirstD(), sData.getLastD());
        }
    }

    void refresh() {
        drawNames();
    }

    void createVPanel(List<Date> dList, int panelW) {
        HTMLTable.RowFormatter fo = g.getRowFormatter();
        fo.setStyleName(0, "day-scroll-panel");
        g.resizeColumns(startG + panelW);
        sData.createSPanel(dList, panelW);
        for (int i = 0; i < sData.getPeriodNo(); i++) {
            Label la = new Label("");
            Widget w = dPart.getColumnEmpty(la);
            setC(i, w);
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
