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
import com.javahotel.common.scrollseason.model.DaySeasonScrollData;
import com.javahotel.common.scrollseason.model.MoveSkip;
import com.javahotel.common.scrollseason.model.PanelDesc;
import java.util.Date;
import java.util.List;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class DayLineWidget implements IGwtWidget {

    private final IResLocator iR;
    private final HorizontalPanel hp;
    private final DaySeasonScrollData sData;

    DayLineWidget(final IResLocator pLoc, final int todayC) {
        this.iR = pLoc;
        sData = new DaySeasonScrollData(todayC);
        hp = new HorizontalPanel();
    }

    private Label getNo(int no) {
        Widget w = hp.getWidget(no);
        return (Label) w;
    }

    private final void drawNames() {
        for (int i = sData.getFirstD(); i <= sData.getLastD(); i++) {
            Date pe = sData.getD(i);
            String na = (pe.getMonth() + 1) + "/" + pe.getDate();
            Label la = getNo(i - sData.getFirstD());
            if (i == sData.getTodayC()) {
                na = "[" + na + "]";
            }
            la.setText(na);
        }
    }

    void refresh() {
        drawNames();
    }

    void createVPanel(List<Date> dList, int panelW) {
        sData.createSPanel(dList, panelW);
        hp.setSpacing(5);
        for (int i = 0; i < sData.getPeriodNo(); i++) {
            Label la = new Label("");
            hp.add(la);
        }
        drawNames();
    }

    public IMvcWidget getMWidget() {
        return new DefaultMvcWidget(hp);
    }

    boolean moveD(MoveSkip m, int noD) {
        return sData.moveD(m, noD);
    }

    void gotoD(Date d) {
        sData.gotoD(d);
    }

    
    public PanelDesc getPanelDesc() {
        return sData.getDayScrollStatus();
    }
    
    int getTodayM() {
        return sData.getTodayM();
    }


}
