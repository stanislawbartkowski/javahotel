/*
 * Copyright 2017 stanislawbartkowski@gmail.com 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not ue this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package com.jythonui.client;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.jythonui.client.service.JythonService;
import com.jythonui.client.service.JythonServiceAsync;
import com.jythonui.client.smessage.IGetStandardMessage;
import com.jythonui.shared.ClientProp;
import com.polymerui.client.view.panel.IMainPanel;

public class M {

	private static String secToken;

	private final static TLogMessages sMess = (TLogMessages) GWT.create(TLogMessages.class);

	private static ClientProp clientProp;

	private static IMainPanel panel;

	@Inject
	private static IGetStandardMessage sMessage;

	public static ClientProp getClientProp() {
		return clientProp;
	}

	public static void setClientProp(ClientProp clientProp) {
		M.clientProp = clientProp;
	}

	public static String getSecToken() {
		return secToken;
	}

	private static final JythonServiceAsync jythonService = GWT.create(JythonService.class);

	public static void setSecToken(String secToken) {
		M.secToken = secToken;
	}

	public static JythonServiceAsync JR() {
		return jythonService;
	}

	public static TLogMessages M() {
		return sMess;
	}

	public static IMainPanel getPanel() {
		return panel;
	}

	public static void setPanel(IMainPanel panel) {
		M.panel = panel;
	}

	public static IGetStandardMessage S() {
		return sMessage;
	}

}
