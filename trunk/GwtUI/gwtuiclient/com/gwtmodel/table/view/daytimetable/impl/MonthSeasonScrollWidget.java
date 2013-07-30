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
package com.gwtmodel.table.view.daytimetable.impl;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.daytimeline.MonthSeasonScrollData;
import com.gwtmodel.table.daytimeline.MoveSkip;
import com.gwtmodel.table.daytimeline.PanelDesc;
import com.gwtmodel.table.daytimeline.YearMonthPe;
import com.gwtmodel.table.injector.MM;
import com.gwtmodel.table.view.util.PopupTip;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class MonthSeasonScrollWidget implements IGWidget {

    private final HorizontalPanel hp;
    private final MonthSeasonScrollData sData;
    private int todayM;
    private final IMonthClicked mClicked;

    interface IMonthClicked {
        void clicked(YearMonthPe m);
    }

    MonthSeasonScrollWidget(IMonthClicked mClicked) {
        sData = new MonthSeasonScrollData();
        hp = new HorizontalPanel();
        this.mClicked = mClicked;
    }

    private DLabel getNo(int no) {
        Widget w = hp.getWidget(no);
        return (DLabel) w;
    }

    boolean moveD(MoveSkip m) {
        return sData.skipMonth(m);
    }

    private final void drawNames() {
        for (int i = sData.getFirstP(); i <= sData.getLastP(); i++) {
            YearMonthPe pe = sData.getPe(i);
            String na = pe.getYear() + " - " + pe.getMonth();
            DLabel la = getNo(i - sData.getFirstP());
            if (i == todayM) {
                la.addStyleName("today");
            } else {
                la.removeStyleName("today");
            }
            la.getLa().setText(na);
        }

    }

    private class MonthEvent implements MouseDownHandler {

        private final int i;

        MonthEvent(int i) {
            this.i = i;
        }

        public void onMouseDown(MouseDownEvent event) {
            YearMonthPe pe = sData.getPe(sData.getFirstP() + i);
            mClicked.clicked(pe);
        }
    }

    private class DLabel extends PopupTip {

        private final Label la = new Label("");

        DLabel(int i) {
            initWidget(la);
            setMessage(MM.getL().GotoMonth());
            la.addMouseDownHandler(new MonthEvent(i));
        }

        /**
         * @return the la
         */
        public Label getLa() {
            return la;
        }

    }

    void createVPanel(List<Date> dList, int panelW, int todayM) {
        this.todayM = todayM;
        hp.clear();
        sData.createVPanel(dList, panelW);
        hp.setSpacing(5);
        hp.setStyleName("month-scroll-panel");
        for (int i = 0; i < sData.getMonthPe(); i++) {
            hp.add(new DLabel(i));
        }
        drawNames();
    }

    public PanelDesc getPanelDesc() {
        return sData.getMonthScrollStatus();
    }

    void refresh() {
        drawNames();
    }

    @Override
    public Widget getGWidget() {
        return hp;
    }

}
