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
package com.javahotel.client.user.widgets.stable.impl;

import java.util.Date;
import java.util.List;

import com.javahotel.client.user.widgets.stable.IDrawPartSeason;
import com.javahotel.common.dateutil.DateUtil;
import com.javahotel.common.scrollseason.model.DaySeasonScrollData;
import com.javahotel.common.scrollseason.model.MoveSkip;
import com.javahotel.common.scrollseason.model.PanelDesc;
import com.javahotel.common.scrollseason.model.YearMonthPe;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class DayLineWidget {

    private final DaySeasonScrollData sData;
    private final IDrawPartSeason dPart;

    DayLineWidget(IDrawPartSeason dPart,
            final Date todayC) {
        sData = new DaySeasonScrollData(todayC);
        this.dPart = dPart;
    }

    int getStartNo() {
        return sData.getFirstD();
    }

    private final void drawNames() {
        dPart.refresh(sData);
    }

    void refresh() {
        drawNames();
    }

    void createVPanel(List<Date> dList, int panelW) {
        sData.createSPanel(dList, panelW);
        drawNames();
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

    PanelDesc getPanelDesc() {
        return sData.getDayScrollStatus();
    }

    int getTodayM() {
        return sData.getTodayM();
    }

}
