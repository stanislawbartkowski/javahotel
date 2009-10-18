/*
 * Copyright 2008 stanislawbartkowski@gmail.com 
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
package com.javahotel.client.mvc.dictcrud.controler.season;

import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.mvc.dictcrud.controler.*;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.javahotel.client.IResLocator;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.dialog.IMvcView;
import com.javahotel.client.dialog.user.tableseason.PanelSeason;
import com.javahotel.client.mvc.crud.controler.RecordModel;
import com.javahotel.common.dateutil.DateUtil;
import com.javahotel.common.dateutil.CalendarTable.PeriodType;
import com.javahotel.common.toobject.OfferSeasonP;
import com.javahotel.common.util.StringU;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class SeasonOfferAuxPanel implements IMvcView, IAuxInfoPanel {

    @SuppressWarnings("unused")
    private final IResLocator rI;
    private final Grid g = new Grid(2, 1);
    private final VerticalPanel sPanel = new VerticalPanel();
    private final HorizontalPanel controlC = new HorizontalPanel();
    private final PanelSeason pS;

    public SeasonOfferAuxPanel(IResLocator rI) {
        this.rI = rI;
        pS = new PanelSeason(rI, g, controlC, 0, null,DateUtil.getToday());
        sPanel.add(controlC);
//        sPanel.add(g);
    }

    public void show() {
    }

    public void hide() {
    }

    public void draw(Widget w, Widget auxW, RecordModel a) {
        OfferSeasonP oP = (OfferSeasonP) a.getA();
//        if (StringU.isEmpty(oP.getName())) {
//            return;
//        }
        pS.drawPa(oP, PeriodType.byDay);
    }

    public IMvcWidget getMWidget() {
        return new DefaultMvcWidget(sPanel);
    }
}
