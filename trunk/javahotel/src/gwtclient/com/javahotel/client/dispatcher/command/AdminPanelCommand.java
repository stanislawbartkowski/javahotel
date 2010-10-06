/*
 * Copyright 2010 stanislawbartkowski@gmail.com 
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

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.javahotel.client.IResLocator;
import com.javahotel.client.dialog.panel.IUserPanelMenuFactory;
import com.javahotel.client.dialog.panel.UserPanel;
import com.javahotel.client.dispatcher.EnumDialog;
import com.javahotel.client.dispatcher.UICommand;

/**
 * 
 * @author stanislawbartkowski@gmail.com
 */
public class AdminPanelCommand extends UICommand {
    
    private final IUserPanelMenuFactory iFactory;
    
    @Inject
	public AdminPanelCommand(final IResLocator i,
	        @Named("AdminPanelFactory") IUserPanelMenuFactory iFactory) {
		super(i, EnumDialog.ADMINPANEL);
		this.iFactory = iFactory;
	}

	public void execute() {
//		IUserPanelMenuFactory iFa = AdminPanelFactory.getFactory();
		new UserPanel(rI, iFactory);

	}
}
