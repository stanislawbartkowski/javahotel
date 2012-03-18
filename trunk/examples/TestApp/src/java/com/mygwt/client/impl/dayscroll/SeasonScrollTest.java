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
package com.mygwt.client.impl.dayscroll;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.common.dateutil.DateFormatUtil;
import com.gwtmodel.table.common.dateutil.DateUtil;
import com.gwtmodel.table.daytimeline.CalendarTable;
import com.gwtmodel.table.daytimeline.CalendarTable.PeriodType;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.view.daytimetable.IDrawPartSeason;
import com.gwtmodel.table.view.daytimetable.IDrawPartSeasonContext;
import com.gwtmodel.table.view.daytimetable.IScrollSeason;
import com.gwtmodel.table.view.daytimetable.impl.WidgetScrollSeasonFactory;
import com.mygwt.client.testEntryPoint;

/**
 * @author hotel
 * 
 */
public class SeasonScrollTest implements testEntryPoint.IGetWidget {

    private final WidgetScrollSeasonFactory wFactory;
    private final VerticalPanel hP = new VerticalPanel();
    private final HorizontalPanel vP = new HorizontalPanel();

    private class DrawClass implements IDrawPartSeason {

        @Override
        public void setW(IGWidget w) {
            hP.add(w.getGWidget());
            hP.add(vP);
            vP.setBorderWidth(1);
            vP.setSpacing(15);
        }

        @Override
        public void refresh(IDrawPartSeasonContext sData) {
            int no = sData.getLastD() - sData.getFirstD() + 1;
            if (vP.getWidgetCount() != no) {
                vP.clear();
                for (int i = 0; i < no; i++) {
                    vP.add(new Label(""));
                }
            }
            for (int i = 0; i < no; i++) {
                Date d = sData.getD(sData.getFirstD() + i);
                String s = DateFormatUtil.toS(d);
                Label l = (Label) vP.getWidget(i);
                l.setText(s);
            }

        }

    }

    public SeasonScrollTest() {
        wFactory = GwtGiniInjector.getI().getWidgetScrollSeasonFactory();
    }

    @Override
    public Widget getW() {
        IScrollSeason i = wFactory.getScrollSeason(new DrawClass(),
                DateUtil.getToday());
        Date d1 = new Date(112, 00, 01);
        Date d2 = new Date(112, 11, 31);
        List<Date> li = CalendarTable.listOfDates(d1, d2, PeriodType.byDay);
        i.createVPanel(li, 10);
        return hP;
    }

}
