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
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.login.ELoginDialog;
import com.javahotel.client.dispatcher.EnumAction;
import com.javahotel.client.dispatcher.EnumDialog;
import com.javahotel.client.dispatcher.UICommand;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class LoginCommand extends UICommand {

	private final ELoginDialog loguser;
	private final ELoginDialog logadmin;

	public LoginCommand(final IResLocator i) {
		super(i, EnumDialog.STARTLOGIN);
		loguser = new ELoginDialog(rI, true, createCLick(EnumAction.LOGINUSER));
		logadmin = new ELoginDialog(rI, false,
				createCLick(EnumAction.LOGINADMIN));
	}

	public void execute() {
		HorizontalPanel ho = new HorizontalPanel();
		ho.setSpacing(15);
		ho.add(loguser);
		ho.add(logadmin);

		rI.getPanel().setDCenter(ho);
	}
}
