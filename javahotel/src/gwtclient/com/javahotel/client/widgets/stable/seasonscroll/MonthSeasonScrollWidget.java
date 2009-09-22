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

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.dialog.IGwtWidget;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.widgets.stable.IDrawPartSeason;
import com.javahotel.client.widgets.stable.IScrollSeason;
import com.javahotel.common.scrollseason.model.MonthSeasonScrollData;
import com.javahotel.common.scrollseason.model.YearMonthPe;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class MonthSeasonScrollWidget implements IGwtWidget {

    private final IResLocator iR;
    private final HorizontalPanel hp;
    private final MonthSeasonScrollData sData;
    private final int todayM;

    MonthSeasonScrollWidget(final IResLocator pLoc,
            final int todayM) {
        this.iR = pLoc;
        sData = new MonthSeasonScrollData();
        this.todayM = todayM;
        hp = new HorizontalPanel();
    }

    private Label getNo(int no) {
        Widget w = hp.getWidget(no);
        return (Label) w;
    }

    private final void drawNames() {
        for (int i = sData.getFirstP(); i <= sData.getLastP(); i++) {
            YearMonthPe pe = sData.getPe(i);
            String na = pe.getYear() + " - " + pe.getMonth();
            if (i == todayM) {
                na = "[ " + na + "]";
            }
            Label la = getNo(i - sData.getFirstP());
            la.setText(na);
        }

    }

    void createVPanel(List<Date> dList, int panelW) {
        sData.createVPanel(dList, panelW);
        hp.setSpacing(5);
        for (int i = 0; i < sData.getMonthPe(); i++) {
            Label la = new Label("");
            hp.add(la);
        }
        drawNames();
    }

    public IMvcWidget getMWidget() {
        return new DefaultMvcWidget(hp);
    }
}
