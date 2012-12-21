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

import com.gwtmodel.table.daytimeline.DaySeasonScrollData;
import com.gwtmodel.table.daytimeline.MoveSkip;
import com.gwtmodel.table.daytimeline.PanelDesc;
import com.gwtmodel.table.daytimeline.YearMonthPe;
import com.gwtmodel.table.view.daytimetable.IDrawPartSeason;
import com.gwtmodel.table.view.daytimetable.IDrawPartSeasonContext;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class DayLineWidget {

    private final DaySeasonScrollData sData;
    private final IDrawPartSeason dPart;
    private final IDrawPartSeasonContext iContext = new DrawContext();

    private class DrawContext implements IDrawPartSeasonContext {

        @Override
        public Date getD(int c) {
            return sData.getD(c);
        }

        @Override
        public int getFirstD() {
            return sData.getFirstD();
        }

        @Override
        public int getLastD() {
            return sData.getLastD();
        }

        @Override
        public Date getTodayC() {
            return sData.getTodayC();
        }

    }

    DayLineWidget(IDrawPartSeason dPart, final Date todayC) {
        sData = new DaySeasonScrollData(todayC);
        this.dPart = dPart;
    }

    int getStartNo() {
        return sData.getFirstD();
    }

    private void drawNames() {
        dPart.refresh(iContext);
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
