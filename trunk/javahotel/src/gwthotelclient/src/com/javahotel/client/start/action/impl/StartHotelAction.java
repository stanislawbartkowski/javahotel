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
package com.javahotel.client.start.action.impl;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.gwtmodel.table.ICommand;
import com.gwtmodel.table.IGWidget;
import com.gwtmodel.table.ISetGWidget;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.view.webpanel.IWebPanel;
import com.javahotel.client.MM;
import com.javahotel.client.injector.HInjector;
import com.javahotel.client.start.action.ILoginDialog;
import com.javahotel.client.start.action.IStartHotelAction;
import com.javahotel.client.start.panel.EPanelCommand;
import com.javahotel.client.start.panel.StackHeaderAddList;
import com.javahotel.client.start.panel.UserPanel;

/**
 * @author hotel
 * 
 */
public class StartHotelAction implements IStartHotelAction {

    private class SynchList extends SynchronizeList {

        private IGWidget loguser, logadmin;

        SynchList() {
            super(2);
        }

        @Override
        protected void doTask() {
            IWebPanel iW = GwtGiniInjector.getI().getWebPanel();
            HorizontalPanel ho = new HorizontalPanel();
            ho.setSpacing(15);
            ho.add(loguser.getGWidget());
            ho.add(logadmin.getGWidget());
            iW.setDCenter(ho);
        }

    }

    private class LogUserAdmin implements ISetGWidget {

        private final SynchList sList;
        private boolean user;

        LogUserAdmin(SynchList sList, boolean user) {
            this.sList = sList;
            this.user = user;
        }

        @Override
        public void setW(IGWidget i) {
            if (user) {
                sList.loguser = i;
            } else {
                sList.logadmin = i;
            }
            sList.signalDone();
        }

    }

    private class GoNextUser implements ICommand {

        /*
         * (non-Javadoc)
         * 
         * @see com.gwtmodel.table.ICommand#execute()
         */
        @Override
        public void execute() {
            EPanelCommand eList[] = { EPanelCommand.ROOMSADMIN,
                    EPanelCommand.BOOKING };
            IGWidget w = StackHeaderAddList.constructMenu(eList);
            IWebPanel iW = GwtGiniInjector.getI().getWebPanel();
            iW.setMenuPanel(w.getGWidget());
            UserPanel.draw(new UserPanelFactory());
        }

    }

    private class GoNextAdmin implements ICommand {

        /*
         * (non-Javadoc)
         * 
         * @see com.gwtmodel.table.ICommand#execute()
         */
        @Override
        public void execute() {
            IWebPanel iW = GwtGiniInjector.getI().getWebPanel();
            iW.setUpInfo(MM.L().UsersAndHotele());
            UserPanel.draw(new AdminPanelFactory());
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.gwtmodel.table.ICommand#execute()
     */
    @Override
    public void execute() {
        SynchList sList = new SynchList();
        ILoginDialog loguser = HInjector.getI().getLoginDialog();
        ILoginDialog logadmin = HInjector.getI().getLoginDialog();

        loguser.run(new LogUserAdmin(sList, true), true, new GoNextUser());
        logadmin.run(new LogUserAdmin(sList, false), false, new GoNextAdmin());
    }

}
