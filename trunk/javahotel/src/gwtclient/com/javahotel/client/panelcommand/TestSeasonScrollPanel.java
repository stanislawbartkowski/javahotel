/*
 * Copyright 2011 stanislawbartkowski@gmail.com 
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

package com.javahotel.client.panelcommand;

import java.util.Date;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.dialog.ISetGwtWidget;
import com.javahotel.client.widgets.stable.IDrawPartSeason;
import com.javahotel.client.widgets.stable.IScrollSeason;
import com.javahotel.client.widgets.stable.ScrollSeasonFactory;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class TestSeasonScrollPanel extends  AbstractPanelCommand {

    private IScrollSeason iS;
    private final IResLocator iR;

    TestSeasonScrollPanel(IResLocator iR) {
        this.iR = iR;
    }

    private class DrawPart implements IDrawPartSeason {

        private final ISetGwtWidget iSet;

        DrawPart(ISetGwtWidget iSet) {
            this.iSet = iSet;
        }


        public void draw(int fromL, int toL) {
        }


        public void setGwtWidget(IMvcWidget i) {
            iSet.setGwtWidget(i);
            
        }


        public Widget getColumnEmpty(Label l) {
            // TODO Auto-generated method stub
            return null;
        }


        public Label getLabel(Widget w) {
            // TODO Auto-generated method stub
            return null;
        }


        public void setColumn(Widget w, int cNo, Date d, Label l) {
            // TODO Auto-generated method stub
            
        }

    }

    public void beforeDrawAction(ISetGwtWidget iSet) {
        iS = ScrollSeasonFactory.getScrollSeason(iR, new DrawPart(iSet), 40);
//        iS.createVPanel(156, -1);

    }

    public void drawAction() {
    }

}
