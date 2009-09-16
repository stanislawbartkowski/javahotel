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

import com.google.gwt.user.client.ui.VerticalPanel;
import com.javahotel.client.IResLocator;
import com.javahotel.client.widgets.stable.IDrawPartSeason;
import com.javahotel.client.widgets.stable.IScrollSeason;
import com.javahotel.common.scrollseason.model.MoveSkip;
import java.util.Date;
import java.util.List;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
public class DaySeasonPanelWidget implements IScrollSeason {

    private final DayLineWidget dW;
    private final VerticalPanel hp = new VerticalPanel();
    private final IDrawPartSeason dPart;
    private final ScrollArrowWidget sW;

    private class ScrollCli implements ScrollArrowWidget.IsignalP {

        public void clicked(MoveSkip a) {
            if (!dW.moveD(a, 1)) {
                return;
            }
            sW.setState(dW.getPanelDesc());
            dW.refresh();
        }
    }

    DaySeasonPanelWidget(final IResLocator pLoc, IDrawPartSeason dPart,
            final int todayC) {
        dW = new DayLineWidget(pLoc, todayC);
        this.dPart = dPart;
        sW = new ScrollArrowWidget(new ScrollCli());
    }

    public int getStartNo() {
        return -1;
    }

    public void createVPanel(int no, int actC) {
    }

    public void createVPanel(List<Date> dList, int actC) {
        dW.createVPanel(dList, actC);
        sW.setState(dW.getPanelDesc());
        hp.add(sW.getMWidget().getWidget());
        hp.add(dW.getMWidget().getWidget());
        dPart.setSWidget(hp);
    }
}
