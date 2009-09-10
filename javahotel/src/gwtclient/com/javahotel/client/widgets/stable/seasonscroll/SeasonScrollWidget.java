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
import com.javahotel.client.widgets.stable.IDrawPartSeason;
import com.javahotel.client.widgets.stable.IScrollSeason;
import com.javahotel.common.util.MaxI;
import java.util.Date;
import java.util.List;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class SeasonScrollWidget implements IScrollSeason {

    private final IResLocator iR;
    private final IDrawPartSeason dPart;
    private final int periodNo;
    private List<Date> dList;
    private final int maxmonthPe = 6;
    private int monthPe;
    private List<YearMonthPe> dMonth;
    private int firstP, lastP;
    private HorizontalPanel hp;

    SeasonScrollWidget(final IResLocator pLoc, final IDrawPartSeason i, final int pNo) {
        this.iR = pLoc;
        this.dPart = i;
        this.periodNo = pNo;
    }

    public int getStartNo() {
        return -1;
    }

    public void createVPanel(int no, int actC) {
    }

    private Label getNo(int no) {
        Widget w = hp.getWidget(no);
        return (Label) w;
    }

    private final void drawNames() {
        for (int i = firstP; i <= lastP; i++) {
            YearMonthPe pe = dMonth.get(i);
            String na = pe.getYear() + " - " + pe.getMonth();
            Label la = getNo(i - firstP);
            la.setText(na);
        }

    }

    public void createVPanel(List<Date> dList, int actC) {
        this.dList = dList;
        dMonth = CreateMonthPeList.createLi(dList);
        monthPe = MaxI.min(maxmonthPe, dMonth.size());
        hp = new HorizontalPanel();
        hp.setSpacing(5);
        for (int i = 0; i < monthPe; i++) {
            Label la = new Label("");
            hp.add(la);
        }
        firstP = 0;
        lastP = monthPe-1;
        drawNames();
        dPart.setSWidget(hp);
    }
}
