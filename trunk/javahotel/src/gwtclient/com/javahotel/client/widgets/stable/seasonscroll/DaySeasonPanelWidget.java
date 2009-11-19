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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.htmlview.IHtmlPanelCallBack;
import com.javahotel.client.widgets.stable.IDrawPartSeason;
import com.javahotel.client.widgets.stable.IScrollSeason;
import com.javahotel.common.command.SynchronizeList;
import com.javahotel.common.scrollseason.model.MoveSkip;
import com.javahotel.common.scrollseason.model.YearMonthPe;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class DaySeasonPanelWidget implements IScrollSeason {

    private final DayLineWidget dW;
    private final VerticalPanel vp = new VerticalPanel();
    private final IDrawPartSeason dPart;
    private ScrollArrowWidget scrollDayW;
    private ScrollArrowWidget scrollMonthW;
    private final MonthSeasonScrollWidget mW;
    private final IResLocator pLoc;

    private void setWidget(List<Date> dList, int panelW, Panel dS, Panel mS) {
        HorizontalPanel hp = new HorizontalPanel();
        dW.createVPanel(dList, panelW);
        hp.add(dS);
        mW.createVPanel(dList, 6, dW.getTodayM());
        hp.add(mW.getMWidget().getWidget());
        hp.add(mS);
        vp.add(hp);

        vp.add(dW.getMWidget().getWidget());
        scrollDayW.setState(dW.getPanelDesc());
        dPart.setGwtWidget(new DefaultMvcWidget(vp));
    }

    private class CallWidget extends SynchronizeList {

        Panel dS, mS;
        private final List<Date> dList;
        private final int panelW;

        CallWidget(List<Date> dList, int panelW) {
            super(2);
            this.dList = dList;
            this.panelW = panelW;
        }

        @Override
        protected void doTask() {
            setWidget(dList, panelW, dS, mS);
        }

    }

    private void refresh() {
        scrollDayW.setState(dW.getPanelDesc());
        dW.refresh();
    }

    private void refreshM() {
        scrollMonthW.setState(mW.getPanelDesc());
        mW.refresh();
    }

    private class ScrollCli implements ScrollArrowWidget.IsignalP {

        public void clicked(MoveSkip a) {
            if (!dW.moveD(a, 1)) {
                return;
            }
            refresh();
        }

        public void clicked(Date d) {
            dW.gotoD(d);
            refresh();
        }
    }

    private class ScrollCliM implements ScrollArrowWidget.IsignalP {

        public void clicked(MoveSkip a) {
            if (!mW.moveD(a)) {
                return;
            }
            refreshM();
        }

        // not called
        public void clicked(Date d) {
        }
    }

    private class MonthClicked implements MonthSeasonScrollWidget.IMonthClicked {

        public void clicked(YearMonthPe m) {
            dW.gotoMonth(m);
            refresh();
        }

    }

    DaySeasonPanelWidget(final IResLocator pLoc, IDrawPartSeason dPart, Grid g,
            int startG, final Date todayC) {
        dW = new DayLineWidget(pLoc, dPart, g, startG, todayC);
        mW = new MonthSeasonScrollWidget(pLoc, new MonthClicked());
        this.dPart = dPart;
        this.pLoc = pLoc;
    }

    public int getStartNo() {
        return dW.getStartNo();
    }

    public void createVPanel(List<Date> dList, int panelW) {

        vp.clear();

        final CallWidget ca = new CallWidget(dList, panelW);

        IHtmlPanelCallBack mBack = new IHtmlPanelCallBack() {

            public void setHtmlPanel(Panel ha) {
                ca.mS = ha;
                ca.signalDone();
            }
        };

        IHtmlPanelCallBack dBack = new IHtmlPanelCallBack() {

            public void setHtmlPanel(Panel ha) {
                ca.dS = ha;
                ca.signalDone();
            }
        };

        scrollDayW = new ScrollArrowWidget(pLoc, new ScrollCli(), true, dBack);
        scrollMonthW = new ScrollArrowWidget(pLoc, new ScrollCliM(), false,
                mBack);

    }
}
