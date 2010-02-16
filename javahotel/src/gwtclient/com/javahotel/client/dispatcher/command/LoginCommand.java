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
package com.javahotel.client.dispatcher.command;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.inject.Inject;
import com.gwtmodel.table.SynchronizeList;
import com.gwtmodel.table.injector.GwtGiniInjector;
import com.gwtmodel.table.view.webpanel.IWebPanel;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.IMvcWidget;
import com.javahotel.client.dialog.ISetGwtWidget;
import com.javahotel.client.dialog.login.ELoginDialog;
import com.javahotel.client.dialog.login.ETableLoginDialog;
import com.javahotel.client.dispatcher.EnumAction;
import com.javahotel.client.dispatcher.EnumDialog;
import com.javahotel.client.dispatcher.UICommand;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class LoginCommand extends UICommand {

    private class SynchList extends SynchronizeList {

        private IMvcWidget loguser, logadmin;

        SynchList() {
            super(2);
        }

        @Override
        protected void doTask() {
            HorizontalPanel ho = new HorizontalPanel();
            ho.setSpacing(15);
            ho.add(loguser.getWidget());
            ho.add(logadmin.getWidget());
            iW.setDCenter(ho);
        }

    }
    
    private final IWebPanel iW;

    @Inject
    public LoginCommand(final IResLocator i) {
        super(i, EnumDialog.STARTLOGIN);
        this.iW = GwtGiniInjector.getI().getWebPanel();
    }

    private class LogUserAdmin implements ISetGwtWidget {

        private final SynchList sList;
        private boolean user;

        LogUserAdmin(SynchList sList, boolean user) {
            this.sList = sList;
            this.user = user;
        }

        public void setGwtWidget(IMvcWidget i) {
            if (user) {
                sList.loguser = i;
            } else {
                sList.logadmin = i;
            }
            sList.signalDone();
        }

    }

    public void execute() {
        SynchList sList = new SynchList();
        ETableLoginDialog loguser = new ETableLoginDialog(rI, new LogUserAdmin(sList,
                true), true, createCLick(EnumAction.LOGINUSER));
        ELoginDialog logadmin = new ELoginDialog(rI, new LogUserAdmin(sList,
                false), false, createCLick(EnumAction.LOGINADMIN));
    }
}
