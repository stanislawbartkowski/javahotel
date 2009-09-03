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
import com.google.gwt.user.client.ui.Widget;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.DefaultMvcWidget;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.panelcommand.EPanelCommand;
import com.javahotel.client.panelcommand.IPanelCommand;
import com.javahotel.client.panelcommand.ISetGwtWidget;
import com.javahotel.client.panelcommand.PanelCommandFactory;
import com.javahotel.common.command.SynchronizeList;
import com.javahotel.view.IDrawTabPanel;
import java.util.List;

/**
 *
 * @author stanislawbartkowski@gmail.com
 */
class NGwtTabPanel implements IDrawTabPanel {

    private final IResLocator rI;
    private final TabPanel sPanel = new TabPanel();

    private class Synch extends SynchronizeList {

        private final String[] iString;
        private final Widget[] iWidget;

        Synch(int no) {
            super(no);
            iString = new String[no];
            iWidget = new Widget[no];
        }

        @Override
        protected void doTask() {
            for (int i = 0; i < iString.length; i++) {
                sPanel.add(iWidget[i], iString[i]);
            }
            sPanel.selectTab(0);
        }
    }

    private class ISetW implements ISetGwtWidget {

        private final int inde;
        private final String label;
        private final Synch sy;

        ISetW(int inde, String label, Synch sy) {
            this.inde = inde;
            this.label = label;
            this.sy = sy;
        }

        public void setGwtWidget(IMvcWidget i) {
            sy.iWidget[inde] = i.getWidget();
            sy.signalDone();
        }
    }

    NGwtTabPanel(IResLocator rI, List<EPanelCommand> pList) {
        this.rI = rI;
        int no = pList.size();
        final IPanelCommand[] iList = new IPanelCommand[no];
        final Synch sy = new Synch(no);

        TabListener t = new TabListener() {

            public boolean onBeforeTabSelected(SourcesTabEvents sender,
                    int tabIndex) {
                return true;
            }

            public void onTabSelected(SourcesTabEvents sender, int tabIndex) {
                if (tabIndex >= iList.length) {
                    return;
                }
                IPanelCommand cr = iList[tabIndex];
                cr.drawAction();
            }
        };

        int inde = 0;

        for (EPanelCommand p : pList) {
            IPanelCommand cr = PanelCommandFactory.getPanelCommand(rI, p);
            ISetW is = new ISetW(inde++, PanelCommandFactory.getPanelCommandLabel(rI, p),
                    sy);
            cr.beforeDrawAction(is);
        }

        sPanel.addTabListener(t);
    }

    public IMvcWidget getMWidget() {
        return new DefaultMvcWidget(sPanel);
    }
}
