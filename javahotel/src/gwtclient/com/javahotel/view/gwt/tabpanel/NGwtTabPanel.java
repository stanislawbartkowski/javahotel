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
package com.javahotel.view.gwt.tabpanel;

import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.panelcommand.EPanelCommand;
import com.javahotel.client.panelcommand.IPanelCommand;
import com.javahotel.client.panelcommand.PanelCommandFactory;
import com.javahotel.view.IDrawTabPanel;
import java.util.ArrayList;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class NGwtTabPanel implements IDrawTabPanel {

    private final IResLocator rI;
    private final TabPanel sPanel = new TabPanel();

    NGwtTabPanel(IResLocator rI, ArrayList<EPanelCommand> pList) {
        this.rI = rI;
        final ArrayList<IPanelCommand> iList = new ArrayList<IPanelCommand>();

        TabListener t = new TabListener() {

            public boolean onBeforeTabSelected(SourcesTabEvents sender,
                    int tabIndex) {
                return true;
            }

            public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
                if (tabIndex >= iList.size()) {
                    return;
                }
                IPanelCommand cr = iList.get(tabIndex);
                cr.drawAction();
            }
        };

        for (EPanelCommand p : pList) {
            IPanelCommand cr = PanelCommandFactory.getPanelCommand(rI, p);
            cr.beforeDrawAction();
            sPanel.add(cr.getMWidget().getWidget(), PanelCommandFactory.getPanelCommandLabel(rI, p));
            iList.add(cr);
        }

        sPanel.addTabListener(t);

        sPanel.selectTab(0);

    }

    public IMvcWidget getMWidget() {
        return new DefaultMvcWidget(sPanel);
    }
}
