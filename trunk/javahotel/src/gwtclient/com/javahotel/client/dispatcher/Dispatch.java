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
package com.javahotel.client.dispatcher;

import java.util.List;
import java.util.Vector;

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.ICommand;
import com.javahotel.client.dispatcher.command.AdminPanelCommand;
import com.javahotel.client.dispatcher.command.LoginCommand;
import com.javahotel.client.dispatcher.command.UserPanelCommand;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
class Dispatch implements IDispatch {

    @SuppressWarnings("unused")
    private IResLocator rI;
    private ICommand decorateAfterLogin;

    class CommandT {

        private final EnumDialog t;
        private final UICommand com;

        CommandT(final EnumDialog t, final UICommand com) {
            this.t = t;
            this.com = com;
        }
    }
    private List<CommandT> comT;

    Dispatch() {
    }

    private void runCommand(final EnumDialog t) {
        for (CommandT ta : comT) {
            if (ta.t == t) {
                ta.com.execute();
                break;
            }
        }
    }

    public void dispatch(final EnumDialog t, final EnumAction a) {
        EnumDialog tn = null;
        switch (t) {
            case STARTLOGIN:
                decorateAfterLogin.execute();
                switch (a) {
                    case LOGINADMIN:
                        tn = EnumDialog.ADMINPANEL;
                        break;
                    case LOGINUSER:
                        tn = EnumDialog.USERPANEL;
                        break;
                }
        }
        if (tn != null) {
            runCommand(tn);
        }

    }

    public void start(final IResLocator i, ICommand decorateAfterLogin) {
        this.rI = i;
        this.decorateAfterLogin = decorateAfterLogin;
        comT = new Vector<CommandT>();
        comT.add(new CommandT(EnumDialog.STARTLOGIN, new LoginCommand(i)));
        comT.add(new CommandT(EnumDialog.ADMINPANEL, new AdminPanelCommand(i)));
        comT.add(new CommandT(EnumDialog.USERPANEL, new UserPanelCommand(i)));
        runCommand(EnumDialog.STARTLOGIN);
    }
}
