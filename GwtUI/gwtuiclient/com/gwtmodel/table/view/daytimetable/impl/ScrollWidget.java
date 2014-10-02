/*
 * Copyright 2014 stanislawbartkowski@gmail.com 
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

import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.gwtmodel.table.DateUtil;
import com.gwtmodel.table.Empty;
import com.gwtmodel.table.GFocusWidgetFactory;
import com.gwtmodel.table.GWidget;
import com.gwtmodel.table.IConsts;
import com.gwtmodel.table.IGFocusWidget;
import com.gwtmodel.table.common.dateutil.DateFormatUtil;
import com.gwtmodel.table.factories.IWebPanelResources;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.injector.MM;
import com.gwtmodel.table.rdef.IFormChangeListener;
import com.gwtmodel.table.rdef.IFormLineView;
import com.gwtmodel.table.view.button.ImgButtonFactory;
import com.gwtmodel.table.view.daytimetable.IDrawPartSeason;
import com.gwtmodel.table.view.daytimetable.IDrawPartSeasonContext;
import com.gwtmodel.table.view.daytimetable.IScrollSeason;
import com.gwtmodel.table.view.ewidget.EditWidgetFactory;

public class ScrollWidget implements IScrollSeason {

    private final IDrawPartSeason dPart;
    private final Date todayC;
    private DatePanelUtil.PanelDesc panelMonth;
    private DatePanelUtil.PanelDesc panelDay;

    private final IGFocusWidget leftP;
    private final IGFocusWidget rightP;
    private final IFormLineView dDate;

    private final static String C_BUTTON_LEFT = "BUTTON-LEFT";
    private final static String C_BUTTON_RIGHT = "BUTTON-LRIGHT";

    private final HorizontalPanel vPanel = new HorizontalPanel();
    private final HorizontalPanel mPanel = new HorizontalPanel();
    private final HorizontalPanel dPanel = new HorizontalPanel();
    private final ListBox cyList = new ListBox();
    private int curPos = -1;

    private Date firstDay, lastDay;

    ScrollWidget(IDrawPartSeason dPart, final Date todayC) {
        this.dPart = dPart;
        this.todayC = todayC;

        IWebPanelResources pResources = GwtGiniInjector.getI()
                .getWebPanelResources();
        String[] titles = MM.getL().ScrollDays();
        leftP = ImgButtonFactory.getButton(C_BUTTON_LEFT, titles[1],
                pResources.getRes(IWebPanelResources.SCROLLLEFT));
        rightP = ImgButtonFactory.getButton(C_BUTTON_RIGHT, titles[2],
                pResources.getRes(IWebPanelResources.SCROLLRIGHT));
        EditWidgetFactory eFactory = GwtGiniInjector.getI()
                .getEditWidgetFactory();
        dDate = eFactory.construcDateBoxCalendar(Empty.getFieldType(), null);
        dPanel.add(leftP.getGWidget());
        dPanel.add(dDate.getGWidget());
        dPanel.add(rightP.getGWidget());
    }

    private class DrawContext implements IDrawPartSeasonContext {

        @Override
        public Date getD(int c) {
            return DatePanelUtil.getPanelDay(panelDay, c);
        }

        @Override
        public int getFirstD() {
            return 0;
        }

        @Override
        public int getLastD() {
            return panelDay.pSize - 1;
        }

    }

    private class ClickArrow implements ClickHandler {

        private final boolean forward;

        ClickArrow(boolean forward) {
            this.forward = forward;
        }

        @Override
        public void onClick(ClickEvent event) {
            Date newD;
            if (forward)
                newD = DateUtil.NextDayD(panelDay.getCurDay());
            else
                newD = DateUtil.PrevDayD(panelDay.getCurDay());
            redraw(newD);
        }

    }

    private class MonthButton implements ClickHandler {

        private final int no;

        MonthButton(int no) {
            this.no = no;
        }

        @Override
        public void onClick(ClickEvent event) {
            Date md = panelMonth.getMonthI(no);
            int m = DateFormatUtil.getM(md);
            int y = DateFormatUtil.getY(md);
            int d = DateFormatUtil.getD(panelDay.getCurDay());
            Date newCur = constructValidDate(y, m, d);
            redraw(newCur);
        }
    }

    private void setCurrentDate() {
        dDate.setValObj(DatePanelUtil.getPanelDay(panelDay, panelDay.curD));
    }

    private void setYear() {
        int curY = DateFormatUtil.getY(panelDay.getCurDay());
        int firstY = Integer.parseInt(cyList.getValue(0));
        cyList.setItemSelected(curY - firstY, true);
    }

    private String getMonthName(int i) {
        Date d = DatePanelUtil.getPanelMonth(panelMonth, i);
        return DateUtil.getMonths()[DateFormatUtil.getM(d) - 1];
    }

    @Override
    public void redraw(Date dCur) {
        // Warning: curPos or -1
        panelDay = DatePanelUtil.createLDays(firstDay, lastDay, dCur,
                panelDay.pSize, -1);
        setCurrentDate();
        dPart.refresh(new DrawContext());
        setYear();
        // check if month in the month panel
        for (int i = 0; i < panelMonth.pSize; i++)
            if (DatePanelUtil.eqMonth(panelMonth.getMonthI(i),
                    panelDay.getCurDay()))
                return;
        panelMonth = DatePanelUtil.createLMonth(firstDay, lastDay,
                panelDay.getCurDay(), panelMonth.pSize);
        for (int i = 0; i < mPanel.getWidgetCount(); i++) {
            Button b = (Button) mPanel.getWidget(i);
            b.setText(getMonthName(i));
        }
    }

    private class DateChange implements IFormChangeListener {

        @Override
        public void onChange(IFormLineView i, boolean afterFocus) {
            if (!afterFocus)
                return;
            Date d = (Date) i.getValObj();
            if (d == null)
                return;
            Date currDay = panelDay.getCurDay();
            if (DateUtil.eqDate(d, currDay))
                return;
            redraw(d);
        }

    }

    private Date constructValidDate(int y, int m, int d) {
        if (DateUtil.isOkDate(y, m, d))
            return DateFormatUtil.toD(y, m, d);
        return DateUtil.toLastMonthDay(y, m);
    }

    private class YearChange implements ChangeHandler {

        @Override
        public void onChange(ChangeEvent event) {
            int selectedY = Integer.parseInt(cyList.getValue(cyList
                    .getSelectedIndex()));
            int curY = DateFormatUtil.getY(panelDay.getCurDay());
            if (curY == selectedY)
                return;
            int montY = DateFormatUtil.getM(panelDay.getCurDay());
            int dayY = DateFormatUtil.getD(panelDay.getCurDay());
            Date newCur = constructValidDate(selectedY, montY, dayY);
            redraw(newCur);
        }

    }

    @Override
    public void createVPanel(Date firstData, Date lastDate, int panelW,
            int curPos) {
        this.firstDay = firstData;
        this.lastDay = lastDate;
        this.curPos = curPos;
        panelMonth = DatePanelUtil.createLMonth(firstData, lastDate, todayC,
                IConsts.MonthPanel);
        for (int i = 0; i < panelMonth.pSize; i++) {
            Button l = new Button(getMonthName(i), new MonthButton(i));
            l.setStyleName("month-style");
            IGFocusWidget w = GFocusWidgetFactory.construct(l, MM.getL()
                    .GotoMonth());
            mPanel.add(w.getGWidget());
        }

        List<Integer> yList = DatePanelUtil.getListOfYears(firstData, lastDate);
        for (Integer y : yList)
            cyList.addItem(y.toString());
        cyList.addChangeHandler(new YearChange());
        dDate.addChangeListener(new DateChange());
        leftP.addClickHandler(new ClickArrow(false));
        rightP.addClickHandler(new ClickArrow(true));

        vPanel.add(cyList);
        vPanel.add(dPanel);
        vPanel.add(mPanel);

        panelDay = DatePanelUtil.createLDays(firstData, lastDate, todayC,
                panelW, curPos);
        setYear();

        dPart.setW(new GWidget(vPanel));
        setCurrentDate();
        dPart.refresh(new DrawContext());
    }
}
