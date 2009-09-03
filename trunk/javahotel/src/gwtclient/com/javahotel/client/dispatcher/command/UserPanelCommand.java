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

import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.panel.IUserPanelMenuFactory;
import com.javahotel.client.dialog.panel.UserPanel;
import com.javahotel.client.dialog.user.UserPanelFactory;
import com.javahotel.client.dispatcher.EnumDialog;
import com.javahotel.client.dispatcher.UICommand;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class UserPanelCommand extends UICommand {

    public UserPanelCommand(final IResLocator i) {
        super(i, EnumDialog.USERPANEL);
    }

    public void execute() {

        IUserPanelMenuFactory iFa = UserPanelFactory.getFactory();
        new UserPanel(rI, iFa);
    }
}
