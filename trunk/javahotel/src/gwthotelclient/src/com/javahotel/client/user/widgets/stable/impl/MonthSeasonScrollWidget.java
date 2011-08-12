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
package com.javahotel.client.user.widgets.stable.impl;

import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IGWidget;
import com.javahotel.client.IResLocator;
import com.javahotel.common.scrollseason.model.MonthSeasonScrollData;
import com.javahotel.common.scrollseason.model.MoveSkip;
import com.javahotel.common.scrollseason.model.PanelDesc;
import com.javahotel.common.scrollseason.model.YearMonthPe;

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

    private Label getNo(int no) {
        Widget w = hp.getWidget(no);
        return (Label) w;
    }

    boolean moveD(MoveSkip m) {
        return sData.skipMonth(m);
    }

    private final void drawNames() {
        for (int i = sData.getFirstP(); i <= sData.getLastP(); i++) {
            YearMonthPe pe = sData.getPe(i);
            String na = pe.getYear() + " - " + pe.getMonth();
            // if (i == todayM) {
            // na = "[ " + na + "]";
            // }
            Label la = getNo(i - sData.getFirstP());
            if (i == todayM) {
                la.addStyleName("today");
            } else {
                la.removeStyleName("today");
            }
            la.setText(na);
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

    void createVPanel(List<Date> dList, int panelW, int todayM) {
        this.todayM = todayM;
        hp.clear();
        sData.createVPanel(dList, panelW);
        hp.setSpacing(5);
        hp.setStyleName("month-scroll-panel");
        for (int i = 0; i < sData.getMonthPe(); i++) {
            Label la = new Label("");
            la.setTitle("aaaaa");
            la.addMouseDownHandler(new MonthEvent(i));
            hp.add(la);
        }
        drawNames();
    }

    public PanelDesc getPanelDesc() {
        return sData.getMonthScrollStatus();
    }

    void refresh() {
        drawNames();
    }

    /* (non-Javadoc)
     * @see com.gwtmodel.table.IGWidget#getGWidget()
     */
    @Override
    public Widget getGWidget() {
        return hp;
    }

}
